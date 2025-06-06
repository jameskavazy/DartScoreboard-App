package com.example.dartscoreboard;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.test.platform.app.InstrumentationRegistry;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* Copyright 2019 Google LLC.
   SPDX-License-Identifier: Apache-2.0 */
public class LiveDataTestUtil {
    public static <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);


        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
                latch.countDown();
                InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
                    liveData.removeObserver(this);
                });
            }
        };

        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            liveData.observeForever(observer);
        });

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("LiveData value was never set.");
        }
        //noinspection unchecked
        return (T) data[0];
    }
}