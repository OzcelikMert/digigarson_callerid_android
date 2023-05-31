package com.digigarson.digigarsoncallerid.layout;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.digigarson.digigarsoncallerid.R;
import com.digigarson.digigarsoncallerid.problem_solution.protected_app.utils;
import com.digigarson.digigarsoncallerid.services.web.check_version;
import com.digigarson.digigarsoncallerid.services.web.login;
import com.digigarson.digigarsoncallerid.sqlite.account;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.Manifest.permission.READ_PHONE_NUMBERS;

public class main extends AppCompatActivity {

    EditText txt_security_code;
    public static TextView lbl_branch_name, lbl_device_name, lbl_version;
    Button btn_login;
    private account db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        utils.startPowerSaverIntent(this);
        initialize();
    }

    private void initialize(){
        set_permissions();
        set_inputs();
        lbl_version.setText(String.format("V.%s", com.digigarson.digigarsoncallerid.library.app.get_version(this)));
        new check_version(this).execute();
        set_events();
        this.db = new account(this);
        Cursor data = this.db.get();
        if(data.moveToNext()){
            this.txt_security_code.setText(data.getString(0));
            btn_login.performClick();
        }
    }

    private void set_inputs(){
        txt_security_code = findViewById(R.id.security_code);
        btn_login         = findViewById(R.id.login);
        lbl_branch_name   = findViewById(R.id.branch_name);
        lbl_device_name   = findViewById(R.id.device_name);
        lbl_version       = findViewById(R.id.version);
    }

    private void set_events(){
        final Context self = this;

        btn_login.setOnClickListener(v -> new login(self, txt_security_code.getText().toString()).execute());
    }

    public void set_permissions(){
        if (
                ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
        ) { request_permissions(); }
    }

    private void request_permissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.WRITE_CALL_LOG,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW
            }, 100);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requestPermissions(new String[]{
                    READ_PHONE_NUMBERS
            }, 100);
        }

    }
}
