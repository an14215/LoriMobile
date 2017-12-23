package com.example.developer.lorimobile.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.developer.lorimobile.R;
import com.example.developer.lorimobile.model.Project;
import com.example.developer.lorimobile.model.ProjectParticipant;
import com.example.developer.lorimobile.model.Task;
import com.example.developer.lorimobile.model.TimeEntry;
import com.example.developer.lorimobile.model.User;
import com.example.developer.lorimobile.rest.APIFactory;
import com.example.developer.lorimobile.rest.APIService;
import com.github.shchurov.horizontalwheelview.HorizontalWheelView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRecordActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{
    private static final String TOKEN_PREFERENCE = "TOKEN_PREFERENCE";
    private static final String TOKEN_KEY = "TOKEN_KEY";

    private final static int SUCCESSFUL_ADD_NEW_TIME_ENTRY = 1;

    private TextView tvTask;
    private TextView tvProject;
    private EditText etHours;
    private EditText etMinutes;
    private EditText etDescription;
    private APIService service;
    private String token;
    private List<Project> projectList;
    private List<String> projectNameList;
    private List<Task> taskList;
    private List<String> taskNameList;
    private User user;
    private Project project;
    private Task task;
    private TimeEntry timeEntry;
    private Calendar calendar;
    private TextView tvTitle;
    private SeekBar seekBarHours;
    private SeekBar seekBarMinutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        tvProject = (TextView) findViewById(R.id.tvProject);
        projectList = new ArrayList<>();
        projectNameList = new ArrayList<>();

        tvTask = (TextView) findViewById(R.id.tvTask);
        etHours = (EditText) findViewById(R.id.etDurationHour);
        etMinutes = (EditText) findViewById(R.id.etDurationMinutes);
        etDescription = (EditText) findViewById(R.id.etDescription);
        seekBarHours = (SeekBar)findViewById(R.id.seekBarHours);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        seekBarHours = (SeekBar)findViewById(R.id.seekBarHours);
        seekBarMinutes = (SeekBar)findViewById(R.id.seekBarMinutes);
        seekBarHours.setOnSeekBarChangeListener(this);
        seekBarMinutes.setOnSeekBarChangeListener(this);

        user = (User) getIntent().getSerializableExtra("user");
        timeEntry = (TimeEntry) getIntent().getSerializableExtra("timeEntry");
        if (timeEntry != null) { //обновление

            tvTitle.setText(R.string.update_time_entry_title);
            tvTask.setText(timeEntry.getTask().getName());
            tvProject.setText(timeEntry.getTask().getProject().getName());
            if (timeEntry.getDescription() != null)
                etDescription.setText(timeEntry.getDescription());
            int hours = (int) timeEntry.getTimeInHours();
            int minutes = timeEntry.getTimeInMinutes() - hours * 60;
            etHours.setText(String.valueOf(hours));
            etMinutes.setText(String.valueOf(minutes));
            tvTask.setEnabled(true);
            task = timeEntry.getTask();
            calendar = Calendar.getInstance();
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            try {
                calendar.setTime(sdfDate.parse(timeEntry.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {//добавление
            tvTitle.setText(R.string.insert_time_entry_title);
            calendar = (Calendar) getIntent().getSerializableExtra("date");
        }
        service = new APIFactory().getAPIService();
        token = getSharedPreferences(TOKEN_PREFERENCE, Context.MODE_PRIVATE).getString(TOKEN_KEY, null);
        JsonObject jo = new JsonObject();
        JsonArray ja = new JsonArray();
        ja.add(user.getId());
        jo.add("userId", ja);
        final Call<List<ProjectParticipant>> projectCall = service.getProjectList(jo, "Bearer " + token);
        projectCall.enqueue(new Callback<List<ProjectParticipant>>() {
            @Override
            public void onResponse(Call<List<ProjectParticipant>> call, Response<List<ProjectParticipant>> response) {
                if (response.isSuccessful()) {
                    List<ProjectParticipant> pp = response.body();
                    for (ProjectParticipant projectParticipant : pp) {
                        projectList.add(projectParticipant.getProject());
                        projectNameList.add(projectParticipant.getProject().getName());
                        if(timeEntry!=null)
                            getTaskList(task.getProject().getId());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ProjectParticipant>> call, Throwable t) {
            }
        });
    }

    public void onClickChooseProject(View view) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle(getString(R.string.title_choose_project));

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, projectNameList);

        builderSingle.setNegativeButton(getString(R.string.dialog_btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvProject.setText(arrayAdapter.getItem(which));
                project = projectList.get(which);
                getTaskList(project.getId());
            }
        });
        builderSingle.show();
    }

    public void onClickChooseTask(View view) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle(getString(R.string.title_choose_task));

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, taskNameList);

        builderSingle.setNegativeButton(getString(R.string.dialog_btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvTask.setText(arrayAdapter.getItem(which));
                task = taskList.get(which);
            }
        });
        builderSingle.show();
    }

    public void onClickSave(View view) {
        if(APIFactory.isNetworkAvailable(this)) {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            Date date = calendar.getTime();
            //Date date = new Date(System.currentTimeMillis());
            final Call<TimeEntry> timeEntryCall;


            if (timeEntry == null)//добавление\
            {
                timeEntry = new TimeEntry(user, sdfDate.format(date), task, Integer.parseInt(etMinutes.getText().toString()), Integer.parseInt(etHours.getText().toString()), etDescription.getText().toString());
                timeEntryCall = service.createTimeEntry(timeEntry, "Bearer " + token);
            } else//изменение
            {
                timeEntry = new TimeEntry(timeEntry.getId(), user, sdfDate.format(date), task, Integer.parseInt(etMinutes.getText().toString()), Integer.parseInt(etHours.getText().toString()), etDescription.getText().toString());
                timeEntryCall = service.updateTimeEntry(timeEntry.getId(), timeEntry, "Bearer " + token);
            }
            timeEntryCall.enqueue(new Callback<TimeEntry>() {
                @Override
                public void onResponse(Call<TimeEntry> call, Response<TimeEntry> response) {
                    if (response.isSuccessful()) {
                        setResult(SUCCESSFUL_ADD_NEW_TIME_ENTRY);
                        AddRecordActivity.super.onBackPressed();
                    }
                }

                @Override
                public void onFailure(Call<TimeEntry> call, Throwable t) {

                }
            });
        }
        else Toast.makeText(this,getString(R.string.network_failed),Toast.LENGTH_SHORT).show();
    }

    private void getTaskList(String projectId) {
        taskList = new ArrayList<Task>();
        taskNameList = new ArrayList<String>();
        tvTask.setEnabled(true);
        JsonObject joCondition = new JsonObject();
        joCondition.addProperty("property", "project");
        joCondition.addProperty("operator", "=");
        joCondition.addProperty("value", projectId);
        JsonArray jaConditions = new JsonArray();
        jaConditions.add(joCondition);
        JsonObject joFilter = new JsonObject();
        joFilter.add("conditions", jaConditions);
        JsonObject joBody = new JsonObject();
        joBody.add("filter", joFilter);
        final Call<List<Task>> projectCall = service.getTaskList(joBody, "Bearer " + token);
        projectCall.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful()) {
                    taskList = response.body();
                    for (Task task : taskList) {
                        taskNameList.add(task.getName());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(AddRecordActivity.this, R.string.network_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(seekBar.getTag().equals("sbHours"))
         etHours.setText(String.valueOf(seekBar.getProgress()));
        if(seekBar.getTag().equals("sbMinutes")) {
            etMinutes.setText(String.valueOf(seekBar.getProgress()*5));
        }
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if(seekBar.getTag().equals("sbMinutes")) {
            progress=progress*5;
            etMinutes.setText(String.valueOf(progress));
        }
        if(seekBar.getTag().equals("sbHours"))
            etHours.setText(String.valueOf(seekBar.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

}
