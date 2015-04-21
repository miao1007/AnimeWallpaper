package com.github.miao1007.myapplication.ui.activity;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.github.miao1007.myapplication.R;
import com.github.miao1007.myapplication.ui.activity.base.BaseActivity;
import com.mikepenz.aboutlibraries.Libs;

public class SettingsActivity extends BaseActivity {

  @InjectView(R.id.include_settings_toolbar) Toolbar mToolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    ButterKnife.inject(this);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    PreferenceFragment fragment = new PreferenceFragment() {
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
                new Libs.Builder().withFields(R.string.class.getFields()).start(getActivity());
                return false;
              }
            });
      }
    };

    getFragmentManager().beginTransaction()
        .replace(R.id.include_settings_container, fragment)
        .commit();
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }
}
