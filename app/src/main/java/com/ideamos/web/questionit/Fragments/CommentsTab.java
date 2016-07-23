package com.ideamos.web.questionit.Fragments;

import android.support.v4.app.Fragment;
import com.ideamos.web.questionit.Models.Post;

/**
 * Creado por Deimer, fecha: 21/07/2016.
 */
public class CommentsTab extends Fragment {

    public static CommentsTab newInstance(Post post_instance) {
        return new CommentsTab();
    }

}
