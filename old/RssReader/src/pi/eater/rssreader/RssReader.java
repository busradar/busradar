package pi.eater.rssreader;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.vikrant.RssParser;
import com.vikrant.RssParser.Item;
import com.vikrant.RssParser.RssFeed;

public class RssReader extends Activity implements OnInitListener 
{
    /** Called when the activity is first created. */
	static TextToSpeech tts;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        RssParser rss_parser = new RssParser("http://www.npr.org/rss/rss.php?id=1001");
        try { rss_parser.parse(); } catch (Exception e) {
        	 Toast.makeText(getApplicationContext(), e.getClass()+": "+ e.getMessage(),
        			 Toast.LENGTH_LONG).show();
        	 return;
        }
        final RssFeed feed = rss_parser.getFeed();
        
        ArrayList<String> titles = new ArrayList<String>();
        for (Item item : feed.items) 
        {
        	titles.add(item.title);
        }
        
        setContentView(R.layout.main);
        ListView lv = (ListView)findViewById(R.id.ListView);
        
        lv.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, titles));
                
        tts = new TextToSpeech(this, this);
        
        lv.setOnItemClickListener(new OnItemClickListener() {
    	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
    	    {
	
    	    	Item item = feed.items.get(position);
    	    	
				Intent intent = new Intent(RssReader.this, HtmlViewer.class);
				String desc = item.description.replaceAll("\\<.*?>","");
				String html = String.format(
						"<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>\n" +
						"<table><tr><td><img src='%s'>\n"+
						"           <td><a href='%s'>%s</a>\n"+
						"</table>\n"+
						"<p>%s\n", 
							item.media_content, item.link, item.title, desc);
				
				intent.putExtra("html", html);
				intent.putExtra("desc", desc);
				
				RssReader.this.startActivity(intent);
    	    }	
    	  });
        
        lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int arg2, long arg3) {
					tts.speak(((TextView)view).getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
				return true;
			}
        	
		});
        
        Button read_btn = (Button) findViewById(R.id.Button_Read);
        read_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!tts.isSpeaking()) {
					for (Item item : feed.items) 
			        {
			        	tts.speak(item.title, TextToSpeech.QUEUE_ADD, null);
			        }
				}
			}
		});
        
        ((Button)findViewById(R.id.Button_Stop)).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				tts.stop();
				
			}
		});
        
    }

	public void onInit(int status) {
		// TODO Auto-generated method stub
		tts.speak("hello", TextToSpeech.QUEUE_ADD, null);
	}
}