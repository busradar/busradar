package busradar.madison;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.MapView.LayoutParams;
import com.google.android.maps.Overlay;

import android.app.Dialog;
import android.content.Context;
import android.database.SQLException;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BusRadar extends MapActivity {

	MapView mapView;
	MapController mc;
	LocationManager lm;

	GeoPoint p;
	Criteria criteria;
    boolean first_time;
    
    Stop[] stops;
    List<Overlay> listOfOverlays;
	private Stop curStop;
    
	public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        MapController mc = mapView.getController(); 
        switch (keyCode) 
        {
            case KeyEvent.KEYCODE_3:
                mc.zoomIn();
                break;
            case KeyEvent.KEYCODE_1:
                mc.zoomOut();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
	
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.setStreetView(false);
        mapView.setSatellite(false);

        // ZOOM CONTROL 
        LinearLayout zoomLayout = (LinearLayout)findViewById(R.id.zoom);  
        View zoomView = mapView.getZoomControls();
 
        zoomLayout.addView(zoomView, 
            new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT)); 
        mapView.displayZoomControls(true);
        
        // SAT VIEW
        Button s = (Button) findViewById(R.id.sat);
        s.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				toggleSatellite();
			}
		});
        
        mc = mapView.getController();
        
        //INIT DB
        DataBaseHelper myDbHelper = new DataBaseHelper(this.getApplicationContext());
        myDbHelper = new DataBaseHelper(this);

        try {
        	myDbHelper.createDataBase();
        } catch (IOException ioe) {
  			throw new Error("Unable to create database");
  		}

        try {
  			myDbHelper.openDataBase();
  		}catch(SQLException sqle){
  			throw sqle;
  		}

  		//PULL BUS STOPS FROM DB
  		stops = Stop.getAllStops();
  		StopOverlay itemizedOverlay = new StopOverlay(stops, this);
  		
  		listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();

        listOfOverlays.add(itemizedOverlay);
        
        // MyLocationOverlay
        MyLocationOverlay myLocationOverlay = new MyLocationOverlay(this, mapView);
        listOfOverlays.add(myLocationOverlay);
        myLocationOverlay.enableMyLocation();
        
        // GPS
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	
	  	// Set the criteria for selection a location provider
	  	criteria = new Criteria();
	  	criteria.setAccuracy(Criteria.ACCURACY_FINE);
	  	criteria.setAltitudeRequired(false);
	  	criteria.setBearingRequired(false);
	  	criteria.setCostAllowed(true);
	  	criteria.setPowerRequirement(Criteria.POWER_LOW);
   
    }
	
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    public void toggleSatellite() {
    	if(!mapView.isSatellite()) {
    		mapView.setSatellite(true);
    	} else {
    		mapView.setSatellite(false);
    	}
    }
  
    @Override
	public void onStart() {
		super.onStart();
		
		first_time = true;
		
		// Find an available provider to use which matches the criteria
		String provider = lm.getBestProvider(criteria, true);
	
		// Update the UI using the last known locations
		Location location = lm.getLastKnownLocation(provider);
		updateWithNewLocation(location);
	
		// Start listening for location changes
		lm.requestLocationUpdates(provider, 
                                           0,
                                           0,
                                           locationListener);
	}
	
	@Override 
	public void onStop() {
	  // Stop listening for location changes 
	  lm.removeUpdates(locationListener);
	  
	  super.onStop();
	}
	
	private final LocationListener locationListener = new LocationListener() {
	    public void onLocationChanged(Location location) {
	    	updateWithNewLocation(location);
	    }
	   
	    public void onProviderDisabled(String provider){
	    	updateWithNewLocation(null);
	    }
	
	    public void onProviderEnabled(String provider) {}
	    public void onStatusChanged(String provider, int status, Bundle extras) {}
	};
	  
	private void updateWithNewLocation(Location location) {
		// Update the map position and overlay
		if (location != null) {
			// Update my location marker
		    mapView.invalidate();

		    // Update the map location.
		    Double geoLat = location.getLatitude()*1E6;
		    Double geoLng = location.getLongitude()*1E6;
		    p = new GeoPoint(geoLat.intValue(), geoLng.intValue());

		    if(first_time == true) {
		    	mc.animateTo(p);
		    	mc.setZoom(17);
		    	first_time = false;
		    }
		}
	}

	public static final int Menu1 = Menu.FIRST + 1;
	public static final int Menu2 = Menu.FIRST + 2;
	public static final int Menu3 = Menu.FIRST + 3;
	public static final int Menu4 = Menu.FIRST + 4;
	public static final int Menu5 = Menu.FIRST + 5;

	/** create the menu items */
	public void populateMenu(Menu menu) {

	  // enable keyb shortcuts, qwerty mode = true means only show keyb shortcuts (not numeric) and vice versa
	  // these only show up in context menu, not options menu
	  //menu.setQwertyMode(true);

	  menu.add(0, Menu1, 0, "My Location").setIcon(R.drawable.ic_menu_mylocation);

	  menu.add(0, Menu2, 0, "Favorites").setIcon(R.drawable.ic_menu_favorite);

	  menu.add(0, Menu3, 0, "Add Favorites").setIcon(R.drawable.ic_menu_add);

	  menu.add(0, Menu4, 0, "Help").setIcon(R.drawable.ic_menu_help);
	  
	  menu.add(0, Menu5, 0, "Info").setIcon(R.drawable.ic_menu_info_details);
	}
	
	/** hook into menu button for activity */
	@Override public boolean onCreateOptionsMenu(Menu menu) {
	  populateMenu(menu);
	  return super.onCreateOptionsMenu(menu);
	}

	/** when menu button option selected */
	@Override public boolean onOptionsItemSelected(MenuItem item) {
	  return applyMenuChoice(item) || super.onOptionsItemSelected(item);
	}
	
	/** respond to menu item selection */
	public boolean applyMenuChoice(MenuItem item) {
		switch (item.getItemId()) {
		case Menu1:
			mc.animateTo(p);
			return true;
		case Menu2:
			Toast.makeText(this, "Favorites is selected", Toast.LENGTH_SHORT).show();
			return true;
		case Menu3:
			Toast.makeText(this, "Add Favorites is selected", Toast.LENGTH_SHORT).show();
			//reverseGeocode("1118 W Dayton St, Madison, WI");
			reverseGeocode("970 University Ave, Madison, WI");
			return true;
		case Menu4:
			Toast.makeText(this, "Help is selected", Toast.LENGTH_SHORT).show();
			//TO DO
			//DISPLAY SOME HTML w/ helpful info
			return true;
		case Menu5:
			Toast.makeText(this, "Info is selected", Toast.LENGTH_SHORT).show();
			//TO DO
			//DISPLAY about.html
		    return true;
		}
		return false;
	}

	public GeoPoint reverseGeocode(String s) {
		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());    
		GeoPoint gp;
		
		try {
			List<Address> addresses = geoCoder.getFromLocationName(s, 5);
				if (addresses.size() > 0) {
					gp = new GeoPoint(
							(int) (addresses.get(0).getLatitude() * 1E6), 
							(int) (addresses.get(0).getLongitude() * 1E6));
					Toast.makeText(this, gp.getLatitudeE6() / 1E6 + "," + gp.getLongitudeE6() /1E6 , Toast.LENGTH_SHORT).show();
					//mc.animateTo(gp);
					return gp;
				}
    	} catch (IOException e) {
        	e.printStackTrace();
    	}
		return null;
	}
	
	protected Dialog onCreateDialog(int id) {
    	return new StopDialog(this, curStop);
    }
	
	public void setStop(Stop item) {
		curStop = item;
	}
	
	protected void onPrepareDialog(int id, Dialog d) {
		((StopDialog) d).refresh(this, curStop);
	}

}
