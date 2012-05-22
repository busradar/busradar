package busradar.madison;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class StopDialog extends Dialog {

	public StopDialog(Context context, Stop curStop) {
		super(context);
		setContentView(R.layout.stop_dialog);
        setTitle(curStop.myName());
        TextView tv = (TextView) findViewById(R.id.ListView01);
        tv.setText(concatBus(curStop));
        ListView lv = (ListView) findViewById(R.id.ListView02);
        lv.setAdapter(new TimeListAdapter(context, R.layout.list_item,
        		curStop.getBusTimes(), curStop.getRoutes(),3));
        Button b = (Button) findViewById(R.id.Button02);
        b.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				StopDialog.this.dismiss();
				
			}

        	
        });
	}

	private String concatBus(Stop curStop) {
		Route[] r = curStop.getRoutes();
		String s = String.valueOf(r[0].busNumber());
		for (int i = 1; i < r.length; i++)
			s = s.concat(", ").concat(String.valueOf(r[i].busNumber()));
		return s;
	}
	
	public void refresh(Context context, Stop curStop) {
		setTitle(curStop.myName());
        TextView tv = (TextView) findViewById(R.id.ListView01);
        tv.setText(concatBus(curStop));
        ListView lv = (ListView) findViewById(R.id.ListView02);
        lv.setAdapter(new TimeListAdapter(context, R.layout.list_item,
        		curStop.getBusTimes(), curStop.getRoutes(),3));
	}
}
