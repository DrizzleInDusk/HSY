package com.zm.hsy.fragment;

import com.zm.hsy.R;
import com.zm.hsy.R.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class RingF4 extends Fragment implements OnClickListener {

	 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
 

		View view = inflater.inflate(R.layout.ringf4, container, false);

		initView(view); 

		return view;
	}

	private void initView(View view) {
		 
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.button1:
//			break;
		default:
			break;
		}
	}

 
}
