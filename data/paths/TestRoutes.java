import java.io.*;
import java.util.*;
import busradar.madison.*;

public class TestRoutes {
    public static void main(String[] args) throws Exception {
        FileInputStream file = new FileInputStream("route_points.bin");
        DataInputStream in = new DataInputStream(file);
        
        int len = in.readInt();
        //Route[] route_points = new Route[in.readInt()];
        for (int i = 0; i < len; i++) {
            Route r = new Route(in);
            System.out.printf("name=%s\n", r.name);
            ArrayList<RouteTree.Line> lines = new ArrayList<RouteTree.Line>();
//             r.tree.find(Integer.MIN_VALUE, Integer.MIN_VALUE,
//                     Integer.MAX_VALUE, Integer.MAX_VALUE, lines);
            r.tree.find(-89531539, 42936252, -89282637, 43207068, lines);
            System.out.printf("  cnt=%d\n", lines.size());
            System.out.printf("  nr leaves %d\n", r.tree.getNumberOfLeaves());
                    
            
        }
    }
}