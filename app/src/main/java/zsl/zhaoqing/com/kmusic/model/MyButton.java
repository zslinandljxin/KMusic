package zsl.zhaoqing.com.kmusic.model;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import zsl.zhaoqing.com.kmusic.R;

/**
 * Created by Administrator on 2016/2/14.
 */
public class MyButton extends ImageView {

    private Bitmap bitmap;
    private Bitmap srcBitmap;
    private Canvas mCanvas;
    private Rect rect;
    private Paint paint;
    private int color;
    private boolean flag = false;

    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.MyButton);
        int n = a.getIndexCount();
        if (n > 0){
            int attr = a.getIndex(0);
            if (attr == R.styleable.MyButton_pressed_color){
                color = a.getColor(attr, Color.GRAY);
            }
        }
        a.recycle();
        int resId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
        srcBitmap = BitmapFactory.decodeResource(getResources(),resId);
        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setDither(true);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == event.ACTION_DOWN){
                    flag = true;
                    invalidate();
                }else{
                    flag = false;
                    invalidate();
                }
                return false;
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int bitmapWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int bitmapHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int left = getMeasuredWidth() / 2 - bitmapWidth / 2;
        int top = getMeasuredHeight() / 2 - bitmapHeight / 2;
        rect = new Rect(left, top, left + bitmapWidth, top + bitmapHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (flag) {
            bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(bitmap);
            mCanvas.drawBitmap(srcBitmap,null ,rect , null);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            mCanvas.drawRect(rect, paint);
            canvas.drawBitmap(bitmap, 0, 0, null);
        }else {
            canvas.drawBitmap(srcBitmap,null,rect,null);
        }
    }

}
