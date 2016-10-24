// Copyright (C) 2010 Aleksandr Dobkin, Michael Choi, and Christopher Mills.
// 
// This file is part of BusRadar <https://github.com/orgs/busradar/>.
// 
// BusRadar is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 3 of the License, or
// (at your option) any later version.
// 
// BusRadar is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

package busradar.madison;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.net.*;
import java.io.*;
import org.json.*;

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
	
	static final String VEHICLES_URL = "http://webwatch.cityofmadison.com/tmwebwatch/GoogleMap.aspx/getVehicles";
	JSONObject rpc(int id) throws Exception {
	
        HttpURLConnection conn = (HttpURLConnection) new URL(VEHICLES_URL).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        
        JSONObject obj = new JSONObject();
        obj.put("routeID", id);
        
        OutputStream os = conn.getOutputStream();
        os.write(obj.toString(0).getBytes("UTF-8"));
        os.close();
        
        InputStream is = conn.getInputStream();
        byte[] b = new byte[1024*8];
        String str = "";
        while ( is.read(b) != -1 ) {
            str += new String(b, "UTF-8");
        }
        
        return new JSONObject(str);
        
	}
	
	void try_fetch(int routeid) throws Exception {
        ArrayList<BusOverlay.BusLocation> bus_locs = new ArrayList<BusOverlay.BusLocation>();
        JSONObject result = rpc(routeid);
        if (result.isNull("d")) {
            return;
        }
        JSONArray vehicles = result.getJSONArray("d");
        for (int i = 0; i < vehicles.length(); i++) {
            JSONObject vehicle = vehicles.getJSONObject(i);
            double lat = vehicle.getDouble("lat");
            double lng = vehicle.getDouble("lon");
            int heading = vehicle.getInt("heading");
            
            BusOverlay.BusLocation bus_loc = new BusOverlay.BusLocation();
            bus_loc.loc = new GeoPoint(
                    (int) (lat*1E6),
                    (int) (lng*1E6));
            bus_loc.heading = heading;
            bus_locs.add(bus_loc);
            
        }
        publishProgress(bus_locs);
}
	
	@SuppressWarnings("unchecked")
	@Override
	protected ArrayList<BusOverlay.BusLocation> doInBackground(Integer... r) {
		int routeid = r[0];
		for (;;) {
			try {
				try_fetch(routeid);
			} catch(Exception e) {
				
				if (e instanceof InterruptedException) {
					return null;
				}
				
				System.out.printf("BusRadar: error getting bus location\n");
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
