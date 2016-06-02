package com.ideamos.web.questionit.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.ideamos.web.questionit.Controllers.UserController;
import com.ideamos.web.questionit.Helpers.SweetDialog;
import com.ideamos.web.questionit.Helpers.Validate;
import com.ideamos.web.questionit.R;
import com.ideamos.web.questionit.Service.Service;
import com.vstechlab.easyfonts.EasyFonts;
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
    private Validate validate;
    private SweetDialog dialog;

    //Elementos
    @Bind(R.id.lbl_title)TextView lbl_title;
    @Bind(R.id.txt_username)EditText txt_username;
    @Bind(R.id.txt_email)EditText txt_email;
    @Bind(R.id.txt_password)EditText txt_password;
    @Bind(R.id.but_register_facebook)Button but_register_facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setupActivity();
    }

    public void setupActivity(){
        context = this;
        userController = new UserController(context);
        validate = new Validate(context);
        dialog = new SweetDialog(context);
        setupBar();
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
                    dialogRegister(title, message, email);
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

    public void dialogRegister(String title, String message, final String email){
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Intent login = new Intent(Register.this, Login.class);
                        login.putExtra("email", email);
                        startActivity(login);
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
        Intent login = new Intent(Register.this, Register.class);
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
