package com.example.zf.news_.bean;

/**
 * Created by Fy on 2018/2/13.
 */

public class NewsDetailBean {

    private String body;
    private String share_url;
    private String css;
    private String img;
    private String title;
    private String image_source;

    public NewsDetailBean(String body, String share_url, String css, String img, String title, String image_source) {
        this.body = body;
        this.share_url = share_url;
        this.css = css;
        this.img = img;
        this.title = title;
        this.image_source = image_source;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }
}
