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

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.google.android.maps.MyLocationOverlay;

public class G {

public static DisplayMetrics metrics;
	
static Bitmap bitmap_stop_west;
static Bitmap bitmap_stop_west_2s;
static Bitmap bitmap_stop_west_4s;
static Bitmap bitmap_stop_west_8s;
static Bitmap bitmap_stop_east;
static Bitmap bitmap_stop_east_2s;
static Bitmap bitmap_stop_east_4s;
static Bitmap bitmap_stop_east_8s;
static Bitmap bitmap_stop_north;
static Bitmap bitmap_stop_north_2s;
static Bitmap bitmap_stop_north_4s;
static Bitmap bitmap_stop_north_8s;
static Bitmap bitmap_stop_south;
static Bitmap bitmap_stop_south_2s;
static Bitmap bitmap_stop_south_4s;
static Bitmap bitmap_stop_south_8s;
static Bitmap bitmap_stop_nodir;
static Bitmap bitmap_stop_nodir_2s;
static Bitmap bitmap_stop_nodir_4s;
static Bitmap bitmap_stop_nodir_8s;

static Bitmap bitmap_bus_west;
static Bitmap bitmap_bus_east;
static Bitmap bitmap_bus_north;
static Bitmap bitmap_bus_northwest;
static Bitmap bitmap_bus_northeast;
static Bitmap bitmap_bus_south;
static Bitmap bitmap_bus_southwest;
static Bitmap bitmap_bus_southeast;
	
static QuadTree stops_tree;
static Route[] routes;

static Main activity;
static BusOverlay bus_overlay;


static SQLiteDatabase db;
static MyLocations favorites;

static LocationOverlay location_overlay;
static int active_route = -1;

static ArrayList<BusOverlay.BusLocation> bus_locs;
static BusLocator bus_locator = new BusLocator();

static boolean gps_enable;
static byte today = G.is_today_weekend_or_holiday() != null ? Route.HOLIDAY : Route.WEEKDAY;
static boolean first_time = true;
    
static boolean inited = false;
static long db_version = -1;
static String db_name = "undefined";


static void init(Main a) 
{
	activity = a;
	
	if (inited)
		return;
	
	metrics = a.getResources().getDisplayMetrics();
	
   	bitmap_stop_west = BitmapFactory.decodeResource(a.getResources(), R.drawable.stop_west);
   	bitmap_stop_west_2s = Bitmap.createScaledBitmap(bitmap_stop_west, 
            round(bitmap_stop_west.getWidth()/2.0), 
            round(bitmap_stop_west.getHeight()/2.0), true);
    bitmap_stop_west_4s = Bitmap.createScaledBitmap(bitmap_stop_west, 
            round(bitmap_stop_west.getWidth()/4.0), 
            round(bitmap_stop_west.getHeight()/4.0), true);
    bitmap_stop_west_8s = Bitmap.createScaledBitmap(bitmap_stop_west, 
            round(bitmap_stop_west.getWidth()/8.0), 
            round(bitmap_stop_west.getHeight()/8.0), true);
   	
   	bitmap_stop_north = BitmapFactory.decodeResource(a.getResources(), R.drawable.stop_north);
   	bitmap_stop_north_2s = Bitmap.createScaledBitmap(bitmap_stop_north, 
            round(bitmap_stop_north.getWidth()/2.0), 
            round(bitmap_stop_north.getHeight()/2.0), true);
    bitmap_stop_north_4s = Bitmap.createScaledBitmap(bitmap_stop_north, 
            round(bitmap_stop_north.getWidth()/4.0), 
            round(bitmap_stop_north.getHeight()/4.0), true);        
    bitmap_stop_north_8s = Bitmap.createScaledBitmap(bitmap_stop_north, 
            round(bitmap_stop_north.getWidth()/8.0), 
            round(bitmap_stop_north.getHeight()/8.0), true);        
    
   	bitmap_stop_south = BitmapFactory.decodeResource(a.getResources(), R.drawable.stop_south);
   	bitmap_stop_south_2s = Bitmap.createScaledBitmap(bitmap_stop_south, 
            round(bitmap_stop_south.getWidth()/2.0), 
            round(bitmap_stop_south.getHeight()/2.0), true);
    bitmap_stop_south_4s = Bitmap.createScaledBitmap(bitmap_stop_south, 
            round(bitmap_stop_south.getWidth()/4.0), 
            round(bitmap_stop_south.getHeight()/4.0), true);
    bitmap_stop_south_8s = Bitmap.createScaledBitmap(bitmap_stop_south, 
            round(bitmap_stop_south.getWidth()/8.0), 
            round(bitmap_stop_south.getHeight()/8.0), true);        
            
   	bitmap_stop_east = BitmapFactory.decodeResource(a.getResources(), R.drawable.stop_east);
   	bitmap_stop_east_2s = Bitmap.createScaledBitmap(bitmap_stop_east, 
            round(bitmap_stop_east.getWidth()/2.0), 
            round(bitmap_stop_east.getHeight()/2.0), true);
    bitmap_stop_east_4s = Bitmap.createScaledBitmap(bitmap_stop_east, 
            round(bitmap_stop_east.getWidth()/4.0), 
            round(bitmap_stop_east.getHeight()/4.0), true);
    bitmap_stop_east_8s = Bitmap.createScaledBitmap(bitmap_stop_east, 
            round(bitmap_stop_east.getWidth()/8.0), 
            round(bitmap_stop_east.getHeight()/8.0), true);
            
   	bitmap_stop_nodir = BitmapFactory.decodeResource(a.getResources(), R.drawable.stop_nodir);
   	bitmap_stop_nodir_2s = Bitmap.createScaledBitmap(bitmap_stop_nodir, 
            round(bitmap_stop_nodir.getWidth()/2.0), 
            round(bitmap_stop_nodir.getHeight()/2.0), true);
    bitmap_stop_nodir_4s = Bitmap.createScaledBitmap(bitmap_stop_nodir, 
            round(bitmap_stop_nodir.getWidth()/4.0), 
            round(bitmap_stop_nodir.getHeight()/4.0), true);
   	bitmap_stop_nodir_8s = Bitmap.createScaledBitmap(bitmap_stop_nodir, 
            round(bitmap_stop_nodir.getWidth()/8.0), 
            round(bitmap_stop_nodir.getHeight()/8.0), true);

   	bitmap_bus_west = BitmapFactory.decodeResource(a.getResources(), R.drawable.bus_west);
   	bitmap_bus_north = BitmapFactory.decodeResource(a.getResources(), R.drawable.bus_north);
   	bitmap_bus_northeast = BitmapFactory.decodeResource(a.getResources(), R.drawable.bus_northeast);
   	bitmap_bus_northwest = BitmapFactory.decodeResource(a.getResources(), R.drawable.bus_northwest);
   	bitmap_bus_south = BitmapFactory.decodeResource(a.getResources(), R.drawable.bus_south);
   	bitmap_bus_southeast = BitmapFactory.decodeResource(a.getResources(), R.drawable.bus_southeast);
   	bitmap_bus_southwest = BitmapFactory.decodeResource(a.getResources(), R.drawable.bus_southwest);
   	bitmap_bus_east = BitmapFactory.decodeResource(a.getResources(), R.drawable.bus_east);
   	
	try {
		//long t1 = System.currentTimeMillis();
		InputStream file = a.getResources().openRawResource(R.raw.stops);
		DataInputStream in = new DataInputStream(file);
		//stops_tree = (QuadTree)in.readObject();
		stops_tree = new QuadTree(in);
		
		in.close();
		file.close();
		
		file = a.getResources().openRawResource(R.raw.routes);
		in = new DataInputStream(file);
		
		routes = new Route[in.readInt()];
		for (int i = 0; i < routes.length; i++)
		{
				routes[i] = new Route(in);
		}
	
		//long t2 = System.currentTimeMillis();
		//System.out.printf("Time to unseralize %d\n", t2 - t1);
		
		in.close();
		file.close();
		
		String path = a.getFilesDir().getParent() + "/db.sqlite";
		
		try {
			db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
			Cursor c = db.rawQuery("SELECT version, name FROM db_version", null);
			c.moveToFirst();
			long version = c.getLong(0);
			String name = c.getString(1);
			c.close();
			
			if (version < config.database_version) {
				//System.out.println("BusRadar: Updating DB");
				throw new SQLiteException();
			}
			G.db_version = version;
			G.db_name = name;
		}
		catch (SQLiteException e)
		{
			try {
				db.close();
			} catch (Exception _) {}
			
			InputStream db_is = a.getResources().openRawResource(R.raw.db);
	    	OutputStream db_os = new FileOutputStream(path, false);
	 
	    	//transfer bytes from the input file to the output file
	    	byte[] buffer = new byte[1024*8];
	    	int length;
	    	while ((length = db_is.read(buffer))>0) {
	    		db_os.write(buffer, 0, length);
	    	}
	 
	    	//Close the streams
	    	db_os.flush();
	    	db_os.close();
	    	db_is.close();
	    	
			db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
			
			Cursor c = db.rawQuery("SELECT version, name FROM db_version", null);
			c.moveToFirst();
			int version = c.getInt(0);
			String name = c.getString(1);
			c.close();
			
			G.db_version = version;
			G.db_name = name;

		}
	
	} catch (Exception e) { throw new RuntimeException(e); }
	
	inited = true;
	
}

static int dot(Point A, Point B, Point C)
{
    int ABx = B.x - A.x;
    int ABy = B.y - A.y;
    int BCx = C.x - B.x;
    int BCy = C.y - B.y;
    
    return ABx*BCx + ABy*BCy;
}


static int cross(Point A, Point B, Point C)
{
    int ABx = B.x - A.x;
    int ABy = B.y - A.y;
    
    int ACx = C.x - A.x;
    int ACy = C.y - A.y;
    
    return ABx*ACy - ABy*ACx;
}


static double dist(Point A, Point B){
    int d1 = A.x - B.x;
    int d2 = A.y - B.y;
    
    return sqrt(d1*d1 + d2*d2);
}

static int round(double n) {
    return (int) Math.round(n);
}


static double pt_to_line_segment_dist(Point A, Point B, Point C) {       
    if (dot(A, B, C) > 0)
    	return dist(B, C);
    if (dot(B, A, C) > 0)
    	return dist(A, C);
    
    return abs(cross(A, B, C) / dist(A, B));
}

static void 
toast(String msg)
{
	Toast.makeText(G.activity, msg, Toast.LENGTH_SHORT).show();
}

static void 
toast_long(String msg)
{
	Toast.makeText(G.activity, msg, Toast.LENGTH_LONG).show();
}

static int count = 1;

static String
is_today_weekend_or_holiday()
{
	GregorianCalendar today = new GregorianCalendar();
	
	if (HolidayChecker.New_Years_Day_Observed(today))
		return "New Year's Day";
	
	if (HolidayChecker.Memorial_Day(today))
		return "Memorial Day";
	
	if (HolidayChecker.Independence_Day_Observed(today))
		return "Independence Day";
	
	if (HolidayChecker.Labor_Day(today))
		return "Labor Day";
	
	if (HolidayChecker.Thanksgiving_Day(today))
		return "Thanksgiving Day";
	
	if (HolidayChecker.Christmas_Day_Observed(today))
		return "Christmas Day";
	
	if (HolidayChecker.Thanksgiving_Day_After(today))
		return "the day after Thanksgiving";
	
	int day_of_week = today.get(Calendar.DAY_OF_WEEK);
	
	if (day_of_week == Calendar.SATURDAY)
		return "";
	if (day_of_week == Calendar.SUNDAY)
		return "";
	
	return null;
}

static final int dp2px(int dp) {
	return (int) Math.round(dp * metrics.density);
}

}
