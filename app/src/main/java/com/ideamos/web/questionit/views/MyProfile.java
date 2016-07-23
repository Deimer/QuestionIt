package com.ideamos.web.questionit.views;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import com.ideamos.web.questionit.Controllers.SocialController;
import com.ideamos.web.questionit.Controllers.UserController;
import com.ideamos.web.questionit.Fragments.DialogUser;
import com.ideamos.web.questionit.Models.SocialUser;
import com.ideamos.web.questionit.Models.User;
import com.ideamos.web.questionit.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.ColorFilterTransformation;

public class MyProfile extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private Context context;
    private UserController userController;
    private SocialController socialController;
    private int percentage_animate_avatar;
    private boolean avatar_show;
    private int max_scroll_size;

    //Elementos de la vista
    @Bind(R.id.app_bar)AppBarLayout app_bar;
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.user_avatar)CircleImageView user_avatar;
    @Bind(R.id.fab)FloatingActionButton fab;
    @Bind(R.id.img_header)ImageView img_header;

    @Bind(R.id.lbl_username)TextView lbl_username;
    @Bind(R.id.lbl_number_followers)TextView lbl_number_followers;
    @Bind(R.id.lbl_number_followed)TextView lbl_number_followed;
    @Bind(R.id.lbl_number_reactions)TextView lbl_number_reactions;
    @Bind(R.id.lbl_number_posts)TextView lbl_number_posts;
    @Bind(R.id.lbl_number_shopping)TextView lbl_number_shopping;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ButterKnife.bind(this);
        setupContext();
    }

    public void setupContext(){
        context = this;
        userController = new UserController(context);
        socialController = new SocialController(context);
        percentage_animate_avatar = 20;
        avatar_show = true;
        setupAppBarLayout();
        setupToolbar();
        loadProfile();
    }

    public void setupAppBarLayout(){
        app_bar.addOnOffsetChangedListener(this);
        max_scroll_size = app_bar.getTotalScrollRange();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        if (max_scroll_size == 0) {
            max_scroll_size = appBarLayout.getTotalScrollRange();
        }
        int percentage = (Math.abs(offset)) * 100 / max_scroll_size;
        if (percentage >= percentage_animate_avatar && avatar_show) {
            avatar_show = false;
            user_avatar.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
            fab.animate().scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }
        if (percentage <= percentage_animate_avatar && !avatar_show) {
            avatar_show = true;
            user_avatar.animate()
                    .scaleY(1).scaleX(1)
                    .start();
            fab.animate().scaleY(1).scaleX(1).start();
        }
    }

    public void setupToolbar(){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(userController.show().getFullName());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
    }

    public void loadProfile(){
        User user = userController.show();
        SocialUser social = socialController.show();
        if(socialController.session()){
            loadHeaderProfile(social.getAvatar());
            loadAvatarProfile(user.getAvatar());
        } else {
            loadHeaderProfile(user.getAvatar());
            loadAvatarProfile(user.getAvatar());
        }
        lbl_username.setText(user.getUsername());
    }

    public void loadAvatarProfile(String imageUrl){
        Picasso.with(context)
                .load(imageUrl)
                .centerCrop()
                .fit()
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .error(R.drawable.com_facebook_profile_picture_blank_square)
                .into(user_avatar);
    }

    public void loadHeaderProfile(String imageUrl){
        List<Transformation> list = new ArrayList<>();
        BlurTransformation blur = new BlurTransformation(context, 25, 1);
        ColorFilterTransformation color = new ColorFilterTransformation(Color.parseColor("#81000000"));
        list.add(blur);
        list.add(color);
        Picasso.with(context)
                .load(imageUrl)
                .transform(list)
                .centerCrop()
                .fit()
                .placeholder(R.drawable.background_collapsing)
                .error(R.drawable.background_collapsing)
                .into(img_header);
    }

    @OnClick(R.id.user_avatar)
    public void setupAvatar(){
        FragmentManager manager = this.getFragmentManager();
        DialogUser dialogUser = DialogUser.newInstance();
        dialogUser.show(manager, "Avatar");
    }

    @OnClick(R.id.fab)
    public void setupFab(){
        System.out.println("Working...");
    }

}