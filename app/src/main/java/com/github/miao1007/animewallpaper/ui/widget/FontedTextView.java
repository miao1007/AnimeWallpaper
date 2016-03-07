package com.github.miao1007.animewallpaper.ui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by leon on 1/28/16.
 */
public class FontedTextView extends TextView {

  private static final String CUSTOM_FONT_PATH = "fonts/System San Francisco Display Regular.ttf";
  private final Typeface lobster;

  public FontedTextView(Context context) {
    this(context, null);
  }

  public FontedTextView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public FontedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    lobster = Typeface.createFromAsset(context.getAssets(), CUSTOM_FONT_PATH);
    setTypeface(lobster);
  }
}
