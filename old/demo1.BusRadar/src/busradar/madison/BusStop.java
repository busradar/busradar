package busradar.madison;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class BusStop extends OverlayItem {
	private Stop stop;
	public BusStop(Stop s) {
		super(new GeoPoint((int)s.latitude(), (int)s.longitude()), String.valueOf(s.id()), s.myName());
		stop = s;
	}

	public Stop myStop()
	{
		return stop;
	}
}