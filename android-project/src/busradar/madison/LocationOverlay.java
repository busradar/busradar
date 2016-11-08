package busradar.madison;

import android.content.Context;
import android.location.*;
import android.graphics.*;
import android.view.*;
import android.hardware.*;
import com.google.android.maps.*;

public class LocationOverlay extends MyLocationOverlay 
        implements SensorListener {
    Bitmap locationIndicator;
    int locationIndicatorHalfWidth;
    Paint paint;
    Point point;
    float bearing = 0;
    SensorManager sensors;
    
    public LocationOverlay(Context ctx, MapView mapView) {
        super(ctx, mapView);
        
        locationIndicator = BitmapFactory.decodeResource(
                ctx.getResources(), 
                R.drawable.user_location_compass);
        locationIndicatorHalfWidth = locationIndicator.getWidth() / 2;
        
        paint = new Paint();
        paint.setFilterBitmap(true);
        
        point = new Point();
        
        sensors = (SensorManager) 
                ctx.getSystemService(Context.SENSOR_SERVICE);
    }
    
    @Override protected void drawMyLocation(
            Canvas canvas, MapView mapView,
            Location lastFix, GeoPoint myLocation, long when) {
        if (!G.gps_enable) {
            return;
        }

        mapView.getProjection().toPixels(myLocation, point);
        
        canvas.save();
        if (bearing != 0 && !Float.isNaN(bearing)) {
            canvas.rotate(bearing, point.x, point.y);
        }
        canvas.drawBitmap(locationIndicator, 
            point.x-locationIndicatorHalfWidth,
            point.y-locationIndicatorHalfWidth,
            paint);
        canvas.restore();
    }
    
    @Override protected void drawCompass(Canvas canvas, float bearing) {
        // do nothing
    }
    
    @Override public void onAccuracyChanged(int sensor, int accuracy) {
        // do nothing
    }
    
    @Override public void onSensorChanged(int sensor, float[] values) {
        bearing = values[0];
    }
    
    void enable() {
        enableMyLocation();
        sensors.registerListener(this, SensorManager.SENSOR_ORIENTATION,
            SensorManager.SENSOR_DELAY_UI);
    }
    
    void disable() {
        disableMyLocation();
        sensors.unregisterListener(this);
    }
    
}