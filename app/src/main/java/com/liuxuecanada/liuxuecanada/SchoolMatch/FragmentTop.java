package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.R;

public class FragmentTop extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_studentchoicestop, container, false);
        updateProgressBar(v);
        return v;
    }

    private void updateProgressBar(View view) {
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressbarid);
        progressBar.setProgress(30);
        TextView textProgress = (TextView) view.findViewById(R.id.textView1);
        textProgress.setText("1/5");
    }
}