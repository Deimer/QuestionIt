package com.ideamos.web.questionit.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ideamos.web.questionit.Adapters.AdapterComment;
import com.ideamos.web.questionit.Adapters.SpaceItemView;
import com.ideamos.web.questionit.Controllers.UserController;
import com.ideamos.web.questionit.Helpers.SweetDialog;
import com.ideamos.web.questionit.Helpers.ToastCustomer;
import com.ideamos.web.questionit.Models.Comment;
import com.ideamos.web.questionit.Models.Post;
import com.ideamos.web.questionit.R;
import com.ideamos.web.questionit.Service.MessageBusComment;
import com.ideamos.web.questionit.Service.Service;
import com.ideamos.web.questionit.Service.StationBus;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Creado por Deimer, fecha: 21/07/2016.
 */
public class CommentsTab extends Fragment {

    //Variables de adeciacion
    private Context context;
    private UserController userController;
    private SweetDialog dialog;
    private ToastCustomer toast;
    private static Post post;

    //Elements
    @Bind(R.id.lbl_number_comments)TextView lbl_number_comments;
    @Bind(R.id.recycler)RecyclerView recycler;

    public static CommentsTab newInstance(Post post_instance) {
        post = post_instance;
        return new CommentsTab();
    }

    public CommentsTab(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
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
    public void recievedComment(MessageBusComment messageBusComment){
        boolean active = messageBusComment.isActive();
        System.out.println(active);
        if(post.isI_answered()) {
            inflateDialogComment();
        } else {
            toast.toastWarning("Antes de comentar debes responder la pregunta.");
        }
    }

    public void setupContext(){
        context = getActivity().getApplicationContext();
        userController = new UserController(context);
        dialog = new SweetDialog(getActivity());
        toast = new ToastCustomer(context);
        getComments();
    }

//region Seccion: funciones para consultar la api de la app

    public void inflateDialogComment(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_comment_post, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        //Elementos del dialog
        final EditText txt_comment = (EditText)view.findViewById(R.id.txt_comment_post);
        final TextView lbl_count = (TextView)view.findViewById(R.id.lbl_count_comment);
        FloatingActionButton but_cancel = (FloatingActionButton)view.findViewById(R.id.fab_cancel);
        FloatingActionButton but_send = (FloatingActionButton)view.findViewById(R.id.fab);
        //Funciones de los elementos
        txt_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setupCountComment(txt_comment, lbl_count);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        but_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        but_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_comment.getText().toString().equalsIgnoreCase("")) {
                    toast.toastWarning("No puedes enviar un comentario vacío.");
                } else {
                    Comment comment = setupComment(txt_comment.getText().toString());
                    sendComment(comment);
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }

    public Comment setupComment(String description){
        Comment comment = new Comment();
        int user_id = userController.show().getUser_id();
        int post_id = post.getPost_id();
        comment.setDescription(description);
        comment.setUser_id(user_id);
        comment.setPost_id(post_id);
        comment.setActive(true);
        return comment;
    }

    public void setupCountComment(EditText description, TextView account){
        int lenght = description.getText().toString().length();
        if(lenght > 130){
            account.setTextColor(Color.parseColor("#FF9B9B"));
        } else if(lenght < 130) {
            account.setTextColor(Color.parseColor("#B6B6B6"));
        }
        account.setText(lenght + "/140");
    }

    public void sendComment(Comment comment){
        dialog.dialogProgress("Enviando tu comentario...");
        final String url = getString(R.string.url_con);
        String token = userController.show().getToken();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.createComment(token, comment, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if(success) {
                    String message = jsonObject.get("message").getAsString();
                    dialog.cancelarProgress();
                    dialog.dialogSuccess("Bien!", message);
                    getComments();
                }
            }
            @Override
            public void failure(RetrofitError error) {
                dialog.cancelarProgress();
                try {
                    dialog.dialogError("Error", "Se ha producido un error durante el proceso, intentarlo más tarde.");
                    Log.e("DetailPost(sendComment)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("DetailPost(sendComment)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

    public void getComments(){
        final String url = getString(R.string.url_con);
        String token = userController.show().getToken();
        int post_id = post.getPost_id();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.getComments(token, post_id, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if(success) {
                    JsonArray comments = jsonObject.get("comments").getAsJsonArray();
                    String total = jsonObject.get("total").getAsString();
                    if(total.equalsIgnoreCase("1")) {
                        lbl_number_comments.setText(total + " Comentario");
                    } else {
                        lbl_number_comments.setText(total + " Comentarios");
                    }
                    saveComments(comments);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                try {
                    dialog.dialogError("Error", "Se ha producido un error durante el proceso, intentarlo más tarde.");
                    Log.e("CommentsTab(getComments)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("CommentsTab(getComments)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

    public void saveComments(JsonArray array){
        List<Comment> list = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JsonObject json = array.get(i).getAsJsonObject();
            Comment comment = new Gson().fromJson(json, Comment.class);
            list.add(comment);
        }
        setupRecycler(list);
    }

    public void setupRecycler(List<Comment> comments){
        if(comments.size() > 0){
            AdapterComment adapter = new AdapterComment(context, comments);
            recycler.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            recycler.setAdapter(adapter);
            SpaceItemView space = new SpaceItemView(0);
            recycler.addItemDecoration(space);
        }
    }

//endregion

}
