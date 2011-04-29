package com.enonic.android.twitranet;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EntryDataHolder
{
    private String text;

    private String userId;

    private String imageUrl;

    private Date entryDate;

    private SimpleDateFormat dateFormat = new SimpleDateFormat( "dd. MMMM HH:mm " );

    public EntryDataHolder( String text, String uid, Date d, String url )
    {
        setText( text );
        setUserId( uid );
        setEntryDate( d );
        setImageUrl( url );
    }

    public String getEntryDate()
    {
        long nowMS = new Date().getTime();
        long entryTimeMs = entryDate.getTime();
        long age = nowMS - entryTimeMs;
        if ( age < 60000 )
        {
            return "Just now ";
        }
        else if ( age < 120000 )
        {
            return "1 minute ago ";
        }
        else if ( age < 3600000 )
        {
            return Long.toString( age / 60000 ) + " minutes ago ";
        }
        else
        {
            return dateFormat.format( entryDate );
        }
    }

    public void setEntryDate( Date d )
    {
        entryDate = d;
    }

    public String getText()
    {
        return text;
    }

    public String getEntryText()
    {
        return getUserId() + ": " + getText();
    }

    public void setText( String t )
    {
        text = t;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId( String uid )
    {
        userId = uid;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }
}
