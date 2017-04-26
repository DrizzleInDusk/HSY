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

public class TabVF3 extends Fragment implements OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.tab_pager_shipin, container,
				false);

		initView(view);

		return view;
	}

	private RelativeLayout tiny, readily, recreation, health, business;
	private RelativeLayout shipin_music, shipin_education, shipin_hsy,
			shipin_other;

	private void initView(View v) {
		tiny = (RelativeLayout) v.findViewById(R.id.tiny);
		tiny.setOnClickListener(this);// 微电影
		readily = (RelativeLayout) v.findViewById(R.id.readily);
		readily.setOnClickListener(this);// 随手拍
		recreation = (RelativeLayout) v.findViewById(R.id.recreation);
		recreation.setOnClickListener(this);// 娱乐
		health = (RelativeLayout) v.findViewById(R.id.health);
		health.setOnClickListener(this);// 健康
		business = (RelativeLayout) v.findViewById(R.id.business);
		business.setOnClickListener(this);// 财经

		shipin_music = (RelativeLayout) v.findViewById(R.id.shipin_music);
		shipin_music.setOnClickListener(this);// 音乐
		shipin_education = (RelativeLayout) v
				.findViewById(R.id.shipin_education);
		shipin_education.setOnClickListener(this);// 教育
		shipin_hsy = (RelativeLayout) v.findViewById(R.id.shipin_hsy);
		shipin_hsy.setOnClickListener(this);// 慧思语专区
		shipin_other = (RelativeLayout) v.findViewById(R.id.shipin_other);
		shipin_other.setOnClickListener(this);// 其他
	}

	private String Tag = null;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tiny:// 微电影
			Tag = "95";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF3Activity(
					Tag, "微电影");
			break;
		case R.id.readily:// 随手拍
			Tag = "94";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF3Activity(
					Tag, "随手拍");
			break;
		case R.id.recreation:// 娱乐
			Tag = "98";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF3Activity(
					Tag, "娱乐");
			break;
		case R.id.health:// 健康
			Tag = "99";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF3Activity(
					Tag, "健康");
			break;
		case R.id.business:// 财经
			Tag = "100";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF3Activity(
					Tag, "财经");
			break;
		case R.id.shipin_music:// 音乐
			Tag = "96";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF3Activity(
					Tag, "音乐");
			break;
		case R.id.shipin_education:// 教育
			Tag = "97";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF3Activity(
					Tag, "教育");
			break;
		case R.id.shipin_hsy:// 慧思语专区
			Tag = "93";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF3Activity(
					Tag, "慧思语专区");
			break;
		case R.id.shipin_other:// 其他
			Tag = "101";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF3Activity(
					Tag, "其他");
			break;
		default:
			break;
		}
	}

}
