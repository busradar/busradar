import static java.lang.System.out;
import java.net.*;
import java.io.*;
import java.util.regex.*;

class A
{
final static Pattern num_vehicles_re = Pattern.compile("Next (\\d) Vehicles Arrive at:<br>");
final static Pattern time_re = Pattern.compile("(\\d\\d?:\\d\\d [AP]\\.M\\.)   TO (.*)<");


public static void main(String[] args)
{
	
	
	new Thread() {
		public void run()
		{
			try {
			out.printf("hello\n");
			InputStream is = new URL("http://webwatch.cityofmadison.com/webwatch/MobileAda.aspx?r=03&d=109&s=1377").openStream();
			String str = "";
			byte[] b = new byte[1024*64];
			while ( is.read(b) != -1 ) {
				str += new String(b, "UTF-8");
				//out.printf("append\n");
			}
			
			//out.printf("out: %s\n", str);
			
			Matcher m = num_vehicles_re.matcher(str);
			if (m.find()) {
				int num_vehicles = Integer.parseInt(m.group(1));
				
				Matcher m2 = time_re.matcher(str);
				
				m2.find();
				out.printf("time <%s> to <%s>\n", m2.group(1), m2.group(2));
				
				
				while (m2.find())
					out.printf("time <%s> to <%s>\n", m2.group(1), m2.group(2));
			}
			else if (str.contains("No further buses scheduled for this stop.")) {
					// no further busses
				out.printf("no stops today\n");
			}
			else {
				// error
				throw new Exception();
			}
			
			
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
			
		}
	}.start();
}
}
