import static java.lang.System.out;
import static busradar.madison.Route.*;
import java.io.*;
import java.util.*;
import org.json.*;
import org.khelekore.prtree.*;
import busradar.madison.*;
import org.apache.commons.io.*;

class CreateRoutes
{




public static void main(String[] args) throws Exception	
{


	JSONObject routes = new JSONObject(FileUtils.readFileToString(new File("../paths.json")));
	JSONArray routes_info = new JSONArray(FileUtils.readFileToString(new File("../routes.json")));
	HashMap<String, Route> routes_dict = new LinkedHashMap<String, Route>();
	
	for (int i = 0; i < routes_info.length(); i++)
	{
            
            
            JSONObject ri = routes_info.getJSONObject(i);
            String name = ri.getString("name");
            int color = Integer.parseInt(ri.getString("color"), 16);
            
            if (routes_dict.get(name) != null)
                throw new Error(name);
            
            Route r = new Route();
            r.color = color;
            r.days = BOTH;
            r.name = name;
            
            routes_dict.put(name, r);
            
            System.out.printf("read %s %s\n", name, color);
	}
	
	
	Iterator route_keys_it = routes.keys();
	
	while (route_keys_it.hasNext())
	{
		
		String route_name = (String) route_keys_it.next();;
		JSONArray lines = routes.getJSONArray(route_name);
		
		try {
                    route_name = Integer.parseInt(route_name) + "";
                }
                catch (NumberFormatException e) {}
                
		Route route = routes_dict.get(route_name);
		
		System.out.printf("processing %s\n", route_name);
		
		RouteTree tree = new RouteTree();
		List<RouteTree.Line> list = new ArrayList<RouteTree.Line>();	 
		
		for (int j = 0; j < lines.length(); j++)
		{
			JSONArray a = lines.getJSONArray(j);
			RouteTree.Line r = new RouteTree.Line();
			r.lat1 = a.getInt(0);
			r.lon1 = a.getInt(1);
			r.lat2 = a.getInt(2);
			r.lon2 = a.getInt(3);
		
			list.add(r);
		}
		
		tree.load(list);
		route.tree = tree;
	}
	

    
	
	FileOutputStream file2 = new FileOutputStream("route_points.bin.tmp");
	DataOutputStream out = new DataOutputStream(file2);
	
	out.writeInt(routes_dict.values().size());
	int i = 0;
	for (String k : routes_dict.keySet())
	{
            Route r = routes_dict.get(k);
            if (r.tree == null) {
                System.err.printf("Paths for route %s are missing!\n", k);
                System.exit(1);
            }
            System.out.printf("%d: %s:%s\n", i, k, r);
            r.write(out);
            i++;
        }
                
	out.close();
	
	new File("route_points.bin.tmp").renameTo(new File("route_points.bin"));
	
	System.out.printf("%s %s\n", routes_dict.values().size(), routes_info.length());
	
			
}

}
