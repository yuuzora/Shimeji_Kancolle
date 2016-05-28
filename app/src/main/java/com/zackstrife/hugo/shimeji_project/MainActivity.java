package com.zackstrife.hugo.shimeji_project;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private boolean mIsBound;
    private boolean mBound = false;
    private Shimeji mBoundService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        doBindService();
        Intent i = new Intent(this, Shimeji.class);
        startService(i);
        if (getIntent().getIntExtra("destroy", 0) == 1){
            stopService(new Intent(getBaseContext(), Shimeji.class));
        }
        finish();
    }



    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mBoundService = ((Shimeji.LocalBinder)service).getService();
            mBound = true;
            // Tell the user about this for our demo.
            Intent intent = getIntent();
            if (intent.getIntExtra("tag", 0) == 1){
                mBoundService.pause();
            }
            if (intent.getIntExtra("mute", 0) == 1){
                mBoundService.Mute();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    void doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        bindService(new Intent(MainActivity.this,
                Shimeji.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }
}



