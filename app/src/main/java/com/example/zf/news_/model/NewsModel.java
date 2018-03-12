package com.example.zf.news_.model;

import android.os.Message;

import com.example.zf.news_.bean.NewsListsBean;
import com.example.zf.news_.interfaces.OnLoadNewsListListener;
import com.example.zf.news_.util.JsonUtil;
import com.example.zf.news_.util.NewsHttpUtil;

import java.util.List;

/**
 * Created by Fy on 2018/3/9.
 */

public class NewsModel implements INewsModel {
    @Override
    public void loadBeforeNews(final String date, final OnLoadNewsListListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<NewsListsBean> list = JsonUtil.newsLists(NewsHttpUtil.getBeforeNewsList(date));
                if (listener != null) {
                    listener.onLoad(list);
                }
            }
        }).start();
    }

    @Override
    public void loadLatestNews(final int type, final OnLoadNewsListListener loadNewsListListener) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                List<NewsListsBean> list = null;
                if (type == 0) {
                    list = JsonUtil.newsLists(NewsHttpUtil.getLatestNewsList());

                } else {
                    list = JsonUtil.newsLists(NewsHttpUtil.getNewsThemes(type));
                }

                if (loadNewsListListener != null) {
                    loadNewsListListener.onLoad(list);
                }

            }
        }).start();
    }

    @Override
    public void loadTopNews(final OnLoadNewsListListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<NewsListsBean> list = JsonUtil.top_NewsLists(NewsHttpUtil.getLatestNewsList());
                if (listener != null) {
                    listener.onLoad(list);
                }
            }
        }).start();
    }


}
