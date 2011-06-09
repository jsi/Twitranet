package com.enonic.android.twitranet.properties;

import android.app.Dialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.EditText;
import com.enonic.android.twitranet.R;

public class PropertiesActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new PropertiesAdapter(this));

    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        PropertiesDialog dialog = new PropertiesDialog(this);
        dialog.setTitle(args.getString(Integer.toString(R.id.propertyName)));
        dialog.setContentView(R.layout.property_dialog);
        EditText value = (EditText)dialog.findViewById(R.id.propertyValue);
        value.setText(args.getString(Integer.toString(R.id.propertyValue)));
        return dialog;
    }
}
