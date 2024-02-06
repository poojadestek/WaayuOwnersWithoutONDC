package com.waayu.owner.notification;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;


import com.waayu.owner.R;
import com.waayu.owner.activity.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.waayu.owner.activity.OrderActivity;

import java.util.List;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG,"notificationstart");
        boolean isAppOpen = false;
        if (isAppIsInBackground(getApplicationContext())) {
            isAppOpen = false;
        } else {
            isAppOpen = true;
        }


        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
       // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            // if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
//            Bitmap image=null;
//            try {
//                ParcelFileDescriptor parcelFileDescriptor =
//                        getContentResolver().openFileDescriptor(remoteMessage.getNotification().getImageUrl(), "r");
//                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
//                 image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
//                parcelFileDescriptor.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Constant.noto=true;
//            System.out.println("anc:-  hhkjkj");
//            Uri defaultSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.notiven);
////

            //startService(new Intent(this,BackgroundMusicService.class));
//            PugNotification.with(this)
//                    .load()
//                    .title(remoteMessage.getNotification().getTitle())
//                    //.message(remoteMessage.getNotification().getBody())
//                    .bigTextStyle(remoteMessage.getNotification().getBody())
//                    .smallIcon(R.mipmap.ic_launcher_round)
//                    //.largeIcon(R.drawable.pugnotification_ic_launcher)
//                    .flags(Notification.DEFAULT_ALL)
//                    .sound(null)
//                    .autoCancel(false)
//                    .simple()
//                    .build();

           // Intent intent = new Intent(getBaseContext(), OrderActivity.class);
//            Bundle bundle = new Bundle();
//            Log.d(TAG, "Inner_Body : >>>>" + jsonObject.toString());
//            bundle.putString("title", "" + jsonObject.optString("title"));
//            bundle.putString("booking_id",jsonObject.optString("booking_id"));
//            bundle.putString("astroid",jsonObject.optString("request_id"));
//            bundle.putString("filename", "" + jsonObject.optString("image"));
//            bundle.putString("type", "" + jsonObject.optString("type"));
//            bundle.putString("image", "" + jsonObject.optString("image"));
//            bundle.putString("name", "" + jsonObject.optString("username"));
//            bundle.putString("message", "" + jsonObject.optString("message"));
//
//            intent.putExtra("oid", 938);
//                    intent.putExtra("d_mode","0");
//
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            //intent.putExtras(bundle);
//            getApplicationContext().startActivity(intent);

//            try {
//                Uri defaultSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.notiven);
//
//                // Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), defaultSoundUri);
//                r.play();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }



            sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        //Log.d(TAG, "Refreshed token: " + token);


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    //,Bitmap img
    private void sendNotification(String messageBody,String title) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("backtohome","true");


        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        /*PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);*/

        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(this,
                    0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        }else {
            pendingIntent = PendingIntent.getActivity(this,
                    0, intent, PendingIntent.FLAG_ONE_SHOT);

        }

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.notiven);
        //Uri defaultSoundUri = Uri.parse("https://www.learningcontainer.com/wp-content/uploads/2020/02/Kalimba.mp3");



        //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        //.setDefaults(Notification.DEFAULT_ALL) //Important for heads-up notification
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setSound(defaultSoundUri)
                        //.setStyle(new NotificationCompat.InboxStyle())
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                        .setContentIntent(pendingIntent)
                        .setVibrate(new long[0])
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setChannelId(getString(R.string.default_notification_channel_id))
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "DIFFRENT_CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH); // Here I tried put different channel name.
            notificationChannel.setShowBadge(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                notificationChannel.canBubble();
            }
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)

                    .build();


            notificationChannel.canShowBadge();
            notificationChannel.enableVibration(true);
            notificationChannel.setSound(defaultSoundUri, audioAttributes);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.notify(0, notificationBuilder.build());

//            try {
//               // Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), defaultSoundUri);
//                r.play();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            Log.d("plays","playtest");
            startService(new Intent(this,BackgroundMusicService.class));


            //playNotificationSound();


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId, "fcm_default_channel", NotificationManager.IMPORTANCE_DEFAULT);
//            manager.createNotificationChannel(channel);
//        }
//        manager.notify(0, builder.build());

        }

            // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());



    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}