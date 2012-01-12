package com.yamba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class StatusData {
	private static final String TAG = StatusData.class.getSimpleName();
	public static final String C_ID = BaseColumns._ID; // Special for id
	public static final String C_CREATED_AT = "yamba_createAT";
	public static final String C_USER = "yamba_user";
	public static final String C_TEXT = "yamba_txt";

	Context context;
	DbHelper dbHelper;

	public StatusData(Context context) {
		this.context = context;
		dbHelper = new DbHelper();
	}

	/**
	 * Inserts into database
	 * 
	 * @param values
	 *            Name/Value pairs data
	 */
	public long insert(ContentValues values) {
		// OPen Database
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		// Insert into table
		long ret = 0;
		
		try {
			db.insertOrThrow(DbHelper.TABLE, null, values);
		} catch (SQLException e) {
			ret = -1;
			e.printStackTrace();
		}

		// Close Database
		db.close();

		return ret;

	}

	/*
	 * Delete ALL the data
	 */

	public void delete() {
		// Open Database
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		// Delete Data
		db.delete(DbHelper.TABLE, null, null);

		// Close Database
		db.close();
	}

	/**
	 * Get the data for timeline activity
	 */
	public Cursor query() {
		// Open Database
		SQLiteDatabase db = dbHelper.getReadableDatabase();

		// Get Data
		Cursor c = db.query(DbHelper.TABLE, null, null, null, null, null,
				C_CREATED_AT + " DESC");

		return c;
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * Class to help open/create/upgrade database
	 * 
	 * @author Quynh
	 * 
	 */
	private class DbHelper extends SQLiteOpenHelper {

		public static final String DB_NAME = "timeline.db";
		public static final int DB_VERSION = 1;
		public static final String TABLE = "statuses";

		public DbHelper() {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			String sql = String
					.format("Create Table %s (%s INT PRIMARY KEY, %s INT, %s TEXT, %s TEXT)",
							TABLE, C_ID, C_CREATED_AT, C_USER, C_TEXT);
			Log.d(TAG, "onCreate sql: " + sql);

			// Execute the database
			db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("Drop Table if exists " + TABLE);
			Log.d(TAG, "Updagrade Drop Table");
			this.onCreate(db);

		}

	}

}
