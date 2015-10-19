package com.liuxuecanada.liuxuecanada.CustomizedComponent.ListViewItemComponent;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.liuxuecanada.liuxuecanada.R;

import java.util.List;


public class ListAdapter extends ArrayAdapter<ContentItem> {

    public ListAdapter(Context context, List<ContentItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ContentItem c = getItem(position);

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.button_list, null);
            holder.listButton = (Button) convertView.findViewById(R.id.listButton);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

 /*       RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.TOP;*/


        holder.listButton.setCompoundDrawablesWithIntrinsicBounds(c.drawable, null, null, null);
        holder.listButton.setCompoundDrawablePadding(16);




        holder.listButton.setText(c.name);
        holder.listButton.setTextSize(16);
        holder.listButton.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        holder.listButton.setBackgroundColor(Color.WHITE);
        holder.listButton.setBackgroundColor(Color.YELLOW);


        return convertView;
    }

    private class ViewHolder {
        Button listButton;
    }
}

