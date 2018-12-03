package com.example.nicho.instagramclone;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.nicho.instagramclone.MainActivityFragments.FavouriteFragment;
import com.example.nicho.instagramclone.MainActivityFragments.HomeFragment;
import com.example.nicho.instagramclone.MainActivityFragments.SearchFragment;
import com.example.nicho.instagramclone.MainActivityFragments.ProfileFragment;
import com.example.nicho.instagramclone.MainActivityFragments.ShareFragment;
import com.example.nicho.instagramclone.Util.MyPermissionHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class MainActivity extends AppCompatActivity implements BottomNavigationViewEx.OnNavigationItemSelectedListener
    {
        private BottomNavigationViewEx mBottomNav;
        private HomeFragment homeFragment;
        private SearchFragment searchFragment;
        private ShareFragment shareFragment;
        private FavouriteFragment favouriteFragment;
        private ProfileFragment profileFragment;

        private static final String TAG = "MAIN_ACTIVITY";
        private static final int camera_request_code = 2;
        private static final int storage_array_request_code = 101;

        @Override
        protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                homeFragment = new HomeFragment();
                searchFragment = new SearchFragment();
                shareFragment = new ShareFragment();
                favouriteFragment = new FavouriteFragment();
                profileFragment = new ProfileFragment();

                mBottomNav = findViewById(R.id.home_bottomnav);
                mBottomNav.setOnNavigationItemSelectedListener(this);
                mBottomNav.setSelectedItemId(R.id.bottom_nav_home);
                CustomiseBottomNav();

            }

        private void CustomiseBottomNav()
            {
                mBottomNav.enableItemShiftingMode(false);
                mBottomNav.enableShiftingMode(false);
                mBottomNav.enableAnimation(false);
                mBottomNav.setTextVisibility(false);
                mBottomNav.setIconSize(26,26);
            }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                    {
                        case R.id.bottom_nav_home:
                            LoadFragment(homeFragment);
                            break;
                        case R.id.bottom_nav_search:
                            LoadFragment(searchFragment);
                            break;
                        case R.id.bottom_nav_post:
                            MyPermissionHelper.RequestStoragePermission(this);
                            LoadFragment(shareFragment);
                            break;
                        case R.id.bottom_nav_favourite:
                            LoadFragment(favouriteFragment);
                            break;
                        case R.id.bottom_nav_user:
                            LoadFragment(profileFragment);
                            break;
                    }
                return true;
            }

        private void LoadFragment(Fragment fragment)
            {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.home_fragmentcontainer, fragment);
                fragmentTransaction.disallowAddToBackStack();
                fragmentTransaction.commit();
            }


        @Override
        public void onBackPressed()
            {
                super.onBackPressed();
                finish();
            }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
            {
                Log.d(TAG, "onRequestPermissionsResult: Method called");
                switch (requestCode)
                    {
                        case camera_request_code:
                            Log.d(TAG, "onRequestPermissionsResult: Request code: " + camera_request_code);
                            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                                {
                                    Log.d(TAG, "onRequestPermissionsResult: Permission granted");
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    if(intent.resolveActivity(getPackageManager()) != null)
                                        {
                                            Log.d(TAG, "onRequestPermissionsResult: Camera intent successful");
                                            startActivityForResult(intent, getResources().getInteger(R.integer.request_image_capture));
                                        }
                                }
                            else{
                                Log.d(TAG, "onRequestPermissionsResult: Permission not granted");
                            }
                            break;
                        case storage_array_request_code:
                            Log.d(TAG, "onRequestPermissionsResult: Request code " + storage_array_request_code);
                            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                                {
                                    //The method below refreshers the fragment
                                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.home_fragmentcontainer, new ShareFragment());
                                    fragmentTransaction.disallowAddToBackStack();
                                    fragmentTransaction.commit();
                                }
                            break;
                    }
            }
    }
