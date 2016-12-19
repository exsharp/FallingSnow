package com.zfliu.fallingsnow.Utils;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;

import java.lang.reflect.Method;

/**
 * 操作权限判断工具类
 */

public class JudgeOpsRight {
    /**
     *  检查是否获得悬浮窗权限
     * @param context
     * @param op
     * @return
     */
    //public static final int OP_READ_SMS = 14;
    //public static final int OP_RECEIVE_SMS = 16;
    //public static final int OP_SEND_SMS = 20;
    //public static final int OP_SYSTEM_ALERT_WINDOW=24   op = 24
    public boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;

        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {

                Class<?> spClazz = Class.forName(manager.getClass().getName());
                Method method = manager.getClass().getDeclaredMethod("checkOp", int.class, int.class, String.class);
                int property = (Integer) method.invoke(manager, op,
                        Binder.getCallingUid(), context.getPackageName());
                if (AppOpsManager.MODE_ALLOWED == property) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("系统版本过低");
            return false;
        }
        return true;
    }
}
