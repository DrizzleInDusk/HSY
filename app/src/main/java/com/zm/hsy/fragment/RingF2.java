package com.zm.hsy.fragment;

import com.zm.hsy.R;
import com.zm.hsy.R.layout;
import com.zm.hsy.util.ActivityJumpControl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RingF2 extends Fragment implements OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.ringf2, container, false);

		initView(view);

		return view;
	}

	private RelativeLayout item1_rl;
	private TextView ring2_item1_goin;

	private void initView(View v) {
		item1_rl = (RelativeLayout) v.findViewById(R.id.item1_rl);
		ring2_item1_goin = (TextView) v.findViewById(R.id.ring2_item1_goin);
		item1_rl.setOnClickListener(this);
	}

	private String toptv;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.item1_rl:
			toptv = ring2_item1_goin.getText().toString();
			System.out.println("RingF2-toptv-"+toptv);
			ActivityJumpControl.getInstance(getActivity())
					.gotoRingF2ListActivity(toptv);
			break;
		default:
			break;
		}
	}

}
