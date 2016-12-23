package com.zfliu.fallingsnow.Porfermor;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.zfliu.fallingsnow.CtxApplication;
import com.zfliu.fallingsnow.Porfermor.Service.MainService;
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

    private RadioGroup radioGroup;
    private Button alterWinBtn;
    private TextView textView;
    private Button finishBtn;

    static boolean smsRightStatus;
    static boolean alterRightStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);

        SMSServiceIntent = new Intent(GuideActivity.this,SMSService.class);
        startService(SMSServiceIntent);

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
        AcpOptions options;
        AcpListener listener;
        switch (v.getId()){
            case R.id.smf_getSmsRightBtn:
                //开启短信权限
                options = new AcpOptions.Builder().setPermissions(
                        Manifest.permission.READ_SMS,
                        Manifest.permission.SEND_SMS).build();
                listener = new AcpListener() {
                    @Override
                    public void onGranted() {
                        Toast.makeText(getApplicationContext(),"已开启短信权限",Toast.LENGTH_LONG).show();
                        smsRightStatus = true;
                        if (!sendSms.getPhoneNumber() && Runtime.getPhoneNumber() == null){
                            sendSms.SendChaXunPhoneNumberSms();
                        }
                    }
                    @Override
                    public void onDenied(List<String> permissions) {
                        Toast.makeText(getApplicationContext(),"未开启短信权限",Toast.LENGTH_LONG).show();
                        smsRightStatus = false;
                    }
                };
                Acp.getInstance(this).request(options,listener);
                break;
            case R.id.smf_NextBtn:
                //跳转到获取悬浮窗权限页面
                if(Runtime.getPhoneNumber()!=null&&Runtime.getPhoneNumber().length()==11){
                    beginTransaction = fragmentManager.beginTransaction();
                    AlterWinFragment alterWinFragment;
                    alterWinFragment = new AlterWinFragment();
                    beginTransaction.replace(R.id.guide_frame,alterWinFragment);
                    beginTransaction.addToBackStack(null);
                    beginTransaction.commit();
                }else {
                    Toast.makeText(getApplicationContext(),"请先开启短信权限",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.awf_FinishBtn:
                ToastDemo.Show();
                radioGroup = (RadioGroup) findViewById(R.id.awf_radioGroup);
                textView = (TextView) findViewById(R.id.awf_textView);
                finishBtn = (Button) findViewById(R.id.awf_FinishBtn);

                radioGroup.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if(i == R.id.awf_radioYes){
                            alterRightStatus = true;
                            finishBtn.setText("完成引导");
                        }else{
                            alterRightStatus = false;
                            finishBtn.setText("设置权限");
                        }
                    }
                });
                if(finishBtn.getText().equals("完成引导")){
                    ToastDemo.Hide();
                    Runtime.setFirstTimeToFalse();
                    String phoneNumber = Runtime.getPhoneNumber();
                    if (phoneNumber != null) {
                        Toast.makeText(this, "本机号码为：" + phoneNumber, Toast.LENGTH_SHORT).show();
                    }
                    intent = new Intent(this, MainService.class);
                    startService(intent);
                    moveTaskToBack(isFinishing());
                }else{
                    options = new AcpOptions.Builder().setPermissions(
                            Manifest.permission.SYSTEM_ALERT_WINDOW).build();
                    listener = new AcpListener() {
                        @Override
                        public void onGranted() {
                        }
                        @Override
                        public void onDenied(List<String> permissions) {
                        }
                    };
                    Acp.getInstance(GuideActivity.this).request(options,listener);
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
