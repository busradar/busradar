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

import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


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
	return new LinearLayout(G.activity) 
	{{
//         addView(new Button(G.activity) {
//             @Override
//             public void setEnabled(boolean e) {
//                 if (e) {
//                     
//                     setBackgroundColor(0xff000000 | 0x00FF0000);
//                     setTextSize(TypedValue.COMPLEX_UNIT_PX, text_size*1.5f);
//                     cur_button = this;
//                 }
//                 else {
//                     setBackgroundColor(0x90000000 | 0x00FF0000);
//                     setTextSize(TypedValue.COMPLEX_UNIT_PX, text_size);
//                 }
//             }
//                 
//             {
//                 setText("ALL");
//                 setTextColor(0xffffffff);
//                 setTypeface(Typeface.DEFAULT_BOLD);
//                 text_size = getTextSize();
//                 setEnabled(false);
//                 
//                 final Button b = this;
//                 
//                 setOnClickListener(new OnClickListener() {
//                     public void onClick(View v) {
//                         if (cur_button != null) {
//                             cur_button.setEnabled(false);
//                         }
//                         
//                         if (cur_button == b) {
//                             cur_button.setEnabled(false);
//                             G.active_route = -1;
//                             cur_button = null;
//                             G.bus_locator.stop();
//                             G.activity.map_view.invalidate();
//                         }
//                         else {
//                             b.setEnabled(true);
//                             G.active_route = -2;
//                             G.activity.map_view.invalidate();
//                         }
//                     }
//                 });
//             }}, new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT) {{
//                                 gravity = Gravity.BOTTOM;
//             }});
		//setGravity(Gravity.BOTTOM);
		for (int i = 0; i < G.routes.length; i++) 
		{
			
			if (G.routes[i] == null)
				continue;
			
			if ((G.routes[i].days & G.today) == 0)
				continue;
			
			final int ix = i; 
			final Route route = G.routes[i];
			setBaselineAligned(false);
			addView(G.routes[i].button=new Button(G.activity) {
				@Override
				public void setEnabled(boolean e) {
					if (e) {
						
						setBackgroundColor(0xff000000 | G.routes[ix].color);
						setTextSize(TypedValue.COMPLEX_UNIT_PX, text_size*1.5f);
						cur_button = this;
					}
					else {
						//setBackgroundColor(0xff000000 | G.routes[ix].color);
						setBackgroundColor(0x90000000 | G.routes[ix].color);
						setTextSize(TypedValue.COMPLEX_UNIT_PX, text_size);
					}
				}
					
				{
					setText(route.name);
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
								G.active_route = -1;
								cur_button = null;
								
								G.bus_locator.stop();
								G.activity.map_view.invalidate();
							}
							else {
								b.setEnabled(true);
								G.active_route = ix;
								
								G.bus_locator.start(G.routes[ix].id);
								G.activity.map_view.invalidate();
							}
						}
		 			});
		 		}}, new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT) {{
                                    gravity = Gravity.BOTTOM;
		 		}});
		}
	}};
}
}
