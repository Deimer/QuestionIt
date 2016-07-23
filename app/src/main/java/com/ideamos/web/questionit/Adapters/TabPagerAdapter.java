package com.ideamos.web.questionit.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.ideamos.web.questionit.Fragments.AnswersTab;
import com.ideamos.web.questionit.Fragments.CommentsTab;
import com.ideamos.web.questionit.Models.Post;

import java.util.List;

/**
 * Creado por Deimer, fecha: 21/07/2016.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    private int page_count;
    private List<String> tab_titles;
    private Post post;

    public TabPagerAdapter(FragmentManager fm, int pages, List<String> tabs, Post post) {
        super(fm);
        this.page_count = pages;
        this.tab_titles = tabs;
        this.post = post;
    }

    @Override
    public int getCount() {
        return page_count;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        String provider = tab_titles.get(position);
        switch (provider){
            case "Respuestas":
                f = AnswersTab.newInstance(post);
                break;
            case "Comentarios":
                f = CommentsTab.newInstance(post);
                break;
        }
        return f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab_titles.get(position);
    }

}
