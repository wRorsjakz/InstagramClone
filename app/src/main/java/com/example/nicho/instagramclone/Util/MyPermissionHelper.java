package com.example.nicho.instagramclone.Util;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.nicho.instagramclone.R;

public class MyPermissionHelper
    {
        /**
         * Requests permission for Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
         * Request code is R.integer.permission_array_request_code (101)
         */
        public static void RequestStoragePermission(Activity activity)
            {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, activity.getResources().getInteger(R.integer.permission_array_storage_request_code));
            }

        public static void RequestCameraPermission(Activity activity)
            {
                ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.CAMERA},
                        activity.getResources().getInteger(R.integer.camera_request_code));
            }



    }
