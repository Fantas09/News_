package com.example.zf.news_.util;

import com.example.zf.news_.bean.NewsDetailBean;
import com.example.zf.news_.bean.NewsListsBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fy on 2018/2/13.
 */

public class JsonUtil {


    public static NewsDetailBean newsDetail(String json) {
        NewsDetailBean newsDetailBean = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            String body = jsonObject.optString("body");
            String img = "";
            if (jsonObject.has("image")) {
                img = jsonObject.optString("image");
            }
            String css = (String) jsonObject.optJSONArray("css").get(0);
            String share_url = jsonObject.optString("share_url");
            String title = jsonObject.optString("title");
            String image_source = jsonObject.optString("image_source");
            newsDetailBean = new NewsDetailBean(body, share_url, css, img, title, image_source);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsDetailBean;
    }

    public static List<NewsListsBean> newsLists(String json) {
        List<NewsListsBean> newsLists = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("stories");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                int id = jsonObject1.optInt("id");
                String title = jsonObject1.optString("title");
                String img = "";
                if (jsonObject1.has("images")) {
                    img = (String) jsonObject1.optJSONArray("images").get(0);
                }
                NewsListsBean newsListsBean = new NewsListsBean(id, title, img);
                newsLists.add(newsListsBean);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsLists;
    }

    public static List<NewsListsBean> top_NewsLists(String json) {
        List<NewsListsBean> newsLists = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("top_stories");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                int id = jsonObject1.optInt("id");
                String title = jsonObject1.optString("title");
                String img = "";
                if (jsonObject1.has("image")) {
                    img = jsonObject1.optString("image");
                }
                NewsListsBean newsListsBean = new NewsListsBean(id, title, img);
                newsLists.add(newsListsBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsLists;
    }

}
