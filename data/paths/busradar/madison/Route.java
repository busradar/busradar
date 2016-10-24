package busradar.madison;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Route
{
	public static final byte WEEKDAY = 1;
	public static final byte HOLIDAY = 2;
	public static final byte BOTH = WEEKDAY | HOLIDAY;
	
	public String name;
	public int id;
	public RouteTree tree;
	public int color;
	public byte days;
	
	public Route() {}
	
	public Route(DataInputStream s) throws IOException {
        name = s.readUTF();
        id = s.readInt();
		tree = new RouteTree(s);
		color = s.readInt();
		days = s.readByte();
	}
	
	public void write(DataOutputStream s) throws IOException {
        s.writeUTF(name);
        s.writeInt(id);
		tree.write(s);
		s.writeInt(color);
		s.writeByte(days);
	}
}
