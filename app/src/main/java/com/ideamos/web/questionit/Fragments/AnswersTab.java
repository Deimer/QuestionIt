package com.ideamos.web.questionit.Fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
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
import com.ideamos.web.questionit.Helpers.SweetDialog;
import com.ideamos.web.questionit.Helpers.ToastCustomer;
import com.ideamos.web.questionit.Models.Answer;
import com.ideamos.web.questionit.Models.Post;
import com.ideamos.web.questionit.Models.UserAnswer;
import com.ideamos.web.questionit.R;
import com.ideamos.web.questionit.Service.MessageBusAnswer;
import com.ideamos.web.questionit.Service.Service;
import com.ideamos.web.questionit.Service.StationBus;
import com.squareup.otto.Subscribe;
import com.triggertrap.seekarc.SeekArc;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Creado por Deimer, fecha: 21/07/2016.
 */
public class AnswersTab extends Fragment {

    //Variables de adeciacion
    private Context context;
    private UserController userController;
    private PostController postController;
    private SweetDialog dialog;
    private ToastCustomer toast;
    private List<RadioButton> listRadios;
    private List<CheckBox> listCheck;
    private static Post post;

    //Elements
    @Bind(R.id.layout_answers)LinearLayout layout_answers;
    @Bind(R.id.layout_not_found)LinearLayout layout_not_found;
    @Bind(R.id.recycler)RecyclerView recycler;

    public static AnswersTab newInstance(Post post_instance) {
        post = post_instance;
        return new AnswersTab();
    }

    public AnswersTab(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answers, container, false);
        ButterKnife.bind(this, view);
        setupContext();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        StationBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        StationBus.getBus().unregister(this);
    }

    @Subscribe
    public void recievedComment(MessageBusAnswer messageBusAnswer){
        int type_post = messageBusAnswer.getType_post();
        if(layout_answers.getVisibility() == View.GONE) {
            toast.toastWarning("Ya respondiste la pregunta.");
        } else {
            activeAnswer(type_post);
        }
    }

    public void activeAnswer(final int type_post){
        new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("¿Enviar respuesta?")
                .setCancelText("No")
                .setConfirmText("Si")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        getOption(type_post);
                    }
                })
                .show();
    }

    public void setupContext(){
        context = getActivity().getApplicationContext();
        userController = new UserController(context);
        postController = new PostController(context);
        dialog = new SweetDialog(getActivity());
        toast = new ToastCustomer(getActivity());
        listRadios = new ArrayList<>();
        listCheck = new ArrayList<>();
        getAnswerOptions();
        getUserAnswers();
    }

    public void getOption(int type_post){
        boolean answered = false;
        if(type_post == 1){
            if(listRadios.size() > 0){
                for (int i = 0; i < listRadios.size(); i++) {
                    if(listRadios.get(i).isChecked()){
                        int answer_id = Integer.parseInt(listRadios.get(i).getTag().toString());
                        int user_id = userController.show().getUser_id();
                        sendVote(answer_id, user_id);
                        answered = true;
                        break;
                    }
                }
                if(!answered) {dialog.dialogWarning("", "Antes debes seleccionar una opción de respuesta.");}
            } else { dialog.dialogWarning("", "No hay opciones disponibles aún."); }
        }
    }

    public void getOptions(int type_post){
        if(listCheck.size() > 0) {
            for (int i = 0; i < listCheck.size(); i++) {
                if(listCheck.get(i).isChecked()){
                    int answer_id = Integer.parseInt(listCheck.get(i).getTag().toString());
                    int user_id = userController.show().getUser_id();
                    System.out.println(answer_id);
                    sendVote(answer_id, user_id);
                }
            }
        } else { dialog.dialogWarning("", "Antes debes dar tu respuesta."); }
    }

    private ValueAnimator slideAnimator(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = layout_answers.getLayoutParams();
                layoutParams.height = value;
                layout_answers.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public void hideOptions(){
        YoYo.with(Techniques.FadeOutUp)
                .duration(700)
                .playOn(layout_answers);
        YoYo.with(Techniques.FadeOutUp)
                .duration(700)
                .playOn(layout_not_found);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int finalHeight = layout_answers.getHeight();
                ValueAnimator mAnimator = slideAnimator(finalHeight, 0);
                mAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        layout_answers.setVisibility(View.GONE);
                        layout_not_found.setVisibility(View.GONE);
                    }
                    @Override
                    public void onAnimationStart(Animator animator) {}
                    @Override
                    public void onAnimationCancel(Animator animator) {}
                    @Override
                    public void onAnimationRepeat(Animator animator) {}
                });
                mAnimator.start();
            }
        }, 1000);
    }

