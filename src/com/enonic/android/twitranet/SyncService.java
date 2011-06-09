package com.enonic.android.twitranet;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class SyncService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
