package com.example.zf.news_.model;

import com.example.zf.news_.interfaces.OnLoadNewsListListener;

/**
 * Created by Fy on 2018/3/9.
 */

public interface INewsModel {

    void loadBeforeNews(String date, OnLoadNewsListListener loadNewsListListener);

    void loadLatestNews(int type, OnLoadNewsListListener loadNewsListListener);

    void loadTopNews(OnLoadNewsListListener listener);

}
