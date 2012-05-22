package map.test;

import java.io.IOException;
import java.util.List;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class Maptest extends MapActivity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.test);
        TestOverlay itemizedoverlay = new TestOverlay(drawable, this);
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
        Stop[] s = Stop.getAllStops();
  		for (int x = 0; x < s.length; x++)
  			itemizedoverlay.addOverlay(new BusStop(s[x]));
        mapOverlays.add(itemizedoverlay);
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}