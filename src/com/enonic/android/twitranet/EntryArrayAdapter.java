package com.enonic.android.twitranet;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.enonic.android.twitranet.util.Base64Util;

public class EntryArrayAdapter
    extends ArrayAdapter<EntryDataHolder>
{
    public static final String CLASS = EntryArrayAdapter.class.getSimpleName();

    public static final boolean DEBUG = true;

    private LayoutInflater mInflater;

    private int layoutResource;

    SimpleDateFormat tempInputFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm" );

    private String serverUrl = "http://intra.enonic.com/";
//    private String serverUrl = "http://vtnode1:8080/cms-commando-unstable-enonic/site/41/";

    private Integer messageCount = 20;

    public EntryArrayAdapter( Context context, int textViewResourceId, List<EntryDataHolder> objects )
    {
        super( context, textViewResourceId, objects );
        if ( DEBUG )
        {
            Log.d( CLASS, "create test data " );
        }
        if ( objects == null || objects.size() == 0 )
        {
            try
            {
//                addStaticItems();
                addItems();
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
        if ( DEBUG )
        {
            Log.d( CLASS, "set up LayoutInflater from context" );
        }
        this.mInflater = LayoutInflater.from( context );
        this.layoutResource = textViewResourceId;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        if ( DEBUG )
        {
            Log.d( CLASS, "start getView()" );
        }
        final ViewHolder holder;
        View v = convertView;
        if ( v == null )
        {
            if ( DEBUG )
            {
                Log.d( CLASS, "mInflater inflate history_list_item" );
            }
            v = mInflater.inflate( layoutResource, null );
            if ( DEBUG )
            {
                Log.d( CLASS, "set up ViewHolder" );
            }
            holder = new ViewHolder();
            holder.userImage = (ImageView) v.findViewById( R.id.userImage );
            holder.text = (TextView) v.findViewById( R.id.entryText );
            holder.date = (TextView) v.findViewById( R.id.dateText );
            v.setTag( holder );
        }
        else
        {
            holder = (ViewHolder) v.getTag();
        }

        EntryDataHolder data = getItem( position );

//        int count = getCount();

        if ( data != null )
        {
            if ( DEBUG )
            {
                Log.d( CLASS, "data is not null" );
            }
            //loading first line
            holder.date.setText( data.getEntryDate() );
            holder.date.setVisibility( View.VISIBLE );

            holder.text.setText( data.getEntryText() );
            holder.text.setVisibility( View.VISIBLE );
            //loading icon
            holder.userImage.setImageResource( data.getThumbId() );
        }
        else
        {
            holder.userImage.setImageResource( R.drawable.th_line );
            holder.text.setVisibility( View.GONE );
            holder.date.setVisibility( View.GONE );
        }

        return v;
    }

    private void addItems()
        throws ParseException, IOException, JDOMException
    {
        String username = "jsi";
        String password = "test";
        URL twitranettMessagesURL = new URL( serverUrl + "twitranettmessages?count=" + messageCount );
        HttpURLConnection connection = (HttpURLConnection) twitranettMessagesURL.openConnection();
        connection.setRequestMethod( "POST" );

        String userpassword = username + ":" + password;
        String encodedAuthorization = Base64Util.encode( userpassword.getBytes() );
        connection.setRequestProperty( "Authorization", "Basic " + encodedAuthorization );
        connection.connect();
        InputStream xmlResponse = connection.getInputStream();

        SAXBuilder xmlDocumentBuilder = new SAXBuilder( false );
        Document twitranettXML = xmlDocumentBuilder.build( xmlResponse );
        List<Element> tweets = twitranettXML.getRootElement().getChild( "twitrs" ).getChildren( "twitr" );
        for ( Element tweet : tweets )
        {
            String tOwner = tweet.getAttributeValue( "owner" );
            String tText = tweet.getChild( "message" ).getText();
            String tDate = tweet.getAttributeValue( "created" );
            add( new EntryDataHolder( tText, tOwner, tempInputFormat.parse( tDate ) ) );
        }

    }
//
//    private void addStaticItems()
//        throws ParseException
//    {
//        add( new EntryDataHolder( "3 minutes ago!", "tsi", new Date( new Date().getTime() - 180000 ) ) );
//        add( new EntryDataHolder(
//            "Vask av pulter og hyller fredag 25. mars. Noter datoen og husk å rydde egne hyller og pulter før bøtteballetten kommer.",
//            "liv", tempInputFormat.parse( "2011-03-17 10:48" ) ) );
//        add( new EntryDataHolder(
//            "@rmy: Den var kjapp. Savner du oss så mye at du logger deg på flere ganger om dagen for å sjekke e-post og twitranett? # :-D",
//            "jsi", tempInputFormat.parse( "2011-03-17 08:35" ) ) );
//        add( new EntryDataHolder( "Vi lagde alt for mange muffins på Filos i går. Overskuddet ligger på kjøkkenet. ;)", "jsi",
//                                  tempInputFormat.parse( "2011-03-17 08:34" ) ) );
//        add( new EntryDataHolder( "Skal fikse det så fort jeg har litt tid :)", "rmy", tempInputFormat.parse( "2011-03-16 13:49" ) ) );
//        add( new EntryDataHolder( "@rmy kunne trengt Enonic Resource Tool akkurat nå", "ljl",
//                                  tempInputFormat.parse( "2011-03-16 10:32" ) ) );
//        add( new EntryDataHolder( "http://www.howstuffworks.com/nuclear-power.htm", "jsi", tempInputFormat.parse( "2011-03-15 13:06" ) ) );
//        add( new EntryDataHolder( "iPad duger visst rimelig godt på tur: http://sives.in/iPadTravel", "jsi",
//                                  tempInputFormat.parse( "2011-03-15 12:21" ) ) );
//        add( new EntryDataHolder( "http://www.youtube.com/watch?v=aVW2BpcMnbo", "tlo", tempInputFormat.parse( "2011-03-14 10:49" ) ) );
//        add( new EntryDataHolder( "NAV kan cache statiske ressurser bedre", "rmy", tempInputFormat.parse( "2011-03-13 20:47" ) ) );
//        add( new EntryDataHolder( "De sliter med oppsettet sitt... Håper de får orden på det...", "mer",
//                                  tempInputFormat.parse( "2011-03-11 16:05" ) ) );
//        add( new EntryDataHolder( "NAV er nå live på 4.4.5", "tsi", tempInputFormat.parse( "2011-03-11 12:25" ) ) );
//        add( new EntryDataHolder( "nice", "tlo", tempInputFormat.parse( "2011-03-10 10:10" ) ) );
//        add( new EntryDataHolder( "Til orientering: rengjøringsbyrået er satt på støvsugersaken ;-)", "liv",
//                                  tempInputFormat.parse( "2011-03-10 09:58" ) ) );
//        add( new EntryDataHolder( "@tlo: Deilig å se Futura for en gangs skyld.", "bhj", tempInputFormat.parse( "2011-03-09 08:11" ) ) );
//        add( new EntryDataHolder( "En nøktern, intern, liten sak i anledning CE: http://www.youtube.com/watch?v=L2q2ZaxZBEc", "tlo",
//                                  tempInputFormat.parse( "2011-03-08 18:21" ) ) );
//    }
//
    private static class ViewHolder
    {
        ImageView userImage;

        TextView text;

        TextView date;
    }
}
