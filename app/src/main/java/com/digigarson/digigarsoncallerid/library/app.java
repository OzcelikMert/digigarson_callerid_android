package com.digigarson.digigarsoncallerid.library;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.ContextThemeWrapper;
import android.webkit.CookieManager;

import com.digigarson.digigarsoncallerid.R;

public class app {

    public static String get_version(Context context){
        String AppVersion;
        try {
            // Get Version
            String packageName = context.getPackageName();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            AppVersion = packageInfo.versionName;
            // end Get Version
        }catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
            AppVersion = "1.0.0";
        }

        return AppVersion;
    }

    public static String get_cookie(String siteName,String CookieName){
        String CookieValue = null;

        CookieManager cookieManager = CookieManager.getInstance();
        String cookies = cookieManager.getCookie(siteName);
        if(cookies != null){
            String[] temp=cookies.split(";");
            for (String ar1 : temp ){
                if(ar1.contains(CookieName)){
                    String[] temp1=ar1.split("=");
                    CookieValue = temp1[1];
                }
            }
        }
        return CookieValue;
    }

    public static Notification.Builder start_notification(
            Context context,
            Class aClass,
            Integer icon,
            String title,
            String contentText,
            Boolean vibration,
            long[] vibrationPattern,
            Boolean led,
            Integer[] lightsValues,
            Uri sound
    ){
        // Create Notification
        Notification.Builder builder = new Notification.Builder(context);
        // end Create Notification

        /*// Intent Settings
        Intent intent = new Intent(context, aClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        // end Intent Settings*/

        // Notification Settings
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setWhen(System.currentTimeMillis());
        //builder.setContentIntent(pendingIntent);
        builder.setContentTitle(title);
        builder.setSmallIcon(icon);
        builder.setContentText(contentText);
        builder.setAutoCancel(false);
        builder.setOngoing(false);
        builder.setSound(sound);
        builder.setVibrate((vibration) ? vibrationPattern : null);
        if(led){
            builder.setLights(lightsValues[0], lightsValues[1], lightsValues[2]);
        }
        // end Notification Settings

        // Return
        return builder;
    }

    public static void get_alert_dialog(
            Context context,
            String title,
            String message,
            Boolean cancelable,
            Boolean positiveButton,
            String positiveButtonText,
            DialogInterface.OnClickListener positiveFunction,
            Boolean negativeButton, String negativeButtonText,
            DialogInterface.OnClickListener negativeFunction
    ){
        // Create Dialog Message
        AlertDialog.Builder dialog = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.Theme_AppCompat_DayNight_Dialog));

        // Set Info
        dialog.setMessage(message)
                .setTitle(title)
                .setCancelable(cancelable);

        // Set Positive Button
        if(positiveButton){
            dialog.setPositiveButton(positiveButtonText, positiveFunction);
        }

        // Set Negative Button
        if(negativeButton){
            dialog.setNegativeButton(negativeButtonText, negativeFunction);
        }

        // Show Dialog
        dialog.create().show();
    }

    public static void change_layout(Context context, Class aClass){
        Intent intent = new Intent(context, aClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
