package com.ideamos.web.questionit.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ideamos.web.questionit.Controllers.UserController;
import com.ideamos.web.questionit.Helpers.SweetDialog;
import com.ideamos.web.questionit.Helpers.Validate;
import com.ideamos.web.questionit.Models.User;
import com.ideamos.web.questionit.R;
import com.ideamos.web.questionit.Service.Service;
import com.vstechlab.easyfonts.EasyFonts;

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
    private SweetDialog dialog;
    private Validate validate;

    //Elementos
    @Bind(R.id.lbl_title)TextView lbl_title;
    @Bind(R.id.txt_email)EditText txt_email;
    @Bind(R.id.txt_password)EditText txt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setupActivity();
    }

    public void setupActivity(){
        context = this;
        userController = new UserController(context);
        dialog = new SweetDialog(context);
        validate = new Validate(context);
        setupBar();
        setupConfig();
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
                    boolean hayConexion = validate.connect();
                    if (hayConexion) {
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
        final String url = getString(R.string.url_con);
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
                        next();
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

//Funciones de la app

    public void next(){
        Intent update = new Intent(Login.this, Update.class);
        startActivity(update);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

}
