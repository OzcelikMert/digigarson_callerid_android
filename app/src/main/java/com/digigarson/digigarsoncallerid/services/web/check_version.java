package com.digigarson.digigarsoncallerid.services.web;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ContextThemeWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.digigarson.digigarsoncallerid.R;

public class check_version extends AsyncTask<String, Void, Void> {
    public static String app = "1.0.0";
    public static String db = "1.0";

    private Context context;
    private ProgressDialog progressDialog;

    public check_version(Context context){
        this.context = context;
        this.progressDialog = new ProgressDialog(new ContextThemeWrapper(this.context, R.style.Theme_AppCompat_DayNight_Dialog));
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setCancelable(false);
        progressDialog.setMessage(context.getString(R.string.waiting));
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(String... values) {
        try {
            _get();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        progressDialog.dismiss();
        progressDialog = null;
        _check();
    }

    private void _get(){
        try {
            String URL = String.format("%s/version.json", values.url_app());

            // URL Connection
            java.net.URL url = new URL(URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            // Get Values
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1));

            String result="";
            String line="";

            while((line = bufferedReader.readLine())!= null) {
                result += line;
            }

            bufferedReader.close();
            inputStream.close();
            // end Get Values

            httpURLConnection.disconnect();
            // end URL Connection

            Log.e("CheckVersion", "Version Result: " + result);

            // Json Result
            if(!result.equals("")){
                JSONObject responseJSON = new JSONObject(result);
                app = responseJSON.getString("app");
                db  = responseJSON.getString("db");
            }
            // end Json Result

        } catch (MalformedURLException e) {
            Log.e("CheckVersion", "Error-1 " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("CheckVersion", "Error-2 " + e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("CheckVersion", "Error-3 " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void _check(){
        if(!com.digigarson.digigarsoncallerid.library.app.get_version(context).equals(app)){
            com.digigarson.digigarsoncallerid.library.app.get_alert_dialog(
                    context,
                    context.getString(R.string.update_message_title),
                    String.format("%s (%s)", context.getString(R.string.update_message), app),
                    false,
                    true,
                    context.getString(R.string.update),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Try Again Button
                            dialog.dismiss();
                            // Update
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            //http://play.google.com/store/apps/details?id=<package_name>
                            intent.setData(Uri.parse("market://details?id=com.digigarson.digigarsoncallerid"));
                            context.startActivity(intent);
                        }
                    },
                    true,
                    context.getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Cancel Button
                            ((Activity)context).finish();
                        }
                    });
        }
    }
}
