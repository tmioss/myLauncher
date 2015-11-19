/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.project.launcher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.project.launcher.R;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

	private static final int MULTI_TOUCH_POINT = 3;

	private String[] mTabnames;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the three primary sections of the app. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will display the three primary sections of the
	 * app, one at a time.
	 */
	ViewPager mViewPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		String[] tabnames = getResources().getStringArray(R.array.tab_name_array);

		// Create the adapter that will return a fragment for each of the three
		// primary sections
		// of the app.
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager(), tabnames);

		// Set up the ViewPager, attaching the adapter and setting up a listener
		// for when the
		// user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);

		mViewPager.setOnTouchListener(new MultiOnTouchListener(mViewPager));

		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {

				Log.w(TAG, "Switch to page" + position);

			}
		});

	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		private String[] mTabnames;

		public AppSectionsPagerAdapter(FragmentManager fm, String[] tabnames) {
			super(fm);
			mTabnames = tabnames;

		}

		@Override
		public Fragment getItem(int i) {

			Bundle args = new Bundle();

			switch (i) {
			case 0:
				Fragment speedfragment = new FragmentSpeed();
				args.putInt(FragmentSpeed.ARG_SECTION_NUMBER, i + 1);
				speedfragment.setArguments(args);
				return speedfragment;

			case 1:
				Fragment navigationfragment = new FragmentNavigation();

				args.putInt(FragmentSpeed.ARG_SECTION_NUMBER, i + 1);
				navigationfragment.setArguments(args);
				return navigationfragment;

			case 2:
				Fragment applistfragment = new FragmentAppList();
				args.putInt(FragmentSpeed.ARG_SECTION_NUMBER, i + 1);
				applistfragment.setArguments(args);
				return applistfragment;

			default:
				Log.e(TAG, "scrolling exceed!!!");
				return null;

			}

		}

		@Override
		public int getCount() {
			return 3;

		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mTabnames[position];

		}
	}

	private class MultiOnTouchListener implements View.OnTouchListener {

		private float mMinMove = 50;
		// private float mMinVelocity = 0;

		private ViewPager mViewPager;

		private static final int DIRECTION_NONE = 0;
		private static final int DIRECTION_LEFT = 1;
		private static final int DIRECTION_RIGHT = 2;

		protected HashMap<Integer, PointF> mTouchPointOld = new HashMap<Integer, PointF>();
		protected HashMap<Integer, PointF> mTouchPointNew = new HashMap<Integer, PointF>();

		private boolean mIsStartDetect;

		public MultiOnTouchListener(ViewPager viewpager) {
			mIsStartDetect = false;
			mViewPager = viewpager;
		}

		private void printCurrentPoint(HashMap<Integer, PointF> map) {
			Iterator<Entry<Integer, PointF>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, PointF> entry = iterator.next();
				Log.w(TAG, "pointer " + entry.getKey() + ":" + entry.getValue().x + ", " + entry.getValue().y);
			}
		}

		private int calcDirection() {

			int direction = DIRECTION_NONE;

			if (mTouchPointNew.size() != MULTI_TOUCH_POINT) {
				return DIRECTION_NONE;
			}

			Iterator<Entry<Integer, PointF>> iterator = mTouchPointNew.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, PointF> entry = iterator.next();

				float distance = entry.getValue().x - mTouchPointOld.get(entry.getKey()).x;
				if (distance > mMinMove) {
					if (direction == DIRECTION_NONE) {
						direction = DIRECTION_RIGHT;
					} else if (direction == DIRECTION_LEFT) {
						return DIRECTION_NONE;
					}
				} else if (distance < 0 && Math.abs(distance) > mMinMove) {
					if (direction == DIRECTION_NONE) {
						direction = DIRECTION_LEFT;
					} else if (direction == DIRECTION_RIGHT) {
						return DIRECTION_NONE;
					}
				} else {
					return DIRECTION_NONE;
				}
			}

			return direction;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction() & MotionEvent.ACTION_MASK) {

			// one finger press down
			case MotionEvent.ACTION_DOWN:

				// Log.v(TAG, "MotionEvent.ACTION_DOWN");

				break;

			case MotionEvent.ACTION_POINTER_DOWN:

				int pointCount = event.getPointerCount();

				if (pointCount == MULTI_TOUCH_POINT && mIsStartDetect == false) {
					// Log.v(TAG, "three finger press down");

					// set flag to true
					mIsStartDetect = true;

					// now we need set current point as
					for (int i = 0; i < MULTI_TOUCH_POINT; i++) {
						Integer id = Integer.valueOf(event.getPointerId(i));

						PointF pointold = new PointF(event.getX(i), event.getY(i));
						mTouchPointOld.put(id, pointold);

						PointF pointnew = new PointF(event.getX(i), event.getY(i));
						mTouchPointNew.put(id, pointnew);
					}
					printCurrentPoint(mTouchPointOld);
				}
				break;

			case MotionEvent.ACTION_MOVE:
				// Log.v(TAG, "MotionEvent.ACTION_MOVE");
				break;

			case MotionEvent.ACTION_POINTER_UP:

				// Log.v(TAG, "MotionEvent.ACTION_POINTER_UP");

				if (mIsStartDetect == true && mTouchPointOld.size() == MULTI_TOUCH_POINT) {
					for (int i = 0; i < event.getPointerCount(); i++) {
						Integer id = Integer.valueOf(event.getPointerId(i));
						if (mTouchPointNew.containsKey(id)) {
							mTouchPointNew.get(id).x = event.getX(i);
							mTouchPointNew.get(id).y = event.getY(i);
						}
					}
				}

				break;

			case MotionEvent.ACTION_UP:

				// it is the last finger up
				if (mIsStartDetect == true && mTouchPointOld.size() == MULTI_TOUCH_POINT) {
					mIsStartDetect = false;

					for (int i = 0; i < event.getPointerCount(); i++) {
						Integer id = Integer.valueOf(event.getPointerId(i));
						if (mTouchPointNew.containsKey(id)) {
							mTouchPointNew.get(id).x = event.getX(i);
							mTouchPointNew.get(id).y = event.getY(i);
						}
					}
					printCurrentPoint(mTouchPointNew);

					int direction = calcDirection();
					if (direction == DIRECTION_NONE) {
						Log.w(TAG, "none");
					} else if (direction == DIRECTION_RIGHT) {

						int item = mViewPager.getCurrentItem();
						if (item > 0) {
							mViewPager.setCurrentItem(item - 1);
						}

						Log.w(TAG, "right");
						return true;
					} else if (direction == DIRECTION_LEFT) {
						
						int item = mViewPager.getCurrentItem();
						if (item < 3) {
							mViewPager.setCurrentItem(item + 1);
						}
						
						Log.w(TAG, "left");
						return true;
					}
				}

				break;
			default:
			}

			return true;

		}
	}

}
