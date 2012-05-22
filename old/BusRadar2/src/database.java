package data.base;

import java.io.IOException;

import android.app.ListActivity;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class database extends ListActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);

	  String[] stops;
	  DataBaseHelper myDbHelper = new DataBaseHelper(this.getApplicationContext());
      myDbHelper = new DataBaseHelper(this);

      try {

      	myDbHelper.createDataBase();

	} catch (IOException ioe) {

		throw new Error("Unable to create database");

	}

	try {

		myDbHelper.openDataBase();

	}catch(SQLException sqle){

		throw sqle;

	}
	stops = new String[Stop.getAllStops().length];
	for (int x = 0; x < stops.length; x++)
		stops[x] = Stop.getAllStops()[x].myName();

	setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, stops));

	  ListView lv = getListView();
	  lv.setTextFilterEnabled(true);

	  lv.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
	        int position, long id) {
	      // When clicked, show a toast with the TextView text
	      Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
	          Toast.LENGTH_SHORT).show();
	    }
	  });
	}
}