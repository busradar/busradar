package busradar.madison;

import java.util.ArrayList;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Overlay.Snappable;

public class StopOverlay extends /*ItemizedOverlay<BusStop>*/ItemizedOverlay<OverlayItem> implements Snappable{
	private ArrayList<BusStop> mOverlays = new ArrayList<BusStop>();
	private BusRadar mContext;

	public StopOverlay(Stop[] stops, BusRadar context) 
	{
		super(boundCenter(context.getResources().getDrawable(R.drawable.bus_nodir)));
		mContext = context;
	
		Drawable n = context.getResources().getDrawable(R.drawable.bus_north);
		Drawable e = context.getResources().getDrawable(R.drawable.bus_east);
		Drawable s = context.getResources().getDrawable(R.drawable.bus_south);
		Drawable w = context.getResources().getDrawable(R.drawable.bus_west);
		Drawable b = context.getResources().getDrawable(R.drawable.bus_nodir);
		
		BusStop stop;
		for(int i = 0; i < stops.length; i++) {
			stop = new BusStop(stops[i]);
			switch (stops[i].dir)
			{
				case Stop.NORTH:
					stop.setMarker(boundCenter(n));
					break;
				case Stop.EAST:
					stop.setMarker(boundCenter(e));
					break;
				case Stop.SOUTH:
					stop.setMarker(boundCenter(s));
					break;
				case Stop.WEST:
					stop.setMarker(boundCenter(w));
					break;
				default:
					stop.setMarker(boundCenter(b));
			}
			mOverlays.add(stop);
			System.out.printf("added overlay\n");
		}
		populate();
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		//BusStop b = mOverlays.get(i);
//		OverlayItem o = b;
		//o = new OverlayItem(new GeoPoint( (int)b.myStop().latitude(), (int)b.myStop().longitude() ), String.valueOf(b.myStop().id()), b.myStop().myName() );
		//o.setMarker(b.getMarker(0));
		//return b; //(OverlayItem) b;
		//double lat = 43.0693926;
		//double lon = -89.4024883;
		//OverlayItem o = new OverlayItem(new GeoPoint( (int)(lat*1E6), (int)(lon*1E6)), "XX", "YY");
		//return o;
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		//return 1;
		return mOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		BusStop item = (BusStop) mOverlays.get(index);
		((BusRadar) mContext).setStop(item.myStop());
		((BusRadar) mContext).showDialog(0);
		return true;
	}
	
	public boolean onSnapToItem(int x, int y, Point snapPoint, MapView mapView)
	{
		if (Math.sqrt((x-snapPoint.x)*(x-snapPoint.x)+(y-snapPoint.y)*(y-snapPoint.y)) < 20)
			return true;
		return false;
	}
}