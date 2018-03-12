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
import com.example.zf.news_.util.ImgHttpUtil;
import com.example.zf.news_.myview.MyImgView;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Fy on 2018/2/19.
 */

public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private LayoutInflater mInflater;
    private Context context;
    private List<NewsListsBean> newsList;
    private ItemClickListener listener;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private boolean mShowFooter = true;
    private Bitmap mBitmap;
    private LruCache<String, BitmapDrawable> mMemoryCache;


    public NewsListAdapter(Context context, List<NewsListsBean> list) {

        this.context = context;
        this.newsList = list;
        mInflater = LayoutInflater.from(context);

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

        if (!mShowFooter) {
            return TYPE_ITEM;
        }
        if (position + 1 == getItemCount()) {

            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }

    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        listener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case TYPE_ITEM:
                View itemView = mInflater.inflate(R.layout.news_item, null);
                return new MyViewHolder(itemView);

            case TYPE_FOOTER:
                View footerView = mInflater.inflate(R.layout.footer_view, null);
                return new FooterViewHolder(footerView);

            default:
                return null;

        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

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
        int begin = mShowFooter ? 1 : 0;
        if (newsList == null) {
            return begin;
        }
        return newsList.size() + begin;
    }


    public void isShowFooter(boolean mShowFooter) {
        this.mShowFooter = mShowFooter;
    }

    public boolean isShowFooter() {

        return this.mShowFooter;

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


    class AsyncDrawable extends BitmapDrawable {
        private WeakReference<DownLoadTask> downLoadTaskWeakReference;

        public AsyncDrawable(Resources resources, Bitmap bitmap, DownLoadTask downLoadTask) {

            super(resources, bitmap);
            downLoadTaskWeakReference = new WeakReference<>(downLoadTask);
        }

        private DownLoadTask getDownLoadTaskFromAD() {
            return downLoadTaskWeakReference.get();
        }
    }

    class DownLoadTask extends AsyncTask<String, Void, BitmapDrawable> {


        private String url;
        private WeakReference<ImageView> imageViewWeakReference;

        public DownLoadTask(ImageView imageView) {

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


    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        MyImgView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.NewsTV);
            imageView = itemView.findViewById(R.id.NewsIV);

        }
    }

}

