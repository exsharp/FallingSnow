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
    }

    public void OnClickA(View view){
        startService(intent);
        Intent MyIntent = new Intent(Intent.ACTION_MAIN);
        MyIntent.addCategory(Intent.CATEGORY_HOME);
        startActivity(MyIntent);
    }

    public void OnClickB(View view){
        stopService(intent);
    }
}
