package com.example.svasthya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter2 extends ArrayAdapter<DataModel> implements View.OnClickListener {

    private ArrayList<DataModel> dataSet;
    Context mContext;

    @Override
    public void onClick(View v) {

    }

    // View lookup cache
    private static class ViewHolder {
        TextView time;
        TextView date;
        TextView diabetes_textview;
        TextView bronchi_textview;
        TextView hypoxemia_textview;
        TextView asthma_textview;
        TextView chd_textview;
        TextView stress_textview;
    }

    public CustomAdapter2(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.row_item2, data);
        this.dataSet = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        CustomAdapter2.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new CustomAdapter2.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item2, parent, false);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time_yo);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date_yo);
            viewHolder.asthma_textview = (TextView) convertView.findViewById(R.id.asthma);
            viewHolder.diabetes_textview = (TextView) convertView.findViewById(R.id.diabetes);
            viewHolder.hypoxemia_textview = (TextView) convertView.findViewById(R.id.hypoxemia);
            viewHolder.chd_textview = (TextView) convertView.findViewById(R.id.chd);
            viewHolder.bronchi_textview = (TextView) convertView.findViewById(R.id.bronchi);
            viewHolder.stress_textview = (TextView) convertView.findViewById(R.id.stress);
//            viewHolder.url.setOnClickListener(this);
            viewHolder.date.setTag(position);


            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomAdapter2.ViewHolder) convertView.getTag();
            result=convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.time.setText(dataModel.getDiabetes());
        viewHolder.date.setText(dataModel.getBronchi());
        viewHolder.stress_textview.setText(dataModel.getStress());
        viewHolder.asthma_textview.setText(dataModel.getAsthma());
        viewHolder.chd_textview.setText(dataModel.getChd());
        viewHolder.hypoxemia_textview.setText(dataModel.getHypoxemia());
        viewHolder.bronchi_textview.setText(dataModel.getTime());
        viewHolder.diabetes_textview.setText(dataModel.getDate());
//        viewHolder.url.setOnClickListener(this);
        viewHolder.time.setTag(position);

        // Return the completed view to render on screen
        return convertView;
    }

}
