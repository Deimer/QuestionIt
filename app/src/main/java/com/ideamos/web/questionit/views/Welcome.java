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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ideamos.web.questionit.Controllers.AnswerController;
import com.ideamos.web.questionit.Controllers.CategoryController;
import com.ideamos.web.questionit.Controllers.UserController;
import com.ideamos.web.questionit.Models.AnswerType;
import com.ideamos.web.questionit.Models.Category;
import com.ideamos.web.questionit.R;
import com.ideamos.web.questionit.Service.Service;
import com.vstechlab.easyfonts.EasyFonts;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import xyz.hanks.library.SmallBang;

public class Welcome extends AppCompatActivity {

    private Context context;
    private UserController userController;
    private CategoryController categoryController;
    private AnswerController answerController;

    @Bind(R.id.layout_logo)LinearLayout layout_logo;
    @Bind(R.id.icon_logo)ImageView icon_logo;
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
        categoryController = new CategoryController(context);
        answerController = new AnswerController(context);
        initInstanceFacebook();
        loadConfiguration();
        setupTitle();
        setupLogo();
        //moveLogo();
        next();
    }

    public void setupLogo(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.Pulse)
                        .duration(700)
                        .playOn(icon_logo);
            }
        }, 4000);
    }

    public void setupTitle(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lbl_title.setTypeface(EasyFonts.caviarDreams(context));
                layout_logo.setVisibility(View.VISIBLE);
                SmallBang smallBang = SmallBang.attach2Window((Welcome)context);
                smallBang.bang(layout_logo);
            }
        }, 1500);
    }

    public void next(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setupSession();
            }
        }, 5000);
    }

    public void openLogin(){
        Intent register = new Intent(Welcome.this, Register.class);
        startActivity(register);
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
        Intent timeline = new Intent(Welcome.this, Timeline.class);
        startActivity(timeline);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void setupSession(){
        boolean session = userController.session();
        if(session){
            int state = userController.show().getState();
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

//Funciones de peteciones a la api

    public void loadConfiguration(){
        List<Category> categories = categoryController.list();
        List<AnswerType> answerTypes = answerController.list();
        if(categories.isEmpty()){
            getCategories();
        }
        if(answerTypes.isEmpty()){
            getAnswerTypes();
        }
    }

    public void getCategories(){
        final String url = getString(R.string.url_con);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.getCategories(new Callback<JsonObject>() {
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
                    Log.e("Login(getCategories)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("Login(getCategories)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

    public void getAnswerTypes(){
        final String url = getString(R.string.url_con);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.getAnswerTypes(new Callback<JsonObject>() {
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

}
