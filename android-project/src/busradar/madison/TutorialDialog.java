package busradar.madison;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class TutorialDialog extends Dialog {
	Gallery gallery;
	
	public TutorialDialog(final Context ctx) {
		//super(ctx, android.R.style.Theme);
		super(ctx);
		
		Window window = getWindow();
		window.requestFeature(Window.FEATURE_NO_TITLE);

		//setTitle("Tutorial");
		
        setContentView(new RelativeLayout(ctx) {{
        	setBackgroundColor(0x0106000d);  // transparent - necessary for clear images
        	addView(gallery=new Gallery(ctx) {{
        		setAdapter(new ImageAdapter(ctx));
        		setBackgroundColor(0x0106000d);  // transparent - also necessary here
        	}});
        	
        	addView(new Button(ctx){{
        		setText("▶");
        		setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						gallery.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT));
					}
				});

        	}}, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT) {{
        		addRule(ALIGN_PARENT_BOTTOM);
        		addRule(ALIGN_PARENT_RIGHT);
        	}});
        	
        	addView(new Button(ctx){{
        		setText("◀");
        		setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						gallery.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT));
					}
				});

        	}}, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT) {{
        		addRule(ALIGN_PARENT_BOTTOM);
        		addRule(ALIGN_PARENT_LEFT);
        	}});
        	
        }});
	}
	
	private class ImageAdapter extends BaseAdapter {
		private Context mContext;
		
		private int[] mImages = {
				R.drawable.tutorial1,
				R.drawable.tutorial2,
		};
		
		public ImageAdapter(Context c) { this.mContext = c; }

		public int getCount() { return this.mImages.length; }

		public Object getItem(int position) { return position; }

		public long getItemId(int position) { return position; }

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(this.mContext);
			
			i.setBackgroundColor(0x0106000d);  // transparent - also necessary here
            i.setImageResource(this.mImages[position]);
            /* Image should be scaled as width/height are set. */
            //i.setScaleType(ImageView.ScaleType.FIT_XY);
            i.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            /* Set the Width/Height of the ImageView. */
            //i.setLayoutParams(new Gallery.LayoutParams(150, 150));
            return i;
		}
	}

}
