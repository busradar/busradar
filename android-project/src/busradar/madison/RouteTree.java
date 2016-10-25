// Copyright (C) 2010 Aleksandr Dobkin, Michael Choi, and Christopher Mills.
// 
// This file is part of BusRadar <https://github.com/orgs/busradar/>.
// 
// BusRadar is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 3 of the License, or
// (at your option) any later version.
// 
// BusRadar is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

package busradar.madison;

import org.khelekore.prtree.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

@SuppressWarnings("serial")
public class RouteTree extends PRTree<RouteTree.Line> implements Serializable
{
	public static class Line {
		public int lon1, lat1, lon2, lat2;
		
		public Line() {}
		public Line(DataInputStream s) throws IOException {
			lon1 = s.readInt();
			lat1 = s.readInt();
			lon2 = s.readInt();
			lat2 = s.readInt();
		}
		
		public void write(DataOutputStream s) throws IOException {
			s.writeInt(lon1);
			s.writeInt(lat1);
			s.writeInt(lon2);
			s.writeInt(lat2);
		}
	}
	
	public static class RouteMBRConverter implements MBRConverter<Line> {
		public double getMinX (Line t) {
			return t.lon1 <= t.lon2 ? t.lon1 : t.lon2;
		}

		public double getMinY (Line t) {
			return t.lat1 <= t.lat2 ? t.lat1 : t.lat2;
		}

		public double getMaxX (Line t) {
			return t.lon1 >= t.lon2 ? t.lon1 : t.lon2;
		}

		public double getMaxY (Line t) {
			return t.lat1 >=t.lat2 ? t.lat1 : t.lat2;
		}
	};

	public RouteTree() {
		super(new RouteMBRConverter(), 10);
	}
	
	public RouteTree(DataInputStream s) throws IOException {
		super(s);
	}
}