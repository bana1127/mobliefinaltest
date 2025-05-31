package com.example.afinal

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "testdb", null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("Create table USER_TB (_id integer primary key autoincrement, name text not null, phone text)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists USER_TB")
        onCreate(db)
    }
}