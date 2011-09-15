package com.enonic.android.twitranet;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.enonic.android.twitranet.preferences.PreferencesListActivity;
import com.enonic.android.twitranet.util.Base64Util;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.enonic.android.twitranet.login.LoginActivity;
import org.jdom.input.SAXBuilder;

public class EntryListActivity
    extends ListActivity
{
    private static final int ACTIVITY_LOGIN = 0;

    public final String CLASS = EntryListActivity.class.getSimpleName();

    public static final boolean DEBUG = true;

    private ArrayList<EntryDataHolder> m_options = null;

    private EntryArrayAdapter m_adapter;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.twitranett_layout );

        m_options = new ArrayList<EntryDataHolder>();
        if ( DEBUG )
        {
            Log.d( CLASS, "calling CreateEntryAdapter()" );
        }
        this.m_adapter = new EntryArrayAdapter( this, R.layout.list_item, m_options );
        ListView listView = getListView();
        listView.setAdapter(this.m_adapter);
        listView.setSmoothScrollbarEnabled(true);

        Intent i = new Intent( this, LoginActivity.class );
        startActivityForResult( i, ACTIVITY_LOGIN );
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent intent )
    {
        if (requestCode == ACTIVITY_LOGIN)
        {
            super.onActivityResult( requestCode, resultCode, intent );

            refreshView();
        }
    }

    private void refreshView() {
        boolean loginFailed = true;
        try
        {
            refreshDataFromServer();
            this.m_adapter.notifyDataSetChanged();
            loginFailed = false;
        }
        catch ( ParseException e )
        {
            e.printStackTrace();
        }
        catch ( IOException e )
        {
            String message = e.getMessage();
            String toastMessage;
            if (message.contains( "authentication" )) {
                toastMessage = "Login error!";
            } else {
                toastMessage = "Communication error! - " + e.getMessage();
            }
            Toast.makeText(getListView().getContext(), toastMessage, Toast.LENGTH_LONG).show();
        }
        catch ( JDOMException e )
        {
            e.printStackTrace();
        }

        if (loginFailed) {
            Intent i = new Intent( this, LoginActivity.class );
            startActivityForResult( i, ACTIVITY_LOGIN );
        }
    }

    public void refreshDataFromServer() throws ParseException, JDOMException, IOException {

        if (getUsername() != null && getUsername().length() > 0 && getPassword() != null && getPassword().length() > 0) {
            URL twitranettMessagesURL = new URL(getServerUrl() + "twitranettmessages?count=" + getMessageCount());
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
            m_adapter.deleteAll();
            for (Element tweet : tweets) {
                String tOwner = tweet.getAttributeValue("owner");
                String tText = tweet.getChild("message").getText();
                String tDate = tweet.getAttributeValue("created");
                String tPhotoUrl = tweet.getChild("photo-url").getText();
//                String tPhotoUrl = tweet.getChild("large-photo-url").getText();
                m_adapter.addMessage(tText, tOwner, tDate, tPhotoUrl);
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout_menu:

                Intent i = new Intent( this, LoginActivity.class );
                startActivityForResult( i, ACTIVITY_LOGIN );
                return true;

            case R.id.preferences_menu:

                startActivity(new Intent( this, PreferencesListActivity.class ));
                return super.onOptionsItemSelected(item);

            default:
                
                return super.onOptionsItemSelected(item);
        }
    }

    public String getUsername () {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String usernameKey = getResources().getText(R.string.key_username).toString();
        return sharedPreferences.getString(usernameKey, null);
    }

    public String getPassword () {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String passwordKey = getResources().getText(R.string.key_password).toString();
        return sharedPreferences.getString(passwordKey, null);
    }

    public Integer getMessageCount () {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String messageCountPreference = prefs.getString("message_count", "0");
        try {
            return Integer.parseInt(messageCountPreference);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getServerUrl() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString("server_url", "0");
    }
}
