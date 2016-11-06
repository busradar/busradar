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

import java.util.ArrayList;
import java.util.Arrays;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

import static busradar.madison.G.*;

class BusOverlay extends com.google.android.maps.Overlay 
    implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener  {

MapView map_view;
final static int touch_allowance = 20; 
GeoPoint selection;
int zoom_level = 0;

GestureDetector gesture_detector;

static class BusLocation {
	public GeoPoint loc;
	public int heading;
}

BusOverlay(MapView map_view) {	
	gesture_detector = new GestureDetector(this);
	gesture_detector.setOnDoubleTapListener(this);
	this.map_view = map_view;
}


static final Paint paint = new Paint(); {
	paint.setColor(0xffff0000);
}

static final int stroke_width = dp2px(5);
static final int stroke_width_2s = round(stroke_width/2.0);
static final int stroke_width_4s = round(stroke_width/4.0);
static final int stroke_width_8s = round(stroke_width/8.0);

static final Paint line_paint = new Paint() {{
	setStrokeWidth(dp2px(5));
	setAntiAlias(true);
	setColor(0x90ff0000);
	setStyle(Paint.Style.STROKE);
	setStrokeJoin(Paint.Join.ROUND);
	setStrokeCap(Paint.Cap.ROUND);
}};

static final Paint text_paint = new Paint() {{
	float size = getTextSize();
	setTextSize( metrics.density * size * 1.0f );
	setTypeface(Typeface.create(getTypeface(), Typeface.BOLD));
	setAntiAlias(true);
	setColor(0xff000000);
}};

static final Paint label_paint = new Paint() {{
	setAntiAlias(true);
	setColor(0xB0ffffff);
}};

static final Paint circle_paint = new Paint() {{
	this.setColor(0x90FFFF99);
	this.setStrokeWidth(0);
	this.setStyle(Style.FILL);
	this.setAntiAlias(true);
}};

@Override public final void 
draw(Canvas canvas, MapView map, boolean shadow) 
{	
	if (shadow)
		return;
	
	Projection proj = map.getProjection();
	Point p1 = new Point();
	Point p2 = new Point();
	Point point = new Point();
	
	
	int lonspan = map.getLongitudeSpan();
	int latspan = map.getLatitudeSpan();
	GeoPoint center = map.getMapCenter();
	
	
	double pixel = (double) map.getLongitudeSpan() / map.getWidth();
	
	int minlon = center.getLongitudeE6() - lonspan / 2;
	int maxlon = center.getLongitudeE6() + lonspan / 2;
	int minlat = center.getLatitudeE6() - latspan / 2;
	int maxlat = center.getLatitudeE6() + latspan / 2;
	Point min = new Point(0, 0);
	Point max = new Point(map.getWidth()-1, map.getHeight()-1);
	//proj.toPixels(new GeoPoint(maxlat, minlon), min);
	//proj.toPixels(new GeoPoint(minlat, maxlon), max);
	
	//ProxyGeoPoint pgp = new ProxyGeoPoint(0, 0);
	
	int x;
	if (zoom_level != (x = map.getZoomLevel()))
		selection = null;
		
	if (selection != null) {
		
		proj.toPixels(selection, point);
		//canvas.drawPoint(point.x, point.y, line_paint);
		canvas.drawCircle(point.x, point.y, Math.round(touch_allowance*metrics.density), circle_paint);
	}
	zoom_level = x;
    int zoomLevel = map.getZoomLevel();
	
	//if (zoomLevel <= 15)
    //    line_paint.setStrokeWidth(stroke_width_8s);
    //else if (zoomLevel == 16)
    //    line_paint.setStrokeWidth(stroke_width_4s);
    if (zoomLevel <= 15)
        line_paint.setStrokeWidth(stroke_width_2s);
    else
        line_paint.setStrokeWidth(stroke_width);
    
	if (G.active_route >= 0) { 
		ArrayList<RouteTree.Line> lines = new ArrayList<RouteTree.Line>();
        line_paint.setColor(0x90000000 | G.routes[G.active_route].color);
        
        RouteTree tree = G.routes[G.active_route].tree;
        tree.find(
            round(minlon-10*pixel), round(minlat-10*pixel), 
            round(maxlon+10*pixel), round(maxlat+10*pixel), lines);
        //tree.find(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, lines);
        //System.out.printf("BusRadar:  tree find %s, %d, %dn %d total=%d leaves=%d\n",
        //    minlon-5*pixel, minlat-5*pixel, maxlon+5*pixel, maxlat+5*pixel,
        //    lines.size(), tree.getNumberOfLeaves());
        
        Path path = new Path();
        for (RouteTree.Line line : lines) {
            
            proj.toPixels(new GeoPoint(line.lat1, line.lon1), p1);
            proj.toPixels(new GeoPoint(line.lat2, line.lon2), p2);
            
            path.moveTo(p1.x, p1.y);
            path.lineTo(p2.x, p2.y);
            
            //canvas.drawLine(p1.x, p1.y, p2.x, p2.y, line_paint);
            
            //canvas.drawCircle(p1.x, p1.y, 3, paint);
            //canvas.drawCircle(p2.x, p2.y, 3, paint);
        }
        canvas.drawPath(path, line_paint);
	}
	
	//paint.setColor(0xffff0000);
	if (zoomLevel >= 14) {
		ArrayList<QuadTree.Element> geopoints = G.stops_tree.get(
                round(minlon-dp2px(32)*pixel), round(minlat-dp2px(32)*pixel), 
                round(maxlon+dp2px(32)*pixel), round(maxlat+dp2px(32)*pixel));
		//System.out.printf("draw %d points\n", geopoints.size());
		
		for (QuadTree.Element geopoint : geopoints ) {
			if (G.active_route >= 0 && Arrays.binarySearch(geopoint.routes, G.active_route) < 0)
				continue;
			
			//pgp.lat = geopoint.lat; pgp.lon = geopoint.lon;
			//proj.toPixels(pgp, point);
			
			proj.toPixels(new GeoPoint(geopoint.lat, geopoint.lon), point);
			
			//canvas.drawCircle(point.x, point.y, 3, paint);
			
			Bitmap b;
			switch (geopoint.dir) {
				case 'N': 
					b = G.bitmap_stop_north;
					if (zoomLevel <= 15)
                        b = G.bitmap_stop_north_8s;
                    else if (zoomLevel == 16)
                        b = G.bitmap_stop_north_4s;
                    else if (zoomLevel == 17)
                        b = G.bitmap_stop_north_2s;
					canvas.drawBitmap(b, point.x, point.y-b.getHeight()/2, paint);
					break;
					
				case 'S': 
					b = G.bitmap_stop_south; 
					if (zoomLevel <= 15)
                        b = G.bitmap_stop_south_8s;
                    else if (zoomLevel == 16)
                        b = G.bitmap_stop_south_4s;
                    else if (zoomLevel == 17)
                        b = G.bitmap_stop_south_2s;
					canvas.drawBitmap(b, point.x-b.getWidth(), point.y-b.getHeight()/2, paint);
					break;
					
				case 'E': 
					b = G.bitmap_stop_east;
					if (zoomLevel <= 15)
                        b = G.bitmap_stop_east_8s;
                    else if (zoomLevel == 16)
                        b = G.bitmap_stop_east_4s;
                    else if (zoomLevel == 17)
                        b = G.bitmap_stop_east_2s;
					canvas.drawBitmap(b, point.x-b.getWidth()/2, point.y, paint);
					break;
					
				case 'W': 
					b = G.bitmap_stop_west;
					if (zoomLevel <= 15)
                        b = G.bitmap_stop_west_8s;
                    else if (zoomLevel == 16)
                        b = G.bitmap_stop_west_4s;
                    else if (zoomLevel == 17)
                        b = G.bitmap_stop_west_2s;
					canvas.drawBitmap(b, point.x-b.getWidth()/2, point.y-b.getHeight(), paint);
					break;
					
				default: 
					b = G.bitmap_stop_nodir;
					if (zoomLevel <= 15)
                        b = G.bitmap_stop_nodir_8s;
                    else if (zoomLevel == 16)
                        b = G.bitmap_stop_nodir_4s;
                    else if (zoomLevel == 17)
                        b = G.bitmap_stop_west_2s;
					canvas.drawBitmap(b, point.x-b.getWidth()/2, point.y-b.getHeight(), paint);
			}

		}
	}
	
	if (G.bus_locs != null) {
		int upper_right_count = 0;
		int upper_left_count = 0;
		int lower_right_count = 0;
		int lower_left_count = 0;
		
		for (BusOverlay.BusLocation bus_loc : G.bus_locs) {			
			proj.toPixels(bus_loc.loc, point);
			if (point.x < min.x || point.x > max.x || point.y < min.y || point.y > max.y) {
			
                int heading = bus_loc.heading;
                char dir = ' ';
                if (heading < (0+22) || bus_loc.heading >= (315+22)) {
                    dir = '↑';
                } else if (heading < (45+22)) {
                    dir = '↗';
                } else if (heading < (90+22)) {
                    dir = '→';
                } else if (heading < (135+22)) {
                    dir = '↘';
                } else if (heading < (180+22)) {
                    dir = '↓';
                }  else if (heading < (225+22)) {
                    dir = '↙';
                } else if (heading < (270+22)) {
                    dir = '←';
                } else if (heading < (315+22)) {
                    dir = '↖';
                }
                
                double dist_meters = dist(center.getLatitudeE6()/1.E6, center.getLongitudeE6()/1.E6, 
                        bus_loc.loc.getLatitudeE6()/1.E6, bus_loc.loc.getLongitudeE6()/1.E6);
                
                double dist_miles = dist_meters * 0.000621371192;
                String msg = String.format("%.2f mi%c", dist_miles, dir);
				//GeoPoint gps = map.getMapCenter(); //G.location_overlay.getMyLocation();
				//float[] results = new float[1];
				//Location.distanceBetween(gps.getLatitudeE6()/1.E6, gps.getLongitudeE6()/1.E6, 
				//			bus_loc.loc.getLatitudeE6()/1.E6, bus_loc.loc.getLongitudeE6()/1.E6, results);
								
				if (point.x > max.x) {
					if (point.y < min.y) {
						draw_text_right(canvas, msg, max.x, min.y + dp2px(13+16*upper_right_count++));
					}
					else if (point.y > max.y - dp2px(13)) {
						draw_text_right(canvas, msg, max.x, max.y - dp2px(45+16*lower_right_count++));
					}
					else {
						draw_text_right(canvas, msg, max.x, point.y);
					}
				}
				else if (point.x < min.x) {
					if (point.y < min.y) {
						draw_text_left(canvas, msg, min.x+dp2px(3), min.y+dp2px(13+16*upper_left_count++));
					}
					else if (point.y > max.y - dp2px(13)) {
						draw_text_left(canvas, msg, min.x+dp2px(3), max.y - dp2px(45+16*lower_left_count++));
					}
					else {
						draw_text_left(canvas, msg, min.x+dp2px(3), point.y);
					}
					
				}
				else if (point.y > max.y) {
					draw_text_left(canvas, msg, point.x, max.y-dp2px(45));
				}
				else { //if (point.y < min.y) {
					draw_text_left(canvas, msg, point.x, min.y+dp2px(13));
				}
				
				continue;
			}
				
			Bitmap b;
			int heading = bus_loc.heading;
			if (heading < (0+22) || heading >= (315+22)) {
                b = G.bitmap_bus_north;
                canvas.drawBitmap(b, point.x, point.y-b.getHeight()/2, paint);
            } else if (heading < (45+22)) {
                b = G.bitmap_bus_northeast;
                canvas.drawBitmap(b, point.x-b.getWidth()/2, point.y-b.getHeight(), paint);
            } else if (heading < (90+22)) {
                b = G.bitmap_bus_east;
                canvas.drawBitmap(b, point.x-b.getWidth()/2, point.y-b.getHeight(), paint);
            } else if (heading < (135+22)) {
                b = G.bitmap_bus_southeast;
                canvas.drawBitmap(b, point.x-b.getWidth()/2, point.y-b.getHeight(), paint);
            } else if (heading < (180+22)) {
                b = G.bitmap_bus_south;
                canvas.drawBitmap(b, point.x-b.getWidth(), point.y-b.getHeight()/2, paint);
            }  else if (heading < (225+22)) {
                b = G.bitmap_bus_southwest;
                canvas.drawBitmap(b, point.x-b.getWidth()/2, point.y-b.getHeight(), paint);
            } else if (heading < (270+22)) {
                b = G.bitmap_bus_west;
                canvas.drawBitmap(b, point.x-b.getWidth()/2, point.y-b.getHeight(), paint);
            } else if (heading < (315+22)) {
                b = G.bitmap_bus_southwest;
                canvas.drawBitmap(b, point.x-b.getWidth()/2, point.y-b.getHeight(), paint);
            }
			//canvas.drawCircle(point.x, point.y, 3, paint);
		}
	}
}

@Override
public boolean onTap(GeoPoint p, MapView map) {
	//ProxyGeoPoint pgp = new ProxyGeoPoint(0, 0);
	
	if (map.getZoomLevel() < 15)
		return false;
	
	selection = p;
	
	int lat = p.getLatitudeE6();
	int lon = p.getLongitudeE6();
	
	double pixel = (double) map.getLongitudeSpan() / map.getWidth();
	ArrayList<QuadTree.Element> geopoints = G.stops_tree.get(
            round(lon-pixel*dp2px(100)), round(lat-pixel*dp2px(100)), 
            round(lon+pixel*dp2px(100)), round(lat+pixel*dp2px(100)));
	
	Projection proj = map.getProjection();
	Point point = new Point();
	Point touch = new Point();
	proj.toPixels(p, touch);
	
	final ArrayList<QuadTree.Element> touched = new ArrayList<QuadTree.Element>();
	Bitmap b;
	for(QuadTree.Element e: geopoints) {
		if (G.active_route >= 0 && Arrays.binarySearch(e.routes, G.active_route) < 0)
			continue;
		
		//pgp.lat = e.lat; pgp.lon = e.lon;
		proj.toPixels(new GeoPoint(e.lat, e.lon), point);
		
		
		switch (e.dir) {
			case 'N': 
				b = G.bitmap_stop_north;
				point.x += b.getWidth()/2;		
				break;
				
			case 'S': 
				b = G.bitmap_stop_south;
				point.x -= b.getWidth()/2;
				break;
				
			case 'E': 
				b = G.bitmap_stop_east;
				point.y += b.getHeight()/2;
				break;
				
			case 'W': 
				b = G.bitmap_stop_west;
				point.y -= b.getHeight()/2;
				break;
				
			default: 
				b = G.bitmap_stop_nodir;
				point.y -= b.getHeight()/2;
		}
		
		double dist = Math.sqrt(Math.pow(point.x-touch.x, 2) + Math.pow(point.y-touch.y, 2));
		
		if (dist < touch_allowance * metrics.density) {
			touched.add(e);
		}
	}
			
	if (touched.size() == 1) {
		int latx = touched.get(0).lat;
		int lonx = touched.get(0).lon;
		
        StopDialog d = new StopDialog(G.activity, touched.get(0).id, latx , lonx);
        d.show();
        
		return true;
	}
	else if (touched.size() > 1) { 
		if (map.getZoomLevel() == 19) {
			String[] names = new String[touched.size()];
			
			for (int i = 0; i < names.length; i++)
				names[i] = 	DB.getStopInfo(touched.get(i).id).name;
			
			
			new AlertDialog.Builder(G.activity, android.R.style.Theme_DeviceDefault_Dialog)
				.setTitle("Choose a stop")
				.setItems(names, 
					new DialogInterface.OnClickListener() {
						public void 
						onClick(DialogInterface dialog, int which) 
						{
							new StopDialog(G.activity, touched.get(which).id,
									touched.get(which).lat, touched.get(which).lon)
									.show();
						}
					})
					.create().show();
			

		}
		else {
			proj.toPixels(p, touch);
			map.getController().zoomInFixing(touch.x, touch.y);
		}
		return true;
	}
	
	return false;
}

@Override
public boolean onTouchEvent(MotionEvent e, MapView m) {
    gesture_detector.onTouchEvent(e);
    return false;
}

@Override
public boolean onDoubleTap(MotionEvent e) {
    if (selection != null) {
        Projection proj = map_view.getProjection();
        Point touch = new Point();
        proj.toPixels(selection, touch);
        map_view.getController().zoomInFixing(touch.x, touch.y);
    } else {
        map_view.getController().zoomIn();
    }
    return true;
}

@Override
public boolean onDoubleTapEvent(MotionEvent e) {
    return false;
}

@Override
public boolean onSingleTapConfirmed(MotionEvent e) {
    return false;
}

@Override
public boolean onFling(MotionEvent e1, MotionEvent e2, float f1, float f2) {
    return false;
}

@Override
public boolean onScroll(MotionEvent e1, MotionEvent e2, float f1, float f2) {
    return false;
}

@Override
public void onLongPress(MotionEvent e1) {
    
}

@Override
public void onShowPress(MotionEvent e1) {
    
}

@Override
public boolean onSingleTapUp(MotionEvent e1) {
    return false;
}

@Override
public boolean onDown(MotionEvent e1) {
    return false;
}
	
final static void 
draw_text_right(Canvas canvas, String msg, float x, float y)
{	
	int len = round(text_paint.measureText(msg));
	RectF rect = new RectF(x-len-dp2px(3), y-dp2px(13), x+dp2px(3), y+dp2px(3));
	canvas.drawRoundRect(rect, dp2px(3), dp2px(3), label_paint);
	
	canvas.drawText(msg, x-len, y, text_paint);
}

final static void 
draw_text_left(Canvas canvas, String msg, float x, float y)
{	
	int len = round(text_paint.measureText(msg));
	RectF rect = new RectF(x-dp2px(3), y-dp2px(13), x+len+dp2px(3), y+dp2px(3));
	canvas.drawRoundRect(rect, dp2px(3), dp2px(3), label_paint);
	
	canvas.drawText(msg, x, y, text_paint);
}

final static double
dist(double lat_a, double lng_a, double lat_b, double lng_b) { 
    double a1 = lat_a / (180f/3.14159265358979);
    double a2 = lng_a / (180f/3.14159265358979); 
    double b1 = lat_b / (180f/3.14159265358979); 
    double b2 = lng_b / (180f/3.14159265358979); 

    double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2); 
    double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2); 
    double t3 = Math.sin(a1)*Math.sin(b1); 
    double tt = Math.acos(t1 + t2 + t3); 
    
    return 6371000*tt; 
 } 



}