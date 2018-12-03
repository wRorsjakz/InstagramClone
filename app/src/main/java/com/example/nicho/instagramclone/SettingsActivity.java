package com.example.nicho.instagramclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.nicho.instagramclone.SettingsActivityFragments.EditProfileFragment;
import com.example.nicho.instagramclone.SettingsActivityFragments.MainSettingsFragment;

public class SettingsActivity extends AppCompatActivity
    {
        private Toolbar mToolbar;

        @Override
        protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_settings);

                SetupToolbar();
                GetIncomingIntent();


            }

        //When back button on toolbar is pressed
        @Override
        public boolean onSupportNavigateUp()
            {
                onBackPressed();
                return true;
            }

        private void SetupToolbar()
            {
                mToolbar = findViewById(R.id.settings_activity_toolbar);
                setSupportActionBar(mToolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

        private void GetIncomingIntent()
            {
                Intent incomingIntent = getIntent();
                if(incomingIntent.hasExtra("MAIN_SETTINGS_TAG"))
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.settings_activity_fragmentcontainer, new MainSettingsFragment())
                                .commit();
                    }
                if(incomingIntent.hasExtra("EDIT_PROFILE_TAG"))
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.settings_activity_fragmentcontainer, new EditProfileFragment())
                                .commit();
                    }
            }
    }
