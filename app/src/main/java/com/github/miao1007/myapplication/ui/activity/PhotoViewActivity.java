package com.github.miao1007.myapplication.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.ui.widget.Position;
import com.github.miao1007.myapplication.utils.LogUtils;
import com.github.miao1007.myapplication.utils.StatusbarUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by leon on 1/19/16.
 */
public class PhotoViewActivity extends AppCompatActivity {

  static final String TAG = LogUtils.makeLogTag(PhotoViewActivity.class);
  static final public String EXTRA_LEFT = "EXTRA_LEFT";
  @Bind(R.id.iv_photo) ImageView mIvPhoto;
  //static final public String EXTRA_RIGHT = "EXTRA_RIGHT";
  //static final public String EXTRA_TOP = "EXTRA_TOP";
  //static final public String EXTRA_BOTTOM = "EXTRA_BOTTOM";
  //static final public String EXTRA_WIDTH = "EXTRA_WIDTH";
  //static final public String EXTRA_POSITION = "EXTRA_POSITION";

  public static Position getPosition(Intent intent) {
    return intent.getParcelableExtra(EXTRA_LEFT);
  }

  static void startScaleActivity(Context context, Position position) {
    Intent intent = new Intent(context, PhotoViewActivity.class);
    intent.putExtra(EXTRA_LEFT, position);
    context.startActivity(intent);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    StatusbarUtils.from(this).setTransparentStatusbar(true).setLightStatusBar(false).process();
    setContentView(R.layout.activity_photoview);
    ButterKnife.bind(this);
    Picasso.with(this).load(R.drawable.place_holder)
        .into(mIvPhoto, new Callback() {
          @Override public void onSuccess() {
            Position position = getPosition(getIntent());
            Log.d(TAG, position.toString());

            ((ViewGroup) mIvPhoto.getParent()).setClipChildren(false);
            float del_scale =
                ((float) getWindowManager().getDefaultDisplay().getHeight()) / ((float) position.height);
            //float del_y = ((float) mIvPhoto.getTop()) / ((float) v.getBottom());
            Log.d(TAG, del_scale + "");
            Animation anim =
                new ScaleAnimation(1f, del_scale, // Start and end values for the X axis scaling
                    1f, del_scale, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f, // scale from mid of x
                    Animation.RELATIVE_TO_SELF,
                    ((float) position.height) / (1f / ((float) position.top)
                        + 1f / ((float) position.bottom))); // scale from mid of y

            //y/top = 1-y/bottom
            // y = 1 * top - y/bottom * top
            // (1 + top/bottom)y = top
            //y = top(1 + top/bottom)
            //y = 1(1/top + 1/bottom)*

            anim.setDuration(3000);
            anim.setFillAfter(true); // Needed to keep the result of the animation
            mIvPhoto.startAnimation(anim);
          }

          @Override public void onError() {

          }
        });
  }


}
