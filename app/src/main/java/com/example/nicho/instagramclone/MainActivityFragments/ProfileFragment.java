package com.example.nicho.instagramclone.MainActivityFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nicho.instagramclone.Model.PhoneImage;
import com.example.nicho.instagramclone.Model.UserAccountSettings;
import com.example.nicho.instagramclone.Model.UserPostsMetadata;
import com.example.nicho.instagramclone.R;
import com.example.nicho.instagramclone.RegisterLoginActivity;
import com.example.nicho.instagramclone.SettingsActivity;
import com.example.nicho.instagramclone.Util.MyStringManipulationHelper;
import com.example.nicho.instagramclone.Adapter.ProfileRecyclerViewAdapter;
import com.example.nicho.instagramclone.Util.MyViewHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.constraint.Constraints.TAG;

public class ProfileFragment extends Fragment
    {
        private View view;
        private Toolbar toolbar;
        private CollapsingToolbarLayout collapsingToolbarLayout;
        private AppBarLayout appBarLayout;
        private CircleImageView profilePic;
        private RecyclerView recyclerView;
        private Button editProfileButton;
        private TextView postsTextView, followersTextView, followingTextView, profileNameTextView, profileStatusTextView,
                profileLocationTextView, profileLinkTextView;

        private FirebaseAuth firebaseAuth;
        private FirebaseUser firebaseUser;
        private FirebaseDatabase firebaseDatabase;
        private DatabaseReference myRef;
        private List<PhoneImage> phoneImageList;

        private UserAccountSettings userAccountSettings;
        private UserPostsMetadata userPostsMetadata;
        private String userID;


        public ProfileFragment()
            {
            }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
            {
                view = inflater.inflate(R.layout.profile_fragment_layout, container, false);

                InitialiseViews();
                InitialiseFirebase();
                SetUpRecyclerView();

                editProfileButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                            {
                                Intent intent = new Intent(requireActivity(), SettingsActivity.class);
                                intent.putExtra("EDIT_PROFILE_TAG",1);
                                startActivity(intent);
                            }
                    });

                return view;
            }

        private void InitialiseViews()
            {
                appBarLayout = view.findViewById(R.id.profile_fragment_appbarlayout);
                collapsingToolbarLayout = view.findViewById(R.id.profile_fragment_collapsingtoolbar);
                toolbar = view.findViewById(R.id.profile_fragment_toolbar);
                profilePic = view.findViewById(R.id.profile_fragment_imageview);
                recyclerView = view.findViewById(R.id.profile_fragment_recyclerview);
                editProfileButton = view.findViewById(R.id.profile_fragment_editprofile_button);
                postsTextView = view.findViewById(R.id.profile_postcount_textview);
                followersTextView = view.findViewById(R.id.profile_followerscount_textview);
                followingTextView = view.findViewById(R.id.profile_followingcount_textview);
                profileNameTextView = view.findViewById(R.id.profile_name_textview);
                profileStatusTextView = view.findViewById(R.id.profile_status_textview);
                profileLocationTextView = view.findViewById(R.id.profile_location_textview);
                profileLinkTextView = view.findViewById(R.id.profile_weblink_textview);
            }

        private void InitialiseFirebase()
            {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();
                userID = firebaseUser.getUid();
                Log.d(TAG, "onCreateView: UserID:" + userID);
                firebaseDatabase = FirebaseDatabase.getInstance();
                myRef = firebaseDatabase.getReference();
            }

        @Override
        public void onResume()
            {
                super.onResume();
                Log.d(TAG, "onResume: ValueEventListener Attached");
                myRef.addValueEventListener(myValueEventListener);

            }

        @Override
        public void onPause()
            {
                super.onPause();
                Log.d(TAG, "onPause: ValueEventListener Removed");
                myRef.removeEventListener(myValueEventListener);
            }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
            {
                inflater.inflate(R.menu.fragment_profile_toolbar_menu, menu);
            }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
            {
                switch (item.getItemId())
                    {
                        case R.id.profile_toolbar_settings:
                            Intent intent = new Intent(getContext(), SettingsActivity.class);
                            intent.putExtra("MAIN_SETTINGS_TAG",0);
                            startActivity(intent);
                            break;
                        case R.id.profile_toolbar_logout:
                            DoSignOut();
                            break;
                    }
                return true;
            }

        private void SetUpToolbar(final String _username)
            {
                ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
                setHasOptionsMenu(true);

                //Prevents title from showing in expanded collapsing toolbar
                appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener()
                    {
                        boolean isShow = true;
                        int scrollRange = -1;

                        @Override
                        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset)
                            {
                                if (scrollRange == -1)
                                    {
                                        scrollRange = appBarLayout.getTotalScrollRange();
                                    }
                                if (scrollRange + verticalOffset == 0)
                                    {
                                        collapsingToolbarLayout.setTitle(_username);
                                        isShow = true;
                                    } else if (isShow)
                                    {
                                        collapsingToolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                                        isShow = false;
                                    }
                            }
                    });

            }

        private void SetUpRecyclerView()
            {
                phoneImageList = MyViewHelper.SetupSampleImageList(requireActivity());

                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(gridLayoutManager);
                ProfileRecyclerViewAdapter adapter = new ProfileRecyclerViewAdapter(phoneImageList, getActivity());

                recyclerView.setAdapter(adapter);
            }

        /**
         * ValueEventListener is attached at onResume() and removed at onPause()
         * The listener retrieves user_account_settings and user_posts_metadata for specific user using UserID
         */
        private ValueEventListener myValueEventListener = new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        Log.d(TAG, "onDataChange: DataSnapShot called");
                        /**
                         * Retrieve user_account_settings and user_posts_metadata from database
                         */
                        for (DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                if (ds.getKey().equals("user_account_settings"))
                                    {
                                        userAccountSettings = ds.child(userID).getValue(UserAccountSettings.class);
                                    }
                                if(ds.getKey().equals("user_posts_metadata"))
                                    {
                                        userPostsMetadata = ds.child(userID).getValue(UserPostsMetadata.class);
                                    }
                            }
                        SetUpToolbar(userAccountSettings.getUsername());
                        LoadUserInformation(userAccountSettings, userPostsMetadata);
                    }

                @Override
                public void onCancelled(DatabaseError databaseError)
                    {
                    }
            };

        /**
         * Loads all the data from user_account_settings and user_posts_metadata to their respective views
         * @param mUserAccountSettings
         * @param mUserPostsMetadata
         */
        private void LoadUserInformation(UserAccountSettings mUserAccountSettings, UserPostsMetadata mUserPostsMetadata)
            {
                profileStatusTextView.setText(mUserAccountSettings.getStatus());
                profileLocationTextView.setText(MyStringManipulationHelper.expandLocation(mUserAccountSettings.getLocation()));
                profileLinkTextView.setText(mUserAccountSettings.getWebsite());
                profileNameTextView.setText(mUserAccountSettings.getProfile_name());
                RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.default_image_profile_placeholder).centerCrop();
                Glide.with(requireActivity()).load(mUserAccountSettings.getProfile_photo()).apply(requestOptions).into(profilePic);

                postsTextView.setText(String.valueOf(mUserPostsMetadata.getPosts()));
                followersTextView.setText(String.valueOf(mUserPostsMetadata.getFollowers()));
                followingTextView.setText(String.valueOf(mUserPostsMetadata.getFollowing()));

            }

        /**
         * Signs out the userAccountSettings and brings them to the RegisterLoginActivity.
         * Also clears the backstack of activities using .setFlags().
         * This prevents userAccountSettings from being able to press the back button and return to profile fragment
         */
        private void DoSignOut()
            {
                firebaseAuth.signOut();
                Intent intent = new Intent(getActivity(), RegisterLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
            }




    }
