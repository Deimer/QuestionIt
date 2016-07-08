package com.ideamos.web.questionit.Adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ideamos.web.questionit.Controllers.FavoriteController;
import com.ideamos.web.questionit.Controllers.UserController;
import com.ideamos.web.questionit.Fragments.DialogAvatar;
import com.ideamos.web.questionit.Helpers.DataOption;
import com.ideamos.web.questionit.Helpers.Validate;
import com.ideamos.web.questionit.Models.Favorite;
import com.ideamos.web.questionit.Models.Post;
import com.ideamos.web.questionit.R;
import com.ideamos.web.questionit.Service.Service;
import com.ideamos.web.questionit.views.DetailPost;
import com.ideamos.web.questionit.views.Timeline;
import com.squareup.picasso.Picasso;
import com.wx.goodview.GoodView;

import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;

/**
 * Creado por Ideamosweb on 9/06/2016.
 */
public class Recycler extends RecyclerView.Adapter<Recycler.AdapterView> {

    private Context context;
    private List<Post> posts = new ArrayList<>();
    private UserController userController = new UserController(context);
    private FavoriteController favoriteController = new FavoriteController(context);
    public Recycler(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    @Override
    public AdapterView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_post, parent, false);
        return new AdapterView(view);
    }

    @Override
    public void onBindViewHolder(final AdapterView holder, int position) {
        String avatar = posts.get(position).getAvatar();
        String username = posts.get(position).getUsername();
        String description = posts.get(position).getQuestion();
        int post_id = posts.get(position).getPost_id();
        boolean active = favoriteController.isFavorite(post_id);
        Picasso.with(context).load(avatar)
                .centerCrop().fit()
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .error(R.drawable.com_facebook_profile_picture_blank_square)
                .into(holder.avatar_author);
        holder.lbl_author_post.setText(username);
        holder.lbl_post_question.setText(description);
        if (active) {
            Picasso.with(context).load(R.mipmap.ic_favorite).into(holder.icon_favorite);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class AdapterView extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.card_post)CardView card_post;
        @Bind(R.id.img_avatar_author)CircleImageView avatar_author;
        @Bind(R.id.lbl_username_author)TextView lbl_author_post;
        @Bind(R.id.lbl_post_author)TextView lbl_post_question;
        @Bind(R.id.icon_like)ImageView icon_like;
        @Bind(R.id.icon_dislike)ImageView icon_dislike;
        @Bind(R.id.icon_favorite)ImageView icon_favorite;
        public AdapterView(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
            card_post.setOnClickListener(this);
            avatar_author.setOnClickListener(this);
            icon_favorite.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (v.getId() == R.id.img_avatar_author) {
                avatar(position);
            } else if(v.getId() == R.id.card_post) {
                next(position);
            } else if(v.getId() == R.id.icon_favorite) {
                int post_id = posts.get(position).getPost_id();
                createFavorite(post_id, icon_favorite);
            }
        }
    }

    public void next(int position){
        Intent intent = new Intent(context, DetailPost.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("code", posts.get(position).getCode());
        context.startActivity(intent);
    }

    public void avatar(int position){
        int id = posts.get(position).getCode();
        FragmentManager manager = ((Activity) context).getFragmentManager();
        DialogAvatar dialogAvatar = DialogAvatar.newInstance(id);
        dialogAvatar.show(manager, "Avatar");
    }

    public void createFavorite(final int post_id, final ImageView icon_favorite){
        Validate validate = new Validate(context);
        if(validate.connect()){
            Favorite favorite;
            if(!favoriteController.thereFavorites(post_id)){
                favorite = new Favorite();
                favorite.setPost_id(post_id);
                favorite.setUser_id(userController.show().getUser_id());
                favorite.setActive(true);
                if(favoriteController.create(favorite)){
                    sendFavorite(favorite, icon_favorite);
                }
            } else {
                favorite = favoriteController.search(post_id);
                if(favorite.isActive()){
                    favorite.setActive(false);
                } else {
                    favorite.setActive(true);
                }
                sendFavorite(favorite, icon_favorite);
            }
        }
    }

    public void sendFavorite(final Favorite favorite, final ImageView icon_favorite){
        final String url = context.getString(R.string.url_con);
        String token = userController.show().getToken();
        int favorite_id = favorite.getFavorite_id();
        int post_id = favorite.getPost_id();
        int user_id = favorite.getUser_id();
        boolean active = favorite.isActive();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Service api = restAdapter.create(Service.class);
        api.createFavorite(token, favorite_id, post_id, user_id, active, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if(success) {
                    Favorite new_favorite = new Gson().fromJson(jsonObject.get("favorite"), Favorite.class);
                    new_favorite.setCode(favorite.getCode());
                    if(favoriteController.create(new_favorite)){
                        animateFavorite(icon_favorite, new_favorite.isActive());
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                try {
                    Log.e("Error", "Se ha producido un error durante el proceso, intentarlo más tarde.");
                    Log.e("Timeline(sendFavorite)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("Timeline(sendFavorite)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

    public void animateFavorite(ImageView icon_favorite, boolean active){
        SmallBang smallBang = SmallBang.attach2Window((Timeline)context);
        if(active){
            Picasso.with(context).load(R.mipmap.ic_favorite).into(icon_favorite);
        } else {
            Picasso.with(context).load(R.mipmap.ic_no_favorite).into(icon_favorite);
        }
        smallBang.bang(icon_favorite, 50, new SmallBangListener() {
            @Override
            public void onAnimationStart(){}
            @Override
            public void onAnimationEnd(){}
        });
    }

}
