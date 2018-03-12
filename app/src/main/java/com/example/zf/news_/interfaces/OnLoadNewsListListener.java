package com.example.zf.news_.interfaces;

import com.example.zf.news_.bean.NewsListsBean;

import java.util.List;

/**
 * Created by Fy on 2018/3/9.
 */

public interface OnLoadNewsListListener {

    void onLoad(List<NewsListsBean> list);

}
