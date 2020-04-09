package edu.ivytech.asyncphotos;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.testing.TestDriver;
import androidx.work.testing.WorkManagerTestInitHelper;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class PhotoWorkerTest {
    private Context myContext;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        myContext = context;
        WorkManagerTestInitHelper.initializeTestWorkManager(myContext);
    }

    @Test
    public void testPeriodicWork() throws Exception {
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(PhotoWorker.class,1, TimeUnit.HOURS).build();

        WorkManager workManager = WorkManager.getInstance(myContext);
        TestDriver testDriver;
        testDriver = WorkManagerTestInitHelper.getTestDriver(myContext);
        workManager.cancelAllWork();
        workManager.enqueueUniquePeriodicWork("photoRequest", ExistingPeriodicWorkPolicy.KEEP, request)
            .getResult().get();
        testDriver.setPeriodDelayMet(request.getId());
        WorkInfo workInfo = workManager.getWorkInfoById(request.getId()).get();
        MatcherAssert.assertThat(workInfo.getState(), is(WorkInfo.State.ENQUEUED));
    }

}
