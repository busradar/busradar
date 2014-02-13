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
	
	public RouteTree tree;
	public int color;
	public byte days;
	public Button button;
	public String name;
	
	public Route() {}
	
	public Route(DataInputStream s) throws IOException {
        name = s.readUTF();
		tree = new RouteTree(s);
		color = s.readInt();
		days = s.readByte();
	}
	
	public void write(DataOutputStream s) throws IOException {
        s.writeUTF(name);
		tree.write(s);
		s.writeInt(color);
		s.writeByte(days);
	}
}