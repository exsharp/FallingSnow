package com.zfliu.fallingsnow.Porfermor;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zfliu.fallingsnow.R;
import com.zfliu.fallingsnow.View.MarqueeTextView;

public class MainActivity extends AppCompatActivity {

    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this,MainService.class);
//        startService(intent);
//        final MarqueeTextView view = (MarqueeTextView)findViewById(R.id.marqueeTextView);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        view.setTextAndScroll("abcdefghijklmnopqrstuvwxyz");
//                    }
//                });
//            }
//        }).start();
    }

    public void OnClickA(View view){
        startService(intent);
    }

    public void OnClickB(View view){
        stopService(intent);
    }
}
