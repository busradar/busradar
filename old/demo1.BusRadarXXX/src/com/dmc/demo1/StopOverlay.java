package com.dmc.demo1;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Overlay.Snappable;

public class StopOverlay extends /*ItemizedOverlay<BusStop>*/ItemizedOverlay<OverlayItem> implements Snappable{

	public StopOverlay(Drawable defaultMarker) {
		super(defaultMarker);
		// TODO Auto-generated constructor stub
	}
//	private ArrayList<BusStop> mOverlays = new ArrayList<BusStop>();
//	private BusRadar mContext;
//
//	public StopOverlay(Stop[] stops, BusRadar context) 
//	{
//		super(boundCenterBottom(context.getResources().getDrawable(R.drawable.bus_basic)));
//		mContext = context;
//	
//		Drawable north = context.getResources().getDrawable(R.drawable.bus_north);
//  		Drawable east = context.getResources().getDrawable(R.drawable.bus_east);
//  		Drawable south = context.getResources().getDrawable(R.drawable.bus_south);
//  		Drawable west = context.getResources().getDrawable(R.drawable.bus_west);
//		Drawable basic = context.getResources().getDrawable(R.drawable.bus_basic);
//  		
//  		BusStop stop;
//  		for(int i = 0; i < stops.length; i++) {
//			//stop = new BusStop(stops[i]);
//			switch (stops[i].dir)
//			{
//				case Stop.NORTH: {
//					System.out.printf("before %s\n", north.getBounds());
//					
//					int w = north.getIntrinsicWidth();
//					int h = north.getIntrinsicHeight();
//					
//					north.setBounds(0,
//		                       (int)(-h/2.0+0.5), 
//		                       w ,
//		                       (int)(h/2.0+0.5));
//					
//					stop.setMarker(north);
//					
//					} break;
//				case Stop.EAST: {
//					int w = east.getIntrinsicWidth();
//					int h = east.getIntrinsicHeight();
//					
//					east.setBounds((int)(-w/2.0+0.5),
//		                       0, 
//		                       (int)(w/2.0+0.5) ,
//		                       h);
//					
//					stop.setMarker(east);
//				
//				} break;
//				case Stop.SOUTH: {
//					
//					int w = south.getIntrinsicWidth();
//					int h = south.getIntrinsicHeight();
//					
//					south.setBounds(-w,
//		                       (int)(-h/2.0+0.5), 
//		                       0 ,
//		                       (int)(h/2.0+0.5));
//					
//					stop.setMarker(south);
//					
//					} break;
//				case Stop.WEST:
//					//System.out.printf("before %s\n", west.getBounds());
//					//System.out.printf("after %s\n", boundCenterBottom(west).getBounds());
//					
//					//stop.setMarker(boundCenterBottom(west));
//					
//					int w = west.getIntrinsicWidth();
//					int h = west.getIntrinsicHeight();
//					
//					west.setBounds((int)(-w/2.0+0.5),
//		                       -h, 
//		                       (int)(w/2.0+0.5) ,
//		                       0);
//					stop.setMarker(west);
//					break;
//				default:
//					stop.setMarker(boundCenterBottom(basic));
//			}
//  			mOverlays.add(stop);
//  			System.out.printf("added overlay\n");
//  		}
//  		populate();
//  		
//	}
//	
//	@Override
//	protected OverlayItem createItem(int i) {
//		BusStop b = mOverlays.get(i);
////		OverlayItem o = b;
//		//o = new OverlayItem(new GeoPoint( (int)b.myStop().latitude(), (int)b.myStop().longitude() ), String.valueOf(b.myStop().id()), b.myStop().myName() );
//		//o.setMarker(b.getMarker(0));
//		return b; //(OverlayItem) b;
//		//double lat = 43.0693926;
//		//double lon = -89.4024883;
//		//OverlayItem o = new OverlayItem(new GeoPoint( (int)(lat*1E6), (int)(lon*1E6)), "XX", "YY");
//		//return o;
//		//return mOverlays.get(i);
//	}
//
//	@Override
//	public int size() {
//		//return 1;
//		return mOverlays.size();
//	}
//
//	@Override
//	protected boolean onTap(int index) {
//		BusStop item = (BusStop) mOverlays.get(index);
//		((BusRadar) mContext).setStop(item.myStop());
//		((BusRadar) mContext).showDialog(0);
//		return true;
//	}
//	
//	public boolean onSnapToItem(int x, int y, Point snapPoint, MapView mapView)
//	{
//		if (Math.sqrt((x-snapPoint.x)*(x-snapPoint.x)+(y-snapPoint.y)*(y-snapPoint.y)) < 20)
//			return true;
//		return false;
//	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}
}
