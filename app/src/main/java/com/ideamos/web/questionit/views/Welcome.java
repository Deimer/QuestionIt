package com.ideamos.web.questionit.views;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.ideamos.web.questionit.R;
import com.vstechlab.easyfonts.EasyFonts;
import butterknife.Bind;
import butterknife.ButterKnife;

public class Welcome extends AppCompatActivity {

    private Context context;

    @Bind(R.id.layout_logo)LinearLayout layout_logo;
    @Bind(R.id.icon_logo)ImageView logo;
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
        lbl_title.setTypeface(EasyFonts.robotoRegular(context));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.FadeIn )
                        .duration(1200)
                        .playOn(lbl_title);
                lbl_title.setVisibility(View.VISIBLE);
            }
        }, 2700);
    }

    public void next(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openLogin();
            }
        }, 6000);
    }

    public void openLogin(){
        Intent login = new Intent(Welcome.this, Login.class);
        startActivity(login);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

}
