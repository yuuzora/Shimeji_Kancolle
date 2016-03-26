package com.zackstrife.hugo.shimeji_project;

import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class RandomMove extends Movesound {
    private int timer;
    private boolean sticky= false,over=false,under=false;
    private CountDownTimer running, timing,moving;
    private int sens=1;
    Handler handler = new Handler(Looper.getMainLooper());
    public void onCreate() {
        super.onCreate();
        randomsens();
       // draw();
        handler.post(draw());


    }
    public void onDestroy() {
        super.onDestroy();
        timing.cancel();
        running.cancel();
    }
    private void randomsens() {
        Random s = new Random();
        timing = new CountDownTimer(8000,(s.nextInt(3000-1000)+1000)) {
            @Override
            public void onTick(long millisUntilFinished) {
                Random r = new Random();
                sens =(r.nextInt(3-1)+1);
            }
            @Override
            public void onFinish() {
                timing.start();
            }
        }.start();
    }
    private Runnable draw(){
        running = new CountDownTimer(10000,10) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (sens==1){
                    if (params.x < 280 && !over) {
                        params.x++;
                        right();
                        //Log.d("push", "up it goes");
                    }
                    else { over=true;}
                    if (over) {
                        params.x--;
                        left();

                        //Log.d("Hell", "Down");
                    }
                    if (over &&  params.x < 85) {
                        //Log.d("Hell", ">80");
                        params.x++;
                        right();
                        over=false;
                    }
                }
                else if (sens==2){
                    if (params.x > -270 && !under) {
                        params.x--;
                        left();
                        //Log.d("push", "down it goes");
                    }
                    else { under=true;}
                    if (under) {
                        params.x++;
                        right();
                        // Log.d("Heaven", "Up");
                    }
                    if (under &&  params.x > -85) {
                        // Log.d("Hell", "<80");
                        params.x--;
                        left();
                        under=false;
                    }
                }
                wm.updateViewLayout(view, params);
            }
            @Override
            public void onFinish() {
                running.start();
            }
        }.start();
        return null;
    }
    private void left() {
        view.setBackgroundResource(R.drawable.idle_reverse);
        idleanimation = (AnimationDrawable)view.getBackground();
        idleanimation.start();

    }
    private void right() {
        view.setBackgroundResource(R.drawable.idle);
        idleanimation = (AnimationDrawable)view.getBackground();
        idleanimation.start();
    }
    // running =  new CountDownTimer(1, 1) {
            //public void onTick(long millisUntilFinished) {
          //  public void onFinish() {
            //    running.start();
            //}
        //}.start();
}


