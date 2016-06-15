package com.ideamos.web.questionit.views;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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

public class TimeLine extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    //Variables de entorno y soporte
    private Context context;
    private PostController postController;
    private UserController userController;
    private SweetDialog dialog;

    //Elementos de la vista
    @Bind(R.id.nav_view)NavigationView nav_view;
    @Bind(R.id.drawer_layout)DrawerLayout drawer_layout;
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
        userController = new UserController(context);
        dialog = new SweetDialog(context);
        setupToolbar();
        setupNavigation();
        getPosts();
    }

    public void setupNavigation(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_layout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_home) {
            System.out.println("Home");
        } else if (id == R.id.action_settings) {
            System.out.println("Settings");
        } else if (id == R.id.action_logout) {
            logout();
        } else if (id == R.id.action_about) {
            System.out.println("About");
        } else if (id == R.id.action_comments) {
            System.out.println("Comments");
        }
        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
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
                try {
                    Log.e("Error", "Se ha producido un error durante el proceso, intentarlo más tarde.");
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

    //Seccion para cerrar la sesion y borrado de datos de la aplicacion
    public void logout(){
        dialog.dialogProgress("Cerrando sesión...");
        final String url = getString(R.string.url_test);
        String token = userController.show(context).getToken();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.logout(token, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {

            }
            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

}
