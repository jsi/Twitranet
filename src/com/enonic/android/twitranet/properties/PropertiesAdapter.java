package com.enonic.android.twitranet.properties;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.enonic.android.twitranet.R;

public class PropertiesAdapter extends BaseAdapter {

    public static final String CLASS = PropertiesAdapter.class.getSimpleName();
    public static final boolean DEBUG = true;

    private LayoutInflater mInflater;

    private Activity mActivity;

    private static int BUTTON_BASE_ID = 100;

    private int[] propertyTypes = new int[]{R.layout.properties_item, R.layout.properties_item, R.layout.properties_item, R.layout.properties_item};

    private String[] propertyNames = new String[]{"Server URL", "Message Count", "Refresh Rate (secs)", "Language"};

    private String[] propertyValues = new String[]{"", "20", "3000", "NO"};

    public PropertiesAdapter (Activity activity) {
        super();
        mActivity = activity;
        mInflater = LayoutInflater.from(activity);
    }

    public int getCount() {
        return propertyTypes.length;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object getItem(int position) {
        return propertyValues[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (DEBUG) {
            Log.d(CLASS, "start getView() for position: " + position);
        }
        View v = convertView;
        TextView propValue;
        if (v == null) {
            if (DEBUG) {
                Log.d(CLASS, "Convert View is NULL at position: " + position + "/" + getCount());
            }
            v = mInflater.inflate(propertyTypes[position], null);
            propValue = (TextView) v.findViewById(R.id.propertyValue);
            // First time around: Initialize the property name:
            TextView propName = (TextView) v.findViewById(R.id.propertyName);
            propName.setText(propertyNames[position]);

            ImageButton popupEditPropertyButton = (ImageButton) v.findViewById(R.id.popupButton);

            popupEditPropertyButton.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            Bundle propertyBundle = new Bundle();
                            propertyBundle.putString(Integer.toString(R.id.propertyName), propertyNames[position]);
                            propertyBundle.putString(Integer.toString(R.id.propertyValue), propertyValues[position]);
                            mActivity.showDialog(BUTTON_BASE_ID + position, propertyBundle);
                        }
                    }
            );
            v.setTag(propValue);
        } else {
            if (DEBUG) {
                Log.d(CLASS, "Convert View at position: " + position + "/" + getCount() + " set to id: " + v.getId());
            }
            propValue = (TextView) v.getTag();
        }

        Object data = getItem(position);

        if (data != null) {
            if (DEBUG) {
                Log.d(CLASS, "data is not null " + position + "/" + getCount());
            }
            propValue.setText(data.toString() + " ");
        }
        return v;
    }
}
