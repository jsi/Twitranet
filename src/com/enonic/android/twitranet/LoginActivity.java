package com.enonic.android.twitranet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends Activity
{
    private static final int ACTIVITY_LOGIN=0;

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
                setResult( RESULT_OK, callingIntent );
                finish();
            }
        } );
    }


}
