package com.ideamos.web.questionit.Fragments;

import com.ideamos.web.questionit.Controllers.PostController;
import com.ideamos.web.questionit.Models.Post;
import com.ideamos.web.questionit.R;
import com.ideamos.web.questionit.views.DetailPost;
import com.squareup.picasso.Picasso;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Creado por Ideamosweb on 14/06/2016.
 */
public class DialogAvatar extends DialogFragment {

    private int id;
    private PostController postController;

    @Bind(R.id.lbl_name_user)TextView lbl_name_user;
    @Bind(R.id.img_avatar_user)ImageView img_avatar_user;
    @Bind(R.id.icon_profile)ImageView icon_profile;
    @Bind(R.id.icon_question)ImageView icon_question;

    public static DialogAvatar newInstance(int id){
        DialogAvatar dAvatar = new DialogAvatar();
        Bundle args = new Bundle();
        args.putInt("id", id);
        dAvatar.setArguments(args);
        return dAvatar;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        setStyle(style, theme);
        id = getArguments().getInt("id");
        postController = new PostController(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_dialog_avatar, container, false);
        ButterKnife.bind(this, v);
        openPostActivity();
        openProfileActivity();
        loadDatauser();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return v;
    }

    public void loadDatauser(){
        Post post = postController.show(id);
        String avatar = post.getAvatar();
        String name_user = post.getFull_name();
        lbl_name_user.setText(name_user);
        Picasso.with(getActivity())
                .load(avatar)
                .centerCrop().fit()
                .placeholder(R.drawable.user_question_it)
                .error(R.drawable.user_question_it)
                .into(img_avatar_user);
    }

    public void openPostActivity(){
        icon_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Post post = postController.show(id);
                Intent intent = new Intent(getActivity(), DetailPost.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("code", post.getCode());
                getActivity().startActivity(intent);
            }
        });
    }

    public void openProfileActivity(){
        icon_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                System.out.println("Click en esta view");
            }
        });
    }

}
