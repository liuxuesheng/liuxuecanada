package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.R;

import java.util.List;


public class FeedbackViewAdapter extends RecyclerView.Adapter<FeedbackViewAdapter.FeedbackViewHolder> {

    private List<ChoicesFeedbackItem> choicesFeedbackItems;
    private Context context;

    public FeedbackViewAdapter(List<ChoicesFeedbackItem> choiceFeedbacks, Context context) {
        this.choicesFeedbackItems = choiceFeedbacks;
        this.context = context;
    }

    @Override
    public FeedbackViewAdapter.FeedbackViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.cardview_choicesfeedback_item, viewGroup, false);
        FeedbackViewHolder nvh = new FeedbackViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(FeedbackViewAdapter.FeedbackViewHolder personViewHolder, int i) {

        final int j = i;
        String chartName;
        personViewHolder.feedbackItem_photo.setImageResource(choicesFeedbackItems.get(i).getPhotoId());
        personViewHolder.feedbackItem_title.setText(choicesFeedbackItems.get(i).getTitle());
        personViewHolder.feedbackItem_desc.setText(choicesFeedbackItems.get(i).getDesc());

        //为btn_share btn_readMore cardView设置点击事件
        switch (i) {
            case 0:
                chartName = "PieChart";
                break;
            case 1:
                chartName = "RadarChart";
                break;
            case 2:
                chartName = "BarChart";
                break;
            case 3:
                chartName = "HorizontalBarChart";
                break;
            default:
                chartName = "HorizontalBarChart";
        }
        final String postion = chartName;
        personViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChoicesFeedbackDetailActivity.class);
                intent.putExtra(postion, ChoicesFeedbackDetailActivity.class);
                context.startActivity(intent);

            }
        });
        personViewHolder.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChoicesFeedbackDetailActivity.class);
                intent.putExtra(postion, ChoicesFeedbackDetailActivity.class);
                context.startActivity(intent);
            }
        });

        personViewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, choicesFeedbackItems.get(j).getDesc());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(intent, choicesFeedbackItems.get(j).getTitle()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return choicesFeedbackItems.size();
    }

    //自定义ViewHolder类
    static class FeedbackViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView feedbackItem_photo;
        TextView feedbackItem_title;
        TextView feedbackItem_desc;
        Button share;
        Button readMore;

        public FeedbackViewHolder(final View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.feedbackItem_cardview);
            feedbackItem_photo = (ImageView) itemView.findViewById(R.id.feedbackItem_photo);
            feedbackItem_title = (TextView) itemView.findViewById(R.id.feedbackItem_title);
            feedbackItem_desc = (TextView) itemView.findViewById(R.id.feedbackItem_desc);
            share = (Button) itemView.findViewById(R.id.btn_share);
            readMore = (Button) itemView.findViewById(R.id.btn_more);
            //设置TextView背景为半透明
            feedbackItem_title.setBackgroundColor(Color.argb(20, 0, 0, 0));

        }

    }
}
