package com.ideamos.web.questionit.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ideamos.web.questionit.Adapters.AdapterRecyclerReactions;
import com.ideamos.web.questionit.Adapters.SpaceItemView;
import com.ideamos.web.questionit.Controllers.UserController;
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
 * Creado por Deimer, fecha: 4/08/2016.
 */
public class DislikeReactionsTab extends Fragment{

    private Context context;
    private static List<ListReactions> reactions;

    @Bind(R.id.recycler)RecyclerView recycler;

    public static DislikeReactionsTab newInstance(List<ListReactions> reactions_instance) {
        reactions = reactions_instance;
        return new DislikeReactionsTab();
    }

    public DislikeReactionsTab(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_dislike, container, false);
        ButterKnife.bind(this, view);
        setupContext();
        return view;
    }

    public void setupContext(){
        context = getActivity().getApplicationContext();
        setupRecycler();
    }

    public void setupRecycler(){
        if(reactions.size() > 0){
            AdapterRecyclerReactions adapter = new AdapterRecyclerReactions(context, reactions);
            recycler.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            recycler.setAdapter(adapter);
            SpaceItemView space = new SpaceItemView(0);
            recycler.addItemDecoration(space);
        }
    }

}
