package com.github.miao1007.myapplication.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.utils.StatusbarUtils;
import com.github.miao1007.myapplication.utils.animation.AnimateUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by leon on 1/19/16.
 */
public class PhotoViewActivity extends AppCompatActivity {

  static final public String EXTRA_LEFT = "EXTRA_LEFT";
  @InjectView(R.id.iv_photo) ImageView mIvPhoto;
  //static final public String EXTRA_RIGHT = "EXTRA_RIGHT";
  //static final public String EXTRA_TOP = "EXTRA_TOP";
  //static final public String EXTRA_BOTTOM = "EXTRA_BOTTOM";
  //static final public String EXTRA_WIDTH = "EXTRA_WIDTH";
  //static final public String EXTRA_HEIGHT = "EXTRA_HEIGHT";

  public static Position getPosition(Intent intent) {
    return intent.getParcelableExtra(EXTRA_LEFT);
  }

  static void startActivity(Context context, Position position) {
    Intent intent = new Intent(context, PhotoViewActivity.class);
    intent.putExtra(EXTRA_LEFT, position);
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    StatusbarUtils.setTranslucent(this);
    setContentView(R.layout.activity_photoview);
    ButterKnife.inject(this);
    Picasso.with(this).load(R.drawable.place_holder)
        .into(mIvPhoto, new Callback() {
          @Override public void onSuccess() {
            Position position = getPosition(getIntent());
            float scaleX =
                ((float) getWindowManager().getDefaultDisplay().getHeight()) / ((float) position.heigth);
            //for (View view = v.getParent(); v)
            ((ViewGroup) mIvPhoto.getParent()).setClipChildren(false);
            float del_scale =
                ((float) getWindowManager().getDefaultDisplay().getHeight()) / ((float) position.heigth);
            //float del_y = ((float) mIvPhoto.getTop()) / ((float) v.getBottom());

            Animation anim =
                new ScaleAnimation(1f, del_scale, // Start and end values for the X axis scaling
                    1f, del_scale, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f, // scale from mid of x
                    Animation.RELATIVE_TO_SELF, del_scale); // scale from mid of y
            anim.setDuration(AnimateUtils.ANIM_DORITION);
            anim.setFillAfter(true); // Needed to keep the result of the animation
            mIvPhoto.startAnimation(anim);
          }

          @Override public void onError() {

          }
        });
  }

  static class Position implements Parcelable {
    public static final Creator<Position> CREATOR = new Creator<Position>() {
      public Position createFromParcel(Parcel source) {
        return new Position(source);
      }

      public Position[] newArray(int size) {
        return new Position[size];
      }
    };
    int left;
    int right;
    int top;
    int bottom;
    int width;
    int heigth;

    public Position() {
    }

    public Position(int left, int right, int top, int bottom, int width, int heigth) {
      this.left = left;
      this.right = right;
      this.top = top;
      this.bottom = bottom;
      this.width = width;
      this.heigth = heigth;
    }

    protected Position(Parcel in) {
      this.left = in.readInt();
      this.right = in.readInt();
      this.top = in.readInt();
      this.bottom = in.readInt();
      this.width = in.readInt();
      this.heigth = in.readInt();
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(this.left);
      dest.writeInt(this.right);
      dest.writeInt(this.top);
      dest.writeInt(this.bottom);
      dest.writeInt(this.width);
      dest.writeInt(this.heigth);
    }
  }
}
