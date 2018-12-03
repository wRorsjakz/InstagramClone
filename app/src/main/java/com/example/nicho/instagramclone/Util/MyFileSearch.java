package com.example.nicho.instagramclone.Util;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.nicho.instagramclone.Model.PhoneImage;

import java.util.ArrayList;
import java.util.List;

public class MyFileSearch
    {
        /**
         * This method gets all the URI paths for images in the phone's storage
         * @param activity
         * @return
         */
        public static List<String> GetAllImagePath(Activity activity)
            {
                Uri uri;
                Cursor cursor;
                int column_index_data, column_index_folder_name;
                ArrayList<String> listOfAllImages = new ArrayList<String>();
                String absolutePathOfImage = null;
                uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                String[] projection = { MediaStore.MediaColumns.DATA,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

                cursor = activity.getContentResolver().query(uri, projection, null,
                        null, null);

                column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                column_index_folder_name = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                while (cursor.moveToNext()) {
                    absolutePathOfImage = cursor.getString(column_index_data);

                    listOfAllImages.add(absolutePathOfImage);
                }
                return listOfAllImages;
            }

        /**
         * This method gets the URI String of each image from a List<String> and creates a new **PhoneImage** class for it.
         * @param listOfImagePath
         * @return
         */
        public static List<PhoneImage> GetAllImages(List<String> listOfImagePath)
            {
                List<PhoneImage> listOfPhoneImages = new ArrayList<>();

                if(listOfImagePath != null && !listOfImagePath.isEmpty())
                {
                    for(int i = 0; i < listOfImagePath.size(); i++)
                        {
                            String imageUrl = listOfImagePath.get(i);
                            listOfPhoneImages.add(new PhoneImage(imageUrl));
                        }
                }
                return listOfPhoneImages;
            }
    }

