package com.hand.hrmexp.activity;

import com.hand.R;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 PreferenceCategory fakeHeader = new PreferenceCategory(this);
		addPreferencesFromResource(R.xml.pref_general);
		bindPreferenceSummaryToValue(findPreference("sys_basic_url"));
		
	}
	
	private void bindPreferenceSummaryToValue(Preference preference) {

		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager
		        .getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
	}
	
	private Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if (preference instanceof ListPreference) {

			}

			else {

				if (preference.getKey().equals("sys_basic_url")) {
					String originalValue = PreferenceManager.getDefaultSharedPreferences(preference.getContext())
					        .getString(preference.getKey(), "");


				}
				// For all other preferences, set the summary to the value's
				// simple string representation.
				preference.setSummary(stringValue);
			}
			return true;
		}
	};
	
}
