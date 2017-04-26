package com.zm.hsy.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zm.hsy.R;
import com.zm.hsy.util.ActivityJumpControl;

public class TabVF2 extends Fragment implements OnClickListener {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.tab_pager_yinpin, container,
				false);

		initView(view);
		return view;
	}
	private RelativeLayout music, letter, movie, fashion, children;
	private LinearLayout games, news, faith, emotion, history, low, education,
			drama, health,traveling,arts;

	private void initView(View view) {
		// 快速注册
		music = (RelativeLayout) view.findViewById(R.id.music);
		letter = (RelativeLayout) view.findViewById(R.id.letter);
		movie = (RelativeLayout) view.findViewById(R.id.movie);
		fashion = (RelativeLayout) view.findViewById(R.id.fashion);
		children = (RelativeLayout) view.findViewById(R.id.children);

		games = (LinearLayout) view.findViewById(R.id.games);
		news = (LinearLayout) view.findViewById(R.id.news);
		faith = (LinearLayout) view.findViewById(R.id.faith);
		emotion = (LinearLayout) view.findViewById(R.id.emotion);
		history = (LinearLayout) view.findViewById(R.id.history);
		low = (LinearLayout) view.findViewById(R.id.low);
		education = (LinearLayout) view.findViewById(R.id.education);
		drama = (LinearLayout) view.findViewById(R.id.drama);
		health = (LinearLayout) view.findViewById(R.id.health);
		arts = (LinearLayout) view.findViewById(R.id.arts);
		traveling = (LinearLayout) view.findViewById(R.id.traveling);

		// 绑定监听器
		music.setOnClickListener(this);
		letter.setOnClickListener(this);
		movie.setOnClickListener(this);
		fashion.setOnClickListener(this);
		children.setOnClickListener(this);

		games.setOnClickListener(this);
		news.setOnClickListener(this);
		faith.setOnClickListener(this);
		emotion.setOnClickListener(this);
		history.setOnClickListener(this);
		low.setOnClickListener(this);
		education.setOnClickListener(this);
		drama.setOnClickListener(this);
		health.setOnClickListener(this);
		arts.setOnClickListener(this);
		traveling.setOnClickListener(this);

	}

	private String Tag = null;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.music:
			Tag = "1";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF2Activity(
					Tag, "音乐");
			break;
		case R.id.movie:
			Tag = "23";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF2Activity(
					Tag, "影视娱乐");
			break;
		case R.id.letter:
			Tag = "3";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF2Activity(
					Tag, "文学");
			break;
		case R.id.fashion:
			Tag = "24";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF2Activity(
					Tag, "时尚生活");
			break;
		case R.id.children:
			Tag = "5";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF2Activity(
					Tag, "儿童");
			break;
		case R.id.games:
			Tag = "25";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF2Activity(
					Tag, "游戏");
			break;
		case R.id.news:
			Tag = "2";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF2Activity(
					Tag, "新闻资讯");
			break;
		case R.id.faith:
			Tag = "27";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF2Activity(
					Tag, "宗教");
			break;
		case R.id.emotion:
			Tag = "22";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF2Activity(
					Tag, "情感生活");
			break;
		case R.id.history:
			Tag = "4";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF2Activity(
					Tag, "历史");
			break;
		case R.id.low:
			Tag = "26";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF2Activity(
					Tag, "法律");
			break;
		case R.id.education:
			Tag = "28";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF2Activity(
					Tag, "教育");
			break;
		case R.id.drama:
			Tag = "20";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF2Activity(
					Tag, "戏曲");
			break;
		case R.id.health:
			Tag = "21";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF2Activity(
					Tag, "健康");
			break;
		case R.id.arts:
			Tag = "103";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF2Activity(
					Tag, "语言艺术");
			break;
		case R.id.traveling:
			Tag = "102";
			ActivityJumpControl.getInstance(getActivity()).gotoTabVF2Activity(
					Tag, "旅游");
			break;
		default:
			break;
		}
	}

}
