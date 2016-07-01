package com.ideamos.web.questionit.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ideamos.web.questionit.Controllers.AnswerController;
import com.ideamos.web.questionit.Controllers.CategoryController;
import com.ideamos.web.questionit.Controllers.UserController;
import com.ideamos.web.questionit.Helpers.DataOption;
import com.ideamos.web.questionit.Helpers.SweetDialog;
import com.ideamos.web.questionit.Helpers.Validate;
import com.ideamos.web.questionit.Models.AnswerType;
import com.ideamos.web.questionit.Models.Category;
import com.ideamos.web.questionit.Models.SocialUser;
import com.ideamos.web.questionit.Models.User;
import com.ideamos.web.questionit.R;
import com.ideamos.web.questionit.Service.Service;
import com.vstechlab.easyfonts.EasyFonts;
import org.json.JSONObject;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Register extends Activity {

    //Variables de entorno
    private Context context;
    private UserController userController;
    private CategoryController categoryController;
    private AnswerController answerController;
    private Validate validate;
    private SweetDialog dialog;
    private CallbackManager callbackManager;
    private DataOption data;

    //Elementos
    @Bind(R.id.lbl_title)TextView lbl_title;
    @Bind(R.id.txt_username)EditText txt_username;
    @Bind(R.id.txt_email)EditText txt_email;
    @Bind(R.id.txt_password)EditText txt_password;
    @Bind(R.id.register_button_facebook)LoginButton register_button_facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setupActivity();
    }

    public void setupActivity(){
        context = this;
        userController = new UserController(context);
        categoryController = new CategoryController(context);
        answerController = new AnswerController(context);
        validate = new Validate(context);
        dialog = new SweetDialog(context);
        data = new DataOption();
        setupBar();
        setupFacebook();
    }

    public void setupBar(){
        lbl_title.setText(getString(R.string.title_register));
        lbl_title.setTextColor(Color.WHITE);
        lbl_title.setTypeface(EasyFonts.caviarDreams(context));
    }

