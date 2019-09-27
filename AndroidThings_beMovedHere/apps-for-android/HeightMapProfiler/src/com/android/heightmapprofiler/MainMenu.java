/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.heightmapprofiler;

import com.android.heightmapprofiler.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

/**
 * Main entry point for the HeightMapTest application.  
 */
public class MainMenu extends PreferenceActivity implements Preference.OnPreferenceClickListener {
    private static final int ACTIVITY_TEST = 0;
    private static final int RESULTS_DIALOG = 0;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        Preference runTestButton = getPreferenceManager().findPreference("runtest");
        if (runTestButton != null) {
        	runTestButton.setOnPreferenceClickListener(this);
        }
         
    }
    
  
    /** Creates the test results dialog and fills in a dummy message. */
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        if (id == RESULTS_DIALOG) {
            
            String dummy = "No results yet.";
            CharSequence sequence = dummy.subSequence(0, dummy.length() -1);
            dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.dialog_ok, null)
                .setMessage(sequence)
                .create();
        }
        return dialog;
    }
    
    /** 
     * Replaces the dummy message in the test results dialog with a string that
     * describes the actual test results.
     */
    protected void onPrepareDialog (int id, Dialog dialog) {
        if (id == RESULTS_DIALOG) {
            // Extract final timing information from the profiler.
            final ProfileRecorder profiler = ProfileRecorder.sSingleton;
            final long frameVerts = profiler.getAverageVerts();
            
            final long frameTime = 
                profiler.getAverageTime(ProfileRecorder.PROFILE_FRAME);
            final long frameMin = 
                profiler.getMinTime(ProfileRecorder.PROFILE_FRAME);
            final long frameMax = 
                profiler.getMaxTime(ProfileRecorder.PROFILE_FRAME);
            
            final long drawTime = 
                    profiler.getAverageTime(ProfileRecorder.PROFILE_DRAW);
            final long drawMin = 
                    profiler.getMinTime(ProfileRecorder.PROFILE_DRAW);
            final long drawMax = 
                    profiler.getMaxTime(ProfileRecorder.PROFILE_DRAW);

            final long simTime = 
                    profiler.getAverageTime(ProfileRecorder.PROFILE_SIM);
            final long simMin = 
                    profiler.getMinTime(ProfileRecorder.PROFILE_SIM);
            final long simMax = 
                    profiler.getMaxTime(ProfileRecorder.PROFILE_SIM);
            
            
            final float fps = frameTime > 0 ? 1000.0f / frameTime : 0.0f;
            
            String result = "Frame: " +  frameTime + "ms (" + fps + " fps)\n"
                + "\t\tMin: " + frameMin + "ms\t\tMax: " + frameMax + "\n" 
                + "Draw: " + drawTime + "ms\n"
                + "\t\tMin: " + drawMin + "ms\t\tMax: " + drawMax + "\n"
                + "Sim: " + simTime + "ms\n"
                + "\t\tMin: " + simMin + "ms\t\tMax: " + simMax + "\n"
                + "\nVerts per frame: ~" + frameVerts + "\n";
            CharSequence sequence = result.subSequence(0, result.length() -1);
            AlertDialog alertDialog = (AlertDialog)dialog;
            alertDialog.setMessage(sequence);
        }
    }
    
    /** Shows the results dialog when the test activity closes. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, 
            Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        showDialog(RESULTS_DIALOG);
             
    }


	public boolean onPreferenceClick(Preference preference) {
		Intent i = new Intent(this, HeightMapTest.class);
		startActivityForResult(i, ACTIVITY_TEST);
		return true;
	}
   
}