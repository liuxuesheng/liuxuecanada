package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.R;

public class FragmentIELTS extends Fragment {
    private static SeekBar seekBar;
    private static TextView ieltsScore;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_studentchoicesielts, container, false);
        createSeekBar();
        return v;
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
            }
        });

    }
}
