package com.fruitguy.workoutpartner.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heliao on 2/27/18.
 */

public class PermissionUtil {
    public static final int PERMISSION_REQUEST_ID = 1000;
    public static final int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;
    public static final int PERMISSION_DENIED = PackageManager.PERMISSION_DENIED;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    /**
     * Check if app has necessary permissions.
     * @param context current context where the method is called.
     * @return A list with missing permissions.
     */
    public static List<String> checkPermissions(Activity context) {
        String[] listPermissionsMissing = new String[] {
                PERMISSION_READ_EXTERNAL_STORAGE,
                PERMISSION_WRITE_EXTERNAL_STORAGE
        };

        return checkPermissions(context, listPermissionsMissing);
    }

    /**
     * Check if app has necessary permissions.
     * @param context current context where the method is called.
     * @param permissions List of permissions to check.
     * @return A list with missing permissions.
     */
    public static List<String> checkPermissions(Activity context, String[] permissions) {
        List<String> listPermissionsMissing = new ArrayList<>();

        for (int i = 0; i < permissions.length; i++) {
            if (!checkForPermissionGranted(context, permissions[i])) {
                listPermissionsMissing.add(permissions[i]);
            }
        }

        return listPermissionsMissing;
    }

    private static boolean checkForPermissionGranted(Activity context, String permission) {
        return (ContextCompat.checkSelfPermission(context, permission) == PERMISSION_GRANTED);
    }

    public static void requestPermissions(Activity context, List<String> listPermissionNeeded) {
        ActivityCompat.requestPermissions(context,
                listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]),
                PERMISSION_REQUEST_ID);
    }
}
