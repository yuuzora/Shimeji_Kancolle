package com.zackstrife.hugo.shimeji_project;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by ZackStrife on 3/12/2016.
 */
public class PassingMode extends IntentService {
    private int Choice;
    public PassingMode() {
        super("PassingMode");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



      @Override
      protected void onHandleIntent(Intent intent) {

          Choice =intent.getIntExtra("INFO", 2);
          if (Choice==0) {
              Intent i = new Intent(this, Movesound.class);
              this.startService(i);
          } else if (Choice==1) {
              Intent i = new Intent(this, Move.class);
              this.startService(i);
          } else if (Choice==2) {
             Intent i = new Intent(this, RandomMove.class);
              this.startService(i);
          }
      }

    @Override
    public void onCreate() {
        super.onCreate();

    }
    public void onDestroy(){
        super.onDestroy();

    }
}
