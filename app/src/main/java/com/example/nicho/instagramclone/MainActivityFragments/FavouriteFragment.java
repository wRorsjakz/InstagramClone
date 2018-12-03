package com.example.nicho.instagramclone.MainActivityFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicho.instagramclone.R;

public class FavouriteFragment extends Fragment
    {
        private View view;

        public FavouriteFragment()
            {
            }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
            {
                view = inflater.inflate(R.layout.notification_fragment_layout, container, false);
                return view;
            }
    }
