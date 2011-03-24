package busradar.madison;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyLocations {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LON = "lon";    
    private static final String TAG = "MyLocations";
    
    private static final String DATABASE_NAME = "locations";
    private static final String DATABASE_TABLE = "favorites";
    private static final int DATABASE_VERSION = 5;

    private static final String DATABASE_CREATE =
        "create table "+ DATABASE_TABLE + " (_id integer primary key autoincrement, "
        + "name text not null, lat int not null, " 
        + "lon int not null, stopid int);";
        
    private final Context context; 
    
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public MyLocations(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            db.execSQL(DATABASE_CREATE);
            //db.execSQL("CREATE TABLE IF NOT EXISTS favorite_stops (stopid INT PRIMARY KEY)");
        }

        @Override public void 
        onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
        {
        	Log.w(TAG, "Upgrading database from version " + oldVersion 
                    + " to "+ newVersion);
        	
            //db.execSQL("CREATE TABLE IF NOT EXISTS favorite_stops (stopid INT PRIMARY KEY)");
        	db.execSQL("ALTER TABLE favorites ADD COLUMN stopid int");
        }
    }    
    
    //---opens the database---
    public MyLocations open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        //System.out.printf("opening database version %d\n", db.getVersion());
        return this;
    }

    public boolean isOpen()
    {
    	return db.isOpen();
    }
    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
    //---insert a title into the database---
    public long insertLocation(String name, int lat, int lon) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_LAT, lat);
        initialValues.put(KEY_LON, lon);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular title---
    public boolean deleteLocation(long rowId) 
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + 
        		"=" + rowId, null) > 0;
    }

    //---retrieves all the titles---
    public Cursor getAllLocations() 
    {
        return db.query(DATABASE_TABLE, new String[] {
        		KEY_ROWID, 
        		KEY_NAME,
        		KEY_LAT,
                KEY_LON,
                "stopid"}, 
                null, 
                null, 
                null, 
                null, 
                KEY_NAME);
    }

    //---retrieves a particular title---
    public Cursor getLocation(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                		KEY_ROWID,
                		KEY_NAME, 
                		KEY_LAT,
                		KEY_LON
                		}, 
                		KEY_ROWID + "=" + rowId, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a title---
    public boolean updateLocation(long rowId, String name, int lat, int lon) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_LAT, lat);
        args.put(KEY_LON, lon);
        return db.update(DATABASE_TABLE, args, 
                         KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public boolean
    is_stop_favorite(int stopid) {
    	
    	 Cursor cursor =
             db.rawQuery("SELECT COUNT(*) FROM favorites WHERE stopid = ?", 
            		 new String[] { stopid+"" }  );
    	 
    	 cursor.moveToFirst();
    	 boolean result = cursor.getInt(0) != 0;
    	 cursor.close();
    	 return result;
    }
    
    public void add_favorite_stop(int stopid, String name, int lat, int lon) {
    	
		db.execSQL("INSERT INTO favorites (name, lat, lon, stopid) VALUES (?, ?, ?, ?)", 
				new String[] { name, lat+"", lon+"", stopid+""} );
    }
    
    public void remove_favorite_stop(int stopid) {
		db.execSQL("DELETE FROM favorites WHERE stopid = ?", 
				new String[] { stopid + ""} );

    }

}
