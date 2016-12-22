package com.zfliu.fallingsnow.Utils;

import com.zfliu.fallingsnow.CtxApplication;
import com.zfliu.fallingsnow.R;

/**
 * Created by zfliu on 12/20/2016.
 */

public class Resource {
    public static String getString(int id){
        return CtxApplication.getContext().getResources().getString(id);
    }

    public static String defaultGreeting(){
        return getString(R.string.greeting);
    }
}
