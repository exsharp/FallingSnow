package com.zfliu.fallingsnow.Porfermor;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zfliu.fallingsnow.R;
import com.zfliu.fallingsnow.Utils.JudgeOpsRight;
import com.zfliu.fallingsnow.View.AlterWinFragment;
import com.zfliu.fallingsnow.View.SmsFragment;

/**
 * 引导界面类
 */

public class GuideActivity extends AppCompatActivity {
    Intent intent = null;
    JudgeOpsRight judgeOpsRight;
    android.app.FragmentManager fragmentManager = null;
    android.app.FragmentTransaction beginTransaction = null;
    AlterWinFragment alterWinFragment;
    SmsFragment smsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);

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
}
