package com.example.nicho.instagramclone.RegisterAndSignInFragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nicho.instagramclone.Model.UserAccountSettings;
import com.example.nicho.instagramclone.Model.UserPostsMetadata;
import com.example.nicho.instagramclone.R;
import com.example.nicho.instagramclone.Util.MyStringManipulationHelper;
import com.example.nicho.instagramclone.Util.MyViewHelper;
import com.example.nicho.instagramclone.Util.TextValidationClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class RegisterFragment extends Fragment implements View.OnClickListener
    {
        private View view;
        private TextInputLayout emailInputLayout, passwordInputLayout, usernameInputLayout, profileNameInputLayout, phoneNumberLayout;
        private TextInputEditText emailEditText, passwordEditText, usernameEditText, profileNameEditText,phoneNumberEditText;
        private Button registerButton, backButton;
        private ProgressBar progressBar;
        private CountryCodePicker phoneNumberCodePicker;

        private FirebaseAuth firebaseAuth;
        private FirebaseUser firebaseUser;
        private FirebaseDatabase firebaseDatabase;
        private DatabaseReference myRef;

        private String userID;
        private String emailAddress;
        private String password;
        private String username;
        private String phoneNumber;
        private String profileName;
        private String appendString;
        private String defaultImageUrl;
        private String validatedUsername;
        private String country;
        private boolean validPhoneNumber;
        private static final String TAG = "REGISTER_FRAGMENT_TAG";

        public RegisterFragment()
            {
            }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
            {
                view = inflater.inflate(R.layout.register_fragment_layout, container, false);

                InitialiseViews();
                InitialiseFirebase();
                defaultImageUrl = requireActivity().getResources().getString(R.string.default_image_url_string);

                return view;
            }

        private void InitialiseViews()
            {
                emailInputLayout = view.findViewById(R.id.register_email_textinputlayout);
                emailEditText = view.findViewById(R.id.register_email_edittext);
                passwordInputLayout = view.findViewById(R.id.register_password_textinputlayout);
                passwordEditText = view.findViewById(R.id.register_password_edittext);
                usernameInputLayout = view.findViewById(R.id.register_username_textinputlayout);
                usernameEditText = view.findViewById(R.id.register_username_edittext);
                profileNameInputLayout = view.findViewById(R.id.register_profilename_textinputlayout);
                profileNameEditText = view.findViewById(R.id.register_profilename_edittext);
                registerButton = view.findViewById(R.id.register_register_button);
                backButton = view.findViewById(R.id.register_back_button);
                progressBar = view.findViewById(R.id.register_progressbar);

                phoneNumberCodePicker = view.findViewById(R.id.register_country_picker);
                phoneNumberLayout = view.findViewById(R.id.register_phonenumber_layout);
                phoneNumberEditText = view.findViewById(R.id.register_phonenumber_edittext);
                phoneNumberCodePicker.registerCarrierNumberEditText(phoneNumberEditText);

                registerButton.setOnClickListener(this);
                backButton.setOnClickListener(this);
            }

        private void InitialiseFirebase()
            {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseDatabase = FirebaseDatabase.getInstance();
                myRef = firebaseDatabase.getReference();
            }

        @Override
        public void onClick(View v)
            {
                switch (v.getId())
                    {
                        case R.id.register_register_button:
                            DoRegisterUser();
                            break;
                        case R.id.register_back_button:
                            requireActivity().onBackPressed();
                            break;
                    }
            }

        @Override
        public void onResume()
            {
                super.onResume();
                firebaseAuth.addAuthStateListener(myAuthStateListener);
                Log.d(TAG, "onResume: Auth Listener attached");
                AttachPhoneNumberListener();
            }

        @Override
        public void onPause()
            {
                super.onPause();
                firebaseAuth.removeAuthStateListener(myAuthStateListener);
                Log.d(TAG, "onPause: Auth Listener removed ");
            }

        /**
         * Listener is attached at onResume() and is called everytime a valid phone number is entered
         */
        private void AttachPhoneNumberListener()
            {
                Log.d(TAG, "AttachPhoneNumberListener: Listener is attached");
                validPhoneNumber = false;
                country = phoneNumberCodePicker.getSelectedCountryEnglishName();
                Log.d(TAG, "InitialiseViews: Default country: " + country);

                phoneNumberCodePicker.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener()
                    {
                        @Override
                        public void onValidityChanged(boolean isValidNumber)
                            {
                                Log.d(TAG, "onValidityChanged: isValidNumber: " + isValidNumber);
                                if(isValidNumber)
                                    {
                                        phoneNumber = phoneNumberCodePicker.getFormattedFullNumber();
                                        Log.d(TAG, "onValidityChanged: Phone number: " + phoneNumber);
                                        validPhoneNumber = true;
                                    }
                                else
                                    {
                                        validPhoneNumber = false;
                                    }
                            }
                    });

                phoneNumberCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener()
                    {
                        @Override
                        public void onCountrySelected()
                            {
                                Log.d(TAG, "onCountrySelected: Method called");
                                country = phoneNumberCodePicker.getSelectedCountryEnglishName();
                                Log.d(TAG, "onCountrySelected: Country: " + country);
                            }
                    });
            }

        /**
         * Firebase Auth Listener is attached at onResume() and removed at onPause().
         * Once the user is authenticated, and thus the user can access the realtime database, the authstatelistener is triggered
         * The .updateProfile() method is called and the profileName and defaultImageUrl is added to firebaseUser.
         * Afterwards, CheckIfUsernameAlreadyExists() is called
         */

        private FirebaseAuth.AuthStateListener myAuthStateListener = new FirebaseAuth.AuthStateListener()
            {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
                    {
                        firebaseUser = firebaseAuth.getCurrentUser();

                        if(firebaseUser != null)
                            {
                                userID = firebaseUser.getUid();
                                Log.d(TAG, "onAuthStateChanged: UserAccountSettings Uid: "+ userID);
                                Log.d(TAG, "onAuthStateChanged: UserAccountSettings is now signed in " );

                                //This method adds the profile name to profile image url to FireBaseAuth
                                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(profileName).setPhotoUri(Uri.parse(defaultImageUrl)).build();
                                firebaseUser.updateProfile(userProfileChangeRequest);

                              CheckIfUsernameAlreadyExists();
                            }
                    }
            };

        /**
         * Register a new user with email address and password
         * SendEmailVerification() method is invoked after successful user registration
         */
        private void DoRegisterUser()
            {
                emailAddress = emailEditText.getText().toString().trim();
                username =  MyStringManipulationHelper.condenseUsername(usernameEditText.getText().toString().toLowerCase().trim());
                profileName = profileNameEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();

                boolean validEmail = TextValidationClass.ValidateEmailAddress(emailAddress, emailInputLayout, emailEditText);
                boolean validPassword =  TextValidationClass.ValidatePassword(password, passwordInputLayout, passwordEditText);
                boolean validProfileName = !profileName.isEmpty();
                boolean validUsername = !username.isEmpty();

                if(!validUsername)
                    {
                        usernameInputLayout.setErrorEnabled(true);
                        usernameInputLayout.setError("Please enter username");
                    }
                else
                    {
                        usernameInputLayout.setErrorEnabled(false);
                    }

                if(!validProfileName)
                    {
                        profileNameInputLayout.setErrorEnabled(true);
                        profileNameInputLayout.setError("Please enter profile name");
                    }
                else
                    {
                        profileNameInputLayout.setErrorEnabled(false);
                    }

                if(!validPhoneNumber)
                    {
                        phoneNumberLayout.setErrorEnabled(true);
                        phoneNumberLayout.setError("Please enter phone number");
                    }
                else
                    {
                        phoneNumberLayout.setErrorEnabled(false);
                    }

                if(validEmail && validPassword && validUsername && validProfileName && validPhoneNumber)
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        firebaseAuth.createUserWithEmailAndPassword(emailAddress, password)
                                .addOnSuccessListener(requireActivity(), new OnSuccessListener<AuthResult>()
                                    {
                                        @Override
                                        public void onSuccess(AuthResult authResult)
                                            {
                                                Log.d(TAG, "onSuccess: Uid: " + userID);
                                                //Send verification email to user's email address
                                                SendVerificationEmail();
                                            }
                                    })
                                .addOnFailureListener(requireActivity(), new OnFailureListener()
                                    {
                                        @Override
                                        public void onFailure(@NonNull Exception e)
                                            {
                                                Toast.makeText(getActivity(), "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                e.printStackTrace();
                                            }
                                    });
                    }
                MyViewHelper.ProgressBarDelayed(2000, progressBar);
            }

        /**
         *  This method is called only when user account is created successfully when the authStateListener is triggered
         *  The query.addListenerForSingleValueEvent() method checks if the user's username already exists in the database
         *  If it already exists, a random string is appended to the end of it. Else, no changes are made
         *  NOTE: The validatedUsername variable is assigned to the final username. (DO NOT USE username variable after the method call)
         *  The DoAddUserDetails() method is called after that.
         */
        private void CheckIfUsernameAlreadyExists()
            {
                Log.d(TAG, "CheckIfUsernameAlreadyExists: Method called");
                Query query = myRef.child("user_account_settings").orderByChild("username").equalTo(username);
                query.addListenerForSingleValueEvent(new ValueEventListener()
                    {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                Log.d(TAG, "onDataChange: Method called");
                                if(dataSnapshot.exists())
                                    {
                                        Log.d(TAG, "onDataChange: Username already exists");
                                        appendString = myRef.push().getKey().substring(3,10);
                                        validatedUsername = username + appendString;
                                        Log.d(TAG, "onDataChange: New username: " + validatedUsername);
                                    }
                                else
                                    {
                                        Log.d(TAG, "onDataChange: Username does not already exist");
                                        validatedUsername = username;
                                    }
                                DoAddUserDetails();
                            }

                        @Override
                        public void onCancelled(DatabaseError databaseError)
                            {

                            }
                    });
            }

        /**
         *  user_account_settings and user_posts_metadata are added to the realtime database
         */
        private void DoAddUserDetails()
            {
                if(firebaseUser != null)
                    {
                        UserAccountSettings newUserAccountSettings = new UserAccountSettings(emailAddress, country, phoneNumber, profileName,
                                defaultImageUrl, "Happy", userID, validatedUsername, "https://www.website.com/");
                        Log.d(TAG, "DoAddUserDetails: Adding new user to realtime database");
                        myRef.child(getString(R.string.user_account_settings)).child(userID).setValue(newUserAccountSettings);

                        UserPostsMetadata userPostsMetadata = new UserPostsMetadata(999,999,999);
                        myRef.child("user_posts_metadata").child(userID).setValue(userPostsMetadata);

                    }
            }

        private void SendVerificationEmail()
            {
                if(firebaseUser != null)
                    {
                        firebaseUser.sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if(task.isSuccessful())
                                                    {
                                                        Log.d(TAG, "onComplete: Sent verification email successfully");
                                                        firebaseAuth.signOut();
                                                        Log.d(TAG, "DoAddUserDetails: UserAccountSettings is signed out");
                                                        Toast.makeText(requireActivity(), "Please verify email address", Toast.LENGTH_SHORT).show();
                                                        requireActivity().onBackPressed();
                                                    }
                                                else
                                                    {
                                                        Toast.makeText(requireActivity(), "Could not send verification email", Toast.LENGTH_SHORT).show();
                                                        Log.d(TAG, "onComplete: Could not send verification email");
                                                    }
                                            }
                                    });
                    }
            }


    }
