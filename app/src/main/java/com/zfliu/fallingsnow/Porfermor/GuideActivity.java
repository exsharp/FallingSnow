package com.zfliu.fallingsnow.Porfermor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zfliu.fallingsnow.CtxApplication;
import com.zfliu.fallingsnow.R;
import com.zfliu.fallingsnow.Utils.JudgeOpsRight;
import com.zfliu.fallingsnow.Utils.SendSms;
import com.zfliu.fallingsnow.Utils.SmsObserver;
import com.zfliu.fallingsnow.View.AlterWinFragment;
import com.zfliu.fallingsnow.View.SmsFragment;

/**
 * 引导界面类
 */

public class GuideActivity extends AppCompatActivity {
    Intent intent = null;
    private JudgeOpsRight judgeOpsRight;
    private android.app.FragmentManager fragmentManager = null;
    private android.app.FragmentTransaction beginTransaction = null;
    private AlterWinFragment alterWinFragment;
    private SmsFragment smsFragment;
    private SendSms sendSms;
    private SmsObserver smsObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("info","GuideActivity-->onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);

        sendSms = new SendSms(CtxApplication.getContext());

        /**
         * 注册短信Listener
         */
        smsObserver = new SmsObserver(new Handler(),this);
        Uri smsUri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(smsUri, true,smsObserver);

        judgeOpsRight = new JudgeOpsRight();

        fragmentManager = getFragmentManager();
        beginTransaction = fragmentManager.beginTransaction();
        smsFragment = new SmsFragment();
        beginTransaction.add(R.id.guide_frame,smsFragment);
        beginTransaction.commit();
    }

    protected void doClick(View v){
        switch (v.getId()){
            case R.id.smf_NextBtn:
                //1.判断短信权限是否开启
                if(judgeOpsRight.checkOp(this,20)!=true){
                    Toast.makeText(this,"未开启发送短信权限",Toast.LENGTH_SHORT).show();
                }else if(judgeOpsRight.checkOp(this,14)!=true){
                    Toast.makeText(this,"未开启读取短信权限",Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences pref = getSharedPreferences("fallingSnowPref",MODE_PRIVATE);
                    if(sendSms.getPhoneNumber()==false && (pref.getString("PhoneNumber","0").length()!=11)){
                        sendSms.SendChaXunPhoneNumberSms();
                    }
                    Toast.makeText(this,"短信权限设置完成，下一步",Toast.LENGTH_SHORT).show();
                    beginTransaction = fragmentManager.beginTransaction();
                    alterWinFragment = new AlterWinFragment();
                    beginTransaction.replace(R.id.guide_frame,alterWinFragment);
                    beginTransaction.addToBackStack(null);
                    beginTransaction.commit();
                }
                break;

            case R.id.awf_NextBtn:
                //下一步：判断是否开启悬浮窗权限
                if(judgeOpsRight.checkOp(this,24)!=true){
                    Toast.makeText(this,"未开启悬浮窗权限",Toast.LENGTH_SHORT).show();
                }else {
                    //替换当前fragment为短信权限fragment
                    Toast.makeText(this,"已开启悬浮窗权限",Toast.LENGTH_SHORT).show();
                    String phoneNumber = getSharedPreferences("fallingSnowPref",MODE_PRIVATE)
                            .getString("PhoneNumber","13800138000");
                    System.out.println("PhoneNumber"+phoneNumber);
                    if(phoneNumber.length()==11&&phoneNumber!="13800138000"){
                        Toast.makeText(this,"本机号码为："+phoneNumber,Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.guideJumpBtn:
                Log.i("info","OnClickB");
                ToastDemo.Hide();
                intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("info","GuideActivity-->onStop");
    }

    @Override
    protected void onResume() {
        super.onStop();
        Log.i("info","GuideActivity-->onResume");
    }
}
