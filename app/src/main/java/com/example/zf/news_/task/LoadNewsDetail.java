package com.example.zf.news_.task;

import android.os.AsyncTask;
import android.webkit.WebView;

import com.example.zf.news_.bean.NewsDetailBean;
import com.example.zf.news_.util.JsonUtil;
import com.example.zf.news_.util.NewsHttpUtil;

/**
 * Created by Fy on 2018/2/20.
 */

public class LoadNewsDetail extends AsyncTask<Integer, Void, NewsDetailBean> {

    private WebView webView;

    public LoadNewsDetail(WebView webView) {
        this.webView = webView;
    }

    @Override
    protected NewsDetailBean doInBackground(Integer... params) {

        NewsDetailBean detail = null;
        detail = JsonUtil.newsDetail(NewsHttpUtil.getNewsDetail(params[0]));

        return detail;
    }

    @Override
    protected void onPostExecute(NewsDetailBean newsDetailBean) {
        super.onPostExecute(newsDetailBean);
        String headerImage = newsDetailBean.getImg();
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"img-wrap\">")
                .append("<h1 class=\"headline-title\">")
                .append(newsDetailBean.getTitle()).append("</h1>")
                .append("<span class=\"img-source\">")
                .append(newsDetailBean.getImage_source()).append("</span>")
                .append("<img src=\"").append(headerImage)
                .append("\" alt=\"\">")
                .append("<div class=\"img-mask\"></div>");
        String mNewsContent = "<link rel=\"stylesheet\" type=\"text/css\" href=\"news_content.css\"/>"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"news_header.css\"/>"
                + newsDetailBean.getBody().replace("<div class=\"img-place-holder\">", sb.toString());
        webView.loadDataWithBaseURL("file:///android_asset/", mNewsContent, "text/html", "UTF-8", null);
    }
}
