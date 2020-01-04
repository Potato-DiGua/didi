package com.example.didi.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "MySQLiteHelper";

    private static final int VERSION=1;
    private static final String DB_NAME="didi.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table cookie (host varchar(100),value varchar(100))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void readCookie(Map<String, List<Cookie>> cookieStore)
    {
        String[] forecast=null;
        SQLiteDatabase db=null;
        try {
            db=getReadableDatabase();
            Cursor cursor=db.query("cookie",null,null,null
                    ,null,null,null);
            Log.d("database",cursor.getCount()+"");
            while (cursor.moveToNext())
            {
                String host=cursor.getString(cursor.getColumnIndex("host"));
                String value=cursor.getString(cursor.getColumnIndex("value"));
                List<Cookie> list=cookieStore.get(host);
                if(list==null)
                {
                    list=new ArrayList<>();
                }
                cookieStore.put(host,list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.close();
        }
    }
}
