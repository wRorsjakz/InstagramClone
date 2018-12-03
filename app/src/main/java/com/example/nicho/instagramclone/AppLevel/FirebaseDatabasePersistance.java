package com.example.nicho.instagramclone.AppLevel;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabasePersistance extends android.app.Application
    {
        @Override
        public void onCreate()
            {
                super.onCreate();
                /**
                 * Enable disk persistence for FirebaseDatabase
                 * Called in the manifest file in the application tag
                 * DO NOT CALL .setPersistenceEnable METHOD ANYWHERE ELSE
                 */
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            }
    }
