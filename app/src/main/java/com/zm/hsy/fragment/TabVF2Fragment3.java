package com.zm.hsy.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.zm.hsy.R;
import com.zm.hsy.adapter.AlbumListAdapter;
import com.zm.hsy.entity.Album;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TabVF2Fragment3 extends Fragment {
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.three) {
				JSONObject object = (JSONObject) msg.obj;
				try {
					JSONArray audioAlbumList = object
							.getJSONArray("audioAlbumList");
					for (int i = 0; i < audioAlbumList.length(); i++) {
						Album album = new Album();
						JSONObject data = audioAlbumList.getJSONObject(i);

						String cover = data.getString("cover");
						String albumName = data.getString("albumName");
						String blurb = data.getString("blurb");
						String playAmount = data.getString("playAmount");
						String episode = data.getString("episode");
						String id = data.getString("id");

						album.setId(id);
						album.setAlbumName(albumName);
						album.setBlurb(blurb);
						album.setPlayAmount(playAmount);
						album.setEpisode(episode);
						album.setCover(cover);
						albumList.add(album);
					}

					adapter = new AlbumListAdapter(getActivity(), albumList);
					mlistView.setAdapter(adapter);

					adapter.notifyDataSetChanged();
					stopProgressDialog();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
//				ml();
			}
			stopProgressDialog();
		}

		private void ml() {
			// TODO Auto-generated method stub
			mlistView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					ActivityJumpControl.getInstance(getActivity())
							.gotoDetailsActivity(albumList.get(arg2).getId());
				}
			});
		}
	};

	private ArrayList<Album> albumList;
	private AlbumListAdapter adapter;
	private ListView mlistView;
	private String audioAlbumRecommendId = "";
	private String key = "id";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.letter_albumlist, container,
				false);
		mlistView = (ListView) view
				.findViewById(R.id.letter_albumlist_content_view);
		mlistView.setFocusable(false);

		albumList = new ArrayList<Album>();

		Bundle bundle = getArguments();
		// Log.i("LF3——bundle", "" + bundle);
		audioAlbumRecommendId = bundle.getString(key);
		gointo();

		return view;
	}

	@SuppressLint("LongLogTag")
	private void gointo() {
		startProgressDialog();
		albumList.clear();
		String strUrl = URLManager.AlbumByWidth;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("audioAlbumRecommendId", audioAlbumRecommendId);
		Futil.xutils(strUrl, map, handler, URLManager.three);
		Log.i("LetterF3_audioAlbumRecommendId", audioAlbumRecommendId);
	}

	/**
	 * 等待页
	 */
	private CustomProgressDialog progressDialog;

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(getActivity());
			progressDialog.setMessage("加载中...");
		}

		progressDialog.show();
	}

	private void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

}
