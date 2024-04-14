package com.example.dartscoreboard.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public final class PermissionCheckController {

    private static PermissionCheckController instance;

    private PermissionCheckController(){
    }

    public static PermissionCheckController getInstance(){
        if (instance == null){
            instance = new PermissionCheckController();
        }
        return instance;
    }

    public boolean checkNotificationPermission(Activity activity) { //true if GRANTED
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        } else return false;
    }

    public void requestNotificationPermission(Activity activity, int requestId) {
        boolean isGranted = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            isGranted = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }

        if (!isGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.POST_NOTIFICATIONS,}, requestId);
            }
        }
    }



}
