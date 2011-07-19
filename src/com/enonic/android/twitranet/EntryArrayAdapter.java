package com.enonic.android.twitranet;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.enonic.android.twitranet.util.Base64Util;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

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

    private String serverUrl = "http://intra.enonic.com/";
//    private String serverUrl = "http://vtnode1:8080/cms-commando-unstable-enonic/site/41/";

    private Integer messageCount = 20;

    private Integer refreshRate = 15;

    private String language = "no";

    private String username;

    private String password;

    public EntryArrayAdapter(Context ctx, int textViewResId, List<EntryDataHolder> objects) {
        super(ctx, textViewResId, objects);
        if (objects == null || objects.size() == 0) {
            try {
                refreshDataFromServer();
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

        String userpassword = getUsername() + ":" + getPassword();
        String encodedAuthorization = Base64Util.encode(userpassword.getBytes());
        connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
        connection.connect();
        InputStream xmlResponse = connection.getInputStream();
        BufferedInputStream buffer = new BufferedInputStream(xmlResponse, 8096);
        return BitmapFactory.decodeStream(buffer);
    }

    public void refreshDataFromServer() throws ParseException, JDOMException, IOException {
        deleteAll();

        if (getUsername() != null && getUsername().length() > 0 && getPassword() != null && getPassword().length() > 0) {
            URL twitranettMessagesURL = new URL(serverUrl + "twitranettmessages?count=" + messageCount);
            HttpURLConnection connection = (HttpURLConnection) twitranettMessagesURL.openConnection();
            connection.setRequestMethod("POST");

            String userpassword = getUsername() + ":" + getPassword();
            String encodedAuthorization = Base64Util.encode(userpassword.getBytes());
            connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
            connection.connect();
            InputStream xmlResponse = connection.getInputStream();

            SAXBuilder xmlDocumentBuilder = new SAXBuilder(false);
            Document twitranettXML = xmlDocumentBuilder.build(xmlResponse);
            List<Element> tweets = twitranettXML.getRootElement().getChild("twitrs").getChildren("twitr");
            for (Element tweet : tweets) {
                String tOwner = tweet.getAttributeValue("owner");
                String tText = tweet.getChild("message").getText();
                String tDate = tweet.getAttributeValue("created");
                String tPhotoUrl = tweet.getChild("photo-url").getText();
//                String tPhotoUrl = tweet.getChild("large-photo-url").getText();
                addMessage(tText, tOwner, tDate, tPhotoUrl);
            }
        }

    }

    public void addMessage(String text, String owner, String date, String photoUrl) throws ParseException {
        add(new EntryDataHolder(text, owner, tempInputFormat.parse(date), photoUrl));
    }

    public void deleteAll() {
        for (int count = getCount() - 1; count >= 0; count--) {
            remove(getItem(count));
        }
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }

    public void setRefreshRate(Integer refreshRate) {
        this.refreshRate = refreshRate;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUsername () {
        if (username == null) {
            if (getRememberMe()) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                String usernameKey = getContext().getResources().getText(R.string.key_username).toString();
                String spUsername = sharedPreferences.getString(usernameKey, null);
                username = spUsername;
            }
        }
        return username;
    }

    public void setUsername (String uid) {
        username = uid;
        if (getRememberMe()) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String usernameKey = getContext().getResources().getText(R.string.key_username).toString();
            String storedUsername = sharedPreferences.getString(usernameKey, null);
            if (storedUsername == null || !storedUsername.equals(uid)) {
                SharedPreferences.Editor spEditor = sharedPreferences.edit();
                spEditor.putString(usernameKey, uid);
                spEditor.commit();
            }
        }
    }

    public String getPassword () {
        if (password == null) {
            if (getRememberMe()) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                String passwordKey = getContext().getResources().getText(R.string.key_password).toString();
                String spPassword = sharedPreferences.getString(passwordKey, null);
                password = spPassword;
            }
        }
        return password;
    }

    public void setPassword (String pw) {
        password = pw;
        if (getRememberMe()) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String passwordKey = getContext().getResources().getText(R.string.key_password).toString();
            String storedPassword = sharedPreferences.getString(passwordKey, null);
            if (storedPassword == null || !storedPassword.equals(pw)) {
                SharedPreferences.Editor spEditor = sharedPreferences.edit();
                spEditor.putString(passwordKey, pw);
                spEditor.commit();
            }
        }
    }

    public Boolean getRememberMe () {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String rememberMeKey = getContext().getResources().getText(R.string.key_rememberMe).toString();
        return sharedPreferences.getBoolean(rememberMeKey, false);
    }

    public void setRememberMe (Boolean rememberMe) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String rememberMeKey = getContext().getResources().getText(R.string.key_rememberMe).toString();
        Boolean storedRememberMe = sharedPreferences.getBoolean(rememberMeKey, !rememberMe);
        if (storedRememberMe != rememberMe) {
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.putBoolean(rememberMeKey, rememberMe);
            spEditor.commit();
        }
    }

}
