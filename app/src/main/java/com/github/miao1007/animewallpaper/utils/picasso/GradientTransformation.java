package com.github.miao1007.animewallpaper.utils.picasso;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import com.squareup.picasso.Transformation;

/**
 * Created by leon on 2/2/15.
 */
class GradientTransformation implements Transformation {

  //black
  private final int startColor = Color.argb(240,0,0,0);
  private final int endColor = Color.TRANSPARENT;

  @Override public Bitmap transform(Bitmap source) {

    int x = source.getWidth();
    int y = source.getHeight();

    Bitmap grandientBitmap = source.copy(source.getConfig(), true);
    Canvas canvas = new Canvas(grandientBitmap);
    //left-top == (0,0) , right-bottom == (x,y);
    LinearGradient grad =
        new LinearGradient(x/2, y, x/2, y/2, startColor, endColor, Shader.TileMode.CLAMP);
    Paint p = new Paint(Paint.DITHER_FLAG);
    p.setShader(null);
    p.setDither(true);
    p.setFilterBitmap(true);
    p.setShader(grad);
    canvas.drawPaint(p);
    source.recycle();
    return grandientBitmap;
  }

  @Override public String key() {
    return "Gradient";
  }
}
