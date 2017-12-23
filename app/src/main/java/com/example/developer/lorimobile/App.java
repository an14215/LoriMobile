package com.example.developer.lorimobile;

import android.app.Application;

import com.example.developer.lorimobile.database.HelperFactory;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HelperFactory.setHelper(getApplicationContext());
    }
    @Override
    public void onTerminate() {
        HelperFactory.releaseHelper();
        super.onTerminate();
    }
}
