package com.digigarson.digigarsoncallerid.services.app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.digigarson.digigarsoncallerid.sqlite.account;

public class set extends Service {

    public Context context;
    private String TAG = "services_app_set";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        Log.e(TAG, "OKAY!");
        try {
            String phone = intent.getStringExtra("phone");
            account db = new account(context);
            Cursor data = db.get();
            if (data.moveToNext()) {
                new com.digigarson.digigarsoncallerid.services.web.set(context, data.getString(0), phone).execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
