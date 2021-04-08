package com.example.inframindfinals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomChatAdapter extends ArrayAdapter<ChatMessage> {
    private ArrayList<ChatMessage> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView rightText;
        TextView leftText;
    }
    public CustomChatAdapter(ArrayList<ChatMessage> data, Context context) {
        super(context, R.layout.msglist, data);
        this.dataSet = data;
        this.mContext=context;

    }
//    public void onClick(View v) {
//
//        int position=(Integer) v.getTag();
//        Object object= getItem(position);
//        ChatMessage dataModel=(ChatMessage) object;
//
//        switch (v.getId())
//        {
////            case R.id.item_info:
////                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
////                        .setAction("No action", null).show();
////                break;
//        }
//    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ChatMessage dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.msglist, parent, false);
            viewHolder.rightText = (TextView) convertView.findViewById(R.id.leftText);
            viewHolder.leftText = (TextView) convertView.findViewById(R.id.rightText);



            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        lastPosition = position;
        if(dataModel.getMsgUser().equals("")){
            viewHolder.leftText.setVisibility(View.GONE);
        }
        viewHolder.rightText.setText(dataModel.getMsgText());
        viewHolder.leftText.setText(dataModel.getMsgUser());

        // Return the completed view to render on screen
        return convertView;
    }
}
