package com.voice.voicerecorder;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionStatus {
    public boolean Connectivity_status(Context context){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo
                (ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            //we are connected to a network
            connected = true;
            System.out.println("connection status"+connected);
        }
        else{
            connected = false;
            System.out.println("connection status"+connected);
        }
        return connected;
    }
}
