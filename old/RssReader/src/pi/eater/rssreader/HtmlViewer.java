package pi.eater.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

public class HtmlViewer extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String html = getIntent().getExtras().getString("html");
		final String desc = getIntent().getExtras().getString("desc");
		
		WebView wv = new WebView(this);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		Button read = new Button(this);
		read.setText("Read");
		Button stop = new Button(this);
		stop.setText("Stop");
		layout.addView(read);
		layout.addView(stop);
		wv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		layout.addView(wv);
		
		
		read.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				RssReader.tts.speak(desc, TextToSpeech.QUEUE_FLUSH, null);
			}
		});
		
		stop.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				RssReader.tts.stop();
			}
		});
		
		
		wv.loadData(html, "text/html", "utf-8");
		
		setContentView(layout);
	}

}
