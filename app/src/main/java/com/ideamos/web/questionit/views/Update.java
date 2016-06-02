package com.ideamos.web.questionit.views;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.ideamos.web.questionit.Controllers.UserController;
import com.ideamos.web.questionit.Helpers.SweetDialog;
import com.ideamos.web.questionit.Helpers.ToastCustomer;
import com.ideamos.web.questionit.Helpers.Validate;
import com.ideamos.web.questionit.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Update extends AppCompatActivity {

    //Variables de entorno
    private Context context;
    private UserController userController;
    private Validate validate;
    private SweetDialog dialog;
    private ToastCustomer toast;

    //Elementos
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.img_user)ImageView img_user;
    @Bind(R.id.cv_photo_profile)CircleImageView cv_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ButterKnife.bind(this);
        setupActivity();
    }

    public void setupActivity(){
        context = this;
        userController = new UserController(context);
        validate = new Validate(context);
        dialog = new SweetDialog(context);
        toast = new ToastCustomer(context);
        setupToolbar();
    }

    public void setupToolbar(){
        setSupportActionBar(toolbar);
        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setDisplayShowTitleEnabled(false);
        }
    }

}
