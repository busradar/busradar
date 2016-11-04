import static java.lang.System.out;
import static busradar.madison.Route.*;
import java.io.*;
import java.util.*;
import org.json.*;
import org.khelekore.prtree.*;
import busradar.madison.*;
import org.apache.commons.io.*;

class CreatePaths {
    public static void main(String[] args) throws Exception {
        int[][][] polylines = null;
        
        JSONObject pathsdb = new JSONObject(
                FileUtils.readFileToString(new File("../paths-joined.json")));
        JSONArray routes = new JSONArray(
                FileUtils.readFileToString(new File("../routes.json")));
                
        ArrayList<Route> paths_out = new ArrayList<Route>();
                
        for (int i = 0; i < routes.length(); i++) {
            JSONObject route = routes.getJSONObject(i);
            if (route.getBoolean("inactive")) {
                continue;
            }
            
            String name = route.getString("name");
            int color = Integer.parseInt(route.getString("color"), 16);
            Route r = new Route();
            r.color = color;
            r.days = BOTH;
            r.name = name;
            r.id = route.getInt("id");
            
            JSONArray polylines_in = pathsdb.getJSONArray(name);
            r.polylines = new int[polylines_in.length()][];
            for (
                    int polylinesi = 0; 
                    polylinesi < polylines_in.length(); 
                    polylinesi++) {
                JSONArray polyline_in = polylines_in.getJSONArray(polylinesi);
                int[] polyline = r.polylines[polylinesi] = 
                        new int[polyline_in.length() * 2];
                for (int j = 0; j < polyline_in.length(); j++) {
                    polyline[j*2] = polyline_in.getJSONArray(j).getInt(0);
                    polyline[j*2+1] = polyline_in.getJSONArray(j).getInt(1);
                }
            }
            paths_out.add(r);
            
        }
        
        FileOutputStream file = new FileOutputStream("routes.bin.tmp");
        DataOutputStream out = new DataOutputStream(file);
        out.writeInt(paths_out.size());
        for (Route r : paths_out) {
            r.write(out);
        }
        file.close();
        new File("routes.bin.tmp").renameTo(new File("routes.bin"));
    }
}