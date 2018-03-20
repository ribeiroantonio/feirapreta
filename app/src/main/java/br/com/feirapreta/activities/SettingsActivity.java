package br.com.feirapreta.activities;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.os.Bundle;

import br.com.feirapreta.R;

public class SettingsActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class Preferences extends android.preference.PreferenceFragment {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.pref_network);
        }
    }
}
