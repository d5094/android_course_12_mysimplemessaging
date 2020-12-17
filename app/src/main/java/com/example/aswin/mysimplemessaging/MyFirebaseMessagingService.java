package com.example.aswin.mysimplemessaging;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessaging";
    private static final int NOTIFICATION_ID = 1;
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >=
                    android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("CustomMessage", "CustomMessage", NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }


            Intent intent = new Intent(this, MainActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(intent);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


            Notification notification = new NotificationCompat.Builder(this, "CustomMessage")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("This is custom payload")
                    .setContentText(remoteMessage.getData().get("score"))
                    .setContentIntent(pendingIntent)
                    .build();

            notificationManager.notify(NOTIFICATION_ID, notification);

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >=
                    android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("CustomMessage", "CustomMessage", NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }

            Intent intent = new Intent(this, MainActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(intent);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


            Notification notification = new NotificationCompat.Builder(this, "CustomMessage")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setContentIntent(pendingIntent)
                    .build();

            notificationManager.notify(NOTIFICATION_ID, notification);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        FirebaseDatabase database = FirebaseDatabase.getInstance(""); //change into your firebase realtime database instance URL
        database.getReference("tokens").push().setValue(token);
    }
}
