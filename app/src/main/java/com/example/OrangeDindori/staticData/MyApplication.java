package com.example.OrangeDindori.staticData;

import android.app.Application;
import android.content.Context;
import android.content.Intent;


public class MyApplication extends Application {

    private static MyApplication mInstance2 =null;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance2 = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance2;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}