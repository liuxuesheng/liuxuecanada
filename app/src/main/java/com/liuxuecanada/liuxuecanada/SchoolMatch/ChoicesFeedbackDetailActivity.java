package com.liuxuecanada.liuxuecanada.SchoolMatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuxuecanada.liuxuecanada.R;


public class ChoicesFeedbackDetailActivity extends Activity{
    private ImageView feedbackItemPhoto;
    private TextView feedbackItemTitle;
    private TextView feedbackItemDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choicesfeedback_detail);

        feedbackItemPhoto= (ImageView) findViewById(R.id.feedbackItem_info_photo);
        feedbackItemTitle= (TextView) findViewById(R.id.feedbackItem_info_title);
        feedbackItemDesc= (TextView) findViewById(R.id.feedbackItem_info_desc);

        Intent intent=getIntent();

        ChoicesFeedbackItem item= (ChoicesFeedbackItem) intent.getSerializableExtra("ChoicesFeedbackItem");
        feedbackItemPhoto.setImageResource(item.getPhotoId());
        feedbackItemTitle.setText(item.getTitle());
        feedbackItemDesc.setText(item.getDesc());

    }
}
