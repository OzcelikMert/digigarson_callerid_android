package com.digigarson.digigarsoncallerid.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.digigarson.digigarsoncallerid.services.web.check_version;

public class account extends SQLiteOpenHelper {
    private String version = "1.0";

    private static SQLiteDatabase db;
    private static String db_name = "db.db";
    private static String table_name = "account";
    private static String column_1 = "security_code";

    public account(@Nullable Context context){
        super(context, db_name, null, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        check_version();
        db.execSQL(String.format("create table IF NOT EXISTS %s(%s TEXT)", table_name, column_1));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", table_name));
        onCreate(db);
    }

    public boolean insert(String security_code){
        ContentValues contentValues = new ContentValues();
        contentValues.put(column_1, security_code);
        long result = db.insert(table_name, null, contentValues);
        return result != -1;
    }

    public Cursor get(){
        return db.rawQuery(String.format("select * from %s", table_name), null);
    }

    public boolean update(String security_code){
        ContentValues contentValues = new ContentValues();
        contentValues.put(column_1, security_code);
        db.update(table_name, contentValues, "", new String[] {});
        return true;
    }

    public void delete_all(){
        db.execSQL(String.format("delete from %s", table_name));
    }

    private void check_version(){
        if(!check_version.db.equals(version)){
            db.execSQL(String.format("DROP TABLE IF EXISTS %s", table_name));
            onCreate(db);
        }
    }
}
