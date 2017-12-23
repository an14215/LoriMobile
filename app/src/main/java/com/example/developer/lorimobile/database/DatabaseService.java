package com.example.developer.lorimobile.database;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.constraint.solver.Goal;
import android.support.v4.content.LocalBroadcastManager;

import com.example.developer.lorimobile.model.TimeEntry;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by developer on 23.12.2017.
 */

public class DatabaseService extends IntentService {

    private final static String TIME_ENTRY_LIST_FOR_DAY = "TIME_ENTRY_LIST_FOR_DAY";
    private final static String TIME_ENTRY_LIST_FOR_SEARCH="TIME_ENTRY_LIST_FOR_SEARCH";

    private final static String KEY_ACTION = "KEY_ACTION";

    private DatabaseHelper dbHelper;

    public DatabaseService() {
        super("DatabaseService");
    }

    public DatabaseService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DatabaseHelper(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case "insertList":
                    try {
                    HelperFactory.getHelper().getTimeEntryDAO().deleteTimeEntryOfDay(intent.getStringExtra("date"));
                    List<TimeEntry> timeEntryList = ( List<TimeEntry>) intent.getSerializableExtra("timeEntryList");
                    for(TimeEntry timeEntry:timeEntryList) {
                        if(!HelperFactory.getHelper().getUserDAO().idExists(timeEntry.getUser().getId()))
                            HelperFactory.getHelper().getUserDAO().create(timeEntry.getUser());
                        if(!HelperFactory.getHelper().getProjectDAO().idExists(timeEntry.getTask().getProject().getId()))
                            HelperFactory.getHelper().getProjectDAO().create(timeEntry.getTask().getProject());
                        if(!HelperFactory.getHelper().getTaskDAO().idExists(timeEntry.getTask().getId()))
                            HelperFactory.getHelper().getTaskDAO().create(timeEntry.getTask());
                        if(!HelperFactory.getHelper().getTimeEntryDAO().idExists(timeEntry.getId()))
                            HelperFactory.getHelper().getTimeEntryDAO().create(timeEntry);
                        else  HelperFactory.getHelper().getTimeEntryDAO().update(timeEntry);
                    }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    break;
                case "getAllOfDay":
                    try {
                    List<TimeEntry> timeEntryAllList = HelperFactory.getHelper().getTimeEntryDAO().getAllTimeEntryOfDay(intent.getStringExtra("date"),intent.getStringExtra("userId"));
                    Intent intentToActivity = new Intent(TIME_ENTRY_LIST_FOR_DAY);
                    intentToActivity.putExtra("timeEntryList", (Serializable) timeEntryAllList);
                    intentToActivity.putExtra("date",intent.getStringExtra("date"));
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intentToActivity);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    break;
                case "findTimeEntry":
                    try {
                    List<TimeEntry> timeEntryFindList = HelperFactory.getHelper().getTimeEntryDAO().findTimeEntry(intent.getStringExtra("dateStart"),intent.getStringExtra("dateEnd"),intent.getStringExtra("userId"),intent.getStringExtra("searchString"));
                    Intent intentToSearchActivity = new Intent(TIME_ENTRY_LIST_FOR_SEARCH);
                    intentToSearchActivity.putExtra("timeEntryList", (Serializable) timeEntryFindList);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intentToSearchActivity);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
            }

            dbHelper.close();
        }

    }


}