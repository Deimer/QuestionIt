package com.ideamos.web.questionit.Adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ideamos.web.questionit.Controllers.PostController;
import com.ideamos.web.questionit.Fragments.DialogAvatar;
import com.ideamos.web.questionit.Models.Post;
import com.ideamos.web.questionit.R;
import com.ideamos.web.questionit.views.DetailPost;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Creado por Ideamosweb on 9/06/2016.
 */
public class Recycler extends RecyclerView.Adapter<Recycler.AdapterView> {

    private Context context;
    private List<Post> posts = new ArrayList<>();
    private PostController postController;

    public Recycler(Context context, List<Post> posts){
        this.context = context;
        this.posts = posts;
        postController = new PostController(context);
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
        String description = posts.get(position).getDescription();
        Picasso.with(context).load(avatar)
                .centerCrop().fit()
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .error(R.drawable.com_facebook_profile_picture_blank_square)
                .into(holder.avatar_author);
        holder.lbl_author_post.setText(username);
        holder.lbl_post_question.setText(description);
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
        public AdapterView(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
            card_post.setOnClickListener(this);
            avatar_author.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (v.getId() == R.id.img_avatar_author) {
                //System.out.println("Click next: " + position);
                avatar(position);
            } else if((v.getId() == R.id.card_post)) {
                //System.out.println("Click delete: " + position);
                next(position);
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
        FragmentManager manager = ((Activity) context).getFragmentManager();
        DialogAvatar dialogAvatar = new DialogAvatar();
        dialogAvatar.show(manager, "Avatar");
    }

}
