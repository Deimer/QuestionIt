package com.ideamos.web.questionit.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ideamos.web.questionit.Controllers.PostController;
import com.ideamos.web.questionit.Helpers.DataOption;
import com.ideamos.web.questionit.Helpers.ParseTime;
import com.ideamos.web.questionit.Helpers.SweetDialog;
import com.ideamos.web.questionit.Helpers.Validate;
import com.ideamos.web.questionit.Models.Post;
import com.ideamos.web.questionit.R;
import com.michaldrabik.tapbarmenulib.TapBarMenu;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailPost extends AppCompatActivity {

    //Variables de entorno
    private Context context;
    private PostController postController;
    private ParseTime parseTime;
    private Validate validate;
    private SweetDialog dialog;
    private DataOption data;

    //Elementos de la vista
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.avatar_user)CircleImageView avatar_user;
    @Bind(R.id.lbl_fullname_user)TextView lbl_fullname_user;
    @Bind(R.id.lbl_username_author)TextView lbl_username_author;
    @Bind(R.id.lbl_post_question)TextView lbl_post_question;
    @Bind(R.id.lbl_created_date)TextView lbl_created_date;
    @Bind(R.id.recycler)RecyclerView recycler;
    @Bind(R.id.tap_bar_menu)TapBarMenu tap_bar_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        setupActivity();
    }

    public void setupActivity(){
        context = this;
        postController = new PostController(context);
        parseTime = new ParseTime(context);
        validate = new Validate(context);
        dialog = new SweetDialog(context);
        data = new DataOption();
        setupToolbar();
        setupConfig();
    }

    public void setupToolbar(){
        setSupportActionBar(toolbar);
        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    public void setupConfig(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            int code = bundle.getInt("code");
            loadInfoPost(code);
        }
    }

    public void loadInfoPost(int code){
        Post post = postController.show(code);
        loadAvtarProfile(post.getAvatar());
        lbl_fullname_user.setText(post.getFull_name());
        lbl_username_author.setText(post.getUsername());
        lbl_post_question.setText(post.getQuestion());
        lbl_created_date.setText(parseTime.toCalendar(post.getCreated_at()));
    }

    public void loadAvtarProfile(String avatar){
        Picasso.with(context)
                .load(avatar)
                .centerCrop().fit()
                .error(R.drawable.com_facebook_profile_picture_blank_square)
                .into(avatar_user);
    }

    @OnClick(R.id.tap_bar_menu)
    public void clickMenuBar(){
        tap_bar_menu.toggle();
    }

    @OnClick({ R.id.item1, R.id.item2, R.id.item3, R.id.item4 })
    public void clickOptionMenu(View view) {
        tap_bar_menu.close();
        switch (view.getId()) {
            case R.id.item1:
                System.out.println("Click item # 1");
                break;
            case R.id.item2:
                System.out.println("Click item # 2");
                break;
            case R.id.item3:
                System.out.println("Click item # 3");
                break;
            case R.id.item4:
                System.out.println("Click item # 4");
                break;
        }
    }

}
