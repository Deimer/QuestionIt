package com.ideamos.web.questionit.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.ideamos.web.questionit.Adapters.TabPagerAdapter;
import com.ideamos.web.questionit.Controllers.PostController;
import com.ideamos.web.questionit.Controllers.UserController;
import com.ideamos.web.questionit.Fragments.FragmentReaction;
import com.ideamos.web.questionit.Helpers.DataOption;
import com.ideamos.web.questionit.Helpers.ParseTime;
import com.ideamos.web.questionit.Helpers.SweetDialog;
import com.ideamos.web.questionit.Helpers.Validate;
import com.ideamos.web.questionit.Models.Post;
import com.ideamos.web.questionit.R;
import com.ideamos.web.questionit.Service.MessageBusAnswer;
import com.ideamos.web.questionit.Service.MessageBusComment;
import com.ideamos.web.questionit.Service.StationBus;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

public class DetailPost extends AppCompatActivity {

    //Variables de entorno
    private Context context;
    private UserController userController;
    private PostController postController;
    private SweetDialog dialog;
    private ParseTime parseTime;
    private Post post;

    //Elementos de la vista
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.avatar_user)CircleImageView avatar_user;
    @Bind(R.id.lbl_fullname_post)TextView lbl_full_name;
    @Bind(R.id.lbl_post_question)TextView lbl_post_question;
    @Bind(R.id.lbl_created_date)TextView lbl_created_date;
    @Bind(R.id.tab_layout)TabLayout tab_layout;
    @Bind(R.id.view_pager)ViewPager view_pager;
    @Bind(R.id.bottom_navigation)BottomNavigation bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        setupActivity();
    }

    public void setupActivity(){
        context = this;
        userController = new UserController(context);
        postController = new PostController(context);
        dialog = new SweetDialog(context);
        parseTime = new ParseTime(context);
        setupConfig();
        setupBottonNavigation();
    }

    public void setupConfig(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            int code = bundle.getInt("code");
            post = postController.show(code);
            setupToolbar();
            setupViewPager();
            loadInfoPost();
        }
    }

    public void setupToolbar(){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            lbl_full_name.setText(post.getFull_name());
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            });
        }
    }

    public void setupViewPager(){
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

    public void loadInfoPost(){
        loadAvatarProfile(post.getAvatar());
        lbl_post_question.setText(post.getQuestion());
        lbl_created_date.setText(parseTime.toCalendar(post.getCreated_at()));
    }

    public void loadAvatarProfile(String avatar){
        Picasso.with(context)
                .load(avatar)
                .centerCrop().fit()
                .placeholder(R.drawable.user_question_it)
                .error(R.drawable.user_question_it)
                .into(avatar_user);
    }

//region Funciones de la barra inferior

    public void setupBottonNavigation(){
        bottom_navigation.setOnMenuItemClickListener(new BottomNavigation.OnMenuItemSelectionListener() {
            @Override
            public void onMenuItemSelect(@IdRes int item, int position) {
                clickItemMenu(position);
            }
            @Override
            public void onMenuItemReselect(@IdRes int item, int position) {
                clickItemMenu(position);
            }
        });
    }

    public void clickItemMenu(int position){
        switch (position) {
            case 0:
                System.out.println("opcion " + position);
                break;
            case 1:
                showSheetDialog();
                System.out.println("opcion " + position);
                break;
            case 2:
                System.out.println("opcion " + position);
                tab_layout.getTabAt(0).select();
                sendAnswer();
                break;
            case 3:
                System.out.println("opcion " + position);
                tab_layout.getTabAt(1).select();
                Validate validate = new Validate(context);
                if(validate.connect()){
                    sendEventBus();
                } else {
                    dialog.dialogWarning(
                            "Error de conexión",
                            "No se pudo detectar una conexión estable a internet."
                    );
                }
                break;
            case 4:
                System.out.println("opcion " + position);
                break;
        }
    }

    public void showSheetDialog(){
        String token = userController.show().getToken();
        BottomSheetDialogFragment sheet_dialog = FragmentReaction.newInstance(post, token);
        sheet_dialog.show(getSupportFragmentManager(), "SheetFragment");
    }

    public void sendAnswer(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.findFragmentById(R.id.fragment_answer);
        StationBus.getBus().post(new MessageBusAnswer(post.getAnswer_type()));
    }

    public void sendEventBus(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.findFragmentById(R.id.fragment_comment);
        StationBus.getBus().post(new MessageBusComment(true));
    }

//region Funciones varias

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                setResult(Activity.RESULT_CANCELED);
                finish();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

//endregion

}
