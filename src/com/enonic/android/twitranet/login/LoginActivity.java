package com.enonic.android.twitranet.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.enonic.android.twitranet.R;
import com.enonic.android.twitranet.properties.PropertiesActivity;

public class LoginActivity extends Activity
{

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.login_layout );

        Button loginButton = (Button) findViewById( R.id.loginButton );
        loginButton.setOnClickListener( new View.OnClickListener() {

            public void onClick( View view )
            {
                Intent callingIntent = getIntent();
                EditText userNameField = (EditText) findViewById( R.id.username );
                EditText passwordField = (EditText) findViewById( R.id.password );
                final String userNameEntered = userNameField.getText().toString();
                final String passwordEntered = passwordField.getText().toString();
                callingIntent.putExtra( getResources().getText(R.string.key_username).toString(), userNameEntered );
                callingIntent.putExtra( getResources().getText(R.string.key_password).toString(), passwordEntered );
                setResult( RESULT_OK, callingIntent );
                finish();
            }
        } );

        Button clearButton = (Button) findViewById( R.id.clearLoginButton );
        clearButton.setOnClickListener( new View.OnClickListener() {
            public void onClick( View view ) {
                EditText userNameField = (EditText) findViewById( R.id.username );
                EditText passwordField = (EditText) findViewById( R.id.password );
                userNameField.setText( "" );
                passwordField.setText( "" );
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preferences_menu:

                Intent i = new Intent( this, PropertiesActivity.class );
                startActivity(i);
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
