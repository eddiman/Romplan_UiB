package com.pensive.android.romplanuib;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.WeekViewEvent;
import com.pensive.android.romplanuib.util.FontController;

public class SettingsActivity extends AppCompatActivity {
    FontController fc = new FontController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getDelegate().installViewFactory();
        //getDelegate().onCreate(savedInstanceState);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


    }

    ///////////////////////////////////////////INNER FRAGMENT/////////////////////////////////////////////////////////////////
    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            String version = BuildConfig.VERSION_NAME;
            Preference prefVersion = findPreference("version");
            prefVersion.setSummary(version);
            getActivity().setTitle("");


            Preference preference = findPreference("third_party");
            preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    createLibDialog();
                    return false;
                }
            });

            Preference backPref = findPreference("key_back");
            backPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    getActivity().onBackPressed();
                    return false;
                }
            });
        }

        private void createLibDialog() {

            final AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.DialogTheme)
                    .setView(R.layout.dialog_third_party_libs)
                    .create();
            dialog.setCancelable(true);
            dialog.show();

        }
    }
}
