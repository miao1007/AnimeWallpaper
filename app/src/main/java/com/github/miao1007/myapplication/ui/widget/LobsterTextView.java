package com.github.miao1007.myapplication.ui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by leon on 1/28/16.
 */
public class LobsterTextView extends TextView {

  private static final String CUSTOM_FONT_PATH = "fonts/Lobster-Regular.ttf";
  Typeface lobster;

  public LobsterTextView(Context context) {
    this(context, null);
  }

  public LobsterTextView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LobsterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    lobster = Typeface.createFromAsset(context.getAssets(), CUSTOM_FONT_PATH);
    setTypeface(lobster);
  }
}
