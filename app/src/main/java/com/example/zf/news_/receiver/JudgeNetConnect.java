package com.example.zf.news_.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by Fy on 2018/2/27.
 */

public class JudgeNetConnect extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {

                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI ||
                            info.getType() == ConnectivityManager.TYPE_MOBILE) {
                        Toast.makeText(context, "已连接网络", Toast.LENGTH_SHORT).show();
                        Log.i("TAG", "connected" + info.getType());
                    }
                } else {
                    Log.i("TAG", "not connect");
                    Toast.makeText(context, "无网络 请检查网络设置", Toast.LENGTH_SHORT).show();
                    Intent netIntent = new Intent("android.settings.WIRELESS_SETTINGS");
                    context.startActivity(netIntent);
                }
            }
        }
    }
}
