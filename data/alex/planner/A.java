import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

class A
{

public static void
main(String[] args) throws Exception
{
if (false) {
	String data = 
		".s="+
		"&FormState=Valid"+
		"&StartGeo=Stop%2C%3B-89405837%3B43072117"+
		"&EndGeo=Stop%2C%3B-89475418%3B43053811"+
		"&Start="+
		"&SelectStartType=0"+
		"&End="+
		"&SelectEndType=0&Date=05-13-2010&Time=+5%3A05&Meridiem=p&TripDirection=DEP&SortBy=1&.a=iTripPlanning";
		
	URL url = new URL("http://trip.cityofmadison.com");
	URLConnection conn = url.openConnection();
	conn.setDoOutput(true);
	OutputStream os = conn.getOutputStream();
	os.write(data.getBytes());
	os.flush();

	InputStream is = conn.getInputStream();
}
//	Scanner scan = new Scanner(is, "UTF-8");

Scanner scan = new Scanner(new File("out.html"), "ISO-8859-1");

//	byte[]buf = new byte[1024];
//	int n;
//	while ( (n = is.read(buf)) > -1 ) {
//		System.out.println(new String(buf, 0, n));
//	} 
	
	Pattern p = Pattern.compile("<tr>.*?ItineraryDetail.*?<td.*?>(.*?)</td>.*?<td.*?>(.*?)</td>.*?<td.*?>(.*?)</td>.*?<td.*?>(.*?)</td>.*?<td.*?>(.*?)</td>.*?<td.*?>(.*?)</td>.*?</tr>", Pattern.DOTALL);
//	Pattern p = Pattern.compile("<tr>.*?ItineraryDetail.*?</tr>", Pattern.MULTILINE | Pattern.DOTALL);
	while (scan.findWithinHorizon(p, 0) != null) {
		System.out.print("found:\n");
		String routes = scan.match().group(1).trim();
		String depart = scan.match().group(2).trim();
		String arrive = scan.match().group(3).trim();
		String duration = scan.match().group(4).trim();
		String transfer = scan.match().group(5).trim();
		String walk = scan.match().group(6).trim();

		System.out.printf("route <%s> depart <%s> arrive '%s' duration <%s> transfer <%s> walk <%s>\n",
			routes, depart, arrive, duration, transfer, walk);		
		
		System.out.print("\n\n");
	}
}

}
