package com.enonic.android.twitranet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.enonic.android.twitranet.util.Base64Util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class EntryArrayAdapter
        extends ArrayAdapter<EntryDataHolder> {

    public static final String CLASS = EntryArrayAdapter.class.getSimpleName();
    public static final boolean DEBUG = true;

    private LayoutInflater mInflater;

    private int layoutResource;

    SimpleDateFormat tempInputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public EntryArrayAdapter(Context ctx, int textViewResId, List<EntryDataHolder> objects) {
        super(ctx, textViewResId, objects);
        if (objects == null || objects.size() == 0) {
            try {
                ((EntryListActivity)ctx).refreshDataFromServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mInflater = LayoutInflater.from(ctx);
        layoutResource = textViewResId;
    }

    private static class ViewHolder {
        ImageView userImage;

        TextView text;

        TextView date;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (DEBUG) {
            Log.d(CLASS, "start getView() for position: " + position);
        }
        final ViewHolder holder;
        View v = convertView;
        if (v == null) {
            if (DEBUG) {
                Log.d(CLASS, "Convert View is NULL at position: " + position + "/" + getCount());
            }
            v = mInflater.inflate(layoutResource, null);
            holder = new ViewHolder();
            holder.userImage = (ImageView) v.findViewById(R.id.userImage);
            holder.text = (TextView) v.findViewById(R.id.entryText);
            holder.date = (TextView) v.findViewById(R.id.dateText);
            v.setTag(holder);
        } else {
            if (DEBUG) {
                Log.d(CLASS, "Convert View at position: " + position + "/" + getCount() + " set to id: " + v.getId());
            }
            holder = (ViewHolder) v.getTag();
        }

        EntryDataHolder data = getItem(position);

        if (data != null) {
            if (DEBUG) {
                Log.d(CLASS, "data is not null " + position + "/" + getCount());
            }
            //loading first line
            holder.date.setText(data.getEntryDate());
            holder.date.setVisibility(View.VISIBLE);

            holder.text.setText(data.getEntryText());
            holder.text.setVisibility(View.VISIBLE);
            //loading icon
            Bitmap userimage;
            try {
                userimage = loadImageFromServer(data.getImageUrl());
                holder.userImage.setImageBitmap(userimage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return v;
    }

    public Bitmap loadImageFromServer(String url) throws IOException {
        URL imageURL = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) imageURL.openConnection();
        connection.setRequestMethod("POST");

        String userpassword = ((EntryListActivity)getContext()).getUsername() + ":" + ((EntryListActivity)getContext()).getPassword();
        String encodedAuthorization = Base64Util.encode(userpassword.getBytes());
        connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
        connection.connect();
        InputStream xmlResponse = connection.getInputStream();
        BufferedInputStream buffer = new BufferedInputStream(xmlResponse, 8096);
        return BitmapFactory.decodeStream(buffer);
    }

    public void addMessage(String text, String owner, String date, String photoUrl) throws ParseException {
        add(new EntryDataHolder(text, owner, tempInputFormat.parse(date), photoUrl));
    }

    public void deleteAll() {
        for (int count = getCount() - 1; count >= 0; count--) {
            remove(getItem(count));
        }
    }
}


