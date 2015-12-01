package com.liuxuecanada.liuxuecanada;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.SchoolMatch.ChoicesFeedbackActivity;
import com.liuxuecanada.liuxuecanada.SchoolMatch.ChoicesFeedbackItem;
import com.liuxuecanada.liuxuecanada.SchoolMatch.FeedbackViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    Activity activity;
    private List<ChoicesFeedbackItem> choicesFeedbackItems;
    private RecyclerView recyclerView;

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


        View v = inflater.inflate(R.layout.activity_choicesfeedback, container, false);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView= (RecyclerView) v.findViewById(R.id.choices_feedback_View);

        initPersonData();
        FeedbackViewAdapter adapter=new FeedbackViewAdapter(choicesFeedbackItems,getActivity());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return v;
    }

    private void initPersonData() {
        choicesFeedbackItems =new ArrayList<>();

        choicesFeedbackItems.add(new ChoicesFeedbackItem("饼图展览", "分析选校情况和申报每所学校的概率!", R.drawable.feedback1));
        choicesFeedbackItems.add(new ChoicesFeedbackItem("雷达图展览", "通过和其他学生对比，分析考生的优点缺点", R.drawable.feedback2));
        choicesFeedbackItems.add(new ChoicesFeedbackItem("柱状图展览", "数据年份分析!", R.drawable.feedback3));
        choicesFeedbackItems.add(new ChoicesFeedbackItem("横向柱状图展览", "数据年份分析!", R.drawable.feedback4));

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
