package com.dmc.demo1;

import java.util.Date;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Stop
{
	int lat;
	int lon;
	String dir;
	int stopID;
	private Route[] routes;
	private String name;
	private Date[][] busTime;
	
	public Stop(int id, int lt, int ln, int d, String n)
	{
		super();
		lat = lt;
		lon = ln;
		//dir = d;
		stopID = id;
		name = n;
	}
	
	@SuppressWarnings("unused")
	private Stop()
	{
		super();
		lat = 45;
		lon = 90;
//		dir = NORTH;
		stopID = 0;
		name = "";
	}
	
	public double latitude()
	{
		return lat;
	}

	public double longitude()
	{
		return lon;
	}

	
	public double id()
	{
		return stopID;
	}
	
	public Route[] getRoutes()
	{
		if (routes == null)
		{
			if (db == null) return null;
			String[] param = {String.valueOf(stopID)};
			Cursor select = db.rawQuery("SELECT [Bus].[_ID] FROM [Bus] INNER JOIN [Route] ON [Bus].[_ID] = [Route].[Route] WHERE [Route].[Stop] = ? ORDER BY [Bus].[_ID]", param);
			select.moveToFirst();
			routes = new Route[select.getCount()];
			for (int x = 0; x < routes.length; x++)
			{
				routes[x] = Route.getRoute(select.getInt(0));
				select.moveToNext();
			}
			select.close();
			busTime = new Date[routes.length][];
		}
		return routes;
	}
	
	public Date[] getBusTime(int b)
	{
		if (busTime[b] == null)
		{
			if (db == null) return null;
			String[] param = {String.valueOf(routes[b].getID()), String.valueOf(stopID)};
			Cursor select = db.rawQuery("SELECT [Time].[Time] FROM [Time] INNER JOIN [Route] ON [Time].[_ID] = [Route].[_ID] WHERE ([Route].[Route] = ? AND [Route].[Stop] = ?) ORDER BY [Time].[_ID]", param);
			select.moveToFirst();
			busTime[b] = new Date[select.getCount()];
			for (int x = 0; x < routes.length; x++)
			{
				busTime[b][x] = new Date(select.getString(0));
				select.moveToNext();
			}
			select.close();
		}
		return busTime[b];
	}
	
	public Date[][] getBusTimes()
	{
		getRoutes();
		for (int b = 0; b < busTime.length; b++)
		if (busTime[b] == null)
		{
			if (db == null) return null;
			String[] param = {String.valueOf(routes[b].getID()), String.valueOf(stopID)};
			Cursor select = db.rawQuery("SELECT [Time].[Time] FROM [Time] INNER JOIN [Route] ON [Time].[Time_ID] = [Route].[_ID] WHERE ([Route].[Route] = ? AND [Route].[Stop] = ?) ORDER BY [Time].[Time]", param);
			select.moveToFirst();
			busTime[b] = new Date[select.getCount()];
			for (int x = 0; x < busTime[b].length; x++)
			{
				busTime[b][x] = new Date(select.getString(0));
				select.moveToNext();
			}
			select.close();
		}
		return busTime;
	}
	
	public String myName()
	{
		return name;
	}
	
	public String toString()
	{
		return name;
	}
	
	public static Stop[] getAllStops()
	{
		if (allStops == null)
		{
			if (db == null) return null;
			Cursor select = db.rawQuery("SELECT [Stop].[_ID], [Stop].[Latitude], [Stop].[Longitude], [Stop].[Direction], [Stop].[Name] FROM [Stop] ORDER BY [Stop].[_ID]", null);
			select.moveToFirst();
			allStops = new Stop[select.getCount()];
			for (int x = 0; x < allStops.length; x++)
			{
				//middle two was originally double
				allStops[x] = new Stop(select.getInt(0), select.getInt(1), select.getInt(2), select.getInt(3), select.getString(4));
				select.moveToNext();
			}
			select.close();
		}
		return allStops;
	}
	
	public static Stop getStop(int id)
	{
		Stop[] s = getAllStops();
		for (int x = 0; x < s.length; x++)
			if (s[x].id() == id) return s[x];
		return null;
	}
	
	public static void setDatabase(SQLiteDatabase d)
	{
		db = d;
	}
	
	private static Stop[] allStops;
	private static SQLiteDatabase db;
	public static final int NORTH = 1;
	public static final int EAST = 2;
	public static final int SOUTH = 3;
	public static final int WEST = 4;
}