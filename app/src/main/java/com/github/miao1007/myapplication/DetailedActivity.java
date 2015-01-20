package com.github.miao1007.myapplication;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.miao1007.myapplication.utils.BlurTransformation;
import com.github.miao1007.myapplication.utils.CircleTransformation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.util.List;

public class DetailedActivity extends ActionBarActivity {

  @InjectView(R.id.toolbar) Toolbar mToolbar;

  @InjectView(R.id.imageView_detail) ImageView mImageview;

  @InjectView(R.id.detail_imageView_blur) ImageView mImageview_blur;

  @InjectView(R.id.detail_imageView_circle) ImageView mImageview_circle;

  @InjectView(R.id.linelayout) LinearLayout mLinearLayout;

  public static final String EXTRA_IMAGE = "URL";

  public String url;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detailed);
    ButterKnife.inject(this);
    if (mToolbar != null) {
      setSupportActionBar(mToolbar);
      mToolbar.inflateMenu(R.menu.menu_detailed);
      // Set Navigation Toggle
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowTitleEnabled(false);
    } else {
      //throw new NullPointerException("Toolbar must be <include> in activity's layout!");
    }

    url = getIntent().getStringExtra(EXTRA_IMAGE);

    //use Picasso target to get bitmap
    final int defalutColor = getResources().getColor(R.color.accent_material_light);
    Target target = new Target() {
      @Override
      public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        mImageview.setImageBitmap(bitmap);
        Palette palette = Palette.generate(bitmap);

        mToolbar.setBackgroundColor(palette.getMutedColor(defalutColor));

        List<Palette.Swatch> swatches = palette.getSwatches();

                /*
                * personally i'd better to use:
                * MutedColor
                * VibrantColor
                * */

        //we will have 14 swatches
        for (int i = 0; i < swatches.size(); i++) {
          System.out.println("swatches = " + swatches.get(i).getRgb());
          View view = new View(DetailedActivity.this);
          view.setBackgroundColor(swatches.get(i).getRgb());
          view.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
          mLinearLayout.addView(view);
        }
      }

      @Override
      public void onBitmapFailed(Drawable errorDrawable) {
        //You can get 400x200 showcase pictures at http://lorempixel.com/
        mImageview.setImageResource(R.drawable.lorempixel);
      }

      @Override
      public void onPrepareLoad(Drawable placeHolderDrawable) {
        mImageview.setImageResource(R.drawable.lorempixel);
      }
    };

    Picasso.with(this).load(url).into(target);

    Picasso.with(this).load(url).transform(new CircleTransformation()).into(mImageview_circle);

    Picasso.with(this).load(url).transform(new BlurTransformation(this)).into(mImageview_blur);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_detailed, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    } else if (id == android.R.id.home) {
      finish();
    }

    return super.onOptionsItemSelected(item);
  }
}
