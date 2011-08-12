package busradar.madison;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Projection;

public class Main extends MapActivity
{
	Button cur_button = null;
	MapView map_view;
	RouteBar route_bar;
	
	//http://github.com/jgilfelt/android-mapviewballoons#readme
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(new ImageView(this) {{ 
        	setImageDrawable(getResources().getDrawable(R.drawable.large_icon));
        }});
        
        // CS "0Ig8W-xZ2oTTUV790AiVTa3KwEH6f-lIPaPek3Q");
        // home laptop 0Ig8W-xZ2oTTfaInEj5tO3IAm8eCWdNzpD3yBOA
        // signed key 0nhR5qUExunzdtDzAYrFjx2tcA9aSJISJEwxhYg
        // debug key "0nhR5qUExuny9Tkp9u8PZguQnBL2NlvYRA7yXfQ"
        G.active_route = 0;
        map_view = new MapView(this,"0nhR5qUExunzdtDzAYrFjx2tcA9aSJISJEwxhYg"); // signed key
        //map_view = new MapView(this,"0Ig8W-xZ2oTQiNf8FBvVMZ3tXEL75F5EHbdsOIg"); // debug key
 		G.location_overlay = new MyLocationOverlay(this, map_view) {
 			boolean droid_x_MyLocationOverlay_bug = false;
 			Bitmap bitmap_curloc_indicator = BitmapFactory.decodeResource(Main.this.getResources(), R.drawable.curloc_pointer);
 			Paint paint = new Paint() {{ setFilterBitmap(true); }};
 			Point p = new Point();
 			
			@Override protected void drawMyLocation(Canvas canvas, MapView mapView,
					Location lastFix, GeoPoint myLocation, long when) 
			{
				if (!G.gps_enable)
					return;
				
				mapView.getProjection().toPixels(myLocation, p);
				
				float bearing = this.getOrientation();
				
				if ( !Float.isNaN(bearing) ) {
					canvas.save();
					canvas.rotate(bearing, p.x, p.y);
					
					canvas.drawBitmap(bitmap_curloc_indicator, p.x-9, p.y-23, paint);
					canvas.restore();
				}
				
				if (!droid_x_MyLocationOverlay_bug) {
					try {
						super.drawMyLocation(canvas, mapView, lastFix, myLocation, when);
						return;
					} catch (Exception e) {
						droid_x_MyLocationOverlay_bug = true;
					}
				}
				
				// code for Droid X bug
				// see http://dimitar.me/applications-that-use-the-mylocationoverlay-class-crash-on-the-new-droid-x/
				// http://community.developer.motorola.com/t5/Android-App-Development-for/Google-Maps/m-p/3421/highlight/true#M396
				{
					Drawable drawable;
					Paint accuracyPaint;
					int width, height;
					Point center, left;

					accuracyPaint = new Paint();
					accuracyPaint.setAntiAlias(true);
					accuracyPaint.setStrokeWidth(2.0f);
					
					drawable = mapView.getContext().getResources().getDrawable(R.drawable.ic_maps_indicator_current_position);
					width = drawable.getIntrinsicWidth();
					height = drawable.getIntrinsicHeight();
					center = new Point();
					left = new Point();
						
					Projection projection = mapView.getProjection();
					
					double latitude = lastFix.getLatitude();
					double longitude = lastFix.getLongitude();
					float accuracy = lastFix.getAccuracy();
					
					float[] result = new float[1];

					Location.distanceBetween(latitude, longitude, latitude, longitude + 1, result);
					float longitudeLineDistance = result[0];

					GeoPoint leftGeo = new GeoPoint((int)(latitude*1e6), (int)((longitude-accuracy/longitudeLineDistance)*1e6));
					projection.toPixels(leftGeo, left);
					projection.toPixels(myLocation, center);
					int radius = center.x - left.x;
					
					accuracyPaint.setColor(0xff6666ff);
					accuracyPaint.setStyle(Style.STROKE);
					canvas.drawCircle(center.x, center.y, radius, accuracyPaint);

					accuracyPaint.setColor(0x186666ff);
					accuracyPaint.setStyle(Style.FILL);
					canvas.drawCircle(center.x, center.y, radius, accuracyPaint);
								
					drawable.setBounds(center.x - width / 2, center.y - height / 2, center.x + width / 2, center.y + height / 2);
					drawable.draw(canvas);
				}
				
			}

			@Override protected void drawCompass(Canvas canvas, float bearing) {
				return;
			}
			
			
 		};

