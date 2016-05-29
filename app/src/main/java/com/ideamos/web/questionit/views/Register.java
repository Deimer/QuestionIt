package com.ideamos.web.questionit.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.ideamos.web.questionit.R;
import com.vstechlab.easyfonts.EasyFonts;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Register extends Activity {

    //Variables de entorno
    private Context context;

    //Elementos
    @Bind(R.id.lbl_title)TextView lbl_title;
    @Bind(R.id.but_register_facebook)Button but_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setupActivity();
    }

    public void setupActivity(){
        context = this;
        setupBar();
    }

    public void setupBar(){
        lbl_title.setText(getString(R.string.title_register));
        lbl_title.setTextColor(Color.WHITE);
        lbl_title.setTypeface(EasyFonts.caviarDreams(context));
    }

    @OnClick(R.id.icon_back)
    public void onClickBack(){
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

}
