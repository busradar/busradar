package com.dmc.demo1;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class BusStop {

int lat;
int lon;
char dir;
int id;

public BusStop(int _id, int _lat, int _lon, char _dir) 
{
	lat = _lat;
	lon = _lon;
	dir = _dir;
	id = _id;
}

}
