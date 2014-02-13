// Copyright (C) 2010 Aleksandr Dobkin, Michael Choi, and Christopher Mills.
// 
// This file is part of BusRadar <https://github.com/orgs/busradar/>.
// 
// BusRadar is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 3 of the License, or
// (at your option) any later version.
// 
// BusRadar is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

package busradar.madison;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper{
 
    //The Android's default system path of your application database.
    private static String DB_PATH;
 
    private static String DB_NAME = "bus";
 
    private static SQLiteDatabase myDataBase; 
 
    private final Context myContext;
//    
//	public static final int NORTH = 1;
//	
//	public static final int EAST = 2;
//	
//	public static final int SOUTH = 3;
//	
//	public static final int WEST = 4;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DB(Context context) {
 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
    }	
 
    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
    	}else{

        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transferring byte stream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local database as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty database
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty database as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the input file to the output file
    	byte[] buffer = new byte[8*1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
 
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

//	public static int[] getStops(int busID)
//	{
//		if (myDataBase == null) return null;
//		String[] param = {String.valueOf(busID)};
//		Cursor select = myDataBase.rawQuery("SELECT [Stop].[_ID] FROM [Stop] INNER JOIN [Route] ON [Stop].[_ID] = [Route].[Stop] WHERE [Route].[Stop_ID] = ? ORDER BY [Route].[Stop_Number]", param);
//		select.moveToFirst();
//		int[] stops = new int[select.getCount()];
//		for (int x = 0; x < stops.length; x++)
//		{
//			stops[x] = select.getInt(0);
//			select.moveToNext();
//		}
//		select.close();
//		return stops;
//	}


	public static String getStopName(int stopID)
	{
		String[] param = {String.valueOf(stopID)};
		Cursor select = G.db.rawQuery("SELECT [Stop].[Name] FROM [Stop] WHERE [Stop].[_ID] = ?", param);
		select.moveToFirst();
		String name = select.getCount() > 0 ? select.getString(0) : "Unknown name";
		select.close();
		return name;
	}
	
//	public static int[] getAllRoutes()
//	{
//		if (myDataBase == null) return null;
//		Cursor select = myDataBase.rawQuery("SELECT [Bus].[_ID] FROM [Bus] ORDER BY [Bus].[_ID]", null);
//		select.moveToFirst();
//		int[] allRoutes = new int[select.getCount()];
//		for (int x = 0; x < allRoutes.length; x++)
//		{
//			allRoutes[x] = select.getInt(0);
//		select.moveToNext();
//		}
//		select.close();
//		return allRoutes;
//	}
//	
//	public static int[] getRoutes(int stopID)
//	{
//		if (myDataBase == null) return null;
//		String[] param = {String.valueOf(stopID)};
//		Cursor select = myDataBase.rawQuery("SELECT [Bus].[_ID] FROM [Bus] INNER JOIN [Route] ON [Bus].[_ID] = [Route].[Route] WHERE [Route].[Stop] = ? ORDER BY [Bus].[_ID]", param);
//		select.moveToFirst();
//		int[] routes = new int[select.getCount()];
//		for (int x = 0; x < routes.length; x++)
//		{
//			routes[x] = select.getInt(0);
//			select.moveToNext();
//		}
//		select.close();
//		return routes;
//	}
//	
//	public static Date[] getBusTime(int stopID, int routeID)
//	{
//		return new Date[0];
//	}
	
//	public static String getHTML(int stopID, int routeID)
//	{
//		if (myDataBase == null) return null;
//		String[] param = {String.valueOf(routeID), String.valueOf(stopID)};
//		Cursor select = myDataBase.rawQuery("SELECT [Route].[HTML] FROM [Route] WHERE ([Route].[Route] = ? AND [Route].[Stop] = ?)", param);
//		select.moveToFirst();
//		String html = select.getString(0);
//		select.close();
//		return html;
//	}
	
//	public static Date[][] getBusTimes(int stopID)
//	{
//		if (myDataBase == null) return null;
//		int[] routes = getRoutes(stopID);
//		Date[][] busTime = new Date[routes.length][];
//		for (int b = 0; b < routes.length; b++)
//		{
//			String[] param = {String.valueOf(routes[b]), String.valueOf(stopID)};
//			Cursor select = myDataBase.rawQuery("SELECT [Time].[Time] FROM [Time] INNER JOIN [Route] ON [Time].[Time_ID] = [Route].[_ID] WHERE ([Route].[Route] = ? AND [Route].[Stop] = ?) ORDER BY [Time].[Time]", param);
//			select.moveToFirst();
//			busTime[b] = new Date[select.getCount()];
//			for (int x = 0; x < busTime[b].length; x++)
//			{
//				busTime[b][x] = new Date(select.getString(0));
//				select.moveToNext();
//			}
//			select.close();
//		}
//		return busTime;
//	}
	
//	public static int[] getAllStops()
//	{
//		if (myDataBase == null) return null;
//		Cursor select = myDataBase.rawQuery("SELECT [Stop].[_ID] FROM [Stop] ORDER BY [Stop].[_ID]", null);
//		select.moveToFirst();
//		int[] allStops = new int[select.getCount()];
//		for (int x = 0; x < allStops.length; x++)
//		{
//			allStops[x] = select.getInt(0);
//			select.moveToNext();
//		}
//		select.close();
//		return allStops;
//	}

}
