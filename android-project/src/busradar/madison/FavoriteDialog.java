package busradar.madison;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.google.android.maps.GeoPoint;

public class FavoriteDialog extends Dialog {
	
	private ListView lw;
	private MyLocations locations;
	private ArrayAdapter<String> adapter;
	private ArrayList<GeoPoint> loc;
	private ArrayList<Integer> rowID;
	private ArrayList<Integer> stopid;
	
	
	public FavoriteDialog(final Main context) {
		super(context);
		
		setTitle("My Favorite Locations");
		
		//ImageView edit_button = (ImageView)findViewById(R.id.edit_button);
		//edit_button.setOnClickListener(new View.OnClickListener() {
		//	@Override public void onClick(View v) {
		//		Toast.makeText(context,"clicked",Toast.LENGTH_SHORT).show();
		//	}
		//});
		
		lw = new ListView(context);
		locations = G.favorites;//new MyLocations(context);
		TextView tv = new TextView(context);
		tv.setText("+ Add Favorite");
		tv.setPadding(10, 10, 10, 10);
		tv.setTextSize(16);
		lw.addHeaderView(tv);
		
		adapter = new ArrayAdapter<String>(context, R.layout.list_item, R.id.text) 
		{
			@Override public View 
			getView(int position, View convertView, ViewGroup parent) 
			{
				View v =  super.getView(position, convertView, parent);
				ImageView edit_button = (ImageView) v.findViewById(R.id.edit_button);
				edit_button.setOnClickListener(new View.OnClickListener() {
					@Override public void onClick(View v) {
						v.performLongClick();
					}
				});
				return v;
			}
			
		};
		
		loc = new ArrayList<GeoPoint>();
		rowID = new ArrayList<Integer>();
		stopid = new ArrayList<Integer>();
		lw.setAdapter(adapter);
		lw.setOnItemClickListener(new OnItemClickListener() {
    	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	    	System.out.println("id is "+id);
    	    	if (position == 0) 
    	    	{
    	    		AddDialog ad = new AddDialog(context, FavoriteDialog.this, locations);
    	    		ad.show();
    	    	}
    	    	else
    	    	{
    	    		FavoriteDialog.this.dismiss();
    	    		GeoPoint l = loc.get(position - 1);
    	    		context.map_view.getController().animateTo(l);
    	    		G.bus_overlay.selection = l;
    	    		
    	    		int sid = stopid.get(position-1);
    	    		if (sid > 0) {
	    	    		 StopDialog d = new StopDialog(G.activity, sid, 
	    	 	        		l.getLatitudeE6(), l.getLongitudeE6());
	    	 	        d.show();
    	    		}
    	    	}
			}
		});
		lw.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				if (position == 0) return false;
				AlertDialog.Builder b = new AlertDialog.Builder(context);
				b.setMessage("What would you like to do with "+ adapter.getItem(position - 1) + "?");
				b.setTitle(adapter.getItem(position - 1));
				b.setPositiveButton("Edit", new OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
						final Dialog d = new Dialog(context);
						d.setTitle("Edit");
						TextView tv = new TextView(context);
						tv.setText("What do you want to change the name to?");
						LinearLayout ll = new LinearLayout(context);
						d.setContentView(ll);
						ll.setOrientation(LinearLayout.VERTICAL);
						ll.addView(tv);
						final EditText et = new EditText(context);
						ll.addView(et);
						Button b = new Button(context);
						b.setText("Cancel");
						b.setOnClickListener(new View.OnClickListener(){
							public void onClick(View v) {
								d.dismiss();
							}});
						LinearLayout ll2 = new LinearLayout(context);
						ll2.setOrientation(LinearLayout.HORIZONTAL);
						ll2.addView(b);
						b = new Button(context);
						b.setText("Save");
						b.setOnClickListener(new View.OnClickListener(){
							public void onClick(View v) {
								if (et.getText().toString() == "")
								{
									Toast.makeText(context,"Please Enter a New Name",Toast.LENGTH_SHORT).show();
									return;
								}
								locations.updateLocation(rowID.get(position - 1), et.getText().toString(), loc.get(position - 1).getLatitudeE6(), loc.get(position - 1).getLongitudeE6());
								d.dismiss();
								requerry();
							}});
						ll2.addView(b);
						ll.addView(ll2);
						d.show();
					}});
				b.setNeutralButton("Delete", new OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						delete(position - 1);
						
					}});
				b.setNegativeButton("Cancel", null);
		        b.setCancelable(true);
				b.create().show();
				return true;
			}
        	
			private void delete(int position)
			{
				loc.remove(position);
				adapter.remove(adapter.getItem(position));
				if (!locations.deleteLocation(rowID.get(position).intValue()))
					Toast.makeText(context, "Delete failed on " + rowID.get(position) + ".", 
			                Toast.LENGTH_LONG).show();
				rowID.remove(position);
			}
		});
		setContentView(lw, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));////250));
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		//locations.open();
		Cursor c = locations.getAllLocations();
		c.moveToFirst();
		while (!c.isAfterLast())
		{
			adapter.add(c.getString(c.getColumnIndex(MyLocations.KEY_NAME)));
			loc.add(new GeoPoint(c.getInt(c.getColumnIndex(MyLocations.KEY_LAT)), c.getInt(c.getColumnIndex(MyLocations.KEY_LON))));
			rowID.add(new Integer(c.getInt(c.getColumnIndex(MyLocations.KEY_ROWID))));
			stopid.add(new Integer(c.getInt(c.getColumnIndex("stopid"))));
			c.moveToNext();
		}
		
		c.close();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		//locations.close();
	}
	
	public void requerry()
	{
		//if (!locations.isOpen()) return;
		adapter.clear();
		loc.clear();
		rowID.clear();
		stopid.clear();
		Cursor c = locations.getAllLocations();
		c.moveToFirst();
		while (!c.isAfterLast())
		{
			adapter.add(c.getString(c.getColumnIndex(MyLocations.KEY_NAME)));
			loc.add(new GeoPoint(c.getInt(c.getColumnIndex(MyLocations.KEY_LAT)), c.getInt(c.getColumnIndex(MyLocations.KEY_LON))));
			rowID.add(new Integer(c.getInt(c.getColumnIndex(MyLocations.KEY_ROWID))));
			stopid.add(new Integer(c.getInt(c.getColumnIndex("stopid"))));
			c.moveToNext();
		}
		
		c.close();
	}
}
