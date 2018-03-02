package me.anacoimbra.androidfirebase;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;

/**
 * Created by anacoimbra on 20/07/17.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);

        Crashlytics.log("App initialized");
    }
}
