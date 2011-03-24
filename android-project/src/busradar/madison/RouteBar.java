package busradar.madison;

import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class RouteBar extends HorizontalScrollView 
{

final int routes_id = 1;
float text_size; 
Button cur_button = null;
LinearLayout layout;

public RouteBar()
{
	super (G.activity);
	
	setId(routes_id);
	setHorizontalScrollBarEnabled(false);
	
	layout = make_layout();
	addView(layout);
}

public void
update()
{
	removeView(layout);
	layout = make_layout();
	addView(layout);
}

private LinearLayout
make_layout()
{
	return new LinearLayout(G.activity) {{
		setGravity(Gravity.BOTTOM);
		for (int i = 0; i < G.route_points.length; i++) {
			
			if (G.route_points[i] == null)
				continue;
			
			if ((G.route_points[i].days & G.today) == 0)
				continue;
			
			final int ix = i; 
			final String name = i+"";
			addView(G.route_points[i].button=new Button(G.activity) {
				@Override
			public void setEnabled(boolean e) {
					if (e) {
						
						setBackgroundColor(0xff000000 | G.route_points[ix].color);
						setTextSize(text_size*1.5f);
						cur_button = this;
					}
					else {
						setBackgroundColor(0x90000000 | G.route_points[ix].color);
						setTextSize(text_size);
					}
				}
				
				{
						setText(name);
			 			setTextColor(0xffffffff);
			 			setTypeface(Typeface.DEFAULT_BOLD);
			 			text_size = getTextSize();
			 			setEnabled(false);
			 			
			 			final Button b = this;
			 			
			 			setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							if (cur_button != null) {
								cur_button.setEnabled(false);
								
								G.bus_locator.stop();
							}
							
							if (cur_button == b) {
								cur_button.setEnabled(false);
								G.active_route = 0;
								cur_button = null;
								
								G.bus_locator.stop();
								G.activity.map_view.invalidate();
							}
							else {
							b.setEnabled(true);
							G.active_route = ix;
							
							G.bus_locator.start(ix);
							G.activity.map_view.invalidate();
							}
						}
				});
	 		}});
		}
	
	}};
}
}
