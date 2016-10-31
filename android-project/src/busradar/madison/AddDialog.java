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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.google.android.maps.GeoPoint;

import static busradar.madison.G.dp2px;

public class AddDialog extends View {

	private Context mcontext;
	AlertDialog d;
	
	public AddDialog(final Context context, final FavoriteDialog fd, final MyLocations ml) {
		super(context);
		
		final View view = View.inflate(context, R.layout.add_location, null);
		d = new AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog)
                .setPositiveButton("Save", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				GeoPoint gp;
				switch (((RadioGroup) view.findViewById(R.id.LinearLayout01)).getCheckedRadioButtonId())
				{
					case R.id.RadioButton01:
						if (((EditText) view.findViewById(R.id.Address)).getText().toString() == "")
						{
							Toast.makeText(context, "Please Specify an Address", Toast.LENGTH_SHORT).show();
							return;
						}
						gp = reverseGeocode(((EditText) view.findViewById(R.id.Address)).getText().toString());
						if (gp == null)
						{
							Toast.makeText(context, "Error: Cannot Find: " + ((EditText) view.findViewById(R.id.Address)).getText().toString(), Toast.LENGTH_SHORT).show();
							return;
						}
					break;
					case R.id.RadioButton02:
						gp = G.location_overlay.getMyLocation();
						if (gp == null)
						{
							Toast.makeText(context, "Error: Cannot Find Current GPS Location", Toast.LENGTH_SHORT).show();
							return;
						}
					break;
					case R.id.RadioButton03:
						gp = G.bus_overlay.selection != null ?
								G.bus_overlay.selection : G.activity.map_view.getMapCenter();
					break;
					default:
						return;
				}
				if (((EditText) view.findViewById(R.id.Name)).getText().toString() == "")
				{
					Toast.makeText(context, "Please Specify a Name", Toast.LENGTH_SHORT).show();
					return;
				}
				ml.insertLocation(((EditText) view.findViewById(R.id.Name)).getText().toString(), gp.getLatitudeE6(), gp.getLongitudeE6());
				d.dismiss();
				fd.requerry();
			}})
			.setTitle("New Favorite Location")
			.setView(view)
			.create();
		
		mcontext = context;
		
		
//		d.setContentView(R.layout.add_location);
//		
		RadioButton rb = (RadioButton) view.findViewById(R.id.RadioButton01);
		rb.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
				view.findViewById(R.id.Address).setEnabled(isChecked);
			}});
		((RadioGroup) view.findViewById(R.id.LinearLayout01)).check(R.id.RadioButton01);
		
		
		//b = (Button) findViewById(R.id.Button02);
		//b.setOnClickListener(
	}

	public GeoPoint reverseGeocode(String s) {
		Geocoder geoCoder = new Geocoder(mcontext, Locale.getDefault());    
		GeoPoint gp;
		try {
			List<Address> addresses = geoCoder.getFromLocationName(s + " Madison, WI", 5);
				if (addresses.size() > 0) {
					gp = new GeoPoint(
							(int) (addresses.get(0).getLatitude() * 1E6), 
							(int) (addresses.get(0).getLongitude() * 1E6));
					//Toast.makeText(mcontext, gp.getLatitudeE6() / 1E6 + "," + gp.getLongitudeE6() /1E6 , Toast.LENGTH_SHORT).show();
					return gp;
				}
    	} catch (IOException e) {
        	e.printStackTrace();
    	}
		return null;
	}
	
	public void show() {
		d.show();
	}
}
