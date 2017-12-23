package com.example.developer.lorimobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.developer.lorimobile.R;
import com.example.developer.lorimobile.model.TimeEntry;

import java.util.List;

public class TimeEntryAdapter extends BaseAdapter {

    private Context context;
    private List<TimeEntry> timeEntryList;
    private LayoutInflater inflater;

    public TimeEntryAdapter(Context context, List<TimeEntry> timeEntryList) {
        this.context = context;
        this.timeEntryList = timeEntryList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return timeEntryList.size();
    }

    @Override
    public Object getItem(int i) {
        return timeEntryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_time_entry, viewGroup, false);
        }

        final TimeEntry timeEntry = getTimeEntry(i);
        int hours=(int)timeEntry.getTimeInHours();
        int minutes=timeEntry.getTimeInMinutes()-hours*60;
        String duration = "";
        if(hours==0)
            duration="00:";
        else
            duration=hours+":";
        if(minutes==0)
            duration+="00";
        else duration+=minutes;

        ((TextView) convertView.findViewById(R.id.tvDuration)).setText(duration);
        //((TextView) convertView.findViewById(R.id.tvDuration)).setText(" "+String.valueOf(timeEntry.getTimeInHours()));
        ((TextView) convertView.findViewById(R.id.tvProject)).setText(timeEntry.getTask().getProject().getName());
        ((TextView) convertView.findViewById(R.id.tvTask)).setText(timeEntry.getTask().getName());
        return convertView;

    }

    public TimeEntry getTimeEntry(int position) {
        return ((TimeEntry) getItem(position));
    }

    public List<TimeEntry> getTimeEntryList() {
        return timeEntryList;
    }

    public void setTimeEntryList(List<TimeEntry> timeEntryList) {
        this.timeEntryList = timeEntryList;
    }
}
