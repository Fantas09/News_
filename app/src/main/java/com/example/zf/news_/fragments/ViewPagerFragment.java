package com.example.zf.news_.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zf.news_.R;
import com.example.zf.news_.adapter.MyPagerAdapter;

/**
 * Created by Fy on 2018/2/11.
 */

public class ViewPagerFragment extends Fragment {

    public static final int NEWS_TOP = 0;
    public static final int NEWS_PE = 8;
    public static final int NEWS_DESIGN = 4;
    public static final int NEWS_RECOM = 12;
    public static final int NEWS_GAME = 2;
    public static final int NEWS_PSY = 13;
    public static final int NEWS_MUSIC = 7;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.news_tab, null);
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tab);
        viewPager.setOffscreenPageLimit(4);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        return view;
    }

    private void setUpViewPager(ViewPager viewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
        adapter.addFragment(NewsListFragment.getFragment(NEWS_TOP), getString(R.string.top));
        adapter.addFragment(NewsListFragment.getFragment(NEWS_PE), getString(R.string.pe));
        adapter.addFragment(NewsListFragment.getFragment(NEWS_DESIGN), getString(R.string.design));
        adapter.addFragment(NewsListFragment.getFragment(NEWS_GAME), getString(R.string.game));
        adapter.addFragment(NewsListFragment.getFragment(NEWS_RECOM), getString(R.string.recommend));
        adapter.addFragment(NewsListFragment.getFragment(NEWS_MUSIC), getString(R.string.music));
        adapter.addFragment(NewsListFragment.getFragment(NEWS_PSY), getString(R.string.psy));


        viewPager.setAdapter(adapter);
    }


}
