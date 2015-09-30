package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.R;

public class FragmentIELTS extends Fragment {
    private static SeekBar seekBar;
    private static TextView ieltsScore;
    private static Button proceedButton;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_studentchoicesielts, container, false);
        createSeekBar();
        return v;
    }

    OnSeekBarUpdateListener mCallback;

    // Container Activity must implement this interface
    public interface OnSeekBarUpdateListener {
        void updateProceedButton();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnSeekBarUpdateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSeekBarUpdateListener");
        }
    }


    public void createSeekBar() {
        seekBar = (SeekBar) v.findViewById(R.id.intensitySlider);
        ieltsScore = (TextView) v.findViewById(R.id.select_ieltsscore_textview);
        ieltsScore.setText("Score: " + seekBar.getProgress());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int score = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                score = progress;
                ieltsScore.setText("Score: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ieltsScore.setText("Score: " + score);
                mCallback.updateProceedButton();
            }
        });

    }
}
