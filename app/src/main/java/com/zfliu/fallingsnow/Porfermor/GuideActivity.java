package com.zfliu.fallingsnow.Porfermor;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import com.zfliu.fallingsnow.Tools.Runtime;
import com.zfliu.fallingsnow.Utils.JudgeOpsRight;
import com.zfliu.fallingsnow.Utils.SendSms;
import com.zfliu.fallingsnow.View.AlterWinFragment;
import com.zfliu.fallingsnow.View.SmsFragment;

import java.util.List;

/**
 * 引导界面类
 */

public class GuideActivity extends AppCompatActivity {
    private Intent intent = null;
    private Intent SMSServiceIntent = null;
    private JudgeOpsRight judgeOpsRight;
    private android.app.FragmentManager fragmentManager = null;
    private android.app.FragmentTransaction beginTransaction = null;
    private SendSms sendSms;
    private ViewFlipper smf_viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);

        SMSServiceIntent = new Intent(this,SMSService.class);
        startService(SMSServiceIntent);

        Acp.getInstance(this).request(new AcpOptions.Builder()
                        .setPermissions(
                                Manifest.permission.READ_SMS,
                                Manifest.permission.SEND_SMS)
                /*以下为自定义提示语、按钮文字
                .setDeniedMessage()
                .setDeniedCloseBtn()
                .setDeniedSettingBtn()
                .setRationalMessage()
                .setRationalBtn()*/
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        Toast.makeText(getApplicationContext(),"可以",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        Toast.makeText(getApplicationContext(),"不可以",Toast.LENGTH_LONG).show();
                    }
                });

        sendSms = new SendSms(CtxApplication.getContext());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(SMSServiceIntent);
    }

    protected void doClick(View v){
        switch (v.getId()){
            case R.id.smf_NextBtn:
                if (!sendSms.getPhoneNumber() && Runtime.isFirstTime(getApplicationContext())){
                    sendSms.SendChaXunPhoneNumberSms();
                }
                Toast.makeText(this,"短信权限设置完成，下一步",Toast.LENGTH_SHORT).show();
                beginTransaction = fragmentManager.beginTransaction();
                AlterWinFragment alterWinFragment;
                alterWinFragment = new AlterWinFragment();
                beginTransaction.replace(R.id.guide_frame,alterWinFragment);
                beginTransaction.addToBackStack(null);
                beginTransaction.commit();
                break;
            case R.id.awf_FinishBtn:
                //下一步：判断是否开启悬浮窗权限
                if(!judgeOpsRight.checkOp(this,24)){
                    try {
                        //Toast.makeText(this,"未开启悬浮窗权限",Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
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
