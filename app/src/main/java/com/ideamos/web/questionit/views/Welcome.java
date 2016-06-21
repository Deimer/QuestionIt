package com.ideamos.web.questionit.views;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ideamos.web.questionit.Controllers.UserController;
import com.ideamos.web.questionit.Models.Try;
import com.ideamos.web.questionit.R;
import com.ideamos.web.questionit.Service.Service;
import com.vstechlab.easyfonts.EasyFonts;

import org.json.JSONArray;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Welcome extends AppCompatActivity {

    private Context context;
    private UserController userController;

    @Bind(R.id.layout_logo)LinearLayout layout_logo;
    @Bind(R.id.lbl_title_app)TextView lbl_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        setupActivity();
    }

    public void setupActivity(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        context = this;
        userController = new UserController(context);
        initInstanceFacebook();
        sendObjets();
        setupLogo();
        moveLogo();
        setupTitle();
        next();
    }

    public void setupLogo(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.FlipInX)
                        .duration(1000)
                        .playOn(layout_logo);
                layout_logo.setVisibility(View.VISIBLE);
            }
        }, 900);
    }

    public void moveLogo(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = new TranslateAnimation(0,0,0,-300);
                animation.setDuration(1000);
                animation.setFillAfter(true);
                layout_logo.startAnimation(animation);
            }
        }, 1800);
    }

    public void setupTitle(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.FadeIn )
                        .duration(1000)
                        .playOn(lbl_title);
                lbl_title.setTypeface(EasyFonts.caviarDreams(context));
                lbl_title.setVisibility(View.VISIBLE);
            }
        }, 2700);
    }

    public void next(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setupSession();
            }
        }, 6000);
    }

    public void openLogin(){
        Intent login = new Intent(Welcome.this, Login.class);
        startActivity(login);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void openUpdate(){
        Intent update = new Intent(Welcome.this, Update.class);
        startActivity(update);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void openTimeline(){
        Intent timeline = new Intent(Welcome.this, TimeLine.class);
        startActivity(timeline);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void setupSession(){
        boolean session = userController.session();
        if(session){
            int state = userController.show(context).getState();
            if(state == 2){
                openUpdate();
            } else if(state == 1){
                openTimeline();
            }
        }else{
            openLogin();
        }
    }

    public void initInstanceFacebook(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.ideamos.web.questionit",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Welcome(NameNotFoundException): ", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.d("Welcome(NoSuchAlgorithmException): ", e.getMessage());
        }
    }

    public String getObjets(){
        List<Try> list = new ArrayList<>();
        Try try1 = new Try("Descripcion 1", true, 1, 1);
        Try try2 = new Try("Descripcion 2", false, 2, 1);
        Try try3 = new Try("Descripcion 3", true, 3, 1);
        Try try4 = new Try("Descripcion 4", false, 4, 1);
        list.add(try1);
        list.add(try2);
        list.add(try3);
        list.add(try4);
        Gson gson = new Gson();
        String json = gson.toJson(list);
        System.out.println(json);
        return json;
    }

    public void sendObjets(){
        final String url = getString(R.string.url_test);
        String list = getObjets();
        System.out.println(list);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.array(list, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if(success){
                    String message = jsonObject.get("message").getAsString();
                    System.out.println(message);
                }
            }
            @Override
            public void failure(RetrofitError error) {
                try {
                    Log.e("Error", "Se ha producido un error durante el proceso, intentarlo más tarde.");
                    Log.e("Update(update)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("Update(update)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

}
