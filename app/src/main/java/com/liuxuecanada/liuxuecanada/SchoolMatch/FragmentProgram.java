package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.liuxuecanada.liuxuecanada.R;

public class FragmentProgram extends Fragment {

    OnTextColorUpdateListener mCallback;
    int colorCode = 0;
    View v = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_studentchoicesprogram, container, false);
        return v;
    }

    // Container Activity must implement this interface
    public interface OnTextColorUpdateListener {
        int updateTextColor();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnTextColorUpdateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTextColorUpdateListener");
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        colorCode = mCallback.updateTextColor();
        Button bt = (Button) v.findViewById(R.id.arts_button);
        bt.setTextColor(colorCode);

    }


}