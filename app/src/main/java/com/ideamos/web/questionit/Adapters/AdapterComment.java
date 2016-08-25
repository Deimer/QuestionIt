package com.ideamos.web.questionit.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ideamos.web.questionit.Models.Comment;
import com.ideamos.web.questionit.R;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Creado por Deimer, fecha: 22/07/2016.
 */
public class AdapterComment extends RecyclerView.Adapter<AdapterComment.AdapterView> {

    private Context context;
    private List<Comment> comments;

    public AdapterComment(Context context, List<Comment> answers_post){
        this.context = context;
        this.comments = answers_post;
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public AdapterView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment_post, parent, false);
        return new AdapterView(v);
    }

    @Override
    public void onBindViewHolder(final AdapterView holder, int position) {
        String avatar = comments.get(position).getAvatar();
        String username = comments.get(position).getUsername();
        String description = comments.get(position).getDescription();
        Picasso.with(context)
                .load(avatar)
                .centerCrop().fit()
                .placeholder(R.drawable.user_question_it)
                .error(R.drawable.user_question_it)
                .into(holder.avatar_user_comment);
        holder.lbl_username_post.setText(username);
        holder.lbl_comment_post.setText(description);
    }

    public class AdapterView extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.avatar_user_comment)CircleImageView avatar_user_comment;
        @Bind(R.id.lbl_username_post)TextView lbl_username_post;
        @Bind(R.id.lbl_comment_post)TextView lbl_comment_post;
        public AdapterView(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
            avatar_user_comment.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (v.getId() == R.id.avatar_user_comment) {
                System.out.println("pulso la foto en la posicion: " + position);
            }
        }
    }

}