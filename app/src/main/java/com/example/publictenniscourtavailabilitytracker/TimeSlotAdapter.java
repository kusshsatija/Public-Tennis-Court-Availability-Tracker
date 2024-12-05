package com.example.publictenniscourtavailabilitytracker;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TimeSlotAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> timeSlots;
    private final List<Boolean> availability;

    public TimeSlotAdapter(Context context, int resource, List<String> timeSlots, List<Boolean> availability) {
        super(context, resource, timeSlots);
        this.context = context;
        this.timeSlots = timeSlots;
        this.availability = availability;
    }

    @Override
    public boolean isEnabled(int position) {
        boolean enabled = availability.get(position);
        Log.d("TimeSlotAdapter", "Position " + position + " is " + (enabled ? "enabled" : "disabled"));
        return enabled;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView textView = (TextView) view;

        if (!availability.get(position)) {
            // Grey out unavailable timings
            textView.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        } else {
            // Restore color for available timings
            textView.setTextColor(context.getResources().getColor(android.R.color.black));
        }

        return view;
    }

}