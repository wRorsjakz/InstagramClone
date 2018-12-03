package com.example.nicho.instagramclone.SettingsActivityFragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.nicho.instagramclone.Model.UserAccountSettings;
import com.example.nicho.instagramclone.R;
import com.example.nicho.instagramclone.Util.MyStringManipulationHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener
    {
        private CircleImageView profilePic;
        private FirebaseAuth firebaseAuth;
        private FirebaseUser firebaseUser;
        private String userID;
        private FirebaseDatabase firebaseDatabase;
        private DatabaseReference myRef;
        private UserAccountSettings userAccountSettings;

        private Preference usernamePreference;
        private EditTextPreference profileNamePreference;
        private EditTextPreference statusPreference;
        private ListPreference locationPreference;
        private EditTextPreference websitePreference;
        private Preference emailAddressPreference;
        private Preference phoneNumberPreference;

        private static final String USERNAME_KEY = "edit_profile_username_key";
        private static final String PROFILE_NAME_KEY = "edit_profile_profile_name_key";
        private static final String STATUS_KEY = "edit_profile_status_key";
        private static final String LOCATION_KEY = "edit_profile_location_key";
        private static final String WEBSITE_KEY = "edit_profile_website_key";
        private static final String EMAIL_ADDRESS_KEY = "edit_profile_email_address_key";
        private static final String PHONE_NUMBER_KEY = "edit_profile_phone_number_key";

        private static final String TAG = "EDIT_PROFILE_FRAGMENT_TAG";

        public EditProfileFragment()
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
                addPreferencesFromResource(R.xml.edit_profile);

                firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();
                userID = firebaseUser.getUid();
                firebaseDatabase = FirebaseDatabase.getInstance();
                myRef = firebaseDatabase.getReference();

                InitialisePreferences();
            }

        @Override
        public void onResume()
            {
                super.onResume();
                getActivity().setTitle("Edit Profile");
                Log.d(TAG, "onStart: ValueEventListener Attached");
                myRef.addValueEventListener(myValueEventListener);

            }

        @Override
        public void onPause()
            {
                super.onPause();
                Log.d(TAG, "onStop: ValueEventListener removed");
                myRef.removeEventListener(myValueEventListener);
            }

        /**
         * 1) Initialises the preferences
         * 2) setOnPreferenceChangeListener() is called here as well
         */
        private void InitialisePreferences()
            {
                PreferenceManager.setDefaultValues(requireActivity(), R.xml.edit_profile, false);

                usernamePreference = findPreference(USERNAME_KEY);
                usernamePreference.setOnPreferenceChangeListener(this);
                profileNamePreference = (EditTextPreference) findPreference(PROFILE_NAME_KEY);
                profileNamePreference.setOnPreferenceChangeListener(this);
                statusPreference = (EditTextPreference) findPreference(STATUS_KEY);
                statusPreference.setOnPreferenceChangeListener(this);
                locationPreference = (ListPreference) findPreference(LOCATION_KEY);
                locationPreference.setOnPreferenceChangeListener(this);
                websitePreference = (EditTextPreference)findPreference(WEBSITE_KEY);
                websitePreference.setOnPreferenceChangeListener(this);
                emailAddressPreference = findPreference(EMAIL_ADDRESS_KEY);
                phoneNumberPreference = findPreference(PHONE_NUMBER_KEY);
            }

        /**
         * ValueEventListener retrieves user_account_settings from the realtime database using userID as the key
         */
        private ValueEventListener myValueEventListener = new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        for(DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                if(ds.getKey().equals("user_account_settings"))
                                    {
                                        userAccountSettings = ds.child(userID).getValue(UserAccountSettings.class);
                                        Log.d(TAG, "onDataChange: User Account Settings retrieved from database");
                                        SetValuesFromDatabase();
                                    }
                            }
                    }

                @Override
                public void onCancelled(DatabaseError databaseError)
                    {

                    }
            };

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                switch (preference.getKey())
                    {
                        case PROFILE_NAME_KEY:
                            String newProfileName = newValue.toString().trim();
                            ChangePreference(newProfileName, "profile_name");
                            break;
                        case STATUS_KEY:
                            String newStatus = newValue.toString().trim();
                            ChangePreference(newStatus, "status");
                            break;
                        case LOCATION_KEY:
                            ChangePreference(newValue, "location");
                            break;
                        case WEBSITE_KEY:
                            String newWebsite = newValue.toString().trim();
                            ChangePreference(newWebsite, "website");
                            break;
                    }
                return true;
            }

        /**
         * @param newValue
         * @param databaseKey
         * Saves the changes in the preferences (newValue) to the realtime database using the databaseKey.
         */
        private void ChangePreference(final Object newValue, String databaseKey )
            {
                Log.d(TAG, "onPreferenceChange: New Value is: " + newValue);
                DatabaseReference databaseReference = firebaseDatabase.getReference("user_account_settings");
                databaseReference.child(userID).child(databaseKey).setValue(newValue)
                        .addOnSuccessListener(requireActivity(), new OnSuccessListener<Void>()
                            {
                                @Override
                                public void onSuccess(Void aVoid)
                                    {
                                        Log.d(TAG, "onSuccess: Saved " + newValue + " to database" );
                                        Toast.makeText(requireActivity(), "Status saved", Toast.LENGTH_SHORT).show();
                                    }
                            }).addOnFailureListener(requireActivity(), new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                            {
                                Toast.makeText(requireActivity(), "Status not saved", Toast.LENGTH_SHORT).show();
                            }
                    });
            }

        /**
         * Sets the values from userAccountSettings variable (username, status, location etc) to the individual preferences
         */
        private void SetValuesFromDatabase()
            {
                if(userAccountSettings != null)
                    {
                        String currentUsername = userAccountSettings.getUsername();
                        String currentProfileName = userAccountSettings.getProfile_name();
                        String currentLocation = userAccountSettings.getLocation();
                        String currentWebsite = userAccountSettings.getWebsite();
                        String currentStatus = userAccountSettings.getStatus();
                        String currentEmail = userAccountSettings.getEmail_address();
                        String currentPhoneNumber = userAccountSettings.getPhone_number();

                        usernamePreference.setDefaultValue(currentUsername);
                        usernamePreference.setSummary(currentUsername);

                        profileNamePreference.setDefaultValue(currentProfileName);
                        profileNamePreference.setSummary(currentProfileName);
                        profileNamePreference.setText(currentProfileName);

                        locationPreference.setDefaultValue(currentLocation);
                        locationPreference.setSummary(MyStringManipulationHelper.expandLocation(currentLocation));

                        websitePreference.setDefaultValue(currentWebsite);
                        websitePreference.setText(currentWebsite);
                        websitePreference.setSummary(currentWebsite);

                        statusPreference.setDefaultValue(currentStatus);
                        statusPreference.setText(currentStatus);
                        statusPreference.setSummary(currentStatus);

                        emailAddressPreference.setDefaultValue(currentEmail);
                        emailAddressPreference.setSummary(currentEmail);

                        phoneNumberPreference.setDefaultValue(currentPhoneNumber);
                        phoneNumberPreference.setSummary(currentPhoneNumber);
                    }
            }

    }
