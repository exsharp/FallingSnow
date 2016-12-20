package com.zfliu.fallingsnow.Porfermor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.zfliu.fallingsnow.Network.HTTP;
import com.zfliu.fallingsnow.R;
import com.zfliu.fallingsnow.View.GifView;

public class MainActivity extends AppCompatActivity {

    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent(this,MainService.class);

        GifView view = (GifView)findViewById(R.id.gif);
        view.setGifResource(R.raw.chr);
        /**
         * 1、判断是否是安装后的首次启动
         */
        HTTP.Get("12345",null);

        SharedPreferences pref = getSharedPreferences("fallingSnowPref", MODE_PRIVATE);
        if (pref.getBoolean("startFirst", true)) {
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);

            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("startFirst", false);
            editor.commit();
            finish();
        }else{
            Toast.makeText(this, "欢迎来到主界面", Toast.LENGTH_SHORT).show();
        }
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

    public void OnClickC(View view){
        Intent intent = new Intent(this,GuideActivity.class);
        startActivity(intent);
    }

}
