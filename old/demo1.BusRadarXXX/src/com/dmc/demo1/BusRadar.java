package com.dmc.demo1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MapView.LayoutParams;
import com.google.android.maps.Overlay;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BusRadar extends MapActivity {

	MapView mapView;
	MapController mc;
	GeoPoint p;
    LocationManager lm;
    LocationListener locationListener;
    Stop[] stops;
    List<Overlay> listOfOverlays;
	private Stop curStop;
    
    protected Dialog onCreateDialog(int id) {
    	return new StopDialog(this, curStop);
    }
    
//    class LocationOverlay extends com.google.android.maps.Overlay
//    {   	
//    	@Override
//        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) 
//        {
//            super.draw(canvas, mapView, shadow);                   
// 
//            //---translate the GeoPoint to screen pixels---
//            Point screenPts = new Point();
//            mapView.getProjection().toPixels(p, screenPts);
// 
//            //---add the marker---
//            Bitmap bmp = BitmapFactory.decodeResource(getResources(),  R.drawable.here);            
//            canvas.drawBitmap(bmp, screenPts.x, screenPts.y-36, null);         
//            return true;
//        }
//
//    } 
	
    
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
        try {
        Log.i("XXXXXXXXXXXXX", "hello");
        
        for (String s : getAssets().list(""))
        	Log.i("XXXXXXXXXXXXX", s);
        
        //InputStream f = getAssets().open("bus");
        AssetFileDescriptor f = getAssets().openFd("routes.sqlite.jet");
       // Log.i("XXXXXXXXXXXXX", String.format("exists %s\n",f.exists()));
        if (true)
        return;
        
        }
        catch(Exception e) { throw new RuntimeException(e); }
        
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
        
        mc = mapView.getController();
        
        //INIT DB 
        
        //getResources().openRawResourceFd(R.)


  		//PULL BUS STOPS FROM DB
  		
//  		Cursor select = db.rawQuery("SELECT id, lat, long, dir FROM routes", null);
//		select.moveToFirst();
//		BusStop[] stops = new BusStop[select.getCount()];
//		for (int x = 0; x < stops.length; x++)
//		{
//			//middle two was originally double
//			stops[x] = new BusStop(select.getInt(0), select.getInt(1), select.getString(2).charAt(0), select.getInt(3));
//			select.moveToNext();
//		}
//		select.close();
		
                                      
        // GPS
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        locationListener = new MyLocationListener();
        
//        lm.requestLocationUpdates(
//            LocationManager.GPS_PROVIDER, 
//            0, 
//            0, 
//            locationListener);
        

        //String nearCSbuilding = {"43.067874", "-89.40936"};
        //String nearHospital[] = {"43.078846", "-89.430023"};
   
    }
 
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
  
    
    class MyLocationListener implements LocationListener 
    {
        //private LocationOverlay LocationOverlay;

		public void onLocationChanged(Location loc) {
            if (loc != null) {                
//                Toast.makeText(getBaseContext(), 
//                    "Location changed : \nLat: " + loc.getLatitude() + 
//                    " \nLong: " + loc.getLongitude(), 
//                    Toast.LENGTH_LONG).show();
                
                p = new GeoPoint(
                        (int) (loc.getLatitude() * 1E6), 
                        (int) (loc.getLongitude() * 1E6));

                
//                if ( LocationOverlay == null)
//                {
//                	// add
//                	mc.animateTo(p);
//                    mc.setZoom(17);
//                	LocationOverlay = new LocationOverlay();
//                	listOfOverlays.add(LocationOverlay);
//                }
//                else
//                {
//                	// update
//                	mc.animateTo(p); // just for the demo
//                	listOfOverlays.remove(LocationOverlay);
//                	LocationOverlay = new LocationOverlay();
//                	listOfOverlays.add(LocationOverlay);
//                }

                mapView.invalidate();
            }
        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }
	
	protected void onPrepareDialog(int id, Dialog d) {
		((StopDialog) d).refresh(this, curStop);
	}

}
