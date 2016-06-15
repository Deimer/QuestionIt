package com.ideamos.web.questionit.views;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.ideamos.web.questionit.Adapters.Recycler;
import com.ideamos.web.questionit.Adapters.SpaceItemView;
import com.ideamos.web.questionit.Controllers.PostController;
import com.ideamos.web.questionit.Controllers.UserController;
import com.ideamos.web.questionit.Helpers.SweetDialog;
import com.ideamos.web.questionit.Models.Post;
import com.ideamos.web.questionit.R;
import com.ideamos.web.questionit.Service.Service;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TimeLine extends AppCompatActivity {

    //Variables de entorno y soporte
    private Context context;
    private PostController postController;
    private SweetDialog dialog;

    //Elementos de la vista
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.recycler)RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        ButterKnife.bind(this);
        setupContext();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupRecycler();
    }

    public void setupContext(){
        context = this;
        postController = new PostController(context);
        dialog = new SweetDialog(context);
        setupToolbar();
        getPosts();
    }

    public void setupToolbar(){
        setSupportActionBar(toolbar);
        if(getActionBar() != null){
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    public void setupRecycler(){
        List<Post> posts = postController.list(context);
        Recycler adapter = new Recycler(context, posts);
        recycler.setLayoutManager(
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        );
        recycler.setAdapter(adapter);
        SpaceItemView space = new SpaceItemView(1);
        recycler.addItemDecoration(space);
    }

    @OnClick(R.id.fab)
    public void add(View view){
        Snackbar.make(
                view, "Esto es una prueba", Snackbar.LENGTH_LONG
        ).show();
    }

    public void getPosts(){
        //dialog.dialogProgress("Obteniendo posts...");
        final String url = getString(R.string.url_test);
        final UserController userController = new UserController(context);
        String token = userController.show(context).getToken();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.getPosts(token, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if (success) {
                    JsonArray array = jsonObject.get("posts").getAsJsonArray();
                    String new_token = jsonObject.get("new_token").getAsString();
                    if(userController.changeToken(new_token)){
                        savePosts(array);
                        //dialog.cancelarProgress();
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                //dialog.cancelarProgress();
                try {
                    dialog.dialogError("Error", "Se ha producido un error durante el proceso, intentarlo más tarde.");
                    Log.e("Timeline(getPosts)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("Timeline(getPosts)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

    public boolean savePosts(JsonArray array){
        boolean res = true;
        try {
            for (int i = 0; i < array.size(); i++) {
                JsonObject json = array.get(i).getAsJsonObject();
                Post post = new Gson().fromJson(json, Post.class);
                if(!postController.ifExist(post)){
                    Log.e(
                            "Timeline(savePosts)",
                            "Error save: Error al guardar post en la iteración " + i
                    );
                    break;
                }
            }
            setupRecycler();
        } catch (JsonIOException ex) {
            res = false;
            Log.e("Timeline(savePosts)", "Error ex: " + ex.getMessage());
        }
        return res;
    }

}
