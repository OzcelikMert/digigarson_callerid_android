package com.digigarson.digigarsoncallerid.library;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaDrm;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.UUID;

public class device {
    private static String TAG = "Trial";

    @SuppressLint("MissingPermission")
    public static String get_serial_number() {
        String sRet = "";

        UUID WIDEVINE_UUID = new UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L);
        MediaDrm mediaDrm = null;

        try {
            mediaDrm = new MediaDrm(WIDEVINE_UUID);
            byte[] widevineId = mediaDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(widevineId);

            sRet = bytesToHex(md.digest()); //we convert byte[] to hex for our purposes
        } catch (Exception e) {
            //WIDEVINE is not available
            Log.e(TAG, "getWidevineSN.WIDEVINE is not available");
            return null;
        } finally {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if(null!=mediaDrm) {
                    mediaDrm.close();
                }
            } else {
                if(null!=mediaDrm) {
                    mediaDrm.release();
                }
            }
        }

        return sRet;
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static Boolean check_ethernet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
