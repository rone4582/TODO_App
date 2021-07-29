package com.myapplication.todoapp.db;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {

    private Context context;
    private static DatabaseClient INSTANCE;

    //database object
    private AppDatabase appDatabase;

    private DatabaseClient(Context context){
        this.context = context;

        //creating app database with Room database builder
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "TODOs").build();

    }

    public static synchronized DatabaseClient getInstance(Context context){
        if (INSTANCE == null){

            INSTANCE = new DatabaseClient(context);
        }
        return INSTANCE;
    }

    public AppDatabase getAppDatabase(){
        return appDatabase;
    }

}
