package com.zfliu.fallingsnow.View;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zfliu.fallingsnow.R;

/**
 * Created by Jacky on 2016/12/20.
 */

public class FragmentMakeCard extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_makecard,container,false);
    }
}
