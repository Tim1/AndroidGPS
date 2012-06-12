package de.timweb.android.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.timweb.android.activity.R;


/*
 * Mit DatabaseManager werden die Datenbanken definiert und erstellt, gemanaged eben.
 * Es ist nicht die Dantenbank an sich ! Datenbanken werden nur durch diese Klasse verwaltet,
 * deswegen acuh database als parameter in den Methoden !
 * */


public class DatabaseManager extends SQLiteOpenHelper {
	private Context context;

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
		if(oldversion == 3 && newversion == 4){
			database.execSQL(context.getResources().getString(R.string.db_alterTable_3to4));
			return;
		}
		
		for(String sql : context.getResources().getStringArray(R.array.db_dropTable))
			database.execSQL(sql);
		onCreate(database);
	}

}
