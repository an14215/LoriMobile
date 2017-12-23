package com.example.developer.lorimobile.rest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by developer on 22.12.2017.
 */

public class APIFactory {
   // private static final String BASE_URL = "http://192.168.1.223:8080";
    private static final String BASE_URL = "http://192.168.43.162:8080";

    private static final OkHttpClient CLIENT = new OkHttpClient();

    @NonNull
    public static APIService getAPIService() {
        return getRetrofit().create(APIService.class);
    }

    @NonNull
    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(CLIENT)
                .baseUrl(BASE_URL)
                .build();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if ((activeNetworkInfo != null) && (activeNetworkInfo.isConnected())) {
            return true;
        } else {
            return false;
        }
    }
}