 		G.gps_enable = getPreferences(Context.MODE_PRIVATE).getBoolean("gps-enabled", true);
 		if (!G.gps_enable) {
 			G.location_overlay.disableMyLocation();
 			G.location_overlay.disableCompass();
 		}
 		
 		
 		new Thread() {

			@Override
			public void run() {
				G.init(Main.this);
		 		runOnUiThread(new Runnable() {
					
					public void run() {
						Main.this.onCreate2();
						
					}
				});
			}
 			
 		}.start();

    }
	
 	@SuppressWarnings("deprecation")
	public void onCreate2() 
 	{
 		map_view.setBuiltInZoomControls(false);
 		final MapController ctrl = map_view.getController();

 		ctrl.setCenter(new GeoPoint((int) (43.07166 * 1E6), (int) (-89.407088 * 1E6)));
 		
 		ctrl.setZoom(14);
 		map_view.setClickable(true);
 		map_view.setBuiltInZoomControls(false);
 
        map_view.getOverlays().add(G.location_overlay);
        
        map_view.getOverlays().add(G.bus_overlay=new BusOverlay());
        
 		setContentView(new RelativeLayout(this) {{
 			addView(map_view);
 			addView(route_bar=new RouteBar(), new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT) {{
 			addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
 		}});
 			addView(map_view.getZoomControls(), new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT) {{
 				//addRule(RelativeLayout.ABOVE, routes_id);
 				addRule(RelativeLayout.ALIGN_TOP);
 				addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
 				//addRule(RelativeLayout.CENTER_HORIZONTAL);
 			}});
 		}});
 		
 		G.location_overlay.runOnFirstFix(new Runnable() {
			public void run() {
				G.activity.map_view.post(new Runnable() {
					public void run() {
						for (int i = map_view.getZoomLevel(); i < 17; i++)
							ctrl.zoomIn();
						ctrl.setCenter(G.location_overlay.getMyLocation());
						
						GeoPoint p = G.location_overlay.getMyLocation();
						
						ArrayList<Integer> routes = new ArrayList<Integer>();
						
						Point C = new Point(p.getLongitudeE6(), p.getLatitudeE6());
						System.out.printf("location is %d %d\n", C.x, C.y);
						for (int r = 0; r < G.route_points.length; r++) {
							if (G.route_points[r] == null)
								continue;
							
							ArrayList<RouteTree.Line> lines = new ArrayList<RouteTree.Line>();
							
							RouteTree tree = G.route_points[r].tree;
							
							tree.find(C.x-100, C.y-100, C.x+100, C.y+100, lines);
							//System.out.printf("for route %d, found %d\n", r, lines.size());
		
							for (RouteTree.Line line : lines) {
								//Point A = proj.toPixels(new GeoPoint(line.lat1, line.lon1), null); //new Point(line.lat1, line.lon1);
								Point A = new Point(line.lon1, line.lat1);
								//Point B = proj.toPixels(new GeoPoint(line.lat2, line.lon2), null); //Point B = new Point(line.lat2, line.lon2);
								Point B = new Point(line.lon2, line.lat2);
								
								double dist = G.pt_to_line_segment_dist(A, B, C);
								if (dist < 100) 
									routes.add(r);
							}
						}
						
						if (routes.size() != 0 && !routes.contains(G.active_route)) 
						{
							Collections.sort(routes);
							
							for (int i : routes) 
							{
								if (G.route_points[i].button != null) 
								{
									G.route_points[i].button.performClick();
									break;
								}
							}
						}
						
					}
				});
				
			}});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		G.favorites = new MyLocations(this);
		G.favorites.open();
		
		if (G.gps_enable) {
			G.location_overlay.enableMyLocation();
			G.location_overlay.enableCompass();
		}
		
		if (G.active_route > 0) {
			G.bus_locator.start(G.active_route);
		}
		
		{
			String holiday_name = G.is_today_weekend_or_holiday();
			byte today = holiday_name != null ? Route.HOLIDAY : Route.WEEKDAY;
			
			if (G.first_time) 
			{
				G.first_time = false;
				
				if (holiday_name != null && holiday_name.length() > 0)
					G.toast_long("Today is "+holiday_name+". Saturday/Sunday/Holiday routing in effect.");
				
			}
			else if (G.today != today)
			{
				G.today = today;
				
				if (holiday_name != null && holiday_name.length() > 0)
					G.toast_long("Today is "+holiday_name+". Saturday/Sunday/Holiday routing in effect.");
				else
					G.toast_long("Weekday routing in effect today.");
				
				if (G.activity.route_bar != null)
					G.activity.route_bar.update();
			}
		
		}
		
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		G.favorites.close();
		
		//Debug.stopMethodTracing();
		
		G.location_overlay.disableMyLocation();
		G.location_overlay.disableCompass();
		G.bus_locator.stop();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {
		// icons http://androiddrawableexplorer.appspot.com/
		menu.add(0, 5, 0, "GPS on/off").setIcon(android.R.drawable.ic_menu_compass);
		menu.add(0, 1, 0, "My Location").setIcon(android.R.drawable.ic_menu_mylocation);
		menu.add(0, 4, 0, "Favorites").setIcon(R.drawable.ic_menu_star);
		menu.add(0, 2, 0, "Map Mode").setIcon(android.R.drawable.ic_menu_mapmode);
		menu.add(0, 6, 0, "Help").setIcon(android.R.drawable.ic_menu_help);
		menu.add(0, 3, 0, "About").setIcon(android.R.drawable.ic_menu_info_details);
		//menu.add(0, 4, 0, "3");
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// this is to disable "my location" when GPS is off
		MenuItem item = menu.findItem(1);
		item.setVisible(true);
		
		if ( !G.gps_enable ) {
            item.setEnabled(false);
        } else {
        	item.setEnabled(true);
        }
		
		return super.onPrepareOptionsMenu(menu);
	}

	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		GeoPoint myloc = G.location_overlay.getMyLocation();
		switch (item.getItemId()) {
			case 5: {
				Editor e = getPreferences(Context.MODE_PRIVATE).edit();
				e.putBoolean("gps-enabled", !G.gps_enable);
				e.commit();
				
				G.gps_enable = !G.gps_enable;
				
				if (G.gps_enable) {
					G.toast("GPS enabled");
					G.location_overlay.enableMyLocation();
					G.location_overlay.enableCompass();
				}
				else {
					G.toast("GPS disabled");
					G.location_overlay.disableCompass();
					G.location_overlay.disableMyLocation();
				}
			} break;
			
			case 6: {				
				new TutorialDialog(this).show();
			} break;
			
			case 4: {
				new FavoriteDialog(this).show();
			} break;
			
			case 2: {
				map_view.setSatellite(!map_view.isSatellite());
				
				//if(map_view.isSatellite()) {
				//	G.toast("Satellite mode enabled");
				//} else {
				//	G.toast("Satellite mode disabled");
				//}
			} break;
			
			case 3: {
				new AboutDialog(this).show();
			} break;
			
			case 1: {
				if (myloc != null)
					map_view.getController().animateTo(myloc);
			} break;
		}
		
		return true;
	}
}
