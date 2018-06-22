package com.voice.voicerecorder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class WifiReceiver extends BroadcastReceiver {

    Time now = new Time();


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        File root = new File(Environment.getExternalStorageDirectory(), "Recordings");
        if (!root.exists()) {
            root.mkdirs();
        }
        String existingFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Recordings/";
        File f = new File(existingFileName);
        File[] contents = f.listFiles();

        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        System.out.println("wifi state"+info.getDetailedState());

        if(info != null && info.isConnected()) {
            if(contents.length!=0 ){
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new FileUpload().execute();
            }
        }
    }
}
