package com.enonic.android.twitranet;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EntryDataHolder
{
    private Integer thumbId;

    private String text;

    private String userId;

    private Date entryDate;

    private SimpleDateFormat dateFormat = new SimpleDateFormat( "dd. MMMM hh:mm " );

    public EntryDataHolder( String text, String uid, Date d )
    {
        setText( text );
        setUserId( uid );
        setEntryDate( d );
        if ( uid.equals( "bhj" ) )
        {
            setThumbId( R.drawable.user_bhj_64 );
        }
        else if ( uid.equals( "jsi" ) )
        {
            setThumbId( R.drawable.user_jsi_64 );
        }
        else if ( uid.equals( "jvs" ) )
        {
            setThumbId( R.drawable.user_jvs_64 );
        }
        else if ( uid.equals( "liv" ) )
        {
            setThumbId( R.drawable.user_liv_64 );
        }
        else if ( uid.equals( "ljl" ) )
        {
            setThumbId( R.drawable.user_ljl );
        }
        else if ( uid.equals( "mer" ) )
        {
            setThumbId( R.drawable.user_mer_64 );
        }
        else if ( uid.equals( "rmy" ) )
        {
            setThumbId( R.drawable.user_rmy_64 );
        }
        else if ( uid.equals( "tan" ) )
        {
            setThumbId( R.drawable.user_tan );
        }
        else if ( uid.equals( "tlo" ) )
        {
            setThumbId( R.drawable.user_tlo_64 );
        }
        else if ( uid.equals( "tsi" ) )
        {
            setThumbId( R.drawable.user_tsi_64 );
        }
        else if ( uid.equals( "daa" ) )
        {
            setThumbId( R.drawable.user_daa_64 );
        }
        else if ( uid.equals( "tre" ) )
        {
            setThumbId( R.drawable.user_tre_64 );
        }
        else
        {
            setThumbId( R.drawable.user_dummy_64 );
        }
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

    public Integer getThumbId()
    {
        return thumbId;
    }

    public void setThumbId( Integer id )
    {
        thumbId = id;
    }

    public String getText()
    {
        return text;
    }

    public String getEntryText()
    {
        return userId + ": " + text;
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
}
