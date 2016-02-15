package zsl.zhaoqing.com.kmusic.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.URL;

/**
 * Created by Administrator on 2016/2/3.
 */
public class ImageDownload extends AsyncTask<String,Void,Bitmap> {

    private ImageView imageView;
    public ImageDownload(ImageView imageView){
       this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        URL url = null;
        Bitmap bitmap = null;
        try {
            url = new URL(params[0]);
            Drawable drawable = (Drawable) url.getContent();
            bitmap = ((BitmapDrawable)drawable).getBitmap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
