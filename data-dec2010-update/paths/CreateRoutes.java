import static java.lang.System.out;
import static busradar.madison.Route.*;
import java.io.*;
import java.util.*;
import org.json.*;
import org.khelekore.prtree.*;
import busradar.madison.*;

class CreateRoutes
{

public static void main(String[] args) throws Exception	
{
	String jsonstr = null;
	FileInputStream file = new FileInputStream("../paths.json");
	byte[] buf = new byte[(int) file.available()];
	file.read(buf);
		
	jsonstr = new String(buf, "UTF-8");
	file.close();

	JSONObject routes = new JSONObject(jsonstr);
	
	Route[] routes_arr = new Route[100];

	Iterator<String> it = routes.keys();
	while(it.hasNext())
	{
		String routestr = it.next();
		int route = Integer.parseInt(routestr);
		routes_arr[route] = new Route();
		RouteTree tree = new RouteTree();
		List<RouteTree.Line> list = new ArrayList<RouteTree.Line>();	 
		
		JSONArray lines = routes.getJSONArray(routestr);
		
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
		routes_arr[route].tree = tree;
	}
	
	
	routes_arr[1].color = 0xE8D03B;
    routes_arr[1].days = BOTH; //WEEKDAY;
    
	routes_arr[2].color = 0xB10069;
	routes_arr[2].days = BOTH;
	
	routes_arr[3].color = 0xCF8484;
	routes_arr[3].days = BOTH; //WEEKDAY;
	
	routes_arr[4].color = 0x007A48;
	routes_arr[4].days = BOTH;
	
	routes_arr[5].color = 0xB6695C;
	routes_arr[5].days = BOTH;
	
	routes_arr[6].color = 0xED1D24;
	routes_arr[6].days = BOTH;
	
	routes_arr[7].color = 0xA8C32E;
	routes_arr[7].days = BOTH; //HOLIDAY;
	
	routes_arr[8].color = 0xEBA05A;
	routes_arr[8].days = BOTH; //HOLIDAY;
	
    routes_arr[9].color = 0xB19C4A;
    routes_arr[9].days = BOTH; //WEEKDAY;
	
	routes_arr[10].color = 0x92923C;
    routes_arr[10].days = BOTH; //WEEKDAY;
	
	routes_arr[11].color = 0xD6886A;
    routes_arr[11].days = BOTH; //WEEKDAY;
    
    routes_arr[12].color = 0xA0881A;
    routes_arr[12].days = BOTH; //WEEKDAY;
	
	routes_arr[13].color = 0x813B08;
	routes_arr[13].days = BOTH;
	
	routes_arr[14].color = 0x7B8D1A;
    routes_arr[14].days = BOTH; //WEEKDAY;
    
    routes_arr[15].color = 0xC3C91D;
    routes_arr[15].days = BOTH; //WEEKDAY;
	
	routes_arr[16].color = 0xBBA7E5;
	routes_arr[16].days = BOTH;
	
	routes_arr[17].color = 0x346DB6;
	routes_arr[17].days = BOTH; //WEEKDAY;
	
	routes_arr[18].color = 0x3069B3;
	routes_arr[18].days = BOTH;
	
	routes_arr[19].color = 0xBCDC9B;
	routes_arr[19].days = BOTH; //WEEKDAY;
	
	routes_arr[20].color = 0xA9513D;
	routes_arr[20].days = BOTH;
	
	routes_arr[21].color = 0x749882;
	routes_arr[21].days = BOTH;
	
	routes_arr[22].color = 0x51BE9D;
	routes_arr[22].days = BOTH;
	
    routes_arr[25].color = 0xA4A19D;
    routes_arr[25].days = BOTH; //WEEKDAY;
    
    routes_arr[26].color = 0x6F3996;
    routes_arr[26].days = BOTH; //WEEKDAY;
    
    routes_arr[27].color = 0x7EC2D1;
    routes_arr[27].days = BOTH; //WEEKDAY;
    
    routes_arr[28].color = 0x00BAD3;
	routes_arr[28].days = BOTH; //WEEKDAY;
	
	routes_arr[29].color = 0x229BA6;
    routes_arr[29].days = BOTH; //WEEKDAY;
	
	routes_arr[30].color = 0xE4AD73;
	routes_arr[30].days = BOTH;
	
	routes_arr[32].color = 0x3183AB;
    routes_arr[32].days = BOTH;
	
	routes_arr[33].color = 0x00A99D;
    routes_arr[33].days = BOTH; //WEEKDAY;
	
	routes_arr[34].color = 0x9A6B20;
    routes_arr[34].days = BOTH; //WEEKDAY;
	
	routes_arr[36].color = 0x6A7840;
	routes_arr[36].days = BOTH;
	
	routes_arr[37].color = 0xCD6C79;
    routes_arr[37].days = BOTH; //WEEKDAY;
	
	routes_arr[38].color = 0x8D8BB6;
    routes_arr[38].days = BOTH; //WEEKDAY;
	
	routes_arr[39].color = 0x6294AA;
    routes_arr[39].days = BOTH; //WEEKDAY;
	
	routes_arr[40].color = 0x00AFD0;
	routes_arr[40].days = BOTH;
	
	routes_arr[44].color = 0x928B65;
    routes_arr[44].days = BOTH; //WEEKDAY;
	
	routes_arr[47].color = 0x875EA1;
    routes_arr[47].days = BOTH; //WEEKDAY;
	
	routes_arr[48].color = 0xF173AC;
    routes_arr[48].days = BOTH; //WEEKDAY;
	
	routes_arr[50].color = 0xF36F21;
	routes_arr[50].days = BOTH;
	
	routes_arr[51].color = 0xEE4D9B;
    routes_arr[51].days = BOTH;
	
	routes_arr[52].color = 0x7D2657;
	routes_arr[52].days = BOTH; //WEEKDAY;
	
    routes_arr[55].color = 0xD8BD5B;
    routes_arr[55].days = BOTH; //WEEKDAY;
    
    routes_arr[56].color = 0x977B6D;
    routes_arr[56].days = BOTH; //WEEKDAY;
    
    routes_arr[57].color = 0x87733B;
    routes_arr[57].days = BOTH; //WEEKDAY;
    
    routes_arr[58].color = 0xA2A399;
	routes_arr[58].days = BOTH; //WEEKDAY;
	
	routes_arr[59].color = 0xF49BC2;
    routes_arr[59].days = BOTH; //HOLIDAY;
    
    routes_arr[63].color = 0xEB9E18;
    routes_arr[63].days = BOTH; //HOLIDAY;
	
	routes_arr[67].color = 0x5186A7;
	routes_arr[67].days = BOTH; //BOTH;
	
	routes_arr[68].color = 0x00AB4F;
    routes_arr[68].days = BOTH; //HOLIDAY;
	
	routes_arr[70].color = 0xA6B4DC;
    routes_arr[70].days = BOTH; //WEEKDAY;
	
	routes_arr[71].color = 0xB88073;
    routes_arr[71].days = BOTH; //WEEKDAY;
	
	routes_arr[72].color = 0x9A061A;
    routes_arr[72].days = BOTH; //WEEKDAY;
	
	routes_arr[73].color = 0xEFB60F;
    routes_arr[73].days = BOTH; //WEEKDAY;
    
    routes_arr[74].color = 0xCD5C2C;
    routes_arr[74].days = BOTH; //WEEKDAY;
    
    routes_arr[78].color = 0x005CAB;
    routes_arr[78].days = BOTH; //HOLIDAY; // Saturday only
    
	routes_arr[80].color = 0xCF2820;
    routes_arr[80].days = BOTH;
    
    routes_arr[81].color = 0xF58C22;
    routes_arr[81].days = BOTH;
    
    routes_arr[82].color = 0x086CB3;
    routes_arr[82].days = BOTH;
	
	routes_arr[85].color = 0x20AE4D;
    routes_arr[85].days = BOTH;
    
	routes_arr[84].color = 0x6A3393;
    routes_arr[84].days = BOTH;
    
	routes_arr[85].color = 0x00AC4E;
    routes_arr[85].days = BOTH;

	// supplemental school day service
	routes_arr[92].color = 0x009b4b;
    routes_arr[92].days = BOTH; //WEEKDAY;
    
	routes_arr[90].color = 0x0172bc;
    routes_arr[90].days = BOTH; //WEEKDAY;
    
	routes_arr[91].color = 0x93268f;
    routes_arr[91].days = BOTH; //WEEKDAY;
    
	routes_arr[93].color = 0xe93724;
    routes_arr[93].days = BOTH; //WEEKDAY;
	
	routes_arr[94] = null;
	
	FileOutputStream file2 = new FileOutputStream("route_points.bin");
	//ObjectOutputStream out = new ObjectOutputStream(file2);
	//out.writeObject(routes_arr);
	DataOutputStream out = new DataOutputStream(file2);
	out.writeInt(routes_arr.length);
	for (int i = 0; i < routes_arr.length; i++)
		if (routes_arr[i] != null) {
			out.writeBoolean(true);	
			routes_arr[i].write(out);
		}
		else
			out.writeBoolean(false);
	out.close();
	
			
}

}
