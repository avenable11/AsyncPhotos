package edu.ivytech.asyncphotos;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class PhotoWorker extends Worker {
    private final String TAG = "PhotoWorker";

    public PhotoWorker (@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork() {
        AsyncPhotosApp app = (AsyncPhotosApp) getApplicationContext();
        long totalPhotos = app.getTotalPhotos();
        Log.d(TAG, "The worker is trying to download something.");
        new FlickrFetchr(app).fetchItems();
        Log.d(TAG, "Download Finished");
        if (app.getTotalPhotos() != totalPhotos) {
            //send a notification
            sendNotification("Select to view new photos");
        } else {
            sendNotification("Select to view existing photos");
        }

        return Result.success();
    }

    private void sendNotification(String text) {
        Log.d(TAG, text + " Notification displayed");
        Intent notificationIntent = new Intent(this.getApplicationContext(), MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,notificationIntent,flags);
        int icon = R.drawable.ic_launcher_foreground;
        CharSequence tickerText = "Photo feed check is complete";
        CharSequence contentTitle = getApplicationContext().getText(R.string.app_name);
        CharSequence contentText = text;
        Notification notification = new Notification.Builder(getApplicationContext())
                .setSmallIcon(icon)
                .setTicker(tickerText)
                .setContentText(contentText)
                .setContentTitle(contentTitle)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        NotificationManager manager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1,notification);
    }
}