//Funciones de peticiones a la api

    public void register(String username, final String email, String password){
        dialog.dialogProgress("Registrando...");
        final String url = getString(R.string.url_con);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.register(username, email, password, password, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if (success) {
                    String message = jsonObject.get("message").getAsString();
                    dialog.cancelarProgress();
                    String title = "¡Registrado!";
                    dialogRegister(title, message, email, false);
                }
            }
            @Override
            public void failure(RetrofitError error) {
                dialog.cancelarProgress();
                try {
                    dialog.dialogError("Error", "Se ha producido un error durante el proceso, intentarlo más tarde.");
                    Log.e("Register(register)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("Register(register)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

    public void socialRegister(User user, SocialUser social){
        dialog.dialogProgress("Registrando...");
        final String url = getString(R.string.url_con);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.socialRegister(
                //user
                user.getUsername(), user.getEmail(), user.getUsername(), user.getFirst_name(),
                user.getLast_name(), user.getBirth_date(),
                //social
                social.getFull_name(), social.getAvatar(), social.getProvider(),
                social.getId_provider(), social.getSocial_token(),
                new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        boolean success = jsonObject.get("success").getAsBoolean();
                        if (success) {
                            String message = jsonObject.get("message").getAsString();
                            String token = jsonObject.get("token").getAsString();
                            User user = new Gson().fromJson(jsonObject.get("user"), User.class);
                            user.setToken(token);
                            if(userController.create(user)){
                                getCategories(token);
                                getAnswerTypes(token);
                                dialog.cancelarProgress();
                                String title = "¡Registrado!";
                                dialogRegister(title, message, "", true);
                            }
                        } else {
                            String message = jsonObject.get("message").getAsString();
                            LoginManager.getInstance().logOut();
                            dialog.cancelarProgress();
                            dialog.dialogWarning("Error", message);
                        }
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        LoginManager.getInstance().logOut();
                        dialog.cancelarProgress();
                        try {
                            dialog.dialogError("Error", "Se ha producido un error durante el proceso, intentarlo más tarde.");
                            Log.e("Register(socialRegister)", "Error: " + error.getBody().toString());
                        } catch (Exception ex) {
                            Log.e("Register(socialRegister)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                        }
                    }
                });
    }

    public void getCategories(String token){
        final String url = getString(R.string.url_con);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.getCategories(token, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if (success) {
                    JsonArray array = jsonObject.get("categories").getAsJsonArray();
                    for (int i = 0; i < array.size(); i++) {
                        JsonObject json = array.get(i).getAsJsonObject();
                        Category category = new Gson().fromJson(json, Category.class);
                        categoryController.create(category);
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                try {
                    Log.e("Error", "Se ha producido un error durante el proceso, intentarlo más tarde.");
                    Log.e("Login(socialLogin)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("Login(socialLogin)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

    public void getAnswerTypes(String token){
        final String url = getString(R.string.url_con);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.getAnswerTypes(token, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if (success) {
                    JsonArray array = jsonObject.get("answer_types").getAsJsonArray();
                    for (int i = 0; i < array.size(); i++) {
                        JsonObject json = array.get(i).getAsJsonObject();
                        AnswerType answerType = new Gson().fromJson(json, AnswerType.class);
                        answerController.create(answerType);
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                try {
                    Log.e("Error", "Se ha producido un error durante el proceso, intentarlo más tarde.");
                    Log.e("Login(getAnswerTypes)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("Login(getAnswerTypes)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

//Funciones e interacciones con apis sociales

    @OnClick(R.id.but_register_facebook)
    public void onClickFacebook(){
        register_button_facebook.performClick();
        registerFacebook();
    }

    public void setupFacebook(){
        register_button_facebook.setReadPermissions(data.scopesUserFacebook());
        callbackManager = CallbackManager.Factory.create();
    }

    public void registerFacebook(){
        if(validate.connect()){
            dialog.dialogProgress("Iniciando facebook...");
            register_button_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    JsonObject json = data.convertToJsonGson(object);
                                    User user = data.jsonToUser(json);
                                    SocialUser social = data.jsonToSocialUser(json, loginResult, context);
                                    System.out.println("User: " + user.toString());
                                    System.out.println("Social: " + social.toString());
                                    socialRegister(user, social);
                                }
                            }
                    );
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender,birthday,picture.type(large),first_name,last_name");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                    dialog.cancelarProgress();
                    dialog.dialogWarning("", "Cancelado por el usuario");
                }

                @Override
                public void onError(FacebookException error) {
                    dialog.cancelarProgress();
                    dialog.dialogError("Error", "Error on Login, check your facebook app_id");
                }
            });
            dialog.cancelarProgress();
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } else {
            dialog.dialogWarning("Error de conexión", "No se pudo detectar una conexión estable a internet.");
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

//Funciones de alertas y dialogs

    public void dialogRegister(String title, String message, final String email, final boolean social){
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        if(social){
                            Intent timeline = new Intent(Register.this, Timeline.class);
                            startActivity(timeline);
                        } else {
                            Intent login = new Intent(Register.this, Login.class);
                            login.putExtra("email", email);
                            startActivity(login);
                        }
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                })
                .show();
    }

//Funciones de click

    @OnClick(R.id.but_register)
    public void clickRegister(){
        String username = txt_username.getText().toString();
        String email = txt_email.getText().toString();
        String password = txt_password.getText().toString();
        if (email.equalsIgnoreCase("") || password.equalsIgnoreCase("") || username.equalsIgnoreCase("")) {
            dialog.dialogWarning("Error", "Antes de continuar, debes proporcionar todos tus datos.");
        } else {
            if (validate.isEmailValid(email)) {
                if (validate.isPasswordValid(password)) {
                    boolean hayConexion = validate.connect();
                    if (hayConexion) {
                        register(username, email, password);
                    } else {
                        dialog.dialogWarning("Error de conexión", "No se pudo detectar una conexión estable a internet.");
                    }
                }
            }
        }
    }

    @OnClick(R.id.icon_back)
    public void onClickBack(){
        Intent login = new Intent(Register.this, Login.class);
        startActivity(login);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                onClickBack();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}
