package com.zfliu.fallingsnow.Porfermor;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.zfliu.fallingsnow.CtxApplication;
import com.zfliu.fallingsnow.Porfermor.Service.SMSService;
import com.zfliu.fallingsnow.R;
import com.zfliu.fallingsnow.Tools.Runtime;
import com.zfliu.fallingsnow.Utils.JudgeOpsRight;
import com.zfliu.fallingsnow.Utils.SMS.SendSms;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);

        SMSServiceIntent = new Intent(GuideActivity.this,SMSService.class);
        startService(SMSServiceIntent);

        AcpOptions options = new AcpOptions.Builder().setPermissions(
                        Manifest.permission.READ_SMS,
                        Manifest.permission.SEND_SMS).build();
        AcpListener listener = new AcpListener() {
            @Override
            public void onGranted() {
                Toast.makeText(getApplicationContext(),"可以",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDenied(List<String> permissions) {
                Toast.makeText(getApplicationContext(),"不可以",Toast.LENGTH_LONG).show();
            }
        };
        Acp.getInstance(this).request(options,listener);

        sendSms = new SendSms(CtxApplication.getContext());
        judgeOpsRight = new JudgeOpsRight();
        SmsFragment smsFragment;
        smsFragment = new SmsFragment();
        fragmentManager = getFragmentManager();
        beginTransaction = fragmentManager.beginTransaction();
        beginTransaction.add(R.id.guide_frame,smsFragment);
        beginTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(SMSServiceIntent);
    }

    public void doClick(View v){
        switch (v.getId()){
            case R.id.smf_NextBtn:
                if (!sendSms.getPhoneNumber() && Runtime.getPhoneNumber() == null){
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
                    Toast.makeText(this,"未开启悬浮窗权限",Toast.LENGTH_SHORT).show();
                }else {
                    //替换当前fragment为短信权限fragment
                    Toast.makeText(this,"已开启悬浮窗权限",Toast.LENGTH_SHORT).show();
                    String phoneNumber = Runtime.getPhoneNumber();
                    if(phoneNumber!=null){
                        Toast.makeText(this,"本机号码为："+phoneNumber,Toast.LENGTH_SHORT).show();
                    }

                    intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.guideJumpBtn:
                Runtime.setFirstTimeToFalse();

                intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            default:
                break;
        }
    }
}
