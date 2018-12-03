package com.example.nicho.instagramclone.Util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.example.nicho.instagramclone.Model.PhoneImage;
import com.example.nicho.instagramclone.R;

import java.util.ArrayList;
import java.util.List;

public class MyViewHelper
    {
        public static void ProgressBarDelayed(int delayInMilliSeconds, final ProgressBar progressBar)
            {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                            {
                                progressBar.setVisibility(View.GONE);
                            }
                    },delayInMilliSeconds);
            }

        public static int GetScreenWidth(Context context)
            {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                return displayMetrics.widthPixels;
            }

        public static int GetScreenHeight(Context context)
            {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                return displayMetrics.heightPixels;
            }

        public static List<PhoneImage> SetupSampleImageList(Context context)
            {
                List<PhoneImage> list = new ArrayList<>();
                list = new ArrayList<>();
                String[] urlArray = context.getResources().getStringArray(R.array.sameple_urls);

                for (int i = 0; i < urlArray.length; i++)
                    {
                        list.add(new PhoneImage(urlArray[i]));
                    }
                return list;
            }

        public static void CloseKeyBoard(Activity activity)
            {
                View view = activity.getCurrentFocus();
                if(view != null)
                    {
                        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
            }

        public static void OpenKeyBoard(Activity activity)
            {
                View view = activity.getCurrentFocus();
                if(view != null)
                    {
                        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
            }

        public static void SetupScrollableFAB(FloatingActionButton floatingActionButton)
            {
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) floatingActionButton.getLayoutParams();
                layoutParams.setBehavior(new MyScrollAwareFAB());
                floatingActionButton.setLayoutParams(layoutParams);
            }
    }
