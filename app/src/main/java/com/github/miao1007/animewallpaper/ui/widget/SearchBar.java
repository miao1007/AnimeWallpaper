package com.github.miao1007.animewallpaper.ui.widget;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.miao1007.animewallpaper.R;

/**
 * Created by leon on 1/28/16.
 */
public class SearchBar extends RelativeLayout {

  static final String TAG = "SearchBar";
  private final boolean in = true;
  @Bind(R.id.internal_iv_search_icon) LinearLayout mInternalIvSearchIcon;
  @Bind(R.id.internal_rv_holder) RelativeLayout mInternalRvHolder;
  @Bind(R.id.internal_vs_cancel) ViewSwitcher mInternalVsCancel;

  @Bind(R.id.internal_iv_clear) ImageView mInternalIvClear;
  private float upDimen = 0f;
  private InputMethodManager imm;

  public EditText getEditTextSearch() {
    return mInternalEtSearch;
  }

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
    mInternalEtSearch.getText().clear();
  }

  public void setOnButton(final Runnable runnable) {
    mInternalVsCancel.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        mInternalVsCancel.showCancel(false, new Animator.AnimatorListener() {
          @Override public void onAnimationStart(Animator animation) {

          }

          @Override public void onAnimationEnd(Animator animation) {
            post(runnable);
          }

          @Override public void onAnimationCancel(Animator animation) {
          }

          @Override public void onAnimationRepeat(Animator animation) {

          }
        });
      }
    });
  }

  private void init() {
    //insert first view into the Framlayout,, and return `root view`(this)
    inflate(getContext(), R.layout.internal_searchbar, this);
    ButterKnife.bind(this);
    /**
     * -root
     * --RelevantLayout(RealSearch) (view[0])
     */
    mInternalEtSearch.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override public void afterTextChanged(final Editable s) {
        if (s.length() == 0) {
          mInternalIvClear.setVisibility(GONE);
        } else {
          mInternalIvClear.setVisibility(VISIBLE);
        }
      }
    });
    imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    mInternalEtSearch.setFocusable(true);
    mInternalEtSearch.requestFocus();
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (imm != null) {
      imm.hideSoftInputFromWindow(mInternalEtSearch.getWindowToken(), 0);
    }
  }


  public boolean isClosed() {
    return in;
  }
}
