package com.zackstrife.hugo.shimeji_project;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by ZackStrife on 3/16/2016.
 */
public class Movesound extends Move {
    private MediaPlayer mp,sp,bp;
    private CountDownTimer touched;
    private int Touch=0;
    private long timu=5000;


    public void onCreate() {
        super.onCreate();
        sp = MediaPlayer.create(getApplicationContext(), R.raw.start);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.poi);
        bp = MediaPlayer.create(getApplicationContext(), R.raw.baka);
        sp.start();
        //view.setOnClickListener(new Click());
        view.setOnTouchListener(new action());
    }
    public void onDestroy(){
        super.onDestroy();
        stopsound();

    }

    private void stopsound() {
        sp.stop();
        mp.stop();
        bp.stop();
    }

    private class action implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        idleanimation.stop();
                        view.setBackgroundResource(R.drawable.blink);
                        blinkanimation = (AnimationDrawable)view.getBackground();
                        blinkanimation.start();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        int x_cord = (int) event.getRawX();
                        int y_cord = (int) event.getRawY();
                        params.x= (x_cord-(width/2));
                        params.y =(y_cord-(height/2)-50);
                        wm.updateViewLayout(view, params);
                        break;
                case MotionEvent.ACTION_UP:
                    touchcheck();
                    view.setBackgroundResource(R.drawable.idle);
                    idleanimation = (AnimationDrawable)view.getBackground();
                    idleanimation.start();
                    break;
                case MotionEvent.ACTION_OUTSIDE:
            }
            return false;
        }
    }
    private void touchcheck() {
        ///Timer

        if(touched != null) {
            touched.cancel();
            touched = null;
        }
                touched =  new CountDownTimer(10000,2500) {
                    public void onTick(long millisUntilFinished) {
                        timu-=2500;
                      // Log.d("ADebugTag", "Value: " + Long.toString(timu));
                       if (timu==0) { timu=5000;

                           touched.cancel();
                           touched = null;
                           Touch=0;
                        }
                    }
                    @Override
                    public void onFinish() {

                    }
                }.start();
        if (Touch >=5) {
            bp.start();
            timu=5000;
        }
        else  {
            mp.start();
            Touch++;
            timu=5000;
        }
    }




}
