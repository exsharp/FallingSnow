package com.zfliu.fallingsnow.Porfermor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cunoraz.gifview.library.GifView;
import com.zfliu.fallingsnow.Network.HTTP;
import com.zfliu.fallingsnow.R;

public class MainActivity extends AppCompatActivity {

    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent(this,MainService.class);
        GifView gif = (GifView)findViewById(R.id.gif);
        gif.setGifResource(R.raw.chr);
        final TextView tv = (TextView)findViewById(R.id.textView);
    }

    public void OnClickA(View view){
        startService(intent);
//        Intent MyIntent = new Intent(Intent.ACTION_MAIN);
//        MyIntent.addCategory(Intent.CATEGORY_HOME);
//        startActivity(MyIntent);
    }

    public void OnClickB(View view){
        stopService(intent);
    }
}
