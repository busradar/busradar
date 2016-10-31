import static java.lang.System.out;
import java.io.*;
import java.util.*;
import org.json.*;
import busradar.madison.*;
import org.apache.commons.io.*;

class CreateQuadTree
{

public static void main(String[] args) throws Exception	
{
	JSONObject json = new JSONObject(FileUtils.readFileToString(new File("../combined-stops.json")));
	JSONArray routes_info_json = new JSONArray(FileUtils.readFileToString(new File("../routes.json")));

		
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
				int route_id = routes_json.getJSONObject(i).getInt("routeid");
				
				int index;
				boolean found = false;
				for (index = 0; index < routes_info_json.length(); index++) {
                    if (routes_info_json.getJSONObject(index).getInt("id") == route_id)
                    {
                        found = true;
                        break;
                    }
				}
				
				if (!found) {
                                    System.err.printf("Route %s not found. Maybe update ../routes.json ?\nGoodbye...\n", route_id);
                                    System.exit(1);
                                }
				
				routes[i] = index;
			}
			Arrays.sort(routes);
			e.routes = routes;
		} catch (Exception ex) {
			e.routes = new int[0];
			throw ex;
		}
		
		if (dirstr.equals("W"))
			dir = 'W';
		else if (dirstr.equals("E"))
			dir = 'E';
		else if (dirstr.equals("N"))
			dir = 'N';
		else if (dirstr.equals("S"))
			dir = 'S';
		else if (dirstr.equals("?"))
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
	
	//for (QuadTree.Element p : points) {
	//		out.printf("%d %d\n", p.lat, p.lon);
	//}
	
	
	QuadTree tree = new QuadTree(points);
	FileOutputStream file2 = new FileOutputStream("stops.bin");
	DataOutputStream out = new DataOutputStream(file2);
	tree.write(out);
			
}

}
