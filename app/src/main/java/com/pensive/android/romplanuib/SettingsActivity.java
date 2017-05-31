package com.pensive.android.romplanuib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.pensive.android.romplanuib.util.FontController;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

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

            Preference clearCachePreference = findPreference("clear_cache");
            clearCachePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    //TODO: Implement dialog for user verification and prevent user error. For developing purposes, easier to not have, for now
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
                    sharedPreferences.edit().clear().apply();

                    Intent intent = new Intent(getActivity().getBaseContext(), SelectUniCampusActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().getBaseContext().startActivity(intent);

                    return false;
                }
            });

            Preference thirdPartyPreference = findPreference("third_party");
            thirdPartyPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
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

            final AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.DialogBlueTheme)
                    .setView(R.layout.dialog_third_party_libs)
                    .create();
            dialog.setCancelable(true);
            dialog.show();

        }
    }


}
