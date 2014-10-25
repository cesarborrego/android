package sferea.todo2day.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelperEvents extends SQLiteOpenHelper {	
	
	private static final String DB_NAME = "EVENTS.SQLite";
	private static final int DB_SCHEME_VERSION = 1;	

	public SQLiteHelperEvents(Context context) {
		super(context, DB_NAME, null, DB_SCHEME_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DataBaseSQLiteManagerEvents.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		db.execSQL("DROP TABLE IF EXISTS "+DB_NAME);
//        db.execSQL(DataBaseSQLiteManager.CREATE_TABLE);
	}
}
