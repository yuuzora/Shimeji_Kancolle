package com.zackstrife.hugo.shimeji_project;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.sax.StartElementListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewManager;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView Moods;
    private Button Start, Stop;
    private Adapter Mainadapter;
    private ArrayList<String> Moodlist;
    private int test;
    private Toast behave;
    public final static String INFO = "data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Moods = (ListView)findViewById(R.id.List);
        Moods.setOnItemClickListener(new Checkmood());
        Start = (Button)findViewById(R.id.Start);
        Stop = (Button)findViewById(R.id.Stop);
        ArrayList<String> Moodlist = new ArrayList<String>();
        Moodlist.add("Idle");
        Moodlist.add("Idle - mute");
        Moodlist.add("Random Behavior");
        Mainadapter = new ArrayAdapter<String>(this,R.layout.listviewwhite, Moodlist);
        Moods.setAdapter((ListAdapter) Mainadapter);

        Moods.setItemChecked(2, true);


    }
        public class Checkmood implements AdapterView.OnItemClickListener{
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                test = position;

                    if (test ==0) {

                        behave.makeText(MainActivity.this, "Idle" , Toast.LENGTH_SHORT).show();
                    }
                    else if (test ==1) {

                        behave.makeText(MainActivity.this, "Idle - mute" , Toast.LENGTH_SHORT).show();
                    }
                    else if (test ==2) {

                        behave.makeText(MainActivity.this, "Random behavior" , Toast.LENGTH_SHORT).show();

                    }

            }
        }


        public void StartShimeji(View view){
            //Pass the mode choice to an IntentService, this one will then call the right service
            Intent launch = new Intent(getBaseContext(), PassingMode.class);
            launch.putExtra("INFO", test);
            this.startService(launch);

        }



        public void StopShimeji(View view) {
            ////////Suppress every working service
            stopService(new Intent(getBaseContext(), Move.class));
            stopService(new Intent(getBaseContext(), Movesound.class));
            stopService(new Intent(getBaseContext(), RandomMove.class));


        }

}



