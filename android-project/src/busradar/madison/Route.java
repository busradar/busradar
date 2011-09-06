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