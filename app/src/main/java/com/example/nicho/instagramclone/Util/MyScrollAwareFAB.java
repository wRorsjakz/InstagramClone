package com.example.nicho.instagramclone.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

public class MyScrollAwareFAB extends CoordinatorLayout.Behavior<FloatingActionButton>
    {
        /**
         * This class makes the FAB scroll aware, thus when scrolling up, it disappears. When scrolling down, the FAB reappears.
         * To use:
         * someFAB = view.findViewById(R.id.some_fab);
         * CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) someFAB.getLayoutParams();
         * layoutParams.setBehavior(new MyScrollAwareFAB());
         * someFAB.setLayoutParams(layoutParams);
         */
        public MyScrollAwareFAB()
            {
            }

        public MyScrollAwareFAB(Context context, AttributeSet attrs)
            {
                super(context, attrs);
            }

        @Override
        public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type)
            {
                return true;
            }

        @Override
        public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull final FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type)
            {
                super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
                if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
                    // User scrolled down and the FAB is currently visible -> hide the FAB
                    child.hide(new FloatingActionButton.OnVisibilityChangedListener()
                        {
                            @Override
                            public void onShown(FloatingActionButton fab)
                                {
                                    super.onShown(fab);
                                }

                            @Override
                            public void onHidden(FloatingActionButton fab)
                                {
                                    super.onHidden(fab);
                                    child.setVisibility(View.INVISIBLE);
                                }
                        });
                } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
                    // User scrolled up and the FAB is currently not visible -> show the FAB
                    child.show();
                }
            }
    }
