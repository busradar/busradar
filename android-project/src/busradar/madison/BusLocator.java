package busradar.madison;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;

import com.google.android.maps.GeoPoint;

public class BusLocator extends AsyncTask<Integer, ArrayList<BusOverlay.BusLocation>, ArrayList<BusOverlay.BusLocation> > {
	BusLocator curr = null;
	Thread t;
	
	void start(int r) {
		if (curr != null)
			curr.cancel(true);
		
		curr = new BusLocator();
		curr.execute(r);
	}
	
	void stop() {
		if (curr != null) {
			curr.cancel(true);
		}
		curr = null;
		
		G.bus_locs = null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected ArrayList<BusOverlay.BusLocation> doInBackground(Integer... r) {
		int route = r[0];
		for (;;) {
			ArrayList<BusOverlay.BusLocation> bus_locs = new ArrayList<BusOverlay.BusLocation>();
			
			try {
				String str = "";
				
				InputStream is = new URL(
					String.format("http://webwatch.cityofmadison.com/webwatch/UpdateWebMap.aspx?u=%02d", route)
				).openStream();
				
				byte[] b = new byte[1024*8];
				while ( is.read(b) != -1 ) {
					str += new String(b, "UTF-8");
				}
				
				String[] vehicles = str.split("\\*")[2].split(";");
				for (String v : vehicles) {
					String[] parts = v.split("\\|");
					String lat = parts[0];
					String lon = parts[1];
					String direction = parts[2];
					//String content = parts[3];
					
					BusOverlay.BusLocation bus_loc = new BusOverlay.BusLocation();
					bus_loc.loc = new GeoPoint((int)(Double.parseDouble(lat)*1E6),
							   (int)(Double.parseDouble(lon)*1E6));
					switch(Integer.parseInt(direction)) {
			             case 1:
			             case 2:
			            	 bus_loc.dir = 'N';
			            	 break;                          
			             case 5:
			             case 6:
			            	 bus_loc.dir = 'S';
			            	 break;           
			             case 7:
			             case 8:
			            	 bus_loc.dir = 'W';
			            	 break;
			             case 3:
			             case 4:
			             default:
			            	 bus_loc.dir = 'E';
			            	 break; 
	
					}
					
					bus_locs.add( bus_loc );
				}
				
				publishProgress(bus_locs);
				
			} catch(Exception e) {
				
				if (e instanceof InterruptedException) {
					return null;
				}
				
				System.out.printf("BusLoactor: error route %d\n", route);
				e.printStackTrace();
				return null;
			}
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				return null;
			}
		}
		
		
	}
	
	
	
	@Override
	protected void onCancelled() {
		 G.bus_locs = null;
		 G.activity.map_view.invalidate();
	}

	protected void onProgressUpdate(ArrayList<BusOverlay.BusLocation>... bus_locs) {
		if (!isCancelled()) {
			G.bus_locs = bus_locs[0];
			G.activity.map_view.invalidate();
		}
	 }
	
	
	 protected void onPostExecute(ArrayList<BusOverlay.BusLocation> bus_locs) {
		 if (!isCancelled()) {
			 G.bus_locs = bus_locs;
			 G.activity.map_view.invalidate();
		 }
	 }


}
