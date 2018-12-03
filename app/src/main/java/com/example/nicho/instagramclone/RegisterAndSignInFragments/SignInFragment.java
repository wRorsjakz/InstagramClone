package com.example.nicho.instagramclone.RegisterAndSignInFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nicho.instagramclone.MainActivity;
import com.example.nicho.instagramclone.R;
import com.example.nicho.instagramclone.Util.MyStringManipulationHelper;
import com.example.nicho.instagramclone.Util.MyViewHelper;
import com.example.nicho.instagramclone.Util.TextValidationClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInFragment extends Fragment implements View.OnClickListener
    {
        private View view;
        private Button registerButton, SignInButton;
        private TextInputLayout emailInputLayout, passwordInputLayout;
        private TextInputEditText emailEditText, passwordEditText;
        private FirebaseAuth firebaseAuth;
        private ProgressBar progressBar;

        public SignInFragment()
            {
            }

        private void InitialiseViews()
            {
                SignInButton = view.findViewById(R.id.signin_signin_button);
                SignInButton.setOnClickListener(this);
                registerButton = view.findViewById(R.id.signin_register_button);
                registerButton.setOnClickListener(this);
                emailInputLayout = view.findViewById(R.id.signin_email_textinputlayout);
                emailEditText = view.findViewById(R.id.signin_email_textinputedittext);
                passwordInputLayout = view.findViewById(R.id.signin_password_textinputlayout);
                passwordEditText = view.findViewById(R.id.signin_password_textinputedittext);
                progressBar = view.findViewById(R.id.signin_progressbar);
            }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
            {
                view = inflater.inflate(R.layout.sign_in_fragment_layout, container, false);
                firebaseAuth = FirebaseAuth.getInstance();
                InitialiseViews();

                return view;
            }

        @Override
        public void onClick(View v)
            {
                switch (v.getId())
                    {
                        case R.id.signin_signin_button:
                            /**
                             * When the sign in button is pressed, the current firebase user is signed out before the login attempt.
                             * This is to prevent the situation where after a new user is registered (hence FirebaseAuth.getCurrentUser() !=null)
                             * and then another user tries to sign in.
                             * This causes the app to crash because there will be two FirebaseAuth instances simultaneously
                             */
                            MyStringManipulationHelper.DoSignOut();
                            DoSignIn();
                            break;
                        case R.id.signin_register_button:
                            requireActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.registerlogin_activity_fragment_container, new RegisterFragment()).addToBackStack(null)
                                    .commit();
                            break;
                    }
            }

        private void DoSignIn()
            {
                String emailAddress = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                boolean validEmail = TextValidationClass.ValidateEmailAddress(emailAddress, emailInputLayout, emailEditText);
                boolean validPassword = !password.isEmpty();

                if (!validPassword)
                    {
                        passwordInputLayout.setErrorEnabled(true);
                        passwordInputLayout.setError("Please enter password");
                    }
                else
                    {
                        passwordInputLayout.setErrorEnabled(false);
                    }

                if (validEmail && validPassword)
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        MyViewHelper.CloseKeyBoard(requireActivity());
                        firebaseAuth.signInWithEmailAndPassword(emailAddress, password)
                                .addOnSuccessListener(requireActivity(), new OnSuccessListener<AuthResult>()
                                    {
                                        @Override
                                        public void onSuccess(AuthResult authResult)
                                            {
                                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                                //Checks if the firebase user's email is verified.
                                                if (firebaseUser.isEmailVerified())
                                                    {
                                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                                        startActivity(intent);
                                                        getActivity().finish();
                                                    } else
                                                    {
                                                        Toast.makeText(requireActivity(), "Please verify email address", Toast.LENGTH_SHORT).show();
                                                    }
                                            }
                                    }).addOnFailureListener(requireActivity(), new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                    {
                                        Toast.makeText(requireActivity(), "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                        MyViewHelper.OpenKeyBoard(requireActivity());
                                    }
                            });
                        MyViewHelper.ProgressBarDelayed(1000, progressBar);
                    }
            }



    }
