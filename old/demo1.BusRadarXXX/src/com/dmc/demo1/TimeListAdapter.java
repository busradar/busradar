package com.dmc.demo1;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.format.DateFormat;
import android.widget.ArrayAdapter;

public class TimeListAdapter extends ArrayAdapter<String> {

	private Date[] times;
	private int[] bus;

	public TimeListAdapter(Context context, int textViewResourceId,
			Date[][] t, Route[] b, int x) {
		super(context, textViewResourceId);
		times = new Date[x];
		bus = new int[x];
		for (int l = 0; l < t.length;l++)
		  for (int i = 0;  i < t[l].length; i++)
		  {
			Calendar c = Calendar.getInstance();
			c.set(2000, 1, 1, t[l][i].getHours(), t[l][i].getMinutes(), 0);
			Calendar d = Calendar.getInstance();
			d.set(2000, 1, 1, d.getTime().getHours(), d.getTime().getMinutes(), 0);
			if (c.getTime().after(d.getTime()))
			  for (int j = 0; j < x; j++)
			  {
				if (times[j] == null)
				{
					bus[j] = b[l].busNumber();
					times[j] = t[l][i];
					j = x;
				}
				else if (times[j].after(t[l][i]))
				{
					for (int k = x - 1; k > j; k--)
					{
						times[k] = times[k-1];
						bus[k] = bus[k-1];
					}
					times[j] = t[l][i];
					bus[j] = b[l].busNumber();
				}
			  }
		  }
		for (int i = 0; i < x; i++)
		{
			
			if (times[i] != null) super.add(String.valueOf(bus[i]).concat(" - ".concat(DateFormat.format("h:mm aa", times[i]).toString())));
		}
	}

}
