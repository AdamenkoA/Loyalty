package com.example.adamenko.loyalty.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Adamenko on 11.08.2016.
 */

public class ListViewAdapter extends BaseAdapter {
    Activity context;
    String title[];
    String description[];

    public ListViewAdapter(Activity context, String[] title, String[] description) {
        super();
        this.context = context;
        this.title = title;
        this.description = description;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return title.length;
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        TextView txtViewTitle;
        TextView txtViewDescription;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();

//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.list_item_themes, parent, false);
//            holder = new ViewHolder();
//            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.tv_number_calendar);
//            holder.txtViewDescription = (TextView) convertView.findViewById(R.id.tv_number_calendar);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }

    //    holder.txtViewTitle.setText(title[position]);
       // holder.txtViewDescription.setText(description[position]);

        return convertView;
    }
}
