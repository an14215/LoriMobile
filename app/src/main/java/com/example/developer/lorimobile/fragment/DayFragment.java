package com.example.developer.lorimobile.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.developer.lorimobile.R;
import com.example.developer.lorimobile.activity.AddRecordActivity;
import com.example.developer.lorimobile.activity.AuthenticationActivity;
import com.example.developer.lorimobile.adapter.TimeEntryAdapter;
import com.example.developer.lorimobile.database.DatabaseService;
import com.example.developer.lorimobile.model.TimeEntry;
import com.example.developer.lorimobile.model.User;
import com.example.developer.lorimobile.rest.APIFactory;
import com.example.developer.lorimobile.rest.APIService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DayFragment extends Fragment {

    private static final String TOKEN_PREFERENCE = "TOKEN_PREFERENCE";
    private static final String TOKEN_KEY = "TOKEN_KEY";

    private final static String TIME_ENTRY_LIST_FOR_DAY = "TIME_ENTRY_LIST_FOR_DAY";

    private final static int ADD_NEW_TIME_ENTRY = 0;
    private final static int SUCCESSFUL_ADD_NEW_TIME_ENTRY = 1;

    private Calendar calendar;
    private View view;
    private TextView tvNameDay;
    private TextView tvNumDayOfMonth;
    private ImageView ivAdd;
    private ListView lvTimeEntry;
    private User user;

    private APIService service;

    private String token;

    private List<TimeEntry> timeEntryList;

    public DayFragment() {
    }

    public static DayFragment newInstance(Calendar dayDate, User user) {
        Bundle bundle = new Bundle(1);
        bundle.putSerializable("day", dayDate);
        bundle.putSerializable("user", user);

        SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM");
        String str = sdfDate.format(dayDate.getTime());

        DayFragment fragment = new DayFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (User) getArguments().getSerializable("user");
        calendar = (Calendar) getArguments().getSerializable("day");
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM");
        String str = sdfDate.format(calendar.getTime());

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        service = new APIFactory().getAPIService();

        timeEntryList = new ArrayList<>();

        token = getActivity().getSharedPreferences(TOKEN_PREFERENCE, Context.MODE_PRIVATE).getString(TOKEN_KEY, null);

        view = inflater.inflate(R.layout.fragment_day, container, false);
        tvNameDay = (TextView) view.findViewById(R.id.tvNameDay);
        tvNumDayOfMonth = (TextView) view.findViewById(R.id.tvNumDayOfMonth);
        lvTimeEntry = (ListView) view.findViewById(R.id.lvTimeEntry);

        SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM");
        Date date = calendar.getTime();
        SimpleDateFormat sdfDay = new SimpleDateFormat("EEEE");
        tvNameDay.setText(sdfDay.format(calendar.getTime()));
        tvNumDayOfMonth.setText(sdfDate.format(date));

        ivAdd = (ImageView) view.findViewById(R.id.ivAdd);
        ivAdd.setImageResource(R.drawable.add);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(APIFactory.isNetworkAvailable(getContext())) {
                    Intent intent = new Intent(getActivity(), AddRecordActivity.class);
                    intent.putExtra("user", (Serializable) user);
                    intent.putExtra("date", (Serializable) calendar);
                    startActivityForResult(intent, ADD_NEW_TIME_ENTRY);
                }
                else Toast.makeText(getActivity(),getString(R.string.network_failed),Toast.LENGTH_SHORT).show();
            }
        });

        lvTimeEntry.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long id) {
                onClickListView(position);
                return true;
            }
        });

       LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver,  new IntentFilter(TIME_ENTRY_LIST_FOR_DAY));

        refreshTineEntryList();

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NEW_TIME_ENTRY && resultCode == SUCCESSFUL_ADD_NEW_TIME_ENTRY) {
            refreshTineEntryList();
        }
    }


    private void refreshTineEntryList() {
        final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        Date date = calendar.getTime();
        JsonObject jo = new JsonObject();
        JsonArray ja = new JsonArray();
        ja.add(user.getId());
        jo.add("userId", ja);
        jo.addProperty("dateFind", sdfDate.format(date));
        if(APIFactory.isNetworkAvailable(getContext())) {
            final Call<List<TimeEntry>> timeEntryCall = service.getTimeEntryOfDay(jo, "Bearer " + token);
            timeEntryCall.enqueue(new Callback<List<TimeEntry>>() {
                @Override
                public void onResponse(Call<List<TimeEntry>> call, Response<List<TimeEntry>> response) {
                    if (response.isSuccessful()) {
                        timeEntryList = response.body();

                        Intent intent = new Intent(getActivity(), DatabaseService.class);
                        intent.putExtra("date", sdfDate.format(calendar.getTime()));
                        intent.setAction("insertList");
                        intent.putExtra("timeEntryList", (Serializable) timeEntryList);
                        getActivity().startService(intent);

                        TimeEntryAdapter timeEntryAdapter = new TimeEntryAdapter(view.getContext(), timeEntryList);
                        lvTimeEntry.setAdapter(timeEntryAdapter);
                    }
                }

                @Override
                public void onFailure(Call<List<TimeEntry>> call, Throwable t) {
                    String str = "";
                }
            });

        }
        else {
            Intent intent = new Intent(getActivity(), DatabaseService.class);
            intent.putExtra("date", sdfDate.format(calendar.getTime()));
            intent.putExtra("userId",user.getId());
            intent.setAction("getAllOfDay");
            getActivity().startService(intent);
        }

    }

    public void onClickListView(final int position) {
        final TimeEntryAdapter timeEntryAdapter = (TimeEntryAdapter) lvTimeEntry.getAdapter();

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());

        String[] typesAction = getResources().getStringArray(R.array.context_menu_action);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, typesAction);

        builderSingle.setNegativeButton(getString(R.string.dialog_btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        TimeEntry timeEntry = timeEntryAdapter.getTimeEntryList().get(position);
                        Intent intent = new Intent(getActivity(), AddRecordActivity.class);
                        intent.putExtra("timeEntry", (Serializable) timeEntry);
                        intent.putExtra("user", (Serializable) user);
                        startActivityForResult(intent, ADD_NEW_TIME_ENTRY);
                        break;

                    case 1:
                        String id = timeEntryAdapter.getTimeEntryList().get(position).getId();
                        final Call<Void> call = service.deleteTimeEntry(id, "Bearer " + token);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                        refreshTineEntryList();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                            }
                        });
                        break;

                }
            }
        });
        builderSingle.show();
    }



    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            if(intent.getAction().equals(TIME_ENTRY_LIST_FOR_DAY)&&intent.getStringExtra("date").equals(sdfDate.format(calendar.getTime()))){
                timeEntryList=(List<TimeEntry>) intent.getSerializableExtra("timeEntryList");
                TimeEntryAdapter timeEntryAdapter = new TimeEntryAdapter(context, timeEntryList);
                lvTimeEntry.setAdapter(timeEntryAdapter);
            }
        }
    };

}
