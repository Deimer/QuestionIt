package com.ideamos.web.questionit.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ideamos.web.questionit.Adapters.AdapterOptionAnswer;
import com.ideamos.web.questionit.Controllers.AnswerController;
import com.ideamos.web.questionit.Controllers.CategoryController;
import com.ideamos.web.questionit.Controllers.UserController;
import com.ideamos.web.questionit.Helpers.SweetDialog;
import com.ideamos.web.questionit.Helpers.ToastCustomer;
import com.ideamos.web.questionit.Models.Answer;
import com.ideamos.web.questionit.Models.AnswerType;
import com.ideamos.web.questionit.Models.Category;
import com.ideamos.web.questionit.R;
import com.ideamos.web.questionit.Service.Service;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;
import com.triggertrap.seekarc.SeekArc;
import com.vstechlab.easyfonts.EasyFonts;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CreatePost extends AppCompatActivity {

    //Variables de entorno
    private Context context;
    private UserController userController;
    private CategoryController categoryController;
    private AnswerController answerController;
    private SweetDialog dialog;
    private ToastCustomer toast;
    private List<Integer> list_items;


    //Elementos de la barra
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.lbl_toolbar)TextView lbl_toolbar;
    @Bind(R.id.icon_back)ImageView icon_back;

    //Elementos de la vista
    @Bind(R.id.spinner_categories)
    MaterialSpinner spinner_categories;
    @Bind(R.id.txt_question)
    EditText txt_question;
    @Bind(R.id.lbl_accountant)
    TextView lbl_accountant;
    @Bind(R.id.icon_concept)
    LinearLayout icon_concept;
    @Bind(R.id.spinner_answer_types)
    MaterialSpinner spinner_answer_types;
    @Bind(R.id.layout_options)
    LinearLayout layout_options;
    @Bind(R.id.recycler)
    RecyclerView recycler;
    @Bind(R.id.icon_add_answers)
    Button icon_add;
    @Bind(R.id.icon_remove_answers)
    Button icon_remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        ButterKnife.bind(this);
        setupActivity();
    }

    public void setupActivity(){
        context = this;
        userController = new UserController(context);
        categoryController = new CategoryController(context);
        answerController = new AnswerController(context);
        dialog = new SweetDialog(context);
        toast = new ToastCustomer(context);
        list_items = new ArrayList<>();
        setupToolbar();
        setupSpinners();
        optionAnswers();
        setupIcons();
        setupRecycler();
        setupAccountant();
    }

    public void setupToolbar(){
        setSupportActionBar(toolbar);
        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setDisplayShowHomeEnabled(true);
        }
        lbl_toolbar.setTypeface(EasyFonts.caviarDreams(context));
    }

    public void setupSpinners(){
        List<Category> categories = categoryController.list();
        if (categories.size() > 0){
            List<String> names = new ArrayList<>();
            names.add(0,"Selecciona una categoria");
            for (int i = 0; i < categories.size(); i++) {
                names.add(categories.get(i).getName());
            }
            spinner_categories.setItems(names);
        }
        List<AnswerType> answer_types = answerController.list();
        if (answer_types.size() > 0){
            List<String> names = new ArrayList<>();
            names.add("Seleccione un tipo");
            for (int i = 0; i < answer_types.size(); i++) {
                names.add(answer_types.get(i).getName());
            }
            spinner_answer_types.setItems(names);
        }
    }

    public void setupRecycler(){
        List<Integer> list = new ArrayList<>();
        recycler.setLayoutManager(new LinearLayoutManager(context));
        recycler.setItemAnimator(new SlideInLeftAnimator());
        final AdapterOptionAnswer adapter = new AdapterOptionAnswer(context, list);
        recycler.setAdapter(adapter);
        recycler.getItemAnimator().setAddDuration(500);
        recycler.getItemAnimator().setRemoveDuration(1000);
        icon_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemRecycler(adapter);
            }
        });
        icon_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItemRecycler(adapter);
            }
        });
    }

    public void setupIcons(){
        icon_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        icon_concept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int code = spinner_answer_types.getSelectedIndex();
                if(code > 0){
                    AnswerType answerType = answerController.show(code);
                    String title = answerType.getName(); String message = answerType.getConcept();
                    dialogConcept(title, message);
                }
            }
        });
    }

    public void setupAccountant(){
        txt_question.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int lenght = txt_question.getText().toString().length();
                if(lenght > 270){
                    lbl_accountant.setTextColor(Color.parseColor("#FF9B9B"));
                } else if(lenght < 270) {
                    lbl_accountant.setTextColor(Color.parseColor("#B6B6B6"));
                }
                lbl_accountant.setText(lenght + "/290");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

//Funciones principales de la activity

    public void addItemRecycler(AdapterOptionAnswer adapter){
        int position = list_items.size();
        if(position < 5){
            list_items.add(1);
            adapter.add(1, position);
            adapter.notifyItemInserted(position);
        } else {
            toast.toastWarning("El limite de opciones es de 5");
        }
    }

    public void removeItemRecycler(AdapterOptionAnswer adapter){
        if(list_items.size() > 2){
            int remove = list_items.size() - 1;
            list_items.remove(remove);
            adapter.remove(remove);
            adapter.notifyItemRemoved(remove);
            adapter.notifyItemRangeChanged(remove, adapter.getItemCount());
        } else {
            toast.toastWarning("Debe proporcionar al menos dos opciones de respuesta.");
        }
    }

    public void dialogConcept(String title, String message){
        new MaterialStyledDialog(context)
                .setHeaderColor(R.color.colorPrimary)
                .setIcon(R.drawable.ic_action_info)
                .setTitle(title)
                .setDescription(message + "\n" + "\n")
                .show();
    }

    public void optionAnswers(){
        spinner_answer_types.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position > 0){
                    YoYo.with(Techniques.FadeIn)
                            .duration(700)
                            .playOn(layout_options);
                    layout_options.setVisibility(View.VISIBLE);
                } else {
                    layout_options.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

//Funciones de creacion de un post

    public List<Answer> answersList(){
        List<Answer> list = new ArrayList<>();
        for (int i = 0 ; i < recycler.getChildCount(); i++){
            LinearLayout layout = ((LinearLayout)recycler.getChildAt(i));
            String description = ((EditText)layout.getChildAt(1)).getText().toString();
            if(!description.equalsIgnoreCase("")){
                Answer answer = new Answer();
                answer.setDescription(description);
                answer.setActive(true);
                list.add(answer);
            }
        }
        return list;
    }

    public String preparedQuestion(){
        String question = txt_question.getText().toString().trim();
        if(!question.endsWith("?")) {
            question = question.concat("?");
        } if(!question.startsWith("¿")) {
            question = "¿" + question;
        }
        return question;
    }

    @OnClick(R.id.icon_done)
    public void clickDone(){
        List<Answer> list = answersList();
        if(list.size() > 0){
            String question = preparedQuestion();
            if(question.equalsIgnoreCase("")){
                toast.toastWarning("Primero debe formular una pregunta.");
            } else {
                int code = spinner_categories.getSelectedIndex();
                if(code > 0){
                    int user_id = userController.show().getUser_id();
                    int category_id = categoryController.show(code).getCategory_id();
                    int index = spinner_answer_types.getSelectedIndex();
                    int answer_type = answerController.show(index).getAnswer_type_id();
                    String answers = new Gson().toJson(list);
                    System.out.println(answers);
                    inflateDialogAnswer();
                    //createQuestion(question, user_id, category_id, answer_type, answers);
                }
            }
        } else {
            toast.toastWarning("Debe agregar un texto a la opciones de respuesta.");
        }
    }

    public void createQuestion(String question, int user_id, int category_id, int answer_type, String answers){
        dialog.dialogProgress("Publicando...");
        final String url = getString(R.string.url_con);
        String token = userController.show().getToken();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.createPost(token, question, user_id, category_id, answer_type, answers, true, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if(success){
                    String message = jsonObject.get("message").getAsString();
                    String new_token = jsonObject.get("new_token").getAsString();
                    if(userController.changeToken(new_token)){
                        dialogRegister("Pregunta publicada", message);
                    }
                    dialog.cancelarProgress();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.cancelarProgress();
                try {
                    dialog.dialogError("Error", "Se ha producido un error durante el proceso, intentarlo más tarde.");
                    Log.e("CreatePost(createQuestion)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("CreatePost(createQuestion)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

//region Seccion de mensajes y menus de validacion

    public void dialogRegister(String title, String message){
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("Ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        setResult(Activity.RESULT_OK);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                })
                .show();
    }

    public void inflateDialogAnswer(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_preview_question, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        CircleImageView avatar = (CircleImageView)view.findViewById(R.id.avatar_preview_post);
        TextView lbl_username = (TextView)view.findViewById(R.id.lbl_username_preview_post);
        TextView lbl_question = (TextView)view.findViewById(R.id.lbl_question_preview_post);
        LinearLayout layout_options = (LinearLayout)view.findViewById(R.id.layout_preview_options);
        FrameLayout frame_scale = (FrameLayout)view.findViewById(R.id.layout_preview_scale);
        SeekArc seekbar = (SeekArc)view.findViewById(R.id.seekbar_preview_post);
        TextView lbl_progress_bar = (TextView)view.findViewById(R.id.lbl_seekarc_preview_post);
        Button but_cancel = (Button)view.findViewById(R.id.but_cancel);
        Button but_send = (Button)view.findViewById(R.id.but_send);
        //setters
        loadAvtarProfile(avatar);
        lbl_username.setText(userController.show().getUsername());
        lbl_question.setText(preparedQuestion());
        enabledAnswers(layout_options, frame_scale, seekbar, lbl_progress_bar);
        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        but_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        //Vistas del dialog
        alertDialog.show();
    }

    public void enabledAnswers(LinearLayout layout, FrameLayout frame, SeekArc seekArc, TextView lbl_progress){
        int index = spinner_answer_types.getSelectedIndex();
        int answer_type = answerController.show(index).getAnswer_type_id();
        if(answer_type == 1 || answer_type == 2){
            enableOptions(layout, answer_type);
        } else if(answer_type == 3){
            enableScale(frame, seekArc, lbl_progress);
        }
    }

    public void enableOptions(LinearLayout layout, int option){
        layout.removeAllViews();
        layout.setVisibility(View.VISIBLE);
        List<Answer> list = answersList();
        if(option == 1){
            RadioGroup group = new RadioGroup(context);
            for (int i = 0; i < list.size(); i++) {
                RadioButton radio = new RadioButton(context);
                radio.setText(list.get(i).getDescription());
                group.addView(radio);
            }
            layout.addView(group);
        } else if(option == 2){
            for (int i = 0; i < list.size(); i++) {
                CheckBox check = new CheckBox(context);
                check.setText(list.get(i).getDescription());
                layout.addView(check);
            }
        }
    }

    public void enableScale(FrameLayout frameLayout, SeekArc seekArc, final TextView lbl_progress){
        frameLayout.setVisibility(View.VISIBLE);

        seekArc.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int progress, boolean b) {
                lbl_progress.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {}
            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {}
        });
    }

    public void loadAvtarProfile(CircleImageView avatar){
        String url_avatar = userController.show().getAvatar();
        Picasso.with(context)
                .load(url_avatar)
                .centerCrop().fit()
                .error(R.drawable.com_facebook_profile_picture_blank_square)
                .into(avatar);
    }

//endregion

}
