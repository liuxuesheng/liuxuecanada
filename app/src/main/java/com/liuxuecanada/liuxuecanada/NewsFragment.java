package com.liuxuecanada.liuxuecanada;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.CustomizedComponent.ListViewItemComponent.ContentItem;
import com.liuxuecanada.liuxuecanada.CustomizedComponent.ListViewItemComponent.ListAdapter;
import com.liuxuecanada.liuxuecanada.SchoolMatch.ChoicesFeedbackActivity;
import com.liuxuecanada.liuxuecanada.SchoolMatch.ChoicesFeedbackItem;
import com.liuxuecanada.liuxuecanada.SchoolMatch.FeedbackViewAdapter;
import com.liuxuecanada.liuxuecanada.Utils.PaintService;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    Activity activity;
    private List<ChoicesFeedbackItem> choicesFeedbackItems;
    private ListView lv;

    public static NewsFragment newInstance(String text) {
        NewsFragment f = new NewsFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*View v = inflater.inflate(R.layout.fragment_pager_news, container, false);
        TextView tv = (TextView) v.findViewById(R.id.text_news);
        ((TextView) tv).setText(getArguments().getString("msg"));

        ViewPager mPager = (ViewPager) v.findViewById(R.id.pager_news);
        NewsImageAdapter newsAdapter = new NewsImageAdapter(((FragmentActivity)activity).getSupportFragmentManager());
        mPager.setAdapter(newsAdapter);
        mPager.setCurrentItem(1);*/


        View v = inflater.inflate(R.layout.fragment_pager_news, container, false);

        //Build news image view
        ViewPager mPager = (ViewPager) v.findViewById(R.id.pager_news);
        NewsImageAdapter newsAdapter = new NewsImageAdapter(((FragmentActivity)activity).getSupportFragmentManager());
        mPager.setAdapter(newsAdapter);
        mPager.setCurrentItem(1);

        //Build news list view
        lv = (ListView) v.findViewById(R.id.list_news);

        ArrayList<ContentItem> objects = new ArrayList<ContentItem>();
        for (int i = 1 ; i<= 5; i++) {
            objects.add(new ContentItem("Our news story #"+i, PaintService.paintTextIconDrawable(getActivity(), "N")));
        }
        ListAdapter adapter = new ListAdapter(getActivity(), objects);
        lv.setAdapter(adapter);
        lv.setVerticalScrollBarEnabled(true);
        lv.setScrollbarFadingEnabled(false);
        lv.setBackgroundColor(Color.TRANSPARENT);
        lv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

}
