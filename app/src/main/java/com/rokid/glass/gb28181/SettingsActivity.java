package com.rokid.glass.gb28181;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;

public class SettingsActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SettingsFragment settingsFragment;




    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);


        // Display the fragment as the main content.
        settingsFragment = new SettingsFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, settingsFragment)
                .commit();


    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences =
                settingsFragment.getPreferenceScreen().getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
//        updateSummary(sharedPreferences,keyprefSipLocalPort);
//        updateSummary(sharedPreferences,keyprefSipTransportProtocol);
//        updateSummary(sharedPreferences,keyprefSipServerId);
//        updateSummary(sharedPreferences,keyprefSipServerPort);
//        updateSummary(sharedPreferences,keyprefSipServerAddress);
//        updateSummary(sharedPreferences,keyprefSipServerDomain);
//        updateSummary(sharedPreferences,keyprefSipUserid);
//        updateSummary(sharedPreferences,keyprefSipPassword);
//        updateSummary(sharedPreferences,keyprefSipHeartbeat);
//        updateSummary(sharedPreferences,keyprefSipValidofRegister);
//        updateSummary(sharedPreferences,keyprefSipMaxHeartbeatTimeoutCount);


    }

    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences sharedPreferences =
                settingsFragment.getPreferenceScreen().getSharedPreferences();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
    private void  updateSummary(SharedPreferences sharedPreferences, String key) {
        Preference updatedPref = settingsFragment.findPreference(key);
        // Set summary to be the user-description for the selected value
        updatedPref.setSummary(sharedPreferences.getString(key, ""));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        if(key.equals(keyprefSipLocalPort)
//                || key.equals(keyprefSipTransportProtocol)
//                || key.equals(keyprefSipServerAddress)
//                || key.equals(keyprefSipServerDomain)
//                || key.equals(keyprefSipServerId)
//                || key.equals(keyprefSipServerPort)
//                || key.equals(keyprefSipUserid)
//                || key.equals(keyprefSipPassword)
//                || key.equals(keyprefSipHeartbeat)
//                || key.equals(keyprefSipMaxHeartbeatTimeoutCount)
//                || key.equals(keyprefSipValidofRegister)
//            ){
            //updateSummary(sharedPreferences,key);
//        }
    }
}
