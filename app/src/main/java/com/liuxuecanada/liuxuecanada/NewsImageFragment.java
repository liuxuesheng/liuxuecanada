package com.liuxuecanada.liuxuecanada;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NewsImageFragment extends Fragment {

    public static NewsImageFragment newInstance(String text) {
        NewsImageFragment f = new NewsImageFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pager_news_images, container, false);

        /*View tv = v.findViewById(R.id.text);
        ((TextView) tv).setText(getArguments().getString("msg"));*/
        Log.d("sad8dn ","1");
        return v;
    }
}
