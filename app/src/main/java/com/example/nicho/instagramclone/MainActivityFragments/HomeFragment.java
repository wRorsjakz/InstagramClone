package com.example.nicho.instagramclone.MainActivityFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nicho.instagramclone.R;

public class HomeFragment extends Fragment
    {
        private View view;
        private Toolbar toolbar;

        public HomeFragment()
            {
            }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
            {
                view = inflater.inflate(R.layout.home_fragment_layout, container, false);
                toolbar = view.findViewById(R.id.home_toolbar);
                toolbar.setTitle("Home");
                ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
                setHasOptionsMenu(true);
                return view;
            }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
            {
                inflater.inflate(R.menu.home_toolbar_menu, menu);
            }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
            {
                switch (item.getItemId())
                    {
                        case R.id.home_toolbar_message:
                            Toast.makeText(getContext(), "Message icon pressed", Toast.LENGTH_SHORT).show();
                            break;
                    }
                return true;
            }
    }
