package com.digigarson.digigarsoncallerid;

import android.app.Application;
import android.content.Context;

public class caller_id extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        caller_id.context = getApplicationContext();
    }

    public static Context getContext() {
        return caller_id.context;
    }
}
