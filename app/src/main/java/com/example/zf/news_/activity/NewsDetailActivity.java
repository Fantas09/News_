package com.example.zf.news_.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.zf.news_.R;
import com.example.zf.news_.task.LoadNewsDetail;

public class NewsDetailActivity extends AppCompatActivity {

    private WebView webView;
    private Toolbar toolbar;
    private Integer newsId;
    private static String share = "http://daily.zhihu.com/story/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        webView = (WebView) findViewById(R.id.Wvt);
        toolbar = (Toolbar) findViewById(R.id.new_detail_bar);
        //       webView.getSettings().setJavaScriptEnabled(true);

        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        Integer id = intent.getIntExtra("ID", 0);
        newsId = id;
        new LoadNewsDetail(webView).execute(id);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the me nu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.share:
                Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, share + newsId);
                startActivity(Intent.createChooser(intent, "分享"));
                break;
            case R.id.good:
                Toast.makeText(this, "赞", Toast.LENGTH_SHORT).show();
                break;
            case R.id.comments:
                Toast.makeText(this, "评论", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }


        return super.onOptionsItemSelected(item);
    }


}
