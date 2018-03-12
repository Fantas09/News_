package com.example.zf.news_.bean;

import java.io.Serializable;

/**
 * Created by Fy on 2018/2/13.
 */

public class NewsListsBean implements Serializable {

    private Integer id;
    private String date;
    private String title;
    private String img;

    public NewsListsBean(Integer id, String title, String img) {
        this.id = id;
        this.title = title;
        this.img = img;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
