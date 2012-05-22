package busradar.madison;

import java.util.Date;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
class Route
{
	private int id;
	private Stop[] stops;
	private Date[][] stopTime;
	private String name;
	private int bus;
	
	public Route(int i, int b, String n)
	{
		super();
		id = i;
		name = n;
		bus = b;
	}
	
	@SuppressWarnings("unused")
	private Route()
	{
		super();
		name = "";
		bus = 0;
	}
	
	public int getID()
	{
		return id;
	}
	
	public Stop[] getStops()
	{
		if (stops == null)
		{
			if (db == null) return null;
			String[] param = {String.valueOf(id)};
			Cursor select = db.rawQuery("SELECT [Stop].[_ID] FROM [Stop] INNER JOIN [Route] ON [Stop].[_ID] = [Route].[Stop] WHERE [Route].[Route] = ? ORDER BY [Route].[Stop_Number]", param);
			select.moveToFirst();
			stops = new Stop[select.getCount()];
			for (int x = 0; x < stops.length; x++)
			{
				stops[x] = Stop.getStop(select.getInt(0));
				select.moveToNext();
			}
			select.close();
			stopTime = new Date[stops.length][];
		}
		return stops;
	}
	
	public Date[] getStopTimes(int s)
	{
		if (stopTime[s] == null)
		{
			if (db == null) return null;
		}
		return stopTime[s];
	}
	
	public String getName()
	{
		return name;
	}
	
	public int busNumber()
	{
		return bus;
	}
	
	public static Route[] getAllRoutes()
	{
		if (allRoutes == null)
		{
			if (db == null) return null;
			Cursor select = db.rawQuery("SELECT [Bus].[_ID], [Bus].[Number], [Bus].[Name] FROM [Bus] ORDER BY [Bus].[_ID]", null);
			select.moveToFirst();
			allRoutes = new Route[select.getCount()];
			for (int x = 0; x < allRoutes.length; x++)
			{
				allRoutes[x] = new Route(select.getInt(0), select.getInt(1),select.getString(2));
				select.moveToNext();
			}
			select.close();
		}
		return allRoutes;
	}
	
	public static Route getRoute(int id)
	{
		Route[] r = getAllRoutes();
		for (int x = 0; x < r.length; x++)
			if (r[x].getID() == id) return r[x];
		return null;
	}
	
	public static void setDatabase(SQLiteDatabase d)
	{
		db = d;
	}
	
	public String toString()
	{
		return name;
	}
	
	private static SQLiteDatabase db;
	private static Route[] allRoutes;
}