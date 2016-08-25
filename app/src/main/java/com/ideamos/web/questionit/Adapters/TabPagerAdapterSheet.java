package com.ideamos.web.questionit.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.ideamos.web.questionit.Fragments.AllReactionsTab;
import com.ideamos.web.questionit.Fragments.DislikeReactionsTab;
import com.ideamos.web.questionit.Fragments.LikeReactionsTab;
import com.ideamos.web.questionit.Objects.ListReactions;
import java.util.List;

/**
 * Creado por Deimer, fecha: 2/08/2016.
 */
public class TabPagerAdapterSheet extends FragmentPagerAdapter {

    private int page_count;
    private List<String> tab_titles;
    private List<ListReactions> reactions;
    private List<ListReactions> likes;
    private List<ListReactions> dislikes;

    public TabPagerAdapterSheet(FragmentManager fm, int pages, List<String> tabs, List<ListReactions> reactions, List<ListReactions> likes, List<ListReactions> dislikes) {
        super(fm);
        this.page_count = pages;
        this.tab_titles = tabs;
        this.reactions = reactions;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    @Override
    public int getCount() {
        return page_count;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f;
        String provider = tab_titles.get(position);
        switch (provider){
            case "Me Gusta":
                f = LikeReactionsTab.newInstance(likes);
                break;
            case "No Me Gusta":
                f = DislikeReactionsTab.newInstance(dislikes);
                break;
            default:
                f = AllReactionsTab.newInstance(reactions);
                break;
        }
        return f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab_titles.get(position);
    }

}
