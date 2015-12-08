package com.liuxuecanada.liuxuecanada;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liuxuecanada.liuxuecanada.Utils.AsyncResponse;

public class FrontPageForumFragment extends Fragment
        implements
        AsyncResponse {
    private Activity activity;

    public static FrontPageForumFragment newInstance(String text) {
        FrontPageForumFragment f = new FrontPageForumFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pager_forum, container, false);

        return v;
    }

    @Override
    public void onTaskComplete(Object out) {

    }

    @Override
    public void onTaskStart() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }
}
