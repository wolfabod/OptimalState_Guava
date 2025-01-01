package com.example.optimalstate;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    // This is the required method that must be implemented when extending BroadcastReceiver
    @Override
    public void onReceive(Context context, Intent intent) {
        // Create the notification manager to send the notification
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a unique channel ID (for Android 8.0 and above)
        String channelId = "check_in_notifications";

        // If the app is targeting Android 8.0 (API level 26) or above, we need to create a notification channel
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Check-in Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Create the notification
        Notification notification = new NotificationCompat.Builder(context, channelId)
                .setContentTitle("Check-in Reminder")
                .setContentText("It's time for your check-in!")
                //.setSmallIcon(R.drawable.ic_notification) // Set the notification icon
                .setAutoCancel(true)
                .build();

        // Send the notification
        notificationManager.notify(1, notification); // 1 is the notification ID
    }
}
