/*
 * Copyright 2000-2011 Enonic AS
 * http://www.enonic.com/license
 */
package com.enonic.android.twitranet.preferences;


import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.enonic.android.twitranet.R;

public class PreferencesListActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
