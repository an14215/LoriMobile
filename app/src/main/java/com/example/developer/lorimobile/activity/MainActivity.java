package com.example.developer.lorimobile.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.developer.lorimobile.R;
import com.example.developer.lorimobile.fragment.WeekFragment;
import com.example.developer.lorimobile.model.User;
import com.example.developer.lorimobile.rest.APIFactory;
import com.example.developer.lorimobile.rest.APIService;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TOKEN_PREFERENCE = "TOKEN_PREFERENCE";
    private static final String TOKEN_KEY = "TOKEN_KEY";

    private static final String USER_PREFERENCE = "USER_PREFERENCE";
    private static final String USER_ID = "USER_ID";


    private APIService service;
    private String token;
    public User user;

    private ImageView ivChooseDate;
    private ImageView ivSearch;
    private ImageView ivToday;
    private TextView tvStartDate;
    private TextView tvEndDate;

    private Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        token = getSharedPreferences(TOKEN_PREFERENCE, Context.MODE_PRIVATE).getString(TOKEN_KEY, null);
        service = new APIFactory().getAPIService();

        ivChooseDate = (ImageView) findViewById(R.id.ivChooseDate);
        ivChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickStartDate(null);
            }
        });

        ivSearch =(ImageView) findViewById(R.id.ivSearch);
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("user",(Serializable) user);
                startActivity(intent);
            }
        });

        ivToday =(ImageView) findViewById(R.id.ivToday);
        ivToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCalendar();
                refreshWeek();
            }
        });

        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);

        initCalendar();

        if(APIFactory.isNetworkAvailable(getApplicationContext())){
            final Call<User> userCall = service.getUser("Bearer " + token);
            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        user = response.body();
                        getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE).edit().putString(USER_ID, user.getId()).apply();
                        refreshWeek();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    String str="";
                }
            });
        }
        else {
            user = new User(getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE).getString(USER_ID, null));
            refreshWeek();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.search:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("user",(Serializable) user);
                startActivity(intent);
                return true;
            case R.id.exit:
                getSharedPreferences(TOKEN_PREFERENCE, Context.MODE_PRIVATE).edit().putString(TOKEN_KEY, null).apply();
                Intent intentExit = new Intent(this, AuthenticationActivity.class);
                intentExit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentExit);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void initCalendar(){
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() - 1000);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.add(Calendar.DAY_OF_MONTH,-calendar.get(Calendar.DAY_OF_WEEK)+2);
    }

    public void onClickStartDate(View view) {
        new DatePickerDialog(this, dateDialogStart, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    DatePickerDialog.OnDateSetListener dateDialogStart = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int newYear, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR, newYear);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.add(Calendar.DAY_OF_MONTH,-calendar.get(Calendar.DAY_OF_WEEK)+2);
            refreshWeek();

        }
    };

    public void refreshWeek(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd MMMM");
        tvStartDate.setText(sdfDate.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH,6);
        tvEndDate.setText(sdfDate.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH,-6);
        WeekFragment weekFragment = new WeekFragment().newInstance(user,calendar);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        fragmentManager.beginTransaction().replace(R.id.week,weekFragment).commit();
    }

}
