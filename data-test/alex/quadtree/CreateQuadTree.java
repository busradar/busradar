import static java.lang.System.out;
import java.io.*;
import java.util.*;
import org.json.*;
import busradar.madison.*;

class CreateQuadTree
{

public static void main(String[] args) throws Exception	
{
	String jsonstr = null;
	FileInputStream file = new FileInputStream("../stops-combined.json");
	byte[] buf = new byte[(int) file.available()];
	file.read(buf);
		
	jsonstr = new String(buf, "UTF-8");
	file.close();

	JSONObject json = new JSONObject(jsonstr);
		
	Iterator<String> it = json.keys();
	ArrayList<QuadTree.Element>points = new ArrayList<QuadTree.Element>();
	

	while (it.hasNext()) {
		String key = it.next();
		int stopid = Integer.parseInt(key);
			
		JSONObject item = json.getJSONObject(key);
			
		//String name = item.getString("name");
		double lat = item.getDouble("lat");
		double lon = item.getDouble("lon");
		String dirstr = item.getString("dir");
		char dir;
		QuadTree.Element e = new QuadTree.Element();

		try {
			JSONArray routes_json = item.getJSONArray("routes");
			int[] routes = new int[routes_json.length()];

			for(int i = 0; i < routes_json.length(); i++)
			{
				routes[i] = routes_json.getInt(i);
			}
			Arrays.sort(routes);
			e.routes = routes;
		} catch (Exception ex) {
			e.routes = new int[0];
		}
		
		if (dirstr.equals("WB"))
			dir = 'W';
		else if (dirstr.equals("EB"))
			dir = 'E';
		else if (dirstr.equals("NB"))
			dir = 'N';
		else if (dirstr.equals("SB"))
			dir = 'S';
		else if (dirstr.equals(""))
			dir = '?';
		else {
			out.println("<"+dirstr+">");
			throw new Exception();
		}
		
		
		//out.printf(name);

		e.lat = (int)(lat*1E6);
		e.lon = (int)(lon*1E6);
		e.dir = dir;
		e.id = stopid;
		points.add(e); 
	}
	
	for (QuadTree.Element p : points) {
			out.printf("%d %d\n", p.lat, p.lon);
	}
	
	
	QuadTree tree = new QuadTree(points);
	FileOutputStream file2 = new FileOutputStream("stops.bin");
	DataOutputStream out = new DataOutputStream(file2);
	tree.write(out);
			
}

}
