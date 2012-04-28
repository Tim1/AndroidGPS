package de.timweb.android.util;

import de.timweb.android.activity.R;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {
	private Context context;
	private String name = "DB_NAME";
	private int version = 1;

	public DatabaseManager(Context context) {
		super(context, 
				context.getResources().getString(R.string.db_name),null,
				Integer.parseInt(context.getResources().getString(R.string.db_version)));

		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		for(String sql : context.getResources().getStringArray(R.array.db_create))
			database.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldversion, int newversion) {
		// TODO Auto-generated method stub

	}

}
