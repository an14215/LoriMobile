package com.example.developer.lorimobile.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.developer.lorimobile.R;
import com.example.developer.lorimobile.model.User;

import java.util.Calendar;


public class WeekFragment extends Fragment {

    private User user;
    private Calendar calendar;

    public WeekFragment() {

    }

    public static WeekFragment newInstance(User user, Calendar dayDate) {
        Bundle bundle = new Bundle(1);
        bundle.putSerializable("day", dayDate);
        bundle.putSerializable("user",user);

        WeekFragment fragment = new WeekFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        calendar = (Calendar) getArguments().getSerializable("day");
        user = (User) getArguments().getSerializable("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_week, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentMonday, createDayFragment(0)); // 0 - day of week.

        transaction.replace(R.id.fragmentTuesday, createDayFragment(1));
        transaction.replace(R.id.fragmentWednesday, createDayFragment(2));
        transaction.replace(R.id.fragmentThursday, createDayFragment(3));
        transaction.replace(R.id.fragmentFriday, createDayFragment(4));
        transaction.replace(R.id.fragmentSaturday, createDayFragment(5));
        transaction.replace(R.id.fragmentSunday, createDayFragment(6));

        transaction.commit();
    }

    private DayFragment createDayFragment(int dayOfWeek) {
        Calendar calendarForDay = Calendar.getInstance();
        calendarForDay.setTime(calendar.getTime());
        calendarForDay.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        calendarForDay.add(Calendar.DAY_OF_WEEK, dayOfWeek);
       return DayFragment.newInstance(calendarForDay, user);
    }



}
