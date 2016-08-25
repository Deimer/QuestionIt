package com.ideamos.web.questionit.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ideamos.web.questionit.Adapters.TabPagerAdapterSheet;
import com.ideamos.web.questionit.Helpers.DataOption;
import com.ideamos.web.questionit.Helpers.Validate;
import com.ideamos.web.questionit.Models.Post;
import com.ideamos.web.questionit.Objects.ListReactions;
import com.ideamos.web.questionit.R;
import com.ideamos.web.questionit.Service.Service;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Creado por Deimer, fecha: 2/08/2016.
 */
public class FragmentReaction extends BottomSheetDialogFragment {

    private static Post post;
    private static String token;
    private static int total_reactions;

    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.tab_layout)TabLayout tab_layout;
    @Bind(R.id.view_pager)ViewPager view_pager;

    public static FragmentReaction newInstance(Post post_instance, String token_instance) {
        post = post_instance;
        token = token_instance;
        return new FragmentReaction();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_main, container, false);
        ButterKnife.bind(this, view);
        setupContext();
        return view;
    }

    public void setupContext(){
        setupToolbar();
        enabledGetReactions();
    }

    public void setupToolbar(){
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.reactions_in_post));
        toolbar.setTitleTextColor(Color.parseColor("#575757"));
        toolbar.setSubtitle(getString(R.string.users_reactions));
        toolbar.setSubtitleTextColor(Color.parseColor("#575757"));
    }

    public void enabledGetReactions(){
        Validate validate = new Validate(getContext());
        if(validate.connect()) {
            getReactions();
        } else {
            toolbar.setTitle("Sin Conexión.");
            toolbar.setSubtitle("Error al tratar de conectar con el servidor.");
            toolbar.setTitleTextColor(Color.parseColor("#575757"));
            toolbar.setSubtitleTextColor(Color.parseColor("#575757"));
        }
    }

    public void getReactions(){
        int post_id = post.getPost_id();
        final String url = getString(R.string.url_con);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.getReactionsInPost(token, post_id, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if (success) {
                    total_reactions = jsonObject.get("total").getAsInt();
                    JsonArray array = jsonObject.get("reactions").getAsJsonArray();
                    saveReactions(array);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                try {
                    Log.e("AllReactionsTab(getReactions)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("AllReactionsTab(getReactions)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

    public void saveReactions(JsonArray array){
        List<ListReactions> reactions = new ArrayList<>();
        List<ListReactions> likes = new ArrayList<>();
        List<ListReactions> dislikes = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JsonObject json = array.get(i).getAsJsonObject();
            ListReactions reaction = new Gson().fromJson(json, ListReactions.class);
            reactions.add(reaction);
            if (reaction.getReaction_id() == 1) {
                likes.add(reaction);
            } else if(reaction.getReaction_id() == 2) {
                dislikes.add(reaction);
            }
        }
        setupViewPager(reactions, likes, dislikes);
    }

    public void setupViewPager(List<ListReactions> reactions,
                               List<ListReactions> likes, List<ListReactions> dislikes){
        DataOption data = new DataOption();
        List<String> tab_titles = data.titleTabsSheet(total_reactions);
        int page_count = tab_titles.size();
        view_pager.setAdapter(new TabPagerAdapterSheet(
                getChildFragmentManager(),
                page_count, tab_titles,
                reactions, likes, dislikes
        ));
        tab_layout.setTabMode(TabLayout.MODE_FIXED);
        tab_layout.setupWithViewPager(view_pager);
    }

}
