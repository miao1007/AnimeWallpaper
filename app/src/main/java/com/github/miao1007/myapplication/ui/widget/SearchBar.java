package com.github.miao1007.myapplication.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.utils.StatusbarUtils;

/**
 * Created by leon on 1/28/16.
 */
public class SearchBar extends RelativeLayout implements View.OnClickListener {

  static final String TAG = "SearchBar";
  boolean in = true;
  @Bind(R.id.internal_iv_search_icon) LinearLayout mInternalIvSearchIcon;
  @Bind(R.id.internal_rv_holder) RelativeLayout mInternalRvHolder;
  @Bind(R.id.internal_vs_cancel) ViewSwitcher mInternalVsCancel;

  Animator left_X;
  Animator up_Y;
  //let top
  int[] location = new int[2];
  @Bind(R.id.internal_vs_text) android.widget.ViewSwitcher mInternalVsText;
  @Bind(R.id.internal_iv_clear) ImageView mInternalIvClear;

  public SearchBar(Context context) {
    this(context, null);
  }

  public SearchBar(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @OnClick(R.id.internal_iv_clear) void clearText() {
    if (mInternalVsText.getChildAt(1) != null && mInternalVsText.getChildAt(
        1) instanceof EditText) {
      ((EditText) mInternalVsText.getChildAt(1)).getEditableText().clear();
    }
  }

  @Override public void onClick(View v) {
    toggle();
  }

  private void toggle() {
    getLocationOnScreen(location);
    Log.d(TAG, location[0] + "/" + location[1]);
    float[] ax = {
        0, -mInternalIvSearchIcon.getLeft() + 2 * getContext().getResources()
        .getDimension(R.dimen.internal_searchbar_radius)
    };
    float[] ay = { 0, -162 + StatusbarUtils.getStatusBarHeightPx(getContext()) };
    left_X = ObjectAnimator.ofFloat(mInternalIvSearchIcon, View.TRANSLATION_X, in ? ax[0] : ax[1],
        in ? ax[1] : ax[0]);
    // TODO: 1/29/16 add bottom view
    up_Y = ObjectAnimator.ofFloat((((ViewGroup) getRootView()).getChildAt(0)), View.TRANSLATION_Y,
        in ? ay[0] : ay[1], in ? ay[1] : ay[0]);
    AnimatorSet s = new AnimatorSet();
    s.playTogether(left_X, up_Y);
    s.start();

    final EditText et = ((EditText) mInternalVsText.getChildAt(1));
    et.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override public void afterTextChanged(Editable s) {
        if (s.length() == 0) {
          mInternalIvClear.setVisibility(GONE);
        } else {
          mInternalIvClear.setVisibility(VISIBLE);
        }
      }
    });
    InputMethodManager imm =
        (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    if (in) {
      mInternalVsText.showNext();
      et.setFocusable(true);
      et.requestFocus();
      //mInternalVsText.getChildAt(1).requestFocusFromTouch();
      //mInternalVsText.getNextView()
    } else {
      et.clearFocus();
      mInternalVsText.showNext();
    }
    //handle imm
    if (et.hasFocus()) {
      imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    } else {
      imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }
    mInternalVsCancel.toggle(new ViewSwitcher.ToggleCallbackListener() {
      @Override public void toggle() {
        ((EditText) mInternalVsText.getChildAt(1)).setText("");
      }
    });
    in = !in;
  }

  void init() {
    View.inflate(getContext(), R.layout.include_searchbar, this);
    ButterKnife.bind(this);
    this.setOnClickListener(this);
  }
}
