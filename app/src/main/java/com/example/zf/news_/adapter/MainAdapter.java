package com.example.zf.news_.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zf.news_.R;
import com.example.zf.news_.bean.NewsListsBean;
import com.example.zf.news_.interfaces.OnLoadNewsListListener;
import com.example.zf.news_.model.NewsModel;
import com.example.zf.news_.myview.MyImgView;
import com.example.zf.news_.util.ImgHttpUtil;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Fy on 2018/2/28.
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<NewsListsBean> newsList;
    private mItemClickListener listener;
    private Bitmap mBitmap;
    private ViewPager viewPager;
    private List<NewsListsBean> top_news;
    private int currentItem = 0;
    private ScheduledExecutorService executorService;
    private Bundle viewpagerPosition = new Bundle();
    private LruCache<String, BitmapDrawable> mMemoryCache;

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_HEADER_SECOND = 2;


    public MainAdapter(Context context, List<NewsListsBean> list) {

        this.context = context;
        this.newsList = list;
        initTop_News();


        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.kulian);
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 6;
        mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();
            }
        };
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {

            return TYPE_HEADER;

        } else if (position == 1) {

            return TYPE_HEADER_SECOND;
        } else {
            return TYPE_ITEM;
        }

    }

    public interface mItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setItemClickListener(mItemClickListener itemClickListener) {
        listener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {

            case TYPE_HEADER:
                View headerView = inflater.inflate(R.layout.item_header, parent, false);
                return new HeaderViewHolder(headerView);

            case TYPE_HEADER_SECOND:
                View headerSecondView = inflater.inflate(R.layout.item_second_header, null);
                return new SecondHeaderViewHolder(headerSecondView);


            case TYPE_ITEM:
                View itemView = inflater.inflate(R.layout.news_item, null);
                return new MyViewHolder(itemView);

            default:
                return null;

        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof HeaderViewHolder) {

            HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
            NewsHeaderAdapter adapter = new NewsHeaderAdapter(context);
            viewPager = viewHolder.viewPager;
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(viewpagerPosition.getInt("position", position));
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    currentItem = position;
                    viewpagerPosition.putInt("position", position);
                }

                @Override
                public void onPageSelected(int position) {


                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }


        if (holder instanceof MyViewHolder) {

            NewsListsBean news = newsList.get(position);
            String imgUrl = newsList.get(position).getImg();

            if (news == null)
                return;
            if (imgUrl == "") {
                ((MyViewHolder) holder).imageView.setImageResource(R.drawable.kulian);
            } else {
                BitmapDrawable drawable = getBDfromCache(imgUrl);

                if (drawable != null) {
                    ((MyViewHolder) holder).imageView.setImageDrawable(drawable);
                } else if (cancelTask(imgUrl, ((MyViewHolder) holder).imageView)) {
                    //  execute download
                    DownLoadTask task = new DownLoadTask(((MyViewHolder) holder).imageView);
                    AsyncDrawable asyncDrawable = new AsyncDrawable(context.getResources(), mBitmap, task);
                    ((MyViewHolder) holder).imageView.setImageDrawable(asyncDrawable);
                    task.execute(imgUrl);
                }
                ((MyViewHolder) holder).imageView.setImageUrl(newsList.get(position).getImg());

            }
            ((MyViewHolder) holder).textView.setText(newsList.get(position).getTitle());
        }

        if (holder instanceof SecondHeaderViewHolder) {

            SecondHeaderViewHolder viewHolder = (SecondHeaderViewHolder) holder;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(holder.itemView, position);
                }
            }
        });


    }


    @Override
    public int getItemCount() {

        return newsList.size();
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if (holder instanceof HeaderViewHolder) {
            startSchedule();
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        if (holder instanceof HeaderViewHolder) {
            executorService.shutdown();
        }
    }

    private Handler topNewsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            top_news = (List<NewsListsBean>) msg.obj;

        }
    };

    private void initTop_News() {

        new NewsModel().loadTopNews(new OnLoadNewsListListener() {
            @Override
            public void onLoad(List<NewsListsBean> list) {
                Message message = Message.obtain();
                message.obj = list;
                topNewsHandler.sendMessage(message);

            }
        });

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }
    };

    private void startSchedule() {

        /**该方法返回一个可以控制线程池内线程定时或周期性执行某
         任务的线程池。该线程池大小为1，

         **/
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                currentItem = (currentItem + 1) % top_news.size();
                mHandler.obtainMessage().sendToTarget();
            }
        }, 3, 3, TimeUnit.SECONDS);
    }

    //   图片缓存


    private void addBitmapToCache(String imgUrl, BitmapDrawable drawable) {
        if (getBDfromCache(imgUrl) == null) {
            mMemoryCache.put(imgUrl, drawable);
        }

    }

    /**
     * 检查复用的ImageView中是否存在其他图片的下载任务，如果存在就取消并且返回ture 否则返回 false
     */

    private boolean cancelTask(String imgUrl, ImageView imageView) {
        DownLoadTask task = getDownloadTask(imageView);
        if (task != null) {
            String url = task.url;
            if (url == null || !url.equals(imgUrl)) {
                task.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    private BitmapDrawable getBDfromCache(String url) {
        return mMemoryCache.get(url);
    }

    private DownLoadTask getDownloadTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                return ((AsyncDrawable) drawable).getDownLoadTaskFromAD();
            }
        }
        return null;
    }


    private class AsyncDrawable extends BitmapDrawable {
        private WeakReference<DownLoadTask> downLoadTaskWeakReference;

        AsyncDrawable(Resources resources, Bitmap bitmap, DownLoadTask downLoadTask) {

            super(resources, bitmap);
            downLoadTaskWeakReference = new WeakReference<>(downLoadTask);
        }

        private DownLoadTask getDownLoadTaskFromAD() {
            return downLoadTaskWeakReference.get();
        }
    }

    private class DownLoadTask extends AsyncTask<String, Void, BitmapDrawable> {


        private String url;
        private WeakReference<ImageView> imageViewWeakReference;

        DownLoadTask(ImageView imageView) {

            imageViewWeakReference = new WeakReference<>(imageView);
        }

        @Override
        protected BitmapDrawable doInBackground(String... params) {

            url = params[0];
            Bitmap bitmap = ImgHttpUtil.loadImg(url);
            BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
            addBitmapToCache(url, drawable);

            return drawable;
        }

        /**
         * 验证ImageView 中的下载任务是否相同 如果相同就返回
         */
        private ImageView getAttachedImageView() {
            ImageView imageView = imageViewWeakReference.get();
            if (imageView != null) {
                DownLoadTask task = getDownloadTask(imageView);
                if (this == task) {
                    return imageView;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(BitmapDrawable drawable) {
            super.onPostExecute(drawable);
            ImageView imageView = getAttachedImageView();
            if (imageView != null && drawable != null) {
                imageView.setImageDrawable(drawable);
            }
        }


    }


    private class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        MyImgView imageView;

        MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.NewsTV);
            imageView = itemView.findViewById(R.id.NewsIV);

        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        ViewPager viewPager;

        HeaderViewHolder(View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.header_pager);

        }
    }

    private class SecondHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView secondHeaderTextView;
        ImageView dotImage;

        SecondHeaderViewHolder(View itemView) {
            super(itemView);
            secondHeaderTextView = itemView.findViewById(R.id.date_header);
            dotImage = itemView.findViewById(R.id.dot);
        }
    }
}
