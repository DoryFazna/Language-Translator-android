package com.example.mobilecoursework2;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import static android.provider.BaseColumns._ID;
import static com.example.mobilecoursework2.Constants.ALL_LANGUAGES;
import static com.example.mobilecoursework2.Constants.CODE;
import static com.example.mobilecoursework2.Constants.LANGUAGE;
import static com.example.mobilecoursework2.Constants.PHRASE;
import static com.example.mobilecoursework2.Constants.SUBSCRIBED;
import static com.example.mobilecoursework2.Constants.TABLE_NAME_LANGUAGE;
import static com.example.mobilecoursework2.Constants.TABLE_NAME_PHRASE;
import static java.sql.Types.BOOLEAN;

public class DBController extends SQLiteOpenHelper {

    String[] allLanguagesWithCodes ;


    private static final String DATABASE_NAME = "TranslatorAPP.db";
    private static final int DATABASE_VERSION = 1;

    public DBController(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String CREATE_TABLE_PHRASES ="CREATE TABLE "+TABLE_NAME_PHRASE+"("
            +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +PHRASE+" TEXT UNIQUE );";

    private static final String CREATE_TABLE_LANGUAGES ="CREATE TABLE "+TABLE_NAME_LANGUAGE+"("
            +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +LANGUAGE+" TEXT UNIQUE,"
            +CODE+" TEXT,"
            +SUBSCRIBED+ " TEXT);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LANGUAGES);
        db.execSQL(CREATE_TABLE_PHRASES);
        allLanguagesWithCodes = ALL_LANGUAGES;

        setData(db);





    }

    public void setData(SQLiteDatabase db){
        for(String language: allLanguagesWithCodes){
            insertLanguage(language.split(",")[0], language.split(",")[1],db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_PHRASE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_LANGUAGE);
        onCreate(db);
    }

    public void insertPhrase( String phrase){
        ContentValues contentValues = new ContentValues();
        contentValues.put(PHRASE, phrase);
        this.getWritableDatabase().insertOrThrow(TABLE_NAME_PHRASE,"",contentValues);
    }

    public void insertLanguage( String language, String code, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(LANGUAGE, language);
        contentValues.put(SUBSCRIBED, "false");
        contentValues.put(CODE, code);
        db.insertOrThrow(TABLE_NAME_LANGUAGE,"",contentValues);
    }

    public void deletePhrase(String phrase){
        this.getWritableDatabase().delete("PHRASES","PHRASE ='"+phrase+"'",null);
    }

    public void updatePhrase(String oldPhrase, String newPhrase) throws NullPointerException{
        if(!(newPhrase.equals("")))
            this.getWritableDatabase().execSQL("UPDATE PHRASES SET PHRASE ='"+newPhrase +"' WHERE PHRASE ='"+oldPhrase+"'");
        else
            throw new NullPointerException();

    }

    public void updateSubscription(String language,String isSubscribed) throws NullPointerException{
        this.getWritableDatabase().execSQL("UPDATE LANGUAGES SET SUBSCRIBED ='"+isSubscribed +"' WHERE LANGUAGE ='"+language+"'");

    }

    public Cursor listAllPhrases(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM PHRASES",null);
        return cursor;
    }

    public Cursor listAllSubscribedLanguages(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM "+TABLE_NAME_LANGUAGE+" WHERE SUBSCRIBED = 'true'",null);
        return cursor;
    }

    public Cursor listAllLanguages(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM "+TABLE_NAME_LANGUAGE,null);
        return cursor;
    }

    public String getCode(String language){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM "+TABLE_NAME_LANGUAGE+" WHERE LANGUAGE ='"+language+"'",null);
        cursor.moveToNext();
        return cursor.getString(2);
    }
}
