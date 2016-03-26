package com.zackstrife.hugo.shimeji_project;

import android.app.ActionBar;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by ZackStrife on 3/7/2016.
 */
public class Move extends Service{

    protected WindowManager wm;
    private FrameLayout Layout;
    private LayoutInflater li;
    private View myview;
    protected ImageView view;
    protected   WindowManager.LayoutParams params;
    public int width, height;
    private NotificationManager nm;

    AnimationDrawable idleanimation, blinkanimation;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate() {
        super.onCreate();
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


        ////NOTIFICATION
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                01200, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
            nm = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Resources res = this.getResources();
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.poi1)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.poi1))
                .setTicker(("Kore wa tr desu"))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(("Shimeji"))

                .setContentText(("Poi is running"));
        Notification n = builder.build();
    n.flags = Notification.FLAG_ONGOING_EVENT;
        nm.notify(01200, n);
    }
    public void onDestroy(){
        super.onDestroy();
        wm.removeViewImmediate(view);
        nm.cancel(001200);

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
                    view.setBackgroundResource(R.drawable.idle);
                    idleanimation = (AnimationDrawable)view.getBackground();
                    idleanimation.start();
                    break;
                    case MotionEvent.ACTION_OUTSIDE:
                }
                return false;
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////


