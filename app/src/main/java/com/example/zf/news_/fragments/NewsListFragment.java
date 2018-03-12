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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zf.news_.R;
import com.example.zf.news_.activity.NewsDetailActivity;
import com.example.zf.news_.adapter.NewsListAdapter;
import com.example.zf.news_.bean.NewsListsBean;
import com.example.zf.news_.interfaces.OnLoadNewsListListener;
import com.example.zf.news_.model.NewsModel;
import com.example.zf.news_.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Fy on 2018/2/18.
 */

public class NewsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private List<NewsListsBean> list = new ArrayList<>();
    private LinearLayoutManager lm;
    private NewsListAdapter adapter;
    private int pageNum = 1;
    private int mType = ViewPagerFragment.NEWS_TOP;
    private SwipeRefreshLayout refresh;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            List<NewsListsBean> listsBeen = (List<NewsListsBean>) msg.obj;
            list.addAll(listsBeen);
            adapter.notifyDataSetChanged();

        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            list.clear();
            list = (List<NewsListsBean>) msg.obj;
            adapter = new NewsListAdapter(getActivity(), list);
            adapter.setItemClickListener(new NewsListAdapter.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                    intent.putExtra("ID", list.get(position).getId());
                    startActivity(intent);
                }
            });
            if (mType != 0) {
                adapter.isShowFooter(false);
            }

            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    };
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            lastVisibleItem = lm.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount() && adapter.isShowFooter() && mType == 0) {
                (pageNum)++;
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.US);
                String getDate = DateUtil.getOtherDateString(null, 1 - pageNum, format);

                new NewsModel().loadBeforeNews(getDate, new OnLoadNewsListListener() {
                    @Override
                    public void onLoad(List<NewsListsBean> list) {
                        Message message = Message.obtain();
                        message.obj = list;
                        handler.sendMessage(message);
                    }
                });

            }
        }
    };

    public static NewsListFragment getFragment(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newslist, null);
        recyclerView = view.findViewById(R.id.recycle_view);
        lm = new LinearLayoutManager
                (getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.addItemDecoration
                (new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(lm);

        refresh = view.findViewById(R.id.swipe_refresh_widget);
        refresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        refresh.setOnRefreshListener(this);

        this.mType = getArguments().getInt("type");

        recyclerView.addOnScrollListener(mOnScrollListener);

        initData();

        return view;
    }

    @Override
    public void onRefresh() {

        refresh.setRefreshing(true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
                Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
            }
        }, 1000);

    }

    private void initData() {

        new NewsModel().loadLatestNews(mType, new OnLoadNewsListListener() {
            @Override
            public void onLoad(List<NewsListsBean> list) {
                Message message = Message.obtain();
                message.obj = list;

                mHandler.sendMessage(message);
            }
        });


    }


}
