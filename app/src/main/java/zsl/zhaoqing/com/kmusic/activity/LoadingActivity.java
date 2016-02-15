package zsl.zhaoqing.com.kmusic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import zsl.zhaoqing.com.kmusic.R;

/**
 * Created by Administrator on 2016/1/3.
 */
public class LoadingActivity extends Activity{

    private ImageView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        loadingView = (ImageView) findViewById(R.id.loading_view);
        AlphaAnimation animation = new AlphaAnimation(0.5f,1f);
        animation.setDuration(2000);
        loadingView.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
