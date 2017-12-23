package com.example.developer.lorimobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.developer.lorimobile.R;
import com.example.developer.lorimobile.model.TimeEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class TimeEntryForSearchAdapter extends BaseAdapter {
    private Context context;
    private List<TimeEntry> timeEntryList;
    private LayoutInflater inflater;

    //private Calendar calendar;

    public TimeEntryForSearchAdapter(Context context, List<TimeEntry> timeEntryList) {
        this.context = context;
        this.timeEntryList = timeEntryList;
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //calendar = Calendar.getInstance();
    }
    @Override
    public int getCount() {
        return timeEntryList.size();
    }

    @Override
    public Object getItem(int position) {
        return timeEntryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_time_entry_in_search, viewGroup, false);
        final TimeEntry timeEntry = getTimeEntry(position);

        SimpleDateFormat sdfReverse = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            ((TextView) convertView.findViewById(R.id.tvDate)).setText(sdf.format(sdfReverse.parse(timeEntry.getDate())));
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
            ((TextView) convertView.findViewById(R.id.tvProject)).setText(timeEntry.getTask().getProject().getName());
            ((TextView) convertView.findViewById(R.id.tvTask)).setText(timeEntry.getTask().getName());
            String description=timeEntry.getDescription();
            if(description==null)
                description=context.getString(R.string.empty_field);
            ((TextView) convertView.findViewById(R.id.tvDescription)).setText(description);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date startDate = new Date(record.getTimeStart());
        String strStartDate = sdf.format(startDate);

        Date duration = new Date(record.getDuration());
        String strDuration = sdf.format(duration);*/

        return convertView;
    }

    public TimeEntry getTimeEntry(int position){
        return ((TimeEntry) getItem(position));
    }

    public List<TimeEntry> getRecordList() {
        return timeEntryList;
    }

    public void setRecordList(List<TimeEntry> timeEntryList) {
        this.timeEntryList = timeEntryList;
    }
}
