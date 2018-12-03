package com.example.nicho.instagramclone.Util;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class MyStringManipulationHelper
    {
        public static String expandUsername(String username)
            {
                return username.replace(".", " ");
            }

        public static String condenseUsername(String username)
            {
                return username.replace(" ", ".");
            }

        public static void DoSignOut()
            {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
            }

        public static String expandLocation(String country)
            {
                return "I live in " + country;
            }

        public static String GetImageName(String fullImagePath)
            {
                return fullImagePath.substring(fullImagePath.lastIndexOf("/") + 1);
            }


    }
