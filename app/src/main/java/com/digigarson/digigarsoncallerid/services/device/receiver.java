package com.digigarson.digigarsoncallerid.services.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.digigarson.digigarsoncallerid.sqlite.account;

public class receiver extends BroadcastReceiver {

    private static String TAG = "services_device_receiver";
    private static int old_state = 0;

    @Override
    public void onReceive(final Context context, Intent intent) {
        try{

            TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            assert telephony != null;
            telephony.listen(new PhoneStateListener(){
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    super.onCallStateChanged(state, incomingNumber);
                    if(state == 1 && old_state != 1) {
                        Log.e(TAG, String.format("ON Receive %s", incomingNumber));
                        account db = new account(context);
                        Cursor data = db.get();
                        if (data.moveToNext()) {
                            new com.digigarson.digigarsoncallerid.services.web.set(context, data.getString(0), incomingNumber).execute();
                        }
                    }
                    old_state = state;
                }
            },PhoneStateListener.LISTEN_CALL_STATE);
        }catch (Exception e){
            Log.e(TAG, "onReceive: " + e);
        }

    }
}
