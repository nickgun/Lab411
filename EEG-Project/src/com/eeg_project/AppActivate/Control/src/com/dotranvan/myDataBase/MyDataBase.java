package com.dotranvan.myDataBase;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBase extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "trando.db";
	public static final String EEG_DATA = "eeg_data";
	public static final String ID = "id";
	public static final String KEY = "key";
	public static final String COMMAND = "command";

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_CREAT = "Create table " + EEG_DATA
			+ " (" + ID + "  text primary key ," + KEY + " text not null, "
			+ COMMAND + " text not null);";
	private Context context;

	public MyDataBase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_CREAT);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists eeg_data");
		onCreate(db);
	}

	public String selectAll(String data) {
		String result = null;
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("Select * from eeg_data where id = ?",
				new String[] { data });
		while (cursor.moveToNext()) {
			result = cursor.getString(2);
			return result;
		}
		return null;

	}

	public ArrayList<String> selectAll() {
		ArrayList<String> result = new ArrayList<String>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("Select * from eeg_data", null);
		while (cursor.moveToNext()) {
			String temp = cursor.getString(0);
			result.add(temp);
		}
		return result;
	}

	public void insert(String id, String key, String command) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ID, id);
		values.put(KEY, key);
		values.put(COMMAND, command);
		// Inserting Row
		db.insert(EEG_DATA, null, values);
		db.close(); // Closing database connection

	}

}
