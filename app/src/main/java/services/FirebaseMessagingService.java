package services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.fruitguy.workoutpartner.R;
import com.google.firebase.messaging.RemoteMessage;

import static com.fruitguy.workoutpartner.constant.FirebaseConstant.FRIEND_USER_ID;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.FROM_USER_ID;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String CHANNEL_ID = "firebase_cloud_messaging_channel";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String notificationTitle = remoteMessage.getNotification().getTitle();
        String notificationBody = remoteMessage.getNotification().getBody();
        String action = remoteMessage.getNotification().getClickAction();
        String fromUserId = remoteMessage.getData().get(FROM_USER_ID);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = "Firebase";
            String description = "Firebase cloud messaging channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(action);
        notificationIntent.putExtra(FRIEND_USER_ID, fromUserId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this
                , 0
                , notificationIntent
                , PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_rings)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Notification notification = mBuilder.build();

        int id = (int) System.currentTimeMillis();
        notificationManager.notify(id, notification);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }
}
