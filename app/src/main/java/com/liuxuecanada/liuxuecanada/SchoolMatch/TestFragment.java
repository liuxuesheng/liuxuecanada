package com.liuxuecanada.liuxuecanada.SchoolMatch;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuxuecanada.liuxuecanada.R;

public class TestFragment extends Fragment {
    OnLayoutCreateListener mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("asdh8sdd ", "A");
        //mCallback.updateLayout();
        Log.d("asdh8sdd ", "B");
        return inflater.inflate(R.layout.flow_main_double, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnLayoutCreateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLayoutCreateListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mCallback.updateLayout();

    }

    public interface OnLayoutCreateListener {
        void updateLayout();

    }

}
