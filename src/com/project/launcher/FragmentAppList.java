package com.project.launcher;

import android.support.v4.app.Fragment;

import com.project.launcher.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentAppList extends Fragment {

	public static final String ARG_SECTION_NUMBER = "section_number";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_app_list, container, false);
		Bundle args = getArguments();
		return rootView;

	}
}
