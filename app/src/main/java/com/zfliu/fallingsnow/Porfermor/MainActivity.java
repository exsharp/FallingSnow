package com.zfliu.fallingsnow.Porfermor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.zfliu.fallingsnow.CtxApplication;
import com.zfliu.fallingsnow.Network.HTTP;
import com.zfliu.fallingsnow.R;
import com.zfliu.fallingsnow.Utils.JudgeOpsRight;
import com.zfliu.fallingsnow.View.GifView;

public class MainActivity extends AppCompatActivity {

    private ToggleButton tgBtnOnOff;
    private EditText et_inputPhone;
    private EditText et_inputContent;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        checkFirstStart();
        initView();
        initEvent();
    }

    private void initView(){
        tgBtnOnOff = (ToggleButton) findViewById(R.id.btn_OnOff);
        et_inputContent = (EditText) findViewById(R.id.et_inputContent);
        et_inputPhone = (EditText) findViewById(R.id.et_inputPhone);
    }

    private void initEvent(){
        tgBtnOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    startService(serviceIntent);
                }else{
                    stopService(serviceIntent);
                }
            }
        });
        SharedPreferences pref = getApplicationContext()
                .getSharedPreferences("fallingSnowPref", Context.MODE_PRIVATE);
        String phoneNumber = pref.getString("PhoneNumber","0");
        if (phoneNumber.length() == 11){
            CtxApplication.setPhoneNumber(phoneNumber);
            Toast.makeText(getApplicationContext(),"手机号码是:"+phoneNumber,Toast.LENGTH_LONG).show();
        }
    }

    private void checkFirstStart(){
        SharedPreferences pref = getSharedPreferences("fallingSnowPref", MODE_PRIVATE);
        if (pref.getBoolean("startFirst", true)) {
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
            finish();
        }else{
            serviceIntent = new Intent(this,MainService.class);
            startService(serviceIntent);
            GifView view = (GifView)findViewById(R.id.gif);
            view.setGifResource(R.raw.chr);
        }
    }

    protected void doClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.btn_send:
                String phoneNumber = et_inputPhone.getText().toString();
                String msgContent = et_inputContent.getText().toString();
                if(phoneNumber.length()!=11){
                    Toast.makeText(MainActivity.this,"手机号码输入有误!",Toast.LENGTH_SHORT).show();
                    break;
                }
                if(msgContent.length()<1){
                    Toast.makeText(MainActivity.this,"祝福语不能为空！",Toast.LENGTH_SHORT).show();
                    break;
                }
                if(!(new JudgeOpsRight()).CheckNetwork(MainActivity.this)){
                    Toast.makeText(MainActivity.this,"请检查网络连接",Toast.LENGTH_SHORT).show();
                    break;
                }
                HTTP.Post(phoneNumber,msgContent,null);
                break;
            case R.id.btn_Desktop:
                moveTaskToBack(isFinishing());
                break;
            case R.id.imgBtn:
                intent = new Intent(this,GuideActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

}
