package com.ideamos.web.questionit.Fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ideamos.web.questionit.Controllers.UserController;
import com.ideamos.web.questionit.Models.User;
import com.ideamos.web.questionit.R;
import com.squareup.picasso.Picasso;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Creado por Deimer, fecha: 19/07/2016.
 */
public class DialogUser extends DialogFragment {

    private UserController userController;

    @Bind(R.id.lbl_name_user)TextView lbl_name_user;
    @Bind(R.id.img_avatar_user)ImageView img_avatar_user;
    @Bind(R.id.layout_bar)LinearLayout layout_bar;
    @Bind(R.id.icon_profile)ImageView icon_profile;
    @Bind(R.id.icon_question)ImageView icon_question;

    public static DialogUser newInstance(){
        return new DialogUser();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        setStyle(style, theme);
        userController = new UserController(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_dialog_avatar, container, false);
        ButterKnife.bind(this, v);
        loadDatauser();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return v;
    }

    public void loadDatauser(){
        User user = userController.show();
        String avatar = user.getAvatar();
        String name_user = user.getFullName();
        lbl_name_user.setText(name_user);
        Picasso.with(getActivity())
                .load(avatar)
                .centerCrop().fit()
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .error(R.drawable.com_facebook_profile_picture_blank_square)
                .into(img_avatar_user);
        layout_bar.setVisibility(View.GONE);
        icon_profile.setVisibility(View.GONE);
        icon_question.setVisibility(View.GONE);
    }

}
