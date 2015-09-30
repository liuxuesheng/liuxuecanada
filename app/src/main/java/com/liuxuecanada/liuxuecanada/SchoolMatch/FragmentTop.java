package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.R;

public class FragmentTop extends Fragment {

    OnProgressCirclePageUpdateListener mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_studentchoicestop, container, false);
        return v;
    }

    // Container Activity must implement this interface
    public interface OnProgressCirclePageUpdateListener {
        void updateProgressCircle();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnProgressCirclePageUpdateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnProgressCirclePageUpdateListener");
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        mCallback.updateProgressCircle();
    }
}
