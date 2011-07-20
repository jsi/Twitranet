package com.enonic.android.twitranet.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.enonic.android.twitranet.R;
import com.enonic.android.twitranet.preferences.PreferencesListActivity;

public class LoginActivity extends Activity
{

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.login_layout );

        Button loginButton = (Button) findViewById( R.id.loginButton );
        loginButton.setOnClickListener( new View.OnClickListener() {
            public void onClick( View view )
            {
                EditText userNameField = (EditText) findViewById( R.id.username );
                EditText passwordField = (EditText) findViewById( R.id.password );
                CheckBox rememberMeField = (CheckBox) findViewById( R.id.rememberMe );
                final String userNameEntered = userNameField.getText().toString();
                final String passwordEntered = passwordField.getText().toString();
                final Boolean rememberMeEntered = rememberMeField.isChecked();
                setRememberMe(rememberMeEntered);
                setUsername(userNameEntered);
                setPassword(passwordEntered);
                setResult( RESULT_OK, getIntent() );
                finish();
            }
        } );

        Button clearButton = (Button) findViewById( R.id.clearLoginButton );
        clearButton.setOnClickListener( new View.OnClickListener() {
            public void onClick( View view ) {
                EditText userNameField = (EditText) findViewById( R.id.username );
                EditText passwordField = (EditText) findViewById( R.id.password );
                CheckBox rememberMeField = (CheckBox) findViewById( R.id.rememberMe );
                userNameField.setText( "" );
                passwordField.setText( "" );
                rememberMeField.setChecked(false);
            }
        });
    }

    @Override
    protected void onResume () {
        super.onResume();

        EditText userNameField = (EditText) findViewById( R.id.username );
        EditText passwordField = (EditText) findViewById( R.id.password );
        CheckBox rememberMeField = (CheckBox) findViewById( R.id.rememberMe );

        if (getRememberMe()) {
            userNameField.setText(getUsername());
            passwordField.setText(getPassword());
            rememberMeField.setChecked(true);
        } else {
            userNameField.setText("");
            passwordField.setText("");
            rememberMeField.setChecked(false);
        }
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        EditText userNameField = (EditText) findViewById( R.id.username );
        EditText passwordField = (EditText) findViewById( R.id.password );
        CheckBox rememberMeField = (CheckBox) findViewById( R.id.rememberMe );
        final String userNameEntered = userNameField.getText().toString();
        final String passwordEntered = passwordField.getText().toString();
        final Boolean rememberMeEntered = rememberMeField.isChecked();
        setUsername(userNameEntered);
        setPassword(passwordEntered);
        setRememberMe(rememberMeEntered);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {

            case R.id.preferences_menu:
                Intent i = new Intent( this, PreferencesListActivity.class );
                startActivity(i);
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

    public void setUsername (String uid) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String usernameKey = getResources().getText(R.string.key_username).toString();
        String storedUsername = sharedPreferences.getString(usernameKey, null);
        if (storedUsername == null || !storedUsername.equals(uid)) {
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.putString(usernameKey, uid);
            spEditor.commit();
        }
    }

    public String getPassword () {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String passwordKey = getResources().getText(R.string.key_password).toString();
        return sharedPreferences.getString(passwordKey, null);
    }

    public void setPassword (String pw) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String passwordKey = getResources().getText(R.string.key_password).toString();
        String storedPassword = sharedPreferences.getString(passwordKey, null);
        if (storedPassword == null || !storedPassword.equals(pw)) {
            SharedPreferences.Editor spEditor = sharedPreferences.edit();
            spEditor.putString(passwordKey, pw);
            spEditor.commit();
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
