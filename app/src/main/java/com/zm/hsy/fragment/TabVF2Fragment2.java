package com.zm.hsy.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.zm.hsy.R;
import com.zm.hsy.activity.TabVF2Activity;
import com.zm.hsy.adapter.AlbumListAdapter;
import com.zm.hsy.adapter.LF2Adapter;
import com.zm.hsy.entity.Album;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;

public class TabVF2Fragment2 extends Fragment implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int size = 0;
            if (msg.what == URLManager.two) {
                JSONObject object = (JSONObject) msg.obj;
                try {
                    // Log.i("object", ""+object);
                    JSONArray data = object.getJSONArray("albumRecommendList");
                    for (int i = 0; i < data.length(); i++) {
                        Album album = new Album();
                        JSONObject object1 = data.getJSONObject(i);
                        String commendId = object1.getString("commendId");
                        String commendName = object1.getString("commendName");
                        JSONObject audioAlbum = object1
                                .getJSONObject("audioAlbum");
                        String audioAlbumCover = audioAlbum.getString("cover");
                        String audioAlbumName = audioAlbum
                                .getString("albumName");
                        String audioAlbumBlurb = audioAlbum.getString("blurb");
                        String audioAlbumPlayAmount = audioAlbum
                                .getString("playAmount");
                        String audioAlbumEpisode = audioAlbum
                                .getString("episode");
                        String id = audioAlbum.getString("id");
                        List<Album> list = albumMap.get(commendId);
                        if (list == null) {
                            list = new ArrayList<Album>();
                            Album title = new Album();
                            title.setCommendName(commendName);
                            title.setCommendId(commendId);
                            list.add(title);
                            size++;
                            albumMap.put(commendId, list);
                        }
                        album.setId(id);
                        album.setAlbumName(audioAlbumName);
                        album.setBlurb(audioAlbumBlurb);
                        album.setPlayAmount(audioAlbumPlayAmount);
                        album.setEpisode(audioAlbumEpisode);
                        album.setCover(audioAlbumCover);
                        list.add(album);
                        size++;
                    }
                    adapter = new LF2Adapter(getActivity(), albumMap, size);
                    mlistView.setAdapter(adapter);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                try {
                    JSONArray data = object.getJSONArray("albumSRecommendList");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.getJSONObject(i);
                        Album album = new Album();

                        String cover = obj.getString("cover");
                        String albumName = obj.getString("albumName");
                        String blurb = obj.getString("blurb");
                        String playAmount = obj.getString("playAmount");
                        String episode = obj.getString("episode");
                        String id = obj.getString("id");

                        album.setId(id);
                        album.setAlbumName(albumName);
                        album.setBlurb(blurb);
                        album.setPlayAmount(playAmount);
                        album.setEpisode(episode);
                        album.setCover(cover);
                        albumList.add(album);
                    }
                    jxadapter = new AlbumListAdapter(getActivity(), albumList);
                    jingxuan_viewp.setAdapter(jxadapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            stopProgressDialog();
        }
    };
    public String TAG, TopName;
    private Map<String, List<Album>> albumMap;
    Timer time = new Timer();
    private LF2Adapter adapter;
    private AlbumListAdapter jxadapter;
    private ArrayList<Album> albumList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        TAG = bundle.getString("Tag");
        TopName = bundle.getString("TopName");
        View view = inflater.inflate(R.layout.tabvf2_tuijian, container, false);
        albumList = new ArrayList<Album>();
        albumMap = new HashMap<String, List<Album>>();
        initView(view);
        gointo();
        return view;
    }

    private ListView mlistView, jingxuan_viewp;
    private TextView jingxuan_name,tabvf2_top50tv;

    private void initView(View view) {
        jingxuan_viewp = (ListView) view
                .findViewById(R.id.letter_jingxuan_viewp);
        jingxuan_viewp.setFocusable(false);
        mlistView = (ListView) view
                .findViewById(R.id.letter_tuijian_content_view);
        mlistView.setFocusable(false);
        view.findViewById(R.id.tabvf2_tuijian).setOnClickListener(this);
        jingxuan_name = (TextView) view.findViewById(R.id.jingxuan_name);
        jingxuan_name.setText(TopName + "精选");
        tabvf2_top50tv = (TextView) view.findViewById(R.id.tabvf2_top50tv);
        tabvf2_top50tv.setText(TopName + "每日TOP50");
    }

    private void gointo() {
        startProgressDialog();
        String strUrl = URLManager.TuiJian;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("albumTypeId", TAG);
        Futil.xutils(strUrl, map, handler, URLManager.two);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tabvf2_tuijian:
                ActivityJumpControl.getInstance(getActivity()).gotoTop50Activity(((TabVF2Activity)getActivity()).Tag);
                break;
        }
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
