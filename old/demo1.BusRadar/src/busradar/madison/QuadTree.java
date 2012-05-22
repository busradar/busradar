package busradar.madison;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;

@SuppressWarnings("serial")
public class QuadTree<E extends QuadTree.Element> implements Serializable
{
	
	public static class Element implements Serializable {
		public int lat;
		public int lon;
	}
	
	public static class BusStop extends Element {
		public char dir;
		public int id;
	}
	
	public static class RoutePoint extends Element {
		public int lat2, lon2;
		public int id;
	}
	
	static int maxchild = 20;
	Object[] items;
	QuadTree nw, ne, sw, se;
	
	int midx, midy;	

	public QuadTree(ArrayList<E> points)
	{	

		if (points.size() <= maxchild) {
			items = points.toArray();
			return;
		}
	
		Collections.sort(points, new Comparator<E>() {
			public int compare(E a, E b) { return a.lon - b.lon; }
			public boolean equals(Object o) { return false; }
		});
			
		midx = points.get(points.size()/2+1).lon;
	
		Collections.sort(points, new Comparator<E>() {
			public int compare(E a, E b) { return a.lat - b.lat; }
			public boolean equals(Object o) { return false; }
		});
	
		midy = points.get(points.size()/2+1).lat;
	
		ArrayList<E> nwl = new ArrayList<E>();
		ArrayList<E> nel = new ArrayList<E>();
		ArrayList<E> swl = new ArrayList<E>();
		ArrayList<E> sel = new ArrayList<E>();
	
		for (E p : points) 
		{
			if (p.lat >= midy) { // north
				if (p.lon >= midx) { // east
					nel.add(p);
				}
				else { //west
					nwl.add(p);
				}
			}
			else { // south
				if (p.lon >= midx) { // east
					sel.add(p);
				}
				else { //west
					swl.add(p);
				}
			}
		}
	
		nw = new QuadTree(nwl);
		ne = new QuadTree(nel);
		sw = new QuadTree(swl);
		se = new QuadTree(sel);
	}


	public ArrayList<E>
			get(int xboundmin, int yboundmin, int xboundmax, int yboundmax, int span)
	{	
	
		ArrayList<E> l = new ArrayList<E>();
	
		if (items != null) // we're a leaf
		{	
			for (Object _p : items) {
				E p = (E)_p;
				if (p.lon >= xboundmin && p.lon <= xboundmax &&
								p.lat >= yboundmin && p.lat <= yboundmax )
					l.add(p);
			}
		
			return l;
		}
		else {
		
			if (yboundmin < midy) { // include south
				if (xboundmin < midx) { // include west
					l.addAll(sw.get(xboundmin, yboundmin, xboundmax, yboundmax, span));
				}
				if (xboundmax >= midx) { // include east
					l.addAll(se.get(xboundmin, yboundmin, xboundmax, yboundmax, span));
				}
			}
		
			if (yboundmax >= midy) { // include north
				if (xboundmin < midx) { // include west
					l.addAll(nw.get(xboundmin, yboundmin, xboundmax, yboundmax, span));
				}
				if (xboundmax >= midx) { // include east
					l.addAll(ne.get(xboundmin, yboundmin, xboundmax, yboundmax, span));
				}
			}
		
			return l;
		}
	}
}
