package com.example.nicho.instagramclone.SettingsActivityFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.widget.Toast;


import com.example.nicho.instagramclone.R;
import com.example.nicho.instagramclone.RegisterLoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainSettingsFragment extends PreferenceFragmentCompat implements PreferenceManager.OnPreferenceTreeClickListener
    {
        private FirebaseAuth firebaseAuth;

        public MainSettingsFragment()
            {
            }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
            {
            }

        @Override
        public void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.settings_main);
                PreferenceManager.setDefaultValues(requireActivity(), R.xml.settings_main, false);
                firebaseAuth = FirebaseAuth.getInstance();
            }

        @Override
        public void onResume()
            {
                super.onResume();
                getActivity().setTitle("Settings");
            }


        @Override
        public boolean onPreferenceTreeClick(Preference preference)
            {
                String key = preference.getKey();

                switch (key)
                    {
                        case "edit_profile_key":
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.settings_activity_fragmentcontainer, new EditProfileFragment())
                                    .addToBackStack(null).commit();
                            break;
                        case "sign_out_key":
                            DoSignOut();
                            break;
                    }
                return true;
            }

        /**
         * Signs out user and brings them to the the RegisterLoginActivity
         * intent.addFlags() clears all previous activities in the backstack.
         */
        private void DoSignOut()
            {
                firebaseAuth.signOut();
                Intent intent = new Intent(requireActivity(), RegisterLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
            }
    }
