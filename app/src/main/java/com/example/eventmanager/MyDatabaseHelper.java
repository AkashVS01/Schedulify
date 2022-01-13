package com.example.eventmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Event.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "Task";
    private static final String COLUMN_ID = "_id";
    private static final String EVENT_NAME = "event_name";
    private static final String EVENT_DESC = "event_desc";
    private static final String DATE = "date";
    private static final String MONTH = "month";
    private static final String TIME = "time";
    private static final String REPEAT = "repeat";

    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    EVENT_NAME + " TEXT, " +
                    EVENT_DESC + " TEXT, " +
                    DATE + " TEXT," + MONTH + " TEXT, " + TIME + " TEXT," + REPEAT + " TEXT );";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addBook(String name, String desc,String date, String month ,String time,String repeat)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(EVENT_NAME, name);
        cv.put(EVENT_DESC, desc);
        cv.put(DATE, date);
        cv.put(MONTH,month);
        cv.put(TIME, time);
        cv.put(REPEAT, repeat);

        long result = db.insert(TABLE_NAME,null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    /*int retreive(String text)
    {
        String q = "select * from " + TABLE_NAME + " where " + EVENT_NAME +" = " + text;
        SQLiteDatabase db = this.getReadableDatabase();

        try
        {
            Cursor c = db.rawQuery(q,null);
            c.moveToFirst();
            int id = c.getInt(0);
            return id;
        }
        catch (Exception e)
        {

        }

        return 53;
    }*/



    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully deleted", Toast.LENGTH_SHORT).show();
            /*AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmBroadcast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(row_id),intent,0);
            am.cancel(pendingIntent);
            pendingIntent.cancel();*/

        }
    }

    void updateBook(String id, String name, String desc,String date, String month ,String time,String repeat)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(EVENT_NAME, name);
        cv.put(EVENT_DESC, desc);
        cv.put(DATE, date);
        cv.put(MONTH,month);
        cv.put(TIME, time);
        cv.put(REPEAT, repeat);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }
}
