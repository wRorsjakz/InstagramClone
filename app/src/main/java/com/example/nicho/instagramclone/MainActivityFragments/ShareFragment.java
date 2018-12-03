package com.example.nicho.instagramclone.MainActivityFragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nicho.instagramclone.Adapter.ShareRecyclerViewAdapter;
import com.example.nicho.instagramclone.Model.PhoneImage;
import com.example.nicho.instagramclone.R;
import com.example.nicho.instagramclone.ShareActivity;
import com.example.nicho.instagramclone.Util.MyFileSearch;
import com.example.nicho.instagramclone.Util.MyPermissionHelper;
import com.example.nicho.instagramclone.Util.MyViewHelper;

import java.util.ArrayList;
import java.util.List;

public class ShareFragment extends Fragment implements View.OnClickListener, ShareRecyclerViewAdapter.ShareRecyclerViewInterface
    {
        //Widgets
        private View view;
        private AppBarLayout appBarLayout;
        private Toolbar toolbar;
        private ImageView imageView;
        private TextView nextTextView;
        private RecyclerView recyclerView;
        private ProgressBar progressBar;
        private FloatingActionButton cameraFAB;
        private SearchView searchView;
        private SearchView.SearchAutoComplete searchAutoComplete;

        //Variables
        private List<String> directories;
        private String selectedString;
        private List<PhoneImage> phoneImageList;
        private PhoneImage selectedPhoneImage;

        //Constant
        private static final String TAG = "SHARE_FRAGMENT";

        public ShareFragment()
            {
            }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
            {
                view = inflater.inflate(R.layout.share_fragment_layout, container, false);
                directories = new ArrayList<>();
                return view;
            }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
            {
                super.onViewCreated(view, savedInstanceState);
                InitialiseViews();
                SetUpToolbar();
                SetupRecyclerView();
            }

        private void InitialiseViews()
            {
                imageView = view.findViewById(R.id.share_fragment_imageview);
                appBarLayout = view.findViewById(R.id.share_fragment_appbarlayout);
                toolbar = view.findViewById(R.id.share_fragment_toolbar);
                nextTextView = view.findViewById(R.id.share_fragment_next_textview);
                recyclerView = view.findViewById(R.id.share_fragment_recyclerview);
                progressBar = view.findViewById(R.id.share_fragment_progressbar);
                cameraFAB = view.findViewById(R.id.share_fragment_camera_fab);
                MyViewHelper.SetupScrollableFAB(cameraFAB);
                nextTextView.setOnClickListener(this);
                cameraFAB.setOnClickListener(this);
            }

        private void SetUpToolbar()
            {
                ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
                setHasOptionsMenu(true);
            }

        @Override
        public void onClick(View v)
            {
                switch (v.getId())
                    {
                        case R.id.share_fragment_camera_fab:
                            DispatchTakePictureIntent();
                            break;
                        case R.id.share_fragment_next_textview:
                            DoShareImage();
                            break;
                    }
            }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
            {
                inflater.inflate(R.menu.fragment_share_toolbar_menu, menu);

                SetupSearchView(menu);
            }

        /**
         * This method sets up the **searchview** and the **searchAutoComplete**. The **searchAutoComplete** is anchored
         * to the **searchview** and searches through the list of directories in the phone.
         * The **@SuppressLint("RestrictedApi")** is for .setAdapter(adapter).
         * @param menu
         */
        @SuppressLint("RestrictedApi")
        private void SetupSearchView(Menu menu)
            {
                MenuItem menuItem = menu.findItem(R.id.share_fragment_searchview);
                searchView = (SearchView) menuItem.getActionView();
                searchView.setQueryHint("Search phone");

                searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
                Drawable whiteBackground = getResources().getDrawable(R.drawable.drawable_color_white, null);
                searchAutoComplete.setDropDownBackgroundDrawable(whiteBackground);
                searchAutoComplete.setDropDownAnchor(R.id.share_fragment_searchview);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), R.layout.searchview_item_layout, R.id.searchview_item_textview, directories);
                searchAutoComplete.setAdapter(adapter);
                searchAutoComplete.setThreshold(0);

                searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                            {
                                Log.d(TAG, "onItemClick: searchAutoComplete is pressed");
                                selectedString = (parent.getItemAtPosition(position)).toString();
                                searchAutoComplete.setText(selectedString);
                            }
                    });

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
                    {
                        @Override
                        public boolean onQueryTextSubmit(String query)
                            {
                                Log.d(TAG, "onQueryTextSubmit: Method called");
                                Toast.makeText(requireActivity(), "Query: " + query, Toast.LENGTH_SHORT).show();
                                searchAutoComplete.dismissDropDown();
                                return true;
                            }

                        @Override
                        public boolean onQueryTextChange(String newText)
                            {
                                return false;
                            }
                    });

            }

        /**
         * When FAB is pressed, this method checks if permission was granted to the app to use the camera.
         * If permission was granted, it goes to the camera app. Else, it asks the user for permission.
         * onRequestPermissionsResult() is invoked in the MainActivity class on the user's response
         */
        private void DispatchTakePictureIntent()
            {
                Log.d(TAG, "DispatchTakePictureIntent: Method Called");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    {
                        Log.d(TAG, "DispatchTakePictureIntent: Camera permission has already been granted");
                        if (intent.resolveActivity(requireActivity().getPackageManager()) != null)
                            {
                                Log.d(TAG, "DispatchTakePictureIntent: Camera intent called");
                                getActivity().startActivityForResult(intent, getResources().getInteger(R.integer.request_image_capture));
                            }
                    } else
                    {
                        Log.d(TAG, "DispatchTakePictureIntent: Camera permission has not been granted");
                        MyPermissionHelper.RequestCameraPermission(requireActivity());
                    }
            }

        private void SetupRecyclerView()
            {
                //Provide sample images for the recyclerview
                /*phoneImageList = MyViewHelper.SetupSampleImageList(requireActivity());*/

                if(ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    {
                        List<String> allImagePaths = MyFileSearch.GetAllImagePath(requireActivity());
                        phoneImageList = MyFileSearch.GetAllImages(allImagePaths);
                        ShareRecyclerViewAdapter adapter = new ShareRecyclerViewAdapter(phoneImageList, requireActivity(), this);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 3));
                        recyclerView.setAdapter(adapter);

                        SetDefaultImagePreview();
                    }
                else
                    {
                        MyPermissionHelper.RequestStoragePermission(requireActivity());
                    }
            }

        @Override
        public void OnViewClicked(View v, int position)
            {
                selectedPhoneImage = phoneImageList.get(position);
                LoadPreviewImage(selectedPhoneImage);
            }

        /**
         * Loads the clicked phoneImage in the recyclerview to the imageview **R.id.share_fragment_imageview**
         * @param phoneImage
         */
        private void LoadPreviewImage(PhoneImage phoneImage)
            {
                RequestOptions requestOptions = new RequestOptions().fitCenter().placeholder(R.drawable.ic_launcher_background);
                Glide.with(requireActivity()).load(phoneImage.getImageUrl()).apply(requestOptions).into(imageView);
                Log.d(TAG, "LoadPreviewImage: " + phoneImage.getImageUrl());
            }

        /**
         * This method loads the first image in the recyclerview into imageview preview when the fragment is first created.
         * It is overridden in the ShareRecyclerViewAdapter class onClickListener().
         */
        private void SetDefaultImagePreview()
            {
                PhoneImage firstPhoneImage = phoneImageList.get(0);
                selectedPhoneImage = firstPhoneImage;
                RequestOptions requestOptions = new RequestOptions().fitCenter().placeholder(R.drawable.ic_launcher_background);
                Glide.with(requireActivity()).load(firstPhoneImage.getImageUrl()).apply(requestOptions).into(imageView);
            }

        private void DoShareImage()
            {
                Log.d(TAG, "DoShareImage: Method called");
                Intent intent = new Intent(requireActivity(), ShareActivity.class);
                intent.putExtra("SELECTED_IMAGE_URL", selectedPhoneImage.getImageUrl());
                startActivity(intent);
            }


    }
