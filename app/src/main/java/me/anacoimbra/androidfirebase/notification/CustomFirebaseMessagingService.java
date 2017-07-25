package me.anacoimbra.androidfirebase.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

import me.anacoimbra.androidfirebase.R;
import me.anacoimbra.androidfirebase.view.activity.LoginActivity;

/**
 * Created by anacoimbra on 25/07/17.
 */

public class CustomFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent notificationsIntent =
                new Intent(this, LoginActivity.class);

        notificationsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(this, 0,
                notificationsIntent, PendingIntent.FLAG_CANCEL_CURRENT);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_notification);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(icon)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setVibrate(new long[]{0, 100, 1000})
                .setContentIntent(intent)
                .setAutoCancel(true);


        notificationManager.notify((int) new Date().getTime(), builder.build());
    }
}
