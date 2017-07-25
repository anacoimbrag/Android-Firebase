package me.anacoimbra.androidfirebase;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.crash.FirebaseCrash;

/**
 * Created by anacoimbra on 20/07/17.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);

        FirebaseCrash.report(new Exception("App initialized"));
    }
}
