package com.zfliu.fallingsnow.Porfermor;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.zfliu.fallingsnow.CtxApplication;
import com.zfliu.fallingsnow.R;
import com.zfliu.fallingsnow.Utils.JudgeOpsRight;
import com.zfliu.fallingsnow.Utils.SendSms;
import com.zfliu.fallingsnow.Utils.SmsObserver;
import com.zfliu.fallingsnow.View.AlterWinFragment;
import com.zfliu.fallingsnow.View.SmsFragment;

import java.util.List;

/**
 * 引导界面类
 */

public class GuideActivity extends AppCompatActivity {
    Intent intent = null;
    private JudgeOpsRight judgeOpsRight;
    private android.app.FragmentManager fragmentManager = null;
    private android.app.FragmentTransaction beginTransaction = null;
    private SendSms sendSms;
    private ViewFlipper smf_viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("info","GuideActivity-->onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);

        sendSms = new SendSms(CtxApplication.getContext());

        //注册短信Listener
        SmsObserver smsObserver;
        smsObserver = new SmsObserver(new Handler(),this);
        getContentResolver().registerContentObserver(Uri.parse("content://sms"), true,smsObserver);

        judgeOpsRight = new JudgeOpsRight();
        SmsFragment smsFragment;
        smsFragment = new SmsFragment();
        fragmentManager = getFragmentManager();
        beginTransaction = fragmentManager.beginTransaction();
        beginTransaction.add(R.id.guide_frame,smsFragment);
        beginTransaction.commit();

        Acp.getInstance(this).request(new AcpOptions.Builder().setPermissions(Manifest.permission.SEND_SMS
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.READ_SMS
                , Manifest.permission.RECEIVE_SMS
                , Manifest.permission.SYSTEM_ALERT_WINDOW).build(), new AcpListener() {
            @Override
            public void onGranted() {
                Toast.makeText(GuideActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(List<String> permissions) {
                Toast.makeText(GuideActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(){
        smf_viewFlipper = (ViewFlipper) findViewById(R.id.smf_viewFlipper);
    }

    protected void doClick(View v){
        switch (v.getId()){
            case R.id.smf_NextBtn:{
                    SharedPreferences pref = getSharedPreferences("fallingSnowPref",MODE_PRIVATE);
                    if(!sendSms.getPhoneNumber() && (pref.getString("PhoneNumber","0").length()!=11)){
                        sendSms.SendChaXunPhoneNumberSms();
                    }
                    Toast.makeText(this,"短信权限设置完成，下一步",Toast.LENGTH_SHORT).show();
                    beginTransaction = fragmentManager.beginTransaction();
                    AlterWinFragment alterWinFragment;
                    alterWinFragment = new AlterWinFragment();
                    beginTransaction.replace(R.id.guide_frame,alterWinFragment);
                    beginTransaction.addToBackStack(null);
                    beginTransaction.commit();
            }
                break;

            case R.id.awf_FinishBtn:
                //下一步：判断是否开启悬浮窗权限
                if(!judgeOpsRight.checkOp(this,24)){
                    Toast.makeText(this,"未开启悬浮窗权限",Toast.LENGTH_SHORT).show();
                }else {
                    //替换当前fragment为短信权限fragment
                    Toast.makeText(this,"已开启悬浮窗权限",Toast.LENGTH_SHORT).show();
                    String phoneNumber = CtxApplication.getPhoneNumber();
                    if(phoneNumber!=null&&phoneNumber.length()==11){
                        Toast.makeText(this,"本机号码为："+phoneNumber,Toast.LENGTH_SHORT).show();
                    }
                    SharedPreferences pref = getSharedPreferences("fallingSnowPref",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("startFirst", false);
                    editor.apply();

                    intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;

            case R.id.guideJumpBtn:
                ToastDemo.Hide();

                SharedPreferences pref = getSharedPreferences("fallingSnowPref",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("startFirst", false);
                editor.apply();

                intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            default:
                break;
        }
    }
}
