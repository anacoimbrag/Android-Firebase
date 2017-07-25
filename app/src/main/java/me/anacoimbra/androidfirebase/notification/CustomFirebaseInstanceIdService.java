package me.anacoimbra.androidfirebase.notification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by anacoimbra on 25/07/17.
 */

public class CustomFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = CustomFirebaseInstanceIdService.class.getName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // Send token to custom server
    }
}
