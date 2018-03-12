package com.example.zf.news_.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.zf.news_.R;
import com.example.zf.news_.fragments.MainFragment;
import com.example.zf.news_.fragments.ViewPagerFragment;
import com.example.zf.news_.interfaces.MainView;
import com.example.zf.news_.receiver.JudgeNetConnect;


public class MainActivity extends AppCompatActivity implements MainView, NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(new JudgeNetConnect(), intentFilter);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toMain();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the me nu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.setting) {
            Toast.makeText(MainActivity.this, "settings", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.day_night) {
            Toast.makeText(MainActivity.this, "day_night", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.share) {
            Toast.makeText(MainActivity.this, "share", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
//     double click exit
//    private long firstTime = 0;
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        long secondTime = System.currentTimeMillis();
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (secondTime - firstTime < 2000) {
//                System.exit(0);
//            } else {
//                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//                firstTime = System.currentTimeMillis();
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void toMain() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout, new MainFragment()).commit();
        toolbar.setTitle("首页");
    }

    @Override
    public void toNews() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout, new ViewPagerFragment()).commit();
        toolbar.setTitle("新闻");
    }

    @Override
    public void toImg() {

    }

    @Override
    public void toAbout() {

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.main:
                Toast.makeText(this, "首页", Toast.LENGTH_SHORT).show();
                toMain();
                break;
            case R.id.news:
                Toast.makeText(MainActivity.this, "新闻", Toast.LENGTH_SHORT).show();
                toNews();
                break;
            case R.id.img:
                toImg();
                break;
            default:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
