package com.example.zf.news_.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zf.news_.R;
import com.example.zf.news_.activity.NewsDetailActivity;
import com.example.zf.news_.adapter.MainAdapter;
import com.example.zf.news_.adapter.NewsListAdapter;
import com.example.zf.news_.bean.NewsListsBean;
import com.example.zf.news_.interfaces.OnLoadNewsListListener;
import com.example.zf.news_.model.NewsModel;
import com.example.zf.news_.util.DateUtil;
import com.example.zf.news_.util.JsonUtil;
import com.example.zf.news_.util.NewsHttpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Fy on 2018/2/28.
 */

public class MainFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<NewsListsBean> list = new ArrayList<>();
    private LinearLayoutManager lm;
    private MainAdapter adapter;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            List<NewsListsBean> listsBeen = (List<NewsListsBean>) msg.obj;
            list.addAll(listsBeen);
            adapter = new MainAdapter(getActivity(), list);
            adapter.setItemClickListener(new MainAdapter.mItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                    intent.putExtra("ID", list.get(position).getId());
                    startActivity(intent);

                }
            });
            adapter.notifyDataSetChanged();

            recyclerView.setAdapter(adapter);

        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_article_list, container, false);

        recyclerView = view.findViewById(R.id.articleList);
        lm = new LinearLayoutManager
                (getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.addItemDecoration
                (new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(lm);

        new NewsModel().loadLatestNews(0, new OnLoadNewsListListener() {
            @Override
            public void onLoad(List<NewsListsBean> list) {
                Message message = Message.obtain();
                message.obj = list;

                mHandler.sendMessage(message);

            }
        });

        return view;
    }

}
