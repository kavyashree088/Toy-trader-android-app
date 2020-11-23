package com.example.toytrader;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DBManager extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "ToyTrader.db";
    public static final String TOY_TABLE_NAME = "toy";
    public static final String TOY_ID = "id";
    public static final String TOY_NAME = "name";
    public static final String TOY_TAGS = "tags";
    public static final String TOY_DESCRIPTION = "description";
    public static final String TOY_COST = "cost";
    public static final String TOY_IMAGE = "image";
    public static final String TOY_DATETIME = "datetime";
    public static final String TOY_LOCATION = "location";
    public static final String USER_ID = "userid";
    public static final String USER_NAME = "username";

    public DBManager( Context context) {
        super(context, DATABASE_NAME,null , 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {



        db.execSQL(
                "create table toy( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "tags TEXT NOT NULL, " +
                        "description TEXT NOT NULL," +
                        "cost DOUBLE NOT NULL," +
                        "image TEXT NOT NULL," +
                        "datetime REAL NOT NULL," +
                        "location TEXT NOT NULL," +
                        "userid INTEGER NOT NULL," +
                        "username TEXT NOT NULL)"
        );


        /*
        db.execSQL(
                "create table toy( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, email TEXT NOT NULL, userid INTEGER NOT NULL)"
        );

         */

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS toy");
        onCreate(db);

    }

    //insert into database
    public String insertEmployee(String name, String email, int number) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("userid", number);
        Long res = db.insert("toy", null, contentValues);
        if (res == 1)
            return "failed";
        else
            return "toy added";
    }

    //insert into database
    public String insertToy(String name, String tag, String description, double cost, String image, String location, int userid, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("tags", tag);
        contentValues.put("description", description);
        contentValues.put("cost",cost);
        contentValues.put("image", image);
        contentValues.put("datetime", dateFormat.format(date));
        contentValues.put("location",location);
        contentValues.put("userid",userid);
        contentValues.put("username",username);
        Long res = db.insert("toy", null, contentValues);
        if (res == 1)
            return "failed";
        else
            return "toy added";




    }

}

