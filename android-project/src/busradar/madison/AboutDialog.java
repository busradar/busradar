package busradar.madison;

import android.app.Dialog;
import android.content.Context;
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
		        	
		        	
		        	setText(R.string.about);
		        	
		        }});
        	}});
        }});
        
        window.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
        		android.R.drawable.ic_dialog_info);
	}

}
