package busradar.madison;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TransparentPanel extends LinearLayout 
{ 
	private Paint innerPaint, borderPaint ;
	private Context mContext;
    
	public TransparentPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		mContext = context;
	}

	public TransparentPanel(Context context) {
		super(context);
		init();
		mContext = context;
	}

	private void init() {
		innerPaint = new Paint();
		innerPaint.setARGB(225, 75, 75, 75); //gray
		innerPaint.setAntiAlias(true);

		borderPaint = new Paint();
		borderPaint.setARGB(255, 255, 255, 255);
		borderPaint.setAntiAlias(true);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeWidth(2);
	}
	
	public void setInnerPaint(Paint innerPaint) {
		this.innerPaint = innerPaint;
	}

	public void setBorderPaint(Paint borderPaint) {
		this.borderPaint = borderPaint;
	}

    @Override
    protected void dispatchDraw(Canvas canvas) {
    	
    	RectF drawRect = new RectF();
    	drawRect.set(0,0, getMeasuredWidth(), getMeasuredHeight());
    	
    	canvas.drawRoundRect(drawRect, 5, 5, innerPaint);
		canvas.drawRoundRect(drawRect, 5, 5, borderPaint);

		
		
//		ADD BUTTONS HERE
//				
//		Button b = (Button) findViewById(R.id.button_1);
//		b.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Toast.makeText(mContext, "Hello!", Toast.LENGTH_SHORT).show();
//			}
//		});
		
		super.dispatchDraw(canvas);
    }

}
