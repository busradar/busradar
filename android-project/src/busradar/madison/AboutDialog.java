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

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class AboutDialog extends Dialog {

	public AboutDialog(final Context ctx) {
		super(ctx);
		
		Window window = getWindow();
		window.requestFeature(Window.FEATURE_LEFT_ICON);

		setTitle("About BusRadar");
		
        setContentView(new LinearLayout(ctx) {{
        	setBackgroundColor(0xffffffff);
        	addView(new ImageView(ctx) {{
        		setPadding(0, 10, 5, 0);
        		setImageResource(R.drawable.icon);
        	}});
	        //addView(new WebView(ctx) {{
	        //	getSettings().setTextSize(TextSize.NORMAL);
	        //	loadUrl("file:///android_asset/about.html");
	        //}});
        	addView(new ScrollView(ctx) {{
        		addView(new TextView(ctx) {{
	        		setPadding(0, 10, 10, 0);
	        		setTextColor(0xff000000);	        		
		        	setMovementMethod(LinkMovementMethod.getInstance());
		        	
		        	String version_string;
		        	try {
		        		version_string = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
		        	}
		        	catch (NameNotFoundException  e) {
		        		version_string = "??";
		        	}
		        	String text = ctx.getString(R.string.about).replace("$VERSION$", version_string)
		        			                                   .replace("$DB_VERSION$", G.db_version + "")
		        			                                   .replace("$DB_NAME$", G.db_name);
		        			
		        	setText(Html.fromHtml(text));
		        	
		        }});
        	}});
        }});
        
        window.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
        		android.R.drawable.ic_dialog_info);
	}

}
