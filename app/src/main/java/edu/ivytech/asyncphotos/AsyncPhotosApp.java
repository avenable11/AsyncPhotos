package edu.ivytech.asyncphotos;

import android.app.Application;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class AsyncPhotosApp extends Application {
    private long mTotalPhotos;

    public long getTotalPhotos() {
        return mTotalPhotos;
    }

    public void setTotalPhotos(long totalPhotos) {
        mTotalPhotos = totalPhotos;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PeriodicWorkRequest photoRequest = new PeriodicWorkRequest.Builder(PhotoWorker.class, 1, TimeUnit.HOURS).build();
        WorkManager.getInstance(getApplicationContext())
                .enqueueUniquePeriodicWork("photoRequest", ExistingPeriodicWorkPolicy.KEEP,photoRequest);
    }
}
