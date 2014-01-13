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
import android.graphics.RectF;
import android.graphics.Typeface;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

import static busradar.madison.G.*;

class BusOverlay extends com.google.android.maps.Overlay {

final static int touch_allowance = 20; 
GeoPoint selection;
int zoom_level = 0;

static class BusLocation {
	public GeoPoint loc;
	public char dir;
}

BusOverlay() {	
	
}


static final Paint paint = new Paint(); {
	paint.setColor(0xffff0000);
}

static final Paint line_paint = new Paint() {{
	setStrokeWidth(dp2px(5));
	setAntiAlias(true);
	setColor(0x90ff0000);
	setStyle(Paint.Style.STROKE);
	setStrokeJoin(Paint.Join.ROUND);
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
	
	
	int pixel = map.getLongitudeSpan() / map.getWidth();
	
	int minlon = center.getLongitudeE6() - lonspan / 2;
	int maxlon = center.getLongitudeE6() + lonspan / 2;
	int minlat = center.getLatitudeE6() - latspan / 2;
	int maxlat = center.getLatitudeE6() + latspan / 2;
	Point min = new Point();
	Point max = new Point();
	proj.toPixels(new GeoPoint(maxlat, minlon), min);
	proj.toPixels(new GeoPoint(minlat, maxlon), max);
	
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
	
	if (G.active_route >= 0) { 
		
		ArrayList<RouteTree.Line> lines = new ArrayList<RouteTree.Line>();
		line_paint.setColor(0x90000000 | G.route_points[G.active_route].color);
		
		RouteTree tree = G.route_points[G.active_route].tree;
		tree.find(minlon-5*pixel, minlat-5*pixel, maxlon+5*pixel, maxlat+5*pixel, lines);
		
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
	if (map.getZoomLevel() >= 15) {
		ArrayList<QuadTree.Element> geopoints = G.stops_tree.get(minlon-32*pixel, minlat-32*pixel, maxlon+32*pixel, maxlat+32*pixel, pixel*15);
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
					canvas.drawBitmap(b, point.x, point.y-b.getHeight()/2, paint);
					break;
					
				case 'S': 
					b = G.bitmap_stop_south; 
					canvas.drawBitmap(b, point.x-b.getWidth(), point.y-b.getHeight()/2, paint);
					break;
					
				case 'E': 
					b = G.bitmap_stop_east;
					canvas.drawBitmap(b, point.x-b.getWidth()/2, point.y, paint);
					break;
					
				case 'W': 
					b = G.bitmap_stop_west;
					canvas.drawBitmap(b, point.x-b.getWidth()/2, point.y-b.getHeight(), paint);
					break;
					
				default: 
					b = G.bitmap_stop_nodir;
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

				char dir = bus_loc.dir == 'N' ? '▲' :
							bus_loc.dir == 'S' ? '▼' :
								bus_loc.dir == 'E' ? '▶' :
									bus_loc.dir == 'W' ? '◀' : ' ';
				
				double dist = dist(center.getLatitudeE6()/1.E6, center.getLongitudeE6()/1.E6, 
						bus_loc.loc.getLatitudeE6()/1.E6, bus_loc.loc.getLongitudeE6()/1.E6);
				
				String msg = ( ((int)(dist * 0.000621371192 * 100 + 0.5)) / 100.0) + "mi" + dir; 
				//GeoPoint gps = map.getMapCenter(); //G.location_overlay.getMyLocation();
				//float[] results = new float[1];
				//Location.distanceBetween(gps.getLatitudeE6()/1.E6, gps.getLongitudeE6()/1.E6, 
				//			bus_loc.loc.getLatitudeE6()/1.E6, bus_loc.loc.getLongitudeE6()/1.E6, results);
								
				if (point.x > max.x) {
					if (point.y < min.y) {
						draw_text_right(canvas, msg, max.x, min.y + (13+16*upper_right_count++)*metrics.density);
					}
					else if (point.y > max.y - 45) {
						draw_text_right(canvas, msg, max.x, max.y - (45-16*lower_right_count++)*metrics.density);
					}
					else {
						draw_text_right(canvas, msg, max.x, point.y);
					}
				}
				else if (point.x < min.x) {
					if (point.y < min.y) {
						draw_text_left(canvas, msg, min.x+3*metrics.density, min.y+(13+16*upper_left_count++)*metrics.density);
					}
					else if (point.y > max.y - 45) {
						draw_text_left(canvas, msg, min.x+3*metrics.density, max.y-(45-16*lower_left_count++)*metrics.density);
					}
					else {
						draw_text_left(canvas, msg, min.x+3*metrics.density, point.y);
					}
					
				}
				else if (point.y > max.y) {
					draw_text_left(canvas, msg, point.x, max.y-45*metrics.density);
				}
				else { //if (point.y < min.y) {
					draw_text_left(canvas, msg, point.x, min.y+13*metrics.density);
				}
				
				continue;
			}
				
			Bitmap b;
			switch (bus_loc.dir) {
				case 'N': 
					b = G.bitmap_bus_north;
					canvas.drawBitmap(b, point.x, point.y-b.getHeight()/2, paint);
					break;
					
				case 'S': 
					b = G.bitmap_bus_south; 
					canvas.drawBitmap(b, point.x-b.getWidth(), point.y-b.getHeight()/2, paint);
					break;
					
				case 'E': 
					b = G.bitmap_bus_east;
					canvas.drawBitmap(b, point.x-b.getWidth()/2, point.y-b.getHeight(), paint);
					break;
					
				case 'W': 
					b = G.bitmap_bus_west;
					canvas.drawBitmap(b, point.x-b.getWidth()/2, point.y-b.getHeight(), paint);
					break;
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
	
	int pixel = map.getLongitudeSpan() / map.getWidth();
	ArrayList<QuadTree.Element> geopoints = G.stops_tree.get(lon-pixel*dp2px(32), lat-pixel*dp2px(32), lon+pixel*dp2px(32), lat+pixel*dp2px(32), pixel);
	
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
				point.y -= b.getHeight();
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
				names[i] = 	DB.getStopName(touched.get(i).id);
			
			
			new AlertDialog.Builder(G.activity)
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
	
final static void 
draw_text_right(Canvas canvas, String msg, float x, float y)
{	
	float len = text_paint.measureText(msg);
	RectF rect = new RectF(x-len-3*metrics.density, y-13*metrics.density, x+3*metrics.density, y+3*metrics.density);
	canvas.drawRoundRect(rect, 3*metrics.density, 3*metrics.density, label_paint);
	
	canvas.drawText(msg, x-len, y, text_paint);
}

final static void 
draw_text_left(Canvas canvas, String msg, float x, float y)
{	
	float len = text_paint.measureText(msg);
	RectF rect = new RectF(x-3*metrics.density, y-13*metrics.density, x+len+3*metrics.density, y+3*metrics.density);
	canvas.drawRoundRect(rect, 3*metrics.density, 3*metrics.density, label_paint);
	
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