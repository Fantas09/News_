package com.example.zf.news_.util;

import android.support.annotation.NonNull;
import android.support.v4.content.Loader;

import com.example.zf.news_.bean.NewsListsBean;
import com.example.zf.news_.interfaces.OnLoadNewsListListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Fy on 2018/2/12.
 */

public class NewsHttpUtil {

    private static final String NEWS_DETAIL = "https://news-at.zhihu.com/api/4/news/";
    private static final String NEWSLIST_LATEST = "https://news-at.zhihu.com/api/4/news/latest";
    private static final String NEWSLIST_BEFORE = "https://news-at.zhihu.com/api/4/news/before/";
    private static final String NEW_EXTRA = "https://news-at.zhihu.com/api/4/story-extra/";
    private static final String SHORT_COMMENTS = "";
    private static final String LONG_COMMENTS = "";
    private static final String NEWS_THEMES = "https://news-at.zhihu.com/api/4/theme/";


    private static String get(String url) {
        try {
            //  创建URL对象
            URL httpUrl = new URL(url);
            try {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpUrl.openConnection();
                //请求时间
                httpsURLConnection.setReadTimeout(5000);
                httpsURLConnection.setConnectTimeout(5000);
                //请求方式
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
                StringBuilder stringBuilder = new StringBuilder();
                //获得读入流
                InputStream inputStream = httpsURLConnection.getInputStream();
                //缓冲区
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                String str = "";
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                while ((str = bufferedReader.readLine()) != null) {
                    stringBuilder.append(str);
                }
                bufferedReader.close();
                inputStream.close();
                inputStreamReader.close();
                return stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getNewsDetail(Integer id) {
        return get(NEWS_DETAIL + id);

    }

    public static String getLatestNewsList() {
        return get(NEWSLIST_LATEST);
    }

    public static void getLatestNewsList(final OnLoadNewsListListener listener) {

        new Thread() {

            @Override
            public void run() {
                super.run();
                List<NewsListsBean> list = JsonUtil.top_NewsLists(get(NEWSLIST_LATEST));
                if (listener != null) {
                    listener.onLoad(list);
                }
            }
        }.start();


    }

    public static String getBeforeNewsList(String date) {
        return get(NEWSLIST_BEFORE + date);
    }

    public static String getNewExtra(Integer id) {
        return get(NEW_EXTRA + id);
    }

    public static String getNewsThemes(Integer id) {
        return get(NEWS_THEMES + id);
    }
}
