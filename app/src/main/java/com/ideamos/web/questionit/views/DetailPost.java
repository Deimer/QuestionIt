package com.ideamos.web.questionit.views;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ideamos.web.questionit.Adapters.RecyclerAnswerPost;
import com.ideamos.web.questionit.Adapters.SpaceItemView;
import com.ideamos.web.questionit.Controllers.PostController;
import com.ideamos.web.questionit.Controllers.UserController;
import com.ideamos.web.questionit.Helpers.ParseTime;
import com.ideamos.web.questionit.Helpers.SweetDialog;
import com.ideamos.web.questionit.Models.Answer;
import com.ideamos.web.questionit.Models.Post;
import com.ideamos.web.questionit.Models.UserAnswer;
import com.ideamos.web.questionit.R;
import com.ideamos.web.questionit.Service.Service;
import com.michaldrabik.tapbarmenulib.TapBarMenu;
import com.squareup.picasso.Picasso;
import com.triggertrap.seekarc.SeekArc;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailPost extends AppCompatActivity {

    //Variables de entorno
    private Context context;
    private UserController userController;
    private PostController postController;
    private ParseTime parseTime;
    private SweetDialog dialog;
    private int answer_type;

    //Elementos de la vista
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.avatar_user)CircleImageView avatar_user;
    @Bind(R.id.lbl_fullname_user)TextView lbl_fullname_user;
    @Bind(R.id.lbl_username_author)TextView lbl_username_author;
    @Bind(R.id.lbl_post_question)TextView lbl_post_question;
    @Bind(R.id.lbl_created_date)TextView lbl_created_date;
    @Bind(R.id.layout_answers)LinearLayout layout_answers;
    @Bind(R.id.layout_not_found)LinearLayout layout_not_found;
    @Bind(R.id.recycler)RecyclerView recycler;
    @Bind(R.id.tap_bar_menu)TapBarMenu tap_bar_menu;
    @Bind(R.id.item1)ImageView item_send;

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
        parseTime = new ParseTime(context);
        dialog = new SweetDialog(context);
        setupToolbar();
        setupConfig();
    }

    public void setupToolbar(){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setDisplayShowTitleEnabled(false);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    public void setupConfig(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            int code = bundle.getInt("code");
            int post_id = postController.show(code).getPost_id();
            answer_type = postController.show(code).getAnswer_type();
            loadInfoPost(code);
            getUserAnswers(post_id);
            getAnswerOptions(post_id);
        }
    }

    public void setupRecycler(List<UserAnswer> answers){
        //recycler.setHasFixedSize(true);
        if(answers.size() > 0){
            RecyclerAnswerPost adapter = new RecyclerAnswerPost(context, answers);
            recycler.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            recycler.setAdapter(adapter);
            SpaceItemView space = new SpaceItemView(0);
            recycler.addItemDecoration(space);
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
        if(tap_bar_menu.isOpened()){
            setupTitle(700);
        }
    }

    public void setupTitle(int time){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.Pulse)
                        .duration(700)
                        .playOn(item_send);
            }
        }, time);
    }

    @OnClick({ R.id.item1, R.id.item2, R.id.item3, R.id.item4 })
    public void clickOptionMenu(View view) {
        tap_bar_menu.close();
        switch (view.getId()) {
            case R.id.item1:
                System.out.println("Click item # 1");
                setupTitle(10);
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

//region Seccion: funciones para almacencar y recorres json's de datos

    public void saveAnswers(JsonArray array){
        List<UserAnswer> list = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JsonObject json = array.get(i).getAsJsonObject();
            UserAnswer answers = new Gson().fromJson(json, UserAnswer.class);
            list.add(answers);
        }
        setupRecycler(list);
    }

    public void saveAnswerOptions(JsonArray array){
        List<Answer> answerList = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JsonObject json = array.get(i).getAsJsonObject();
            Answer answers = new Gson().fromJson(json, Answer.class);
            answerList.add(answers);
        }
        createOptionsAnswers(answerList);
    }

//region Seccion: funciones de consultas a la api

    public void getAnswerOptions(int post_id){
        final String url = getString(R.string.url_con);
        String token = userController.show().getToken();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.getAnswerOptions(token, post_id, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if(success) {
                    JsonArray array = jsonObject.get("answer_options").getAsJsonArray();
                    saveAnswerOptions(array);
                }
            }
            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void getUserAnswers(int post_id){
        final String url = getString(R.string.url_con);
        String token = userController.show().getToken();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.getVotes(token, post_id, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if (success) {
                    JsonArray array = jsonObject.get("votes").getAsJsonArray();
                    saveAnswers(array);
                } else {
                    String message = jsonObject.get("message").getAsString();
                    answersNotFound(message);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                try {
                    dialog.dialogError("Error", "Se ha producido un error durante el proceso, intentarlo más tarde.");
                    Log.e("DetailPost(getUserAnswers)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("DetailPost(getUserAnswers)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

//endregion

//region Seccion: funciones para inicializar dialogs

    public void answersNotFound(String message){
        TextView textView = new TextView(context);
        textView.setText(message);
        layout_not_found.setVisibility(View.VISIBLE);
        layout_not_found.addView(textView);
    }

    public void createOptionsAnswers(List<Answer> answerList){
        layout_answers.removeAllViews();
        switch (answer_type){
            case 1:
                uniqueOptions(answerList);
                break;
            case 2:
                multipleOptions(answerList);
                break;
            case 3:
                scaleOptions();
                break;
        }
    }

    public void uniqueOptions(List<Answer> answerList){
        RadioGroup group = new RadioGroup(context);
        for (int i = 0; i < answerList.size(); i++) {
            Answer answer = answerList.get(i);
            RadioButton radio = new RadioButton(context);
            radio.setText(answer.getDescription());
            radio.setId(answer.getAnswer_id());
            group.addView(radio);
        }
        layout_answers.addView(group);
        layout_answers.setVisibility(View.VISIBLE);
    }

    public void multipleOptions(List<Answer> answerList){
        for (int i = 0; i < answerList.size(); i++) {
            Answer answer = answerList.get(i);
            CheckBox checkbox = new CheckBox(context);
            checkbox.setText(answer.getDescription());
            checkbox.setId(answer.getAnswer_id());
            layout_answers.addView(checkbox);
        }
        layout_answers.setVisibility(View.VISIBLE);
    }

    public void scaleOptions(){
        FrameLayout frame = new FrameLayout(context);
        SeekArc seekArc = new SeekArc(context);
        final TextView lbl_progress_bar = new TextView(context);
        frame.setVisibility(View.VISIBLE);
        seekArc.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int progress, boolean b) {
                lbl_progress_bar.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {}
            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {}
        });
    }

//endregion

}
