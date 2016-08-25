package com.ideamos.web.questionit.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ideamos.web.questionit.Objects.ListReactions;
import com.ideamos.web.questionit.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Creado por Deimer, fecha: 3/08/2016.
 */
public class AdapterRecyclerReactions extends RecyclerView.Adapter<AdapterRecyclerReactions.AdapterView> {

    private Context context;
    private List<ListReactions> reactions = new ArrayList<>();

    public AdapterRecyclerReactions(Context context, List<ListReactions> reactions){
        this.context = context;
        this.reactions = reactions;
    }

    @Override
    public AdapterView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reaction_post, parent, false);
        return new AdapterView(view);
    }

    @Override
    public void onBindViewHolder(final AdapterView holder, int position) {
        String full_name = reactions.get(position).getUser_fullname();
        String avatar = reactions.get(position).getUser_avatar();
        int reaction_id = reactions.get(position).getReaction_id();
        holder.lbl_name_reaction.setText(full_name);
        Picasso.with(context)
                .load(avatar)
                .centerCrop().fit()
                .placeholder(R.drawable.user_question_it)
                .error(R.drawable.user_question_it)
                .into(holder.avatar_user_reaction);
        if (reaction_id == 1) {
            Picasso.with(context).load(R.mipmap.ic_like_light).into(holder.icon_reaction);
        } else if(reaction_id == 2) {
            Picasso.with(context).load(R.mipmap.ic_dislike_light).into(holder.icon_reaction);
        }
    }

    @Override
    public int getItemCount() {
        return reactions.size();
    }

    public class AdapterView extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.avatar_user_reaction)CircleImageView avatar_user_reaction;
        @Bind(R.id.lbl_name_reaction)TextView lbl_name_reaction;
        @Bind(R.id.icon_reaction)ImageView icon_reaction;
        public AdapterView(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
            avatar_user_reaction.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (v.getId() == R.id.avatar_user_reaction) {
                profile(position);
            }
        }
    }

    public void profile(int position){
        int user_id = reactions.get(position).getUser_id();
        System.out.println("User Id" + user_id);
    }

}
