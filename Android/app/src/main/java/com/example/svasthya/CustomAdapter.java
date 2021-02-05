package com.example.svasthya;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Upload> implements View.OnClickListener{

    private ArrayList<Upload> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView name;
        TextView url;
    }

    public CustomAdapter(ArrayList<Upload> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Upload dataModel=(Upload)object;
        Toast.makeText(this.mContext, dataModel.getUrl(), Toast.LENGTH_LONG).show();

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dataModel.getUrl()));
        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.mContext.startActivity(browserIntent);

//        this.mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.stackoverflow.com")));

//        switch (v.getId())
//        {
//            case R.id.url:
////                Snackbar.make(v, "Release date " +Upload.getFeature(), Snackbar.LENGTH_LONG)
////                        .setAction("No action", null).show();
//                this.mContext.startActivity(browserIntent);
//                break;
//        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Upload dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.url = (TextView) convertView.findViewById(R.id.url);
//            viewHolder.url.setOnClickListener(this);
//            viewHolder.url.setTag(position);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.name.setText(dataModel.getName());
        viewHolder.url.setText(dataModel.getUrl());
        viewHolder.url.setOnClickListener(this);
        viewHolder.url.setTag(position);

        // Return the completed view to render on screen
        return convertView;
    }
}