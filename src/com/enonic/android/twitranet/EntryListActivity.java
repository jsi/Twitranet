package com.enonic.android.twitranet;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class EntryListActivity
    extends ListActivity
{
    private static final int ACTIVITY_LOGIN=0;

    public final String CLASS = EntryListActivity.class.getSimpleName();

    public static final boolean DEBUG = true;

    private ArrayList<EntryDataHolder> m_options = null;

    private EntryArrayAdapter m_adapter;

//    private Runnable entryListActivity;

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
        this.m_adapter = new EntryArrayAdapter( this, R.layout.twitranett_list_item, m_options );
        ListView listView = getListView();
        listView.setAdapter( this.m_adapter );
        listView.setSmoothScrollbarEnabled( true );

        Intent i = new Intent(this, LoginActivity.class);
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

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
//                Object obj = (Object)m_adapter.getItem(position);
//                if (obj != null) {
//                    //TODO set this intent creation based on the unique parameter of the each CreateTestOption class
//                    Intent intent = new Intent();
//                    if (position == 2) {
//                        intent.setClass(ViewCreateTestActivity.this, placeholderActivity2.class);
//                    }else if (position == 1){
//                        intent.setClass(ViewCreateTestActivity.this, placeholderActivity1.class);
//                    }else if (position == 0 ) {
//                        intent.setClass(ViewCreateTestActivity.this, placeholderActivity0.class);
//                    }
//                    startActivity(intent);
//                }
//            }
//        });

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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // TODO: fill in rest of method

    }

}
