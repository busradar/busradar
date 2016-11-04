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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.widget.Button;

public class Route
{
	public static final byte WEEKDAY = 1;
	public static final byte HOLIDAY = 2;
	public static final byte BOTH = WEEKDAY | HOLIDAY;
	
	public int color;
	public byte days;
	public Button button;
	public String name;
	public int id;
	public int[][] polylines;
	
	public Route() {}
	
	public Route(DataInputStream s) throws IOException {
        name = s.readUTF();
        id = s.readInt();
        color = s.readInt();
        days = s.readByte();
        
        int numPolylines = s.readInt();
        polylines = new int[numPolylines][];
        for (int i = 0; i < numPolylines; i++) {
            int numCoords = s.readInt();
            int[] coords = new int[numCoords];
            polylines[i] = coords;
            for (int j = 0; j < numCoords; j++) {
                coords[j] = s.readInt();
            }
        }
	}
}