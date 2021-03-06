package com.liuxuecanada.liuxuecanada.CustomizedComponent.ListViewItemComponent;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.liuxuecanada.liuxuecanada.R;
import com.liuxuecanada.liuxuecanada.Utils.JSONToComponentService;

import java.util.List;


public class ListAdapter extends ArrayAdapter<ContentItem> {

    private int index = -1;

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

        holder.listButton.setCompoundDrawablesWithIntrinsicBounds(c.drawable, null, null, null);
        holder.listButton.setCompoundDrawablePadding(16);

        holder.listButton.setText(c.name);
        holder.listButton.setTextSize(15);
        holder.listButton.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        holder.listButton.setBackgroundColor(Color.TRANSPARENT);
        holder.listButton.setFocusable(false);
        holder.listButton.setClickable(false);
        holder.listButton.setFocusableInTouchMode(false);

        if(position == getIndex()){
            convertView.setBackgroundColor(Color.rgb(255,165,0));
        } else{
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }

    private class ViewHolder {
        Button listButton;
    }

    public int getIndex(){
        return this.index;
    }

    public void setIndex(int indexNumber){
        this.index = indexNumber;
    }
}

