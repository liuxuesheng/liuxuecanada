package com.liuxuecanada.liuxuecanada;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.SchoolMatch.ChoicesFeedbackActivity;
import com.liuxuecanada.liuxuecanada.SchoolMatch.EnterStudentChoicesActivity;

public class ArrayListFragment extends ListFragment {
    Activity activity;

    public static ArrayListFragment newInstance(String text) {
        ArrayListFragment f = new ArrayListFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pager_list, container, false);
        View tv = v.findViewById(R.id.text);
        ((TextView) tv).setText(getArguments().getString("msg"));
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, new String[]{"测评开始", "测评结果", "注册"}));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("FragmentList", "Item clicked: " + id);
        Intent myIntent = null;

        if (id == 0) {
            myIntent = new Intent(activity, EnterStudentChoicesActivity.class);
            myIntent.putExtra("test1",EnterStudentChoicesActivity.class);
        }
        else if (id == 1)
            myIntent = new Intent(activity, ChoicesFeedbackActivity.class);
        else if (id == 2) {
            myIntent = new Intent(activity, EnterStudentChoicesActivity.class);
            myIntent.putExtra("login",EnterStudentChoicesActivity.class);
        }
        activity.startActivity(myIntent);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }
}
