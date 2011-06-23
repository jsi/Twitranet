package com.enonic.android.twitranet.properties;

import android.app.Dialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.enonic.android.twitranet.R;

public class PropertiesActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new PropertiesAdapter(this));

    }

    @Override
    protected Dialog onCreateDialog(final int id, final Bundle args) {
        final int position = id - PropertiesAdapter.BUTTON_BASE_ID;
        PropertiesDialog dialog = new PropertiesDialog(this);
        dialog.setTitle(args.getString(Integer.toString(R.id.propertyName)));
        dialog.setContentView(args.getInt(Integer.toString(R.string.property_data_type)));
        final EditText editValueContainer = (EditText)dialog.findViewById(R.id.propertyValue);
        editValueContainer.setText(args.getString(Integer.toString(R.id.propertyValue)));

        Button setButton = (Button)dialog.findViewById(R.id.setButton);
        setButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        String value = editValueContainer.getText().toString();
                        PropertiesAdapter dataAdapter = (PropertiesAdapter)getListAdapter();
                        dataAdapter.setPropertyValue(position, value);
                        dismissDialog(id);
                    }
                }
        );

        Button cancelButton = (Button)dialog.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        dismissDialog(id);
                    }
                }
        );
        return dialog;
    }
}
