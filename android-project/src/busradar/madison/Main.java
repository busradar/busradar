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
import java.util.Collections;
import java.lang.reflect.Method;

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
import android.os.Build;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.*;
import android.view.*;
import android.*;
import android.content.pm.PackageManager;

import com.google.android.maps.*;

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
        // alex hp laptop 0Ig8W-xZ2oTTr3MmiHSsA98C7_KVHwhUQe849bQ
        G.active_route = -1;
        map_view = new MapView(this,"0nhR5qUExunzdtDzAYrFjx2tcA9aSJISJEwxhYg"); // signed key
        //map_view = new MapView(this,"0Ig8W-xZ2oTTr3MmiHSsA98C7_KVHwhUQe849bQ"); // debug key
        
 		G.location_overlay = new LocationOverlay(this, map_view);
 		

 		G.gps_enable = getPreferences(Context.MODE_PRIVATE).getBoolean("gps-enabled", true);
 		if (!G.gps_enable) {
            G.location_overlay.enable();
 		}
 		
 		if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != 
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
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
 	
        boolean success = false;
        try {
            Window wind = getWindow();
            Method setNeedsMenuKey = Window.class.getDeclaredMethod("setNeedsMenuKey", int.class);
            setNeedsMenuKey.setAccessible(true);
            int NEEDS_MENU_SET_TRUE =  WindowManager.LayoutParams.class.getField("NEEDS_MENU_SET_TRUE").getInt(null);
            setNeedsMenuKey.invoke(wind, NEEDS_MENU_SET_TRUE);
            success = true;
        } catch (Exception e) {
            // ignore
        }
        if (!success) {
            try {
                getWindow().addFlags(WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null));
            } catch (Exception e) {
                // ignore
            }
        }
        
 		map_view.setBuiltInZoomControls(true);
 		final MapController ctrl = map_view.getController();

 		ctrl.setCenter(new GeoPoint((int) (43.07166 * 1E6), (int) (-89.407088 * 1E6)));
 		
 		ctrl.setZoom(14);
 		map_view.setClickable(true);
 		//map_view.setBuiltInZoomControls(false);
 
        map_view.getOverlays().add(G.location_overlay);
        
        map_view.getOverlays().add(G.bus_overlay=new BusOverlay(map_view));
        map_view.setId(1);
        route_bar=new RouteBar();
        route_bar.setId(2);
//        setContentView(new LinearLayout(this) {{
//        	setOrientation(LinearLayout.VERTICAL);
//        	setGravity(Gravity.BOTTOM);
//        	
//        	addView(route_bar);
//        	addView(new LinearLayout(Main.this) {{
//        		addView(new Button(Main.this));
//        	}});
        	
        	
//        }});
        
        //final Button btn = new Button(Main.this);
        //btn.setId(1);
        route_bar.measure(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        final int route_bar_height = route_bar.getMeasuredHeight();
        final View spacer = new View(this);
        spacer.setId(3);
        
 		setContentView(new RelativeLayout(this) {{
 			addView(new TextView(Main.this) {{
 				setBackgroundColor(0xffffffff);
 			}}, new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT));
 			addView(map_view, new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT) {{
 				addRule(RelativeLayout.ABOVE, 3);
 			}});
 			addView(route_bar, new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT) {{
 				addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
 			}});
 			addView(spacer, new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, route_bar_height) {{
                addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            }});
 			addView(map_view.getZoomControls(), new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT) {{
 				//addRule(RelativeLayout.ABOVE, routes_id);
 				addRule(RelativeLayout.ALIGN_TOP);
 				addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
 				//addRule(RelativeLayout.CENTER_HORIZONTAL);
 			}});
 		}});
 		
 		
 		ZoomButtonsController zbc = map_view.getZoomButtonsController();
 		zbc.setVisible(true);
        ViewGroup container = zbc.getContainer();
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            if (child instanceof ZoomControls) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
                lp.gravity = Gravity.CENTER | Gravity.TOP;
                child.requestLayout();
                break;
            } 
        }
 		
 		G.location_overlay.runOnFirstFix(new Runnable() {
			public void run() {
				G.activity.map_view.post(new Runnable() {
					public void run() {
                        if (!G.gps_enable) {
                            return;
                        }
						for (int i = map_view.getZoomLevel(); i < 17; i++)
							ctrl.zoomIn();
						ctrl.setCenter(G.location_overlay.getMyLocation());
						
						GeoPoint p = G.location_overlay.getMyLocation();
						
						ArrayList<Integer> routes = new ArrayList<Integer>();
						
						Point C = new Point(p.getLongitudeE6(), p.getLatitudeE6());
						for (int r = 0; r < G.routes.length; r++) {
							if (G.routes[r] == null)
								continue;
							
							ArrayList<RouteTree.Line> lines = new ArrayList<RouteTree.Line>();
							
							RouteTree tree = G.routes[r].tree;
							
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
								if (G.routes[i].button != null) 
								{
									G.routes[i].button.performClick();
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
			G.location_overlay.enable();
		}
		
		if (G.active_route >= 0) {
			G.bus_locator.start(G.routes[G.active_route].id);
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
		
		G.location_overlay.disable();
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
		menu.add(0, 4, 0, "Favorites").setIcon(android.R.drawable.btn_star_big_off);
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
					G.location_overlay.enable();
				}
				else {
					G.toast("GPS disabled");
					G.location_overlay.disable();
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
