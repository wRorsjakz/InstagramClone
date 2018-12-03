package com.example.nicho.instagramclone.Util;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Patterns;

import java.util.regex.Pattern;

public class TextValidationClass
    {
        private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=!])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$");


        public static boolean ValidateEmailAddress(String _emailAddress, TextInputLayout _emailLayout, TextInputEditText _emailEditText)
            {
                if (_emailAddress.isEmpty())
                    {
                        _emailLayout.setErrorEnabled(true);
                        _emailLayout.setError("Please enter email address");
                        _emailEditText.requestFocus();
                        return false;
                    } else if ((Patterns.EMAIL_ADDRESS.matcher(_emailAddress).matches() == false))
                    {
                        _emailLayout.setErrorEnabled(true);
                        _emailLayout.setError("Please enter a valid email address");
                        _emailEditText.requestFocus();
                        return false;
                    } else
                    {
                        _emailLayout.setErrorEnabled(false);
                    }
                return true;
            }

        public static boolean ValidatePassword(String _password, TextInputLayout _passwordLayout, TextInputEditText _passwordEditText)
            {
                if (_password.isEmpty())
                    {
                        _passwordLayout.setErrorEnabled(true);
                        _passwordLayout.setError("Please enter a password");
                        _passwordEditText.requestFocus();
                        return false;
                    } else if ((PASSWORD_PATTERN.matcher(_password).matches() == false))
                    {
                        _passwordLayout.setErrorEnabled(true);
                        _passwordLayout.setError("Password too weak");
                        _passwordEditText.requestFocus();
                        return false;
                    } else
                    {
                        _passwordLayout.setErrorEnabled(false);
                    }
                return true;
            }
    }
