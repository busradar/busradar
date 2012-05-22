package busradar.madison;

import java.util.ArrayList;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

public class QuadTree
{
	
	public static class Element {
		public int lat;
		public int lon;
		
		public char dir;
		public String id;
		public int[] routes;
		
		public Element() {}
		
		public void write(DataOutputStream s) throws IOException {
                        System.out.printf("writeInt: lat=%s\n", lat);
                        System.out.printf("writeInt: lon=%s\n", lon);
			s.writeInt(lat);
			s.writeInt(lon);
			
			System.out.printf("writeChar: dir=%s\n", dir);
			System.out.printf("writeInt: id=%s\n", id);
			System.out.printf("writeInt: routes.lenght=%s\n", routes.length);
			s.writeChar(dir);
			s.writeUTF(id);
			s.writeInt(routes.length);
			for(int i = 0; i < routes.length; i++)
			{
                            System.out.printf("writeInt: route[%s]=%s\n", i, routes[i]);
                            s.writeInt(routes[i]);
                        }
		}
		
		public Element(DataInputStream s) throws IOException {
			lat = s.readInt();
			lon = s.readInt();
			
			dir = s.readChar();
			id = s.readUTF();
			routes = new int[s.readInt()];
			for(int i = 0; i < routes.length; i++)
				routes[i] = s.readInt();
		}
	}
	
	final static int maxchild = 20;
	Element[] items;
	QuadTree nw, ne, sw, se;
	
	int midx, midy;	
	
	public void write(DataOutputStream s) throws IOException {
		if (items != null) {
                        System.out.printf("writeBoolean: true\n");
			s.writeBoolean(true);

			System.out.printf("writeInt: items.length=%s\n", items.length);
			s.writeInt(items.length);
			
			for (int i = 0; i < items.length; i++)
			{
				items[i].write(s);
                        }
		}
		else {
                        System.out.printf("writeBoolean: flase\n");
			s.writeBoolean(false);
		
			nw.write(s);
			ne.write(s);
			sw.write(s);
			se.write(s);
		
                        System.out.printf("writeInt: midx=%s\n", midx);
                        System.out.printf("writeInt: midy=%s\n", midy);
			s.writeInt(midx);
			s.writeInt(midy);
		}
	}
	
	public QuadTree(DataInputStream s) throws IOException {
		if (s.readBoolean() == true) {
			int x = s.readInt();
			items = new Element[x];
			for (int i = 0; i < items.length; i++) {
				items[i] = new Element(s);
			}
		} else {
		
			nw = new QuadTree(s);
			ne = new QuadTree(s);
			sw = new QuadTree(s);
			se = new QuadTree(s);
		
			midx = s.readInt();
			midy = s.readInt();
		}
	}

	public QuadTree(ArrayList<Element> points)
	{	

		if (points.size() <= maxchild) {
			items = points.toArray(new Element[0]);
			return;
		}
	
		Collections.sort(points, new Comparator<Element>() {
			public int compare(Element a, Element b) { return a.lon - b.lon; }
			public boolean equals(Object o) { return false; }
		});
			
		midx = points.get(points.size()/2+1).lon;
	
		Collections.sort(points, new Comparator<Element>() {
			public int compare(Element a, Element b) { return a.lat - b.lat; }
			public boolean equals(Object o) { return false; }
		});
	
		midy = points.get(points.size()/2+1).lat;
	
		ArrayList<Element> nwl = new ArrayList<Element>();
		ArrayList<Element> nel = new ArrayList<Element>();
		ArrayList<Element> swl = new ArrayList<Element>();
		ArrayList<Element> sel = new ArrayList<Element>();
	
		for (Element p : points) 
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


	public ArrayList<Element>
	get(int xboundmin, int yboundmin, int xboundmax, int yboundmax, int span)
	{	
		ArrayList<Element> l = new ArrayList<Element>();
	
		if (items != null) // we're a leaf
		{	
			for (Element p : items) {
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
