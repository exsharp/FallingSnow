package com.zfliu.fallingsnow.View;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfliu.fallingsnow.R;

/**
 * Created by Jacky on 2016/12/19.
 */

public class AlterWinFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alterwin_fragment,container,false);
        return view;
    }
}
