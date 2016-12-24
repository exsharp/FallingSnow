package com.zfliu.fallingsnow.Porfermor;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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
import com.zfliu.fallingsnow.View.MyViewFliper;
import com.zfliu.fallingsnow.View.SmsFragment;


import java.util.List;

/**
 * 引导界面类
 */

public class GuideActivity extends AppCompatActivity {
    private Intent intent = null;
    private Intent SMSServiceIntent = null;
    private Handler handler = new Handler();
    private android.app.FragmentManager fragmentManager = null;
    private android.app.FragmentTransaction beginTransaction = null;

    private RadioGroup radioGroup;
    private TextView textView;
    private ImageView awfImageView;
    private TextView setTextView;
    private MyViewFliper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);

        SmsFragment smsFragment;
        smsFragment = new SmsFragment();
        fragmentManager = getFragmentManager();
        beginTransaction = fragmentManager.beginTransaction();
        beginTransaction.add(R.id.guide_frame,smsFragment);
        beginTransaction.commit();

        viewFlipper = (MyViewFliper) findViewById(R.id.guide_viewFlipper);
        viewFlipper.addImageView();

        String text = "因为这个APP要一点权限才能有效果，希望你能看看这里的权限设置引导，" +
                "看完点击右上角去设置(例子中是小米的，好多东西要弄，如果有通知类短信的话记得允许)，" +
                "此外如果有类似于“信任这个APP”的选项希望能勾上,APP绝对没有流氓行为= =\n"+
                "需要这些权限：\n1、读、写短信（小米还有一个通知类短信选项(有的话)）\n2、悬浮窗";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setMessage(text).setTitle("提示")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create();
        dialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(SMSServiceIntent);
    }

    private void stepSMS(){
        //开启短信权限
        SMSServiceIntent = new Intent(GuideActivity.this,SMSService.class);
        startService(SMSServiceIntent);

        AcpOptions options = new AcpOptions.Builder().setPermissions(
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS).build();
        AcpListener listener = new AcpListener() {
            @Override
            public void onGranted() {
                SendSms sendSms = new SendSms(CtxApplication.getContext());
                if (!sendSms.getPhoneNumber() && Runtime.getPhoneNumber() == null){
                    sendSms.SendChaXunPhoneNumberSms();
                }
            }
            @Override
            public void onDenied(List<String> permissions) {
            }
        };
        Acp.getInstance(this).request(options,listener);
        handler.post(waitForSMS);
    }

    private Runnable waitForSMS = new Runnable() {
        @Override
        public void run() {
            boolean ret = stepGetNum(2);
            if (!ret){
                handler.postDelayed(this,5000);
            }
        }
    };

    private boolean stepGetNum(int i){
        //跳转到获取悬浮窗权限页面
        if(Runtime.getPhoneNumber()!=null&&Runtime.getPhoneNumber().length()==11){
            Toast.makeText(this,"本机号码："+Runtime.getPhoneNumber(),Toast.LENGTH_SHORT).show();
            beginTransaction = fragmentManager.beginTransaction();
            AlterWinFragment alterWinFragment;
            alterWinFragment = new AlterWinFragment();
            beginTransaction.replace(R.id.guide_frame,alterWinFragment);
            beginTransaction.addToBackStack(null);
            beginTransaction.commit();
            return true;
        }else {
            switch (i){
                case 1:
                    Toast.makeText(getApplicationContext(),"未获取本机号码",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(),"正在获得本机号码，等会噢",Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    }

    private void stepFinish(){
        ToastDemo.Show();
        radioGroup = (RadioGroup) findViewById(R.id.awf_radioGroup);
        textView = (TextView) findViewById(R.id.awf_textView);
        setTextView = (TextView) findViewById(R.id.awf_setTextView);
        awfImageView = (ImageView) findViewById(R.id.awf_imageView);

        radioGroup.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.awf_radioYes){
                    ToastDemo.Hide();
                    setTextView.setText("完成引导");
                    awfImageView.setBackgroundResource(R.drawable.christmashead);
                    setTextView.setTextColor(getResources().getColor(R.color.colorChristmasRed));

                }else{
                    setTextView.setText("设置权限");
                    awfImageView.setBackgroundResource(R.drawable.next);
                    setTextView.setTextColor(getResources().getColor(R.color.colorGray));
                    AcpOptions options = new AcpOptions.Builder().setPermissions(
                            Manifest.permission.SYSTEM_ALERT_WINDOW).build();
                    AcpListener listener = new AcpListener() {
                        @Override
                        public void onGranted() {
                        }
                        @Override
                        public void onDenied(List<String> permissions) {
                        }
                    };
                    Acp.getInstance(GuideActivity.this).request(options,listener);
                }
            }
        });
        if(setTextView.getText().equals("完成引导")){
            ToastDemo.Hide();
            Runtime.setFirstTimeToFalse();
            intent = new Intent(this, MainService.class);
            startService(intent);
            moveTaskToBack(true);
            finish();
        }
    }
    
    public void doClick(View v){
        switch (v.getId()){
            case R.id.smf_getSmsRightBtn:
                stepSMS();
                break;
            case R.id.smf_NextBtn:
                stepGetNum(1);
                break;
            case R.id.awf_FinishBtn:
                stepFinish();
                break;
            case R.id.guide_JumpBtn:
                Runtime.jumpToSetting(GuideActivity.this);
//                Runtime.setFirstTimeToFalse();
//                intent = new Intent(this,MainActivity.class);
//                startActivity(intent);
//                finish();
            default:
                break;
        }
        ToastDemo.Hide();
    }

    private void makeText(String text,int i){
        Toast.makeText(this,text, i).show();
    }
}
