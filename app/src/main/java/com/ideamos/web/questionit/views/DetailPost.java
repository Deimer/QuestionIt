package com.ideamos.web.questionit.views;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.ideamos.web.questionit.Adapters.TabPagerAdapter;
import com.ideamos.web.questionit.Controllers.PostController;
import com.ideamos.web.questionit.Helpers.DataOption;
import com.ideamos.web.questionit.Helpers.ParseTime;
import com.ideamos.web.questionit.Models.Post;
import com.ideamos.web.questionit.R;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class DetailPost extends AppCompatActivity {

    //Variables de entorno
    private Context context;
    private PostController postController;
    private ParseTime parseTime;

    //Elementos de la vista
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.avatar_user)CircleImageView avatar_user;
    @Bind(R.id.lbl_fullname_post)TextView lbl_full_name;
    @Bind(R.id.lbl_post_question)TextView lbl_post_question;
    @Bind(R.id.lbl_created_date)TextView lbl_created_date;
    @Bind(R.id.tab_layout)TabLayout tab_layout;
    @Bind(R.id.view_pager)ViewPager view_pager;

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
        setupConfig();
    }

    public void setupConfig(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            int code = bundle.getInt("code");
            Post post = postController.show(code);
            setupToolbar(post);
            setupViewPager(post);
            loadInfoPost(post);
        }
    }

    public void setupToolbar(Post post){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            lbl_full_name.setText(post.getFull_name());
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    public void setupViewPager(Post post){
        DataOption data = new DataOption();
        List<String> tab_titles = data.titleTabs();
        int page_count = tab_titles.size();
        System.out.println(tab_titles);
        view_pager.setAdapter(new TabPagerAdapter(
                getSupportFragmentManager(),
                page_count, tab_titles,
                post
        ));
        tab_layout.setTabMode(TabLayout.MODE_FIXED);
        tab_layout.setupWithViewPager(view_pager);
    }

    public void loadInfoPost(Post post){
        loadAvatarProfile(post.getAvatar());
        lbl_post_question.setText(post.getQuestion());
        lbl_created_date.setText(parseTime.toCalendar(post.getCreated_at()));
    }

    public void loadAvatarProfile(String avatar){
        Picasso.with(context)
                .load(avatar)
                .centerCrop().fit()
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .error(R.drawable.com_facebook_profile_picture_blank_square)
                .into(avatar_user);
    }

//endregion

}