//region Seccion: funciones para almacencar y recorres json's de datos

    public void setupRecycler(List<UserAnswer> answers){
        if(answers.size() > 0){
            int post_id = post.getPost_id();
            int user_id = postController.search(post_id).getUser_id();
            RecyclerAnswerPost adapter = new RecyclerAnswerPost(context, answers, user_id);
            recycler.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            recycler.setAdapter(adapter);
            SpaceItemView space = new SpaceItemView(0);
            recycler.addItemDecoration(space);
        }
    }

    public void saveAnswers(JsonArray array){
        List<UserAnswer> list = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JsonObject json = array.get(i).getAsJsonObject();
            UserAnswer answers = new Gson().fromJson(json, UserAnswer.class);
            if (answers.getUser_id().equals(userController.show().getUser_id())) {
                post.setI_answered(true);
                postController.update(post);
            }
            list.add(answers);
        }
        setupRecycler(list);
    }

    public void saveAnswerOptions(JsonArray array, boolean author_response){
        List<Answer> answerList = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JsonObject json = array.get(i).getAsJsonObject();
            Answer answers = new Gson().fromJson(json, Answer.class);
            answerList.add(answers);
        }
        if (!author_response) {
            createOptionsAnswers(answerList);
        }
    }

//region Seccion: funciones de consultas a la api

    public void sendVote(int answer_id, int user_id){
        dialog.dialogProgress("Enviando tu respuesta...");
        final String url = getString(R.string.url_con);
        String token = userController.show().getToken();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.sendVote(token, answer_id, user_id, true, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if(success) {
                    String message = jsonObject.get("message").getAsString();
                    String new_token = jsonObject.get("new_token").getAsString();
                    dialog.cancelarProgress();
                    if(userController.changeToken(new_token)){
                        toast.toastBasic(message);
                        post.setI_answered(true);
                        postController.update(post);
                        hideOptions();
                        getUserAnswers();
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                try {
                    dialog.dialogError("Error", "Se ha producido un error durante el proceso, intentarlo más tarde.");
                    Log.e("DetailPost(sendVote)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("DetailPost(sendVote)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

    public void getAnswerOptions(){
        int post_id = post.getPost_id();
        final String url = getString(R.string.url_con);
        String token = userController.show().getToken();
        int user_id = userController.show().getUser_id();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.getAnswerOptions(token, post_id, user_id, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if(success) {
                    JsonArray array = jsonObject.get("answer_options").getAsJsonArray();
                    boolean author_response = jsonObject.get("author_response").getAsBoolean();
                    saveAnswerOptions(array, author_response);
                }
            }
            @Override
            public void failure(RetrofitError error) {
                try {
                    dialog.dialogError("Error", "Se ha producido un error durante el proceso, intentarlo más tarde.");
                    Log.e("DetailPost(getAnswerOptions)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("DetailPost(getAnswerOptions)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

    public void getUserAnswers(){
        int post_id = post.getPost_id();
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
        textView.setTextColor(ContextCompat.getColor(context, R.color.primary_text));
        layout_not_found.setVisibility(View.VISIBLE);
        layout_not_found.addView(textView);
    }

    public void createOptionsAnswers(List<Answer> answerList){
        layout_answers.removeAllViews();
        int answer_type = post.getAnswer_type();
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
            LayoutInflater inflater = getActivity().getLayoutInflater();
            RadioButton radio = (RadioButton)inflater.inflate(R.layout.template_radio_button, null);
            radio.setText(answer.getDescription());
            radio.setId(answer.getAnswer_id());
            radio.setTag(answer.getAnswer_id());
            radio.setTextColor(ContextCompat.getColor(context, R.color.primary_text));
            group.addView(radio);
            listRadios.add(radio);
        }
        layout_answers.addView(group);
        layout_answers.setVisibility(View.VISIBLE);
    }

    public void multipleOptions(List<Answer> answerList){
        for (int i = 0; i < answerList.size(); i++) {
            Answer answer = answerList.get(i);
            LayoutInflater inflater = getActivity().getLayoutInflater();
            CheckBox checkbox = (CheckBox)inflater.inflate(R.layout.template_check_box, null);
            checkbox.setText(answer.getDescription());
            checkbox.setId(answer.getAnswer_id());
            checkbox.setTag(answer.getAnswer_id());
            checkbox.setTextColor(ContextCompat.getColor(context, R.color.primary_text));
            layout_answers.addView(checkbox);
            listCheck.add(checkbox);
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

}
