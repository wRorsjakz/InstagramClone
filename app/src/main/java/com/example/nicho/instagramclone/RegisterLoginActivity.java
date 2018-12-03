package com.example.nicho.instagramclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.nicho.instagramclone.RegisterAndSignInFragments.SignInFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterLoginActivity extends AppCompatActivity
    {
        private FirebaseAuth firebaseAuth;
        private FirebaseUser firebaseUser;

        @Override
        protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_register_login);

                firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();

                if(firebaseUser != null)
                    {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    }

                getSupportFragmentManager().beginTransaction().replace(R.id.registerlogin_activity_fragment_container, new SignInFragment()).commit();

            }

        @Override
        public void onBackPressed()
            {
                int count = getSupportFragmentManager().getBackStackEntryCount();

                if(count == 0)
                    {
                        super.onBackPressed();
                    }
                else
                    {
                        getSupportFragmentManager().popBackStack();
                    }
            }
    }
