package com.example.developer.lorimobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.developer.lorimobile.model.User;
import com.example.developer.lorimobile.rest.APIFactory;
import com.example.developer.lorimobile.rest.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends Activity {
    private static final String TOKEN_PREFERENCE = "TOKEN_PREFERENCE";
    private static final String TOKEN_KEY = "TOKEN_KEY";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;
        if (getSharedPreferences(TOKEN_PREFERENCE, Context.MODE_PRIVATE).getString(TOKEN_KEY, null)!= null)
            intent = new Intent(this, MainActivity.class);
        else
            intent = new Intent(this, AuthenticationActivity.class);
        startActivity(intent);
        finish();
    }
}
