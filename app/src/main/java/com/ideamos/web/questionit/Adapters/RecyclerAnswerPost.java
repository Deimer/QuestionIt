package com.ideamos.web.questionit.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ideamos.web.questionit.Models.UserAnswer;
import com.ideamos.web.questionit.R;
import com.ideamos.web.questionit.views.MyProfile;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Creado por Deimer, fecha: 30/06/2016.
 */
public class RecyclerAnswerPost extends RecyclerView.Adapter<RecyclerAnswerPost.AdapterView> {

    private Context context;
    private List<UserAnswer> answers_post = new ArrayList<>();
    public RecyclerAnswerPost(Context context, List<UserAnswer> answers_post){
        this.context = context;
        this.answers_post = answers_post;
    }

    @Override
    public AdapterView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_answer_post, parent, false);
        return new AdapterView(view);
    }

    @Override
    public void onBindViewHolder(final AdapterView holder, int position) {
        String avatar = answers_post.get(position).getAvatar();
        String full_name = answers_post.get(position).getUser_fullname();
        String answer = answers_post.get(position).getAnswer();
        String date = answers_post.get(position).getCreated_at();
        Picasso.with(context)
                .load(avatar)
                .centerCrop().fit()
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .error(R.drawable.com_facebook_profile_picture_blank_square)
                .into(holder.avatar_user_answer);
        holder.lbl_fullname_asnwer.setText(full_name);
        holder.lbl_user_answer.setText(answer);
        holder.lbl_date_answer.setText(date);
    }

    @Override
    public int getItemCount() {
        return answers_post.size();
    }

    public class AdapterView extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.avatar_user_answer)CircleImageView avatar_user_answer;
        @Bind(R.id.lbl_fullname_asnwer)TextView lbl_fullname_asnwer;
        @Bind(R.id.lbl_user_answer)TextView lbl_user_answer;
        @Bind(R.id.lbl_date_answer)TextView lbl_date_answer;
        public AdapterView(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
            avatar_user_answer.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (v.getId() == R.id.img_avatar_author) {
                profile(position);
            } else if((v.getId() == R.id.card_post)) {
                profile(position);
            }
        }
    }

    public void profile(int position){
        Intent intent = new Intent(context, MyProfile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id", answers_post.get(position).getUser_answer_id());
        context.startActivity(intent);
    }

}
