package com.github.miao1007.myapplication;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class DetailedActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private ImageView mImageview;
    public static final String EXTRA_IMAGE = "URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.inflateMenu(R.menu.menu_detailed);
            // Set Navigation Toggle
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } else {
            //throw new NullPointerException("Toolbar must be <include> in activity's layout!");
        }
        mImageview = (ImageView) findViewById(R.id.imageView_detail);

        //Picasso target get bitmap
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                //You can get 400x200 showcase pictures at http://lorempixel.com/
                mImageview.setImageBitmap(bitmap);
                Palette palette = Palette.generate(bitmap);
                mToolbar.setBackgroundColor(palette.getDarkMutedColor(Color.RED));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                mImageview.setImageResource(R.drawable.lorempixel);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                mImageview.setImageResource(R.drawable.lorempixel);
            }
        };
        Picasso.with(this)
                .load(getIntent().getStringExtra(EXTRA_IMAGE))
                .into(target);

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
