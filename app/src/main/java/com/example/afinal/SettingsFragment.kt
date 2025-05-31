package com.example.afinal

import android.os.Bundle
import android.util.TypedValue
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.view_settings, rootKey)

        val colorPreference = findPreference<ListPreference>("s_color")
        val scalePreference = findPreference<ListPreference>("scale_type")

        colorPreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
        scalePreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
    }

}