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

//    private Runnable entryListActivity;

    private String serverUrl = "http://intra.enonic.com/";
//    private String serverUrl = "http://vtnode1:8080/cms-commando-unstable-enonic/site/41/";

    private Integer messageCount = 20;

    private String username;

    private String password;



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

//        if ( DEBUG )
//        {
//            Log.d( CLASS, "initiate new viewTest = new Runable" );
//        }
//        entryListActivity = new Runnable()
//        {
//            public void run()
//            {
//                while ( true )
//                {
//                    try
//                    {
//                        Thread.sleep( 180000 );
//                    }
//                    catch ( InterruptedException e )
//                    {
//                        // Do nothing
//                    }
//                    m_adapter.notifyDataSetChanged();
//                    if (DEBUG) {
//                        Log.d(CLASS, "Data changed notification sent.");
//                    }
//                }
//            }
//        };
//        if ( DEBUG )
//        {
//            Log.d( CLASS, "create new thread with viewTestRunable" );
//        }
//        Thread thread = new Thread( null, entryListActivity, "UpdateData" );
//
//        if ( DEBUG )
//        {
//            Log.d( CLASS, "thread start()" );
//        }
//        thread.start();

    }

//    private Runnable returnRes = new Runnable()
//    {
//        public void run()
//        {
//            if ( DEBUG )
//            {
//                Log.d( CLASS, "returnRes runable run() start" );
//            }
//            if ( m_options != null && m_options.size() > 0 )
//            {
//                if ( DEBUG )
//                {
//                    Log.d( CLASS, "m_tests got something" );
//                }
//                m_adapter.notifyDataSetChanged();
//                if ( DEBUG )
//                {
//                    Log.d( CLASS, "m_adapter.notifydatasetchanged() since m_tests got something" );
//                }
//                for ( int i = 0; i < m_options.size(); i++ )
//                {
//                    m_adapter.add( m_options.get( i ) );
//                }
//            }
//            m_adapter.notifyDataSetChanged();
//            if ( DEBUG )
//            {
//                Log.d( CLASS, "m_adapter.notifydatasetchanged() after dismiss m_progressdialog" );
//            }
//        }
//    };


    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent intent )
    {
        if (requestCode == ACTIVITY_LOGIN) {

            boolean loginFailed = true;

            super.onActivityResult( requestCode, resultCode, intent );

            // Important to set "rememberMe" first, as it is used by the following to setters for username and password.
            setRememberMe( intent.getExtras().getBoolean( getResources().getText(R.string.key_rememberMe).toString() ) );
            setUsername( intent.getExtras().getString( getResources().getText(R.string.key_username).toString() ) );
            setPassword( intent.getExtras().getString( getResources().getText(R.string.key_password).toString() ) );
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
                    toastMessage = "Communication error!";
                }
                Toast.makeText( getListView().getContext(), toastMessage, Toast.LENGTH_LONG ).show();
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
    }

    public void refreshDataFromServer() throws ParseException, JDOMException, IOException {

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        if (username == null) {
            if (getRememberMe()) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                String usernameKey = getResources().getText(R.string.key_username).toString();
                String spUsername = sharedPreferences.getString(usernameKey, null);
                username = spUsername;
            }
        }
        return username;
    }

    public void setUsername (String uid) {
        username = uid;
        if (getRememberMe()) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String usernameKey = getResources().getText(R.string.key_username).toString();
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
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                String passwordKey = getResources().getText(R.string.key_password).toString();
                String spPassword = sharedPreferences.getString(passwordKey, null);
                password = spPassword;
            }
        }
        return password;
    }

    public void setPassword (String pw) {
        password = pw;
        if (getRememberMe()) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String passwordKey = getResources().getText(R.string.key_password).toString();
            String storedPassword = sharedPreferences.getString(passwordKey, null);
            if (storedPassword == null || !storedPassword.equals(pw)) {
                SharedPreferences.Editor spEditor = sharedPreferences.edit();
                spEditor.putString(passwordKey, pw);
                spEditor.commit();
            }
        }
    }

    public Boolean getRememberMe () {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String rememberMeKey = getResources().getText(R.string.key_rememberMe).toString();
        return sharedPreferences.getBoolean(rememberMeKey, false);
    }

    public void setRememberMe (Boolean rememberMe) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String rememberMeKey = getResources().getText(R.string.key_rememberMe).toString();
        Boolean storedRememberMe = sharedPreferences.getBoolean(rememberMeKey, !rememberMe);
        if (storedRememberMe != rememberMe) {
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.putBoolean(rememberMeKey, rememberMe);
            spEditor.commit();
        }
    }

}
