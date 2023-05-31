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

public class login extends AsyncTask<String, Void, Void> {
    private String security_code, branch_name, device_name;
    private boolean status;
    private Context context;
    private ProgressDialog progressDialog;
    private account db;
    private String TAG = "services_web_set";

    public login(Context context, String security_code){
        this.branch_name = "";
        this.status = false;
        this.security_code = security_code;
        this.context = context;
        this.progressDialog = new ProgressDialog(new ContextThemeWrapper(this.context, R.style.Theme_AppCompat_DayNight_Dialog));
        this.db = new account(this.context);
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
        _set();
    }

    private void _get(){
        try {
            String URL = String.format("%s/functions/login.php", values.url_app());

            JSONObject data = new JSONObject();
            data.put("security_code", this.security_code);
            data.put("token", device.get_serial_number());
            Log.e(TAG, "_get: " + device.get_serial_number() + " | security_code:  " + this.security_code);
            // URL Connection
            java.net.URL url = new URL(URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            // Set POST Values
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            bufferedWriter.write(variable.get_post_data_string(data));
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            // end Set POST Values

            // Get Values
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.ISO_8859_1));

            String result = "";
            String line = "";

            while((line = bufferedReader.readLine())!= null) {
                result += line;
            }

            bufferedReader.close();
            inputStream.close();
            // end Get Values

            httpURLConnection.disconnect();
            // end URL Connection

            Log.e(TAG, "Login Result: " + result);

            // Json Result
            if(!result.equals("")){
                JSONObject responseJSON = new JSONObject(result);
                this.branch_name = responseJSON.getString("branch_name");
                this.device_name = responseJSON.getString("device_name");
                this.status = responseJSON.getBoolean("status");
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

    private void _set(){
        if(this.status){
            this.db.delete_all();
            this.db.insert(this.security_code);
        }else{
            this.branch_name = this.context.getString(R.string.wrong_security_code);
        }
        main.lbl_branch_name.setText(this.branch_name);
        main.lbl_device_name.setText(this.device_name);
    }
}
