package com.github.miao1007.animewallpaper.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.miao1007.animewallpaper.R;

/**
 * Created by leon on 1/28/16.
 */
public class SearchBar extends LinearLayout implements View.OnClickListener {

  static final String TAG = "SearchBar";
  boolean in = true;
  @Bind(R.id.internal_iv_search_icon) LinearLayout mInternalIvSearchIcon;
  @Bind(R.id.internal_rv_holder) RelativeLayout mInternalRvHolder;
  @Bind(R.id.internal_vs_cancel) ViewSwitcher mInternalVsCancel;

  Animator left_X;
  Animator up_Y;
  RelativeLayout realSearchBar;
  PopupWindow popupWindow;
  //let top
  int[] location = new int[2];
  @Bind(R.id.internal_iv_clear) ImageView mInternalIvClear;
  @Bind(R.id.internal_search) RelativeLayout mInternalSearch;
  float upDimen;
  @Bind(R.id.internal_et_search) EditText mInternalEtSearch;

  public SearchBar(Context context) {
    this(context, null);
  }

  public SearchBar(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray ta =
        attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.SearchBar);
    if (ta != null) {
      upDimen = ta.getDimension(R.styleable.SearchBar_sb_float_height, upDimen);
      ta.recycle();
    }
    init();
  }

  @OnClick(R.id.internal_iv_clear) void clearText() {
    if (mInternalEtSearch != null) {
      mInternalEtSearch.getEditableText().clear();
    }
  }

  @Override public void onClick(View v) {
    toggle();
  }

  public void toggle() {
    anime();
    handleIME(in);
  }

  private void anime() {
    getLocationOnScreen(location);
    Log.d(TAG, location[0] + "/" + location[1]);
    float[] ax = {
        0, 0
    };
    float[] ay = { 0, -upDimen };
    left_X = ObjectAnimator.ofFloat(mInternalIvSearchIcon, View.TRANSLATION_X, in ? ax[0] : ax[1],
        in ? ax[1] : ax[0]);
    // `setContentView`
    final ViewGroup root =
        ((ViewGroup) ((ViewGroup) getRootView().findViewById(android.R.id.content)).getChildAt(0));
    up_Y = ObjectAnimator.ofFloat(root, View.TRANSLATION_Y, in ? ay[0] : ay[1], in ? ay[1] : ay[0]);
    AnimatorSet s = new AnimatorSet();
    s.setInterpolator(new LinearInterpolator());
    s.playTogether(left_X, up_Y);
    s.addListener(new Animator.AnimatorListener() {
      @Override public void onAnimationStart(Animator animation) {
        Log.d(TAG, "onAnimationStart: ");
        //if (in) {
        //  root.getLayoutParams().height = root.getHeight() + (in ? (1) : (-1)) * getHeight();
        //}
      }

      @Override public void onAnimationEnd(Animator animation) {
        Log.d(TAG, "onAnimationEnd: ");
        addList(in);
        //if (!in) {
        //  root.getLayoutParams().height = root.getHeight() + (in ? (1) : (-1)) * getHeight();
        //}
        in = !in;
      }

      @Override public void onAnimationCancel(Animator animation) {
        Log.d(TAG, "onAnimationCancel: ");
      }

      @Override public void onAnimationRepeat(Animator animation) {
        Log.d(TAG, "onAnimationRepeat: ");
      }
    });
    s.start();
  }

  private void addList(boolean isIn) {
    getLayoutParams().height = getHeight() + (isIn ? (1) : (-1)) * getBottom();

    ListView listView =
        ((ListView) inflate(getContext(), R.layout.internal_search_result_list, null));
    String[] data = new String[] { "aaaa", "bbbb", "cccc", "ddd", "eee", "fff" };
    listView.setAdapter(
        new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, data));
    if (isIn) {
      addView(listView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    } else {
      removeViewAt(getChildCount() - 1);
    }
  }

  private void handleIME(boolean isIn) {
    mInternalEtSearch.addTextChangedListener(new TextWatcher() {
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
    if (isIn) {

      mInternalEtSearch.setFocusable(true);
      mInternalEtSearch.requestFocus();
      //mInternalVsText.getChildAt(1).requestFocusFromTouch();
      //mInternalVsText.getNextView()
    } else {

      mInternalEtSearch.clearFocus();
    }
    //handle imm
    if (isIn) {
      imm.showSoftInput(mInternalEtSearch, InputMethodManager.SHOW_IMPLICIT);
    } else {
      imm.hideSoftInputFromWindow(mInternalEtSearch.getWindowToken(), 0);
    }
    mInternalVsCancel.toggle(new ViewSwitcher.ToggleCallbackListener() {
      @Override public void toggle() {
        clearText();
      }
    });
  }

  void init() {
    //set attr
    setAttr();
    //insert first view into the Framlayout,, and return `root view`(this)
    inflate(getContext(), R.layout.include_searchbar, this);
    ButterKnife.bind(this);
    /**
     * -root
     * --RelevantLayout(RealSearch) (view[0])
     */
    popupWindow = new PopupWindow(inflate(getContext(), R.layout.internal_search_result_list, null),
        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);
    popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
    //popupWindow.setSoftInputMode();
  }

  private void setAttr() {
    setOnClickListener(this);
    setOrientation(VERTICAL);
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (popupWindow != null && popupWindow.isShowing()) {
      popupWindow.dismiss();
    }
  }

  public boolean isClosed() {
    return in;
  }
}
