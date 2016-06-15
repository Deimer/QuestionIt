package com.ideamos.web.questionit.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.ideamos.web.questionit.Controllers.CategoryController;
import com.ideamos.web.questionit.Controllers.UserController;
import com.ideamos.web.questionit.Helpers.DataOption;
import com.ideamos.web.questionit.Helpers.SweetDialog;
import com.ideamos.web.questionit.Helpers.Validate;
import com.ideamos.web.questionit.Models.Category;
import com.ideamos.web.questionit.Models.User;
import com.ideamos.web.questionit.R;
import com.ideamos.web.questionit.Service.Service;
import com.vstechlab.easyfonts.EasyFonts;
import org.json.JSONObject;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Login extends AppCompatActivity {

    //Variables de entorno
    private Context context;
    private UserController userController;
    private CategoryController categoryController;
    private SweetDialog dialog;
    private Validate validate;
    private DataOption data;
    private CallbackManager callbackManager;

    //Elementos
    @Bind(R.id.lbl_title)TextView lbl_title;
    @Bind(R.id.txt_email)EditText txt_email;
    @Bind(R.id.txt_password)EditText txt_password;
    @Bind(R.id.login_button_facebook)LoginButton login_button_facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setupActivity();
    }

    public void setupActivity(){
        context = this;
        userController = new UserController(context);
        categoryController = new CategoryController(context);
        dialog = new SweetDialog(context);
        validate = new Validate(context);
        data = new DataOption();
        setupBar();
        setupConfig();
        initFacebookInstances();
    }

    public void setupBar(){
        lbl_title.setText(getString(R.string.title_login));
        lbl_title.setTextColor(Color.WHITE);
        lbl_title.setTypeface(EasyFonts.caviarDreams(context));
    }

    public void setupConfig(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String email_register = bundle.getString("email");
            txt_email.setText(email_register);
        }
    }

//Funciones de click y operaciones

    @OnClick(R.id.lbl_register_with_us)
    public void clickRegister(){
        Intent register = new Intent(Login.this, Register.class);
        startActivity(register);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @OnClick(R.id.but_login)
    public void clickLogin(){
        String email = txt_email.getText().toString();
        String password = txt_password.getText().toString();
        if (email.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
            dialog.dialogError("Error", "Antes de continuar, debes proporcionar tus credenciales.");
        } else {
            if (validate.isEmailValid(email)) {
                if (validate.isPasswordValid(password)) {
                    boolean connection = validate.connect();
                    if (connection) {
                        login(email, password);
                    } else {
                        dialog.dialogWarning("Error de conexión", "No se pudo detectar una conexión estable a internet.");
                    }
                } else {
                    dialog.dialogError("Error", "La contraseña debe tener mas de 6 caracteres.");
                }
            } else {
                dialog.dialogError("Error", "El email ingresado no es valido.");
            }
        }
    }

//Funciones de acceso a la api

    public void login(String email, String password) {
        dialog.dialogProgress("Iniciando sesión...");
        final String url = getString(R.string.url_test);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.login(email, password, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if (success) {
                    String token = jsonObject.get("token").getAsString();
                    User user = new Gson().fromJson(jsonObject.get("user"), User.class);
                    user.setToken(token);
                    if(userController.create(user)){
                        dialog.cancelarProgress();
                        next(user.getState());
                    }
                } else {
                    String message = jsonObject.get("message").getAsString();
                    dialog.cancelarProgress();
                    dialog.dialogWarning("", message);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.cancelarProgress();
                try {
                    dialog.dialogError("Error", "Se ha producido un error durante el proceso, intentarlo más tarde.");
                    Log.e("Login(login)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("Login(login)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

    public void socialLogin(String provider, String id_provider, String email, String avatar){
        dialog.dialogProgress("Iniciando sesión...");
        final String url = getString(R.string.url_test);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.socialLogin(provider, id_provider, email, avatar, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if (success) {
                    try {
                        String token = jsonObject.get("token").getAsString();
                        User user = new Gson().fromJson(jsonObject.get("user"), User.class);
                        user.setToken(token);
                        if(userController.create(user)){
                            getCategories();
                            dialog.cancelarProgress();
                            next(user.getState());
                        }
                    } catch (JsonIOException jsonEx) {
                        dialog.cancelarProgress();
                        dialog.dialogWarning("Error: ", jsonEx.getMessage());
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
                    Log.e("Login(socialLogin)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("Login(socialLogin)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

    public void getCategories(){
        final String url = getString(R.string.url_test);
        String token = userController.show(context).getToken();
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

            }
        });
    }

//Funciones de la app

    public void next(int state){
        if(state == 1){
            Intent timeLine = new Intent(Login.this, TimeLine.class);
            startActivity(timeLine);
        } else if(state == 2) {
            Intent update = new Intent(Login.this, Update.class);
            startActivity(update);
        }
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

//Funciones de login y registro social

    @OnClick(R.id.but_login_facebook)
    public void onClickFacebook(){
        login_button_facebook.performClick();
        loginSocial();
    }

    public void initFacebookInstances(){
        login_button_facebook.setReadPermissions(data.scopesUserFacebook());
        callbackManager = CallbackManager.Factory.create();
    }

    public void loginSocial(){
        if (validate.connect()){
            dialog.dialogProgress("Validando usuario...");
            login_button_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    JsonObject json = data.convertToJsonGson(object);
                                    String provider = getString(R.string.provider_social);
                                    String id_provider = json.get("id").getAsString();
                                    String email_social = json.get("email").getAsString();
                                    String avatar = json
                                            .get("picture").getAsJsonObject()
                                            .get("data").getAsJsonObject()
                                            .get("url").getAsString();
                                    socialLogin(provider, id_provider, email_social, avatar);
                                }
                            }
                    );
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,email,picture.type(large)");
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

}
