package com.github.miao1007.animewallpaper.ui.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.github.miao1007.animewallpaper.R;
import com.github.miao1007.animewallpaper.ui.frag.base.PreferenceCompatFragment;
import com.github.miao1007.animewallpaper.ui.widget.NavigationBar;
import com.github.miao1007.animewallpaper.utils.StatusbarUtils;

/**
 * Created by leon on 2/6/16.
 */
public class SettingsActivity extends AppCompatActivity {

  //@Bind(R.id.settings_back) ImageView mSettingsBack;
  @Bind(R.id.settings_ngbar) NavigationBar mSettingsNgbar;

  //@OnClick(R.id.settings_back) void settings_back() {
  //  finish();
  //}

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    StatusbarUtils.from(this).setTransparentStatusbar(true).setLightStatusBar(true).process();
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    ButterKnife.bind(this);
    mSettingsNgbar.setTitle(getTitle());
    //getSupportFragmentManager()
    PreferenceCompatFragment fragment = new PreferenceCompatFragment() {
      @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {
        setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar);
        View view = super.onCreateView(inflater, container, savedInstanceState);
        //Set night-mode or other UI changes
        view.setBackgroundDrawable(
            new ColorDrawable(getContext().getResources().getColor(R.color.ios_internal_bg)));
        return view;
      }

      @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_general);
      }
    };
    getSupportFragmentManager().beginTransaction()
        .add(R.id.include_settings_container, fragment)
        .commit();
  }
}
