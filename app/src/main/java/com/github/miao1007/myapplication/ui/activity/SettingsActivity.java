package com.github.miao1007.myapplication.ui.activity;

import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.ui.activity.base.BaseFragmentActivity;
import com.github.miao1007.myapplication.ui.frag.base.PreferenceCompatFragment;

public class SettingsActivity extends BaseFragmentActivity {
  @Override public Fragment getSupportFragment() {
    Fragment fragment = new PreferenceCompatFragment() {
      @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        //Set night-mode for background

        return view;
      }

      @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_general);
        findPreference("about").setOnPreferenceClickListener(
            new Preference.OnPreferenceClickListener() {
              @Override public boolean onPreferenceClick(Preference preference) {
                //new Libs.Builder().withActivityTitle("About")
                //    .withFields(R.string.class.getFields())
                //    .start(getActivity());

                return false;
              }
            });
      }
    };
    return fragment;
  }
}
