package map.test;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Overlay.Snappable;

public class TestOverlay extends ItemizedOverlay<BusStop> implements Snappable{
	private ArrayList<BusStop> mOverlays = new ArrayList<BusStop>();
	private Context mContext;
	public TestOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public void addOverlay(BusStop overlay) {
		mOverlays.add(overlay);
		populate();
	}
	
	@Override
	protected BusStop createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	public TestOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = mOverlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;
	}
	
	public boolean onSnapToItem(int x, int y, Point snapPoint, MapView mapView)
	{
		if (Math.sqrt((x-snapPoint.x)*(x-snapPoint.x)+(y-snapPoint.y)*(y-snapPoint.y)) < 20)
			return true;
		return false;
	}
}
