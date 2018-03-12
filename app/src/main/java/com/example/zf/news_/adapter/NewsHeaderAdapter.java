package com.example.zf.news_.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zf.news_.R;
import com.example.zf.news_.activity.NewsDetailActivity;
import com.example.zf.news_.bean.NewsListsBean;
import com.example.zf.news_.interfaces.OnLoadNewsListListener;
import com.example.zf.news_.model.NewsModel;
import com.example.zf.news_.myview.MyImgView;
import com.example.zf.news_.util.JsonUtil;
import com.example.zf.news_.util.NewsHttpUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fy on 2018/2/28.
 */

public class NewsHeaderAdapter extends PagerAdapter {

    private Context context;
    private List<NewsListsBean> top_news;
    private LayoutInflater mInflater;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            top_news = (List<NewsListsBean>) msg.obj;
            notifyDataSetChanged();
        }
    };

    public NewsHeaderAdapter(Context context) {

        this.context = context;

        initData();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return top_news == null ? 0 : top_news.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        View view = mInflater.inflate(R.layout.news_item_viewpager, container, false);
        TextView title = view.findViewById(R.id.viewpager_title);
        MyImgView imageView = view.findViewById(R.id.viewpager_image);
        LinearLayout linearLayout = view.findViewById(R.id.image_indicator_layout);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("ID", top_news.get(position).getId());
                context.startActivity(intent);
            }
        });

        for (int i = 0; i < 5; i++) {
            ImageView dot = new ImageView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5, 5, 5, 5);
            dot.setImageResource(i == position ? R.drawable.dot_select : R.drawable.dot_);
            linearLayout.addView(dot);
        }

        title.setText(top_news.get(position).getTitle());
        imageView.setImageUrl(top_news.get(position).getImg());
        view.setVisibility(View.VISIBLE);
        Log.i("TAG", "" + position);
        //  imageView.setVisibility(View.VISIBLE);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);
    }

    private void initData() {

        new NewsModel().loadTopNews(new OnLoadNewsListListener() {
            @Override
            public void onLoad(List<NewsListsBean> list) {
                Message message = Message.obtain();
                message.obj = list;
                mHandler.sendMessage(message);
            }
        });
    }
}
