package com.digigarson.digigarsoncallerid.services.web;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ContextThemeWrapper;

import com.digigarson.digigarsoncallerid.R;
import com.digigarson.digigarsoncallerid.layout.main;
import com.digigarson.digigarsoncallerid.library.device;
import com.digigarson.digigarsoncallerid.library.variable;
import com.digigarson.digigarsoncallerid.sqlite.account;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class set extends AsyncTask<String, Void, Void> {
    private String security_code, phone;
    private boolean status;
    private Context context;
    private String TAG = "services_web_set";

    public set(Context context, String security_code, String phone){
        Log.e(TAG, "OKAY!");
        this.security_code = security_code;
        this.phone = phone;
        this.status = false;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected Void doInBackground(String... values) {
        try {
            _set();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) { }

    private void _set(){
        try {
            String URL = String.format("%s/functions/set.php", values.url_app());

            JSONObject data = new JSONObject();
            data.put("security_code", this.security_code);
            data.put("token", device.get_serial_number());
            data.put("phone", this.phone);

            Log.e(TAG, "OKAY - 2!");
            // URL Connection
            java.net.URL url = new URL(URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            //httpURLConnection.setChunkedStreamingMode(0);

            Log.e(TAG, "OKAY - 3!");
            // Set POST Values
            OutputStream outputStream = httpURLConnection.getOutputStream();
            Log.e(TAG, "OKAY - 3.1!");
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            Log.e(TAG, "OKAY - 3.2!");
            bufferedWriter.write(variable.get_post_data_string(data));
            Log.e(TAG, "OKAY - 3.3!");
            bufferedWriter.flush();
            Log.e(TAG, "OKAY - 3.4!");
            bufferedWriter.close();
            Log.e(TAG, "OKAY - 3.5!");
            outputStream.close();
            // end Set POST Values

            Log.e(TAG, "OKAY - 4!");
            // Get Values
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1));

            String result = "";
            String line = "";

            Log.e(TAG, "OKAY - 5!");
            while((line = bufferedReader.readLine())!= null) {
                result += line;
            }

            bufferedReader.close();
            inputStream.close();
            // end Get Values

            httpURLConnection.disconnect();
            // end URL Connection

            Log.e(TAG, "Set Result: " + result);

            // Json Result
            if(!result.equals("")){
                JSONObject responseJSON = new JSONObject(result);
                //this.status = responseJSON.getBoolean("status");
            }
            // end Json Result

        } catch (MalformedURLException e) {
            Log.e(TAG, "Error-1 " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "Error-2 " + e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e(TAG, "Error-3 " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "Error-4 " + e.getMessage());
            e.printStackTrace();
        }
    }
}
