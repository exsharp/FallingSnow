package com.zfliu.fallingsnow.Porfermor;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.zfliu.fallingsnow.Network.HTTP;
import com.zfliu.fallingsnow.Porfermor.Service.MainService;
import com.zfliu.fallingsnow.R;
import com.zfliu.fallingsnow.Tools.GetNumFromContact;
import com.zfliu.fallingsnow.Tools.Runtime;
import com.zfliu.fallingsnow.Utils.JudgeOpsRight;
import com.zfliu.fallingsnow.View.GifView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ToggleButton tgBtnOnOff;
    private EditText et_inputPhone;
    private EditText et_inputContent;
    private Intent serviceIntent;
    private GetNumFromContact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        checkFirstStart();
        initView();
        initEvent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        contact.onActivityResult(requestCode,resultCode,data,contact.new ContactListener(){
            @Override
            public void onSucc(String name, String tele) {
                if (tele.length() != 11){
                    Toast.makeText(MainActivity.this,"不是合法的号码",Toast.LENGTH_LONG).show();
                }else{
                    et_inputPhone.setText(tele);
                }
            }
        });
    }

    private void checkFirstStart(){
        if (Runtime.isFirstTime()) {
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);
            finish();
        }else{
            serviceIntent = new Intent(this,MainService.class);
            startService(serviceIntent);
        }
    }

    private void initView(){
        tgBtnOnOff = (ToggleButton) findViewById(R.id.btn_OnOff);
        et_inputContent = (EditText) findViewById(R.id.et_inputContent);
        et_inputPhone = (EditText) findViewById(R.id.et_inputPhone);
        contact = new GetNumFromContact(this);
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
    }

    private void toContact(){
        contact.getPermission(new AcpListener() {
            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied(List<String> permissions) {
                Toast.makeText(MainActivity.this,"如果不能获得这个权限的话，可能没办法自动输入好嘛",Toast.LENGTH_LONG).show();
            }
        });
        try{
            contact.startActivityForResult();
        }finally {}
    }

    private void sendMsg(){
        String phoneNumber = et_inputPhone.getText().toString();
        String msgContent = et_inputContent.getText().toString();
        msgContent = msgContent.replace("\\","");
        if(phoneNumber.length()!=11){
            Toast.makeText(MainActivity.this,"手机号码输入有误!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(msgContent.length()<1){
            Toast.makeText(MainActivity.this,"祝福语不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        HTTP.Post(phoneNumber,msgContent,new HTTP.OnHttpStatusListener(){
            @Override
            public void Ok(String text) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        et_inputContent.setText("");
                        Toast.makeText(MainActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void Error() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (JudgeOpsRight.CheckNetwork(getApplicationContext())){
                            Toast.makeText(MainActivity.this,"发送失败，原因挺复杂",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this,"发送失败，可能是网络没开启",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void doClick(View view){
        switch (view.getId()){
            case R.id.bt_toContact:
                toContact();
                break;
            case R.id.btn_send:
                sendMsg();
                break;
            case R.id.btn_Desktop:
                //moveTaskToBack(isFinishing());
                Intent intent = new Intent(this,GuideActivity.class);
                startActivity(intent);
                break;
        }
    }

}
