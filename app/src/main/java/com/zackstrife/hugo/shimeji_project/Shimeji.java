package com.zackstrife.hugo.shimeji_project;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;

import java.util.Random;

/**
 * Created by Zack-Strife on 5/21/2016.
 */
public class Shimeji extends Service {


    protected WindowManager wm;
    //private FrameLayout Layout;
    private LayoutInflater li;
    private View myview;
    protected ImageView view;
    protected   WindowManager.LayoutParams params;
    public int width, height;
    private NotificationManager nm;
    private MediaPlayer mp,sp,bp;
    private CountDownTimer touched;
    private int Touch=0;
    private long timu=5000;
    private int timer;
    private boolean sticky= false, over=false, under=false;
    private CountDownTimer running, timing, moving;
    private int sens=1;
    private boolean isPaused = true;
    private boolean isMuted = false;
    Handler handler = new Handler(Looper.getMainLooper());

    NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
    AnimationDrawable idleanimation, blinkanimation;

    /** Start of binder
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        Shimeji getService() {
            return Shimeji.this;
        }
    }


    /** end of binder */
    public void onCreate() {
        super.onCreate();
        Notification();
        pause();
    }
    public void onDestroy(){
        super.onDestroy();
        wm.removeViewImmediate(view);
        nm.cancel(001200);
        if (!isMuted){
            stopsound();
        }
        timing.cancel();
        running.cancel();
    }

    protected void pause(){
        if (isPaused) {
            ShimejiView();
            randomsens();
            handler.post(draw());
            sp = MediaPlayer.create(getApplicationContext(), R.raw.start);
            mp = MediaPlayer.create(getApplicationContext(), R.raw.poi);
            bp = MediaPlayer.create(getApplicationContext(), R.raw.baka);
            sp.start();
            isPaused = false;
            view.setOnTouchListener(new action());

            builder.setContentText("Poi is running");
            nm.notify(
                    01200,
                    builder.build());
        } else if(!isPaused){
        wm.removeViewImmediate(view);
        if (!isMuted){
            stopsound();
        }
        timing.cancel();
        running.cancel();
        isPaused = true;
            builder.setContentText("Poi is paused");
            nm.notify(
                    01200,
                    builder.build());
        }
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
    private void ShimejiView()
    {
        view = new ImageView(this);
        view.setBackgroundResource(R.drawable.idle);
        idleanimation = (AnimationDrawable)view.getBackground();
        idleanimation.start();
        view.setOnTouchListener(new action());
        li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.NO_GRAVITY;
        myview = li.inflate(R.layout.playground, null);
        wm.addView(view, params);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void Notification()
    {
        Intent notificationmuteIntent = new Intent(this, MainActivity.class);
        notificationmuteIntent.putExtra("mute", 1);
        PendingIntent muteintent = PendingIntent.getActivity(this,
                01202, notificationmuteIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.mipmap.poi_icons, "Mute", muteintent).build();
        builder.addAction(action);

        Intent destroyIntent = new Intent(this, MainActivity.class);
        destroyIntent.putExtra("destroy", 1);
        PendingIntent shutdown = PendingIntent.getActivity(this,
                01201, destroyIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Action destroy = new NotificationCompat.Action.Builder(R.mipmap.poi_icons, "Destroy", shutdown).build();
        builder.addAction(destroy);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra("tag", 1);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                01200, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Resources res = this.getResources();
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.poi1)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.poi1))
                .setTicker(("Kore wa tr desu"))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(("Shimeji"))
                .setContentText(("Poi has probably crashed"));
        Notification n = builder.build();
        n.flags = Notification.FLAG_ONGOING_EVENT;
        nm.notify(01200, n);

    }
    protected void touchcheck()
    {

        if(touched != null)
        {
            touched.cancel();
            touched = null;
        }
        touched =  new CountDownTimer(10000,2500)
        {
            public void onTick(long millisUntilFinished)
            {
                timu-=2500;
                // Log.d("ADebugTag", "Value: " + Long.toString(timu));
                if (timu==0)
                {
                    timu=5000;
                    touched.cancel();
                    touched = null;
                    Touch=0;
                }
            }
            @Override
            public void onFinish() {}
        }.start();
        if (Touch >=5)
        {
            if (!isMuted) {
                bp.start();
            }
            timu=5000;
        }
        else{
            if (!isMuted){
                mp.start();
            }
            Touch++;
            timu=5000;
        }
    }

    protected void stopsound()
    {
        sp.stop();
        mp.stop();
        bp.stop();
    }

    protected void Mute()
    {
        if (!isMuted){
            sp.release();
            mp.release();
            bp.release();
            isMuted = true;
        } else if (isMuted){
            sp = MediaPlayer.create(getApplicationContext(), R.raw.start);
            mp = MediaPlayer.create(getApplicationContext(), R.raw.poi);
            bp = MediaPlayer.create(getApplicationContext(), R.raw.baka);
            isMuted = false;
        }
    }

    private void randomsens()
    {
        Random s = new Random();
        timing = new CountDownTimer(8000,(s.nextInt(3000-1000)+1000))
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                Random r = new Random();
                sens =(r.nextInt(3-1)+1);
            }
            @Override
            public void onFinish() {
                timing.start();
            }
        }.start();
    }

    private Runnable draw()
    {
        running = new CountDownTimer(10000,10)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
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
    private void left()
    {
        view.setBackgroundResource(R.drawable.idle_reverse);
        idleanimation = (AnimationDrawable)view.getBackground();
        idleanimation.start();
    }
    private void right()
    {
        view.setBackgroundResource(R.drawable.idle);
        idleanimation = (AnimationDrawable)view.getBackground();
        idleanimation.start();
    }
}