package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.liuxuecanada.liuxuecanada.R;

public class FragmentGPA extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_studentchoicesgpa, container, false);
        startAuto();
        return v;
    }

    private void
    startAuto(){
        ObjectAnimator animation = ObjectAnimator.ofInt ( R.layout.fragment_studentchoicesgpa, "progress", 1, 100);
        animation.setDuration (5000); //in milliseconds
        animation.setInterpolator (new DecelerateInterpolator());
        animation.start ();
    }
}
