package com.zfliu.fallingsnow.Tools;

import com.zfliu.fallingsnow.CtxApplication;
import com.zfliu.fallingsnow.R;
import com.zfliu.fallingsnow.Utils.Random;

/**
 * Created by zfliu on 12/20/2016.
 */

public class Resource {
    public static String getString(int id){
        return CtxApplication.getContext().getResources().getString(id);
    }

    private static int[] gifRes = {
            R.raw.s2,
            R.raw.s4,
            R.raw.s5,
            R.raw.s6,
            R.raw.s7,
            R.raw.s8,
    };
    public static int gifResource(){
        int idx = Random.getRandom(0,gifRes.length);
        return gifRes[idx];
    }

    public static String defaultGreeting(){
        return getString(R.string.greeting);
    }
}
