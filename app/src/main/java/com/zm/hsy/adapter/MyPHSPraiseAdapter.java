package com.zm.hsy.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zm.hsy.R;
import com.zm.hsy.activity.MainActivity;
import com.zm.hsy.entity.AudioList;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.ActivityJumpControl;
import com.squareup.picasso.Picasso;

/**
 * Adapter 我赞过的
 * 
 * @author Administrator
 * 
 */
public class MyPHSPraiseAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private ArrayList<AudioList> albumList = new ArrayList<AudioList>();
	private int playcode;
	String playerpath;
	String playerpath1;

	public MyPHSPraiseAdapter(Context context,
			ArrayList<AudioList> albumList) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.albumList = albumList;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return albumList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = minflater.inflate(
					R.layout.activity_phs_praise_item, null);
			holder = new ViewHolder();
			holder.zanguo_addtime = (TextView) convertView
					.findViewById(R.id.zanguo_addtime_tv);
			holder.zanguo_nickname = (TextView) convertView
					.findViewById(R.id.zanguo_nickname);
			holder.zanguo_audioname = (TextView) convertView
					.findViewById(R.id.zanguo_audioname_tv);
			holder.zanguo_commentnumber = (TextView) convertView
					.findViewById(R.id.zanguo_commentnumber_tv);
			holder.zanguo_playamount = (TextView) convertView
					.findViewById(R.id.zanguo_playamount_tv);
			holder.zanguo_timelong = (TextView) convertView
					.findViewById(R.id.zanguo_timelong_tv);

			holder.zanguo_download = (ImageView) convertView
					.findViewById(R.id.zanguo_download_iv);
			holder.zanguo_cover = (RoundedImageView) convertView
					.findViewById(R.id.zanguo_cover_iv);

			holder.zanguo_ll = (LinearLayout) convertView
					.findViewById(R.id.zanguo_ll);

			holder.sing_line = (View) convertView
					.findViewById(R.id.sing_line);
			holder.zanguo_playstate = (TextView) convertView
					.findViewById(R.id.zanguo_playstate);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == 0) {
			holder.sing_line.setVisibility(View.GONE);
		}
		String addTime = albumList.get(position).getAddTime();
		String count = albumList.get(position).getCount();
		String blurb = albumList.get(position).getBlurb();
		String commentNumber = albumList.get(position).getCommentNumber();
		String playAmount = albumList.get(position).getPlayAmount();
		String timeLong = albumList.get(position).getTimeLong();
		String nickname = albumList.get(position).getNickname();

		String path = albumList.get(position).getPath();
		String audioName = albumList.get(position).getAudioName();
		String id = albumList.get(position).getId();
		String audioAlbumId = albumList.get(position).getAudioAlbumId();
		/** 记得放开 */
		playerpath = path;
		playerpath = "http://sc1.111ttt.com/2015/1/05/10/98101254356.mp3";
		final String mcover;
		final String mblurb = blurb;
		final String sname = audioName;
		final String mid = id;
		final String maudioAlbumId = audioAlbumId;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = df.parse(addTime);
			Date curDate = new Date(System.currentTimeMillis());
			long diff = curDate.getTime() - d1.getTime();// 这样得到的差值是微秒级别
			long days = diff / (1000 * 60 * 60 * 24);
			if (days >= 1) {
				holder.zanguo_addtime.setText(days + "天前");
			} else {
				long hours = diff / (1000 * 60 * 60);
				if (hours < 1) {
					holder.zanguo_addtime.setText("刚刚");
				} else {
					holder.zanguo_addtime.setText(hours + "小时前");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		holder.zanguo_nickname.setText("by" + nickname);
		holder.zanguo_audioname.setText(audioName);
		holder.zanguo_commentnumber.setText(count);
		holder.zanguo_playamount.setText(playAmount);
		holder.zanguo_timelong.setText(timeLong);
		holder.zanguo_download.setTag(path);

		String cover = albumList.get(position).getCover();// 封面图
		mcover = cover;
		cover = URLManager.COVER_URL + cover;
		Picasso.with(context).load(cover).resize(400, 400)
				.placeholder(R.mipmap.details_img3)
				.error(R.mipmap.details_img3).into(holder.zanguo_cover);

//		holder.zanguo_cover.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				String apppath = mapplication.getPlayerpath();
//				if (!playerpath.equals(apppath)) {// 是否在播放---判断url是否相同
//
//					MainActivity.player.playUrl(playerpath);
//					playerpath1 = playerpath;
//					mapplication.setPlayerpath(playerpath1);
//					AudioList al = new AudioList();
//					al.setPath(playerpath);
//					al.setBlurb(mblurb);
//					al.setAudioName(sname);
//					al.setCover(mcover);
//					al.setId(mid);
//					al.setAudioAlbumId(maudioAlbumId);
//					mapplication.setContextAudioList(al);
//					ArrayList<String> sKey = Futil
//							.loadKeyArray((Activity) context,"2");
//					String key = maudioAlbumId + "_" + mid;
//					sKey.remove(key);
//					sKey.add(key);
//					Futil.saveKeyArray((Activity) context, sKey, "2");
//					Futil.saveValue((Activity) context, key + "id", mid);
//					Futil.saveValue((Activity) context, key + "audioName",
//							sname);
//					Futil.saveValue((Activity) context, key + "cover",
//							mcover);
//					Futil.saveValue((Activity) context, key + "path",
//							playerpath);
//					Futil.saveValue((Activity) context, key + "blurb",
//							mblurb);
//					Futil.saveValue((Activity) context, key
//							+ "audioAlbumId", maudioAlbumId);
//					Date date = new Date(System.currentTimeMillis());
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//					final String tim = sdf.format(date);
//					Futil.saveValue((Activity) context, key + "addTime", tim);
//				}
//				int pcode = mapplication.getPlaycode();
//				System.out.println("pcode--" + pcode);
//				if (pcode == 1) {// Playcode 0=已播放 1已暂停
//					setcontextaudio(true);
//					System.out.println("播放--" + pcode);
//					MainActivity.player.play();
//					playcode = 0;
//				} else {
//					setcontextaudio(true);
//					System.out.println("暂停--" + pcode);
//					MainActivity.player.pause();
//					playcode = 1;
//				}
//				mapplication.setPlaycode(playcode);
//
//			}
//		});
		holder.zanguo_ll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg1) {
				ActivityJumpControl.getInstance((Activity) context)
						.gotoDetailsPlayActivity(
								albumList.get(position).getId());
			}
		});
		return convertView;
	}

	class ViewHolder {
		private LinearLayout zanguo_ll;
		// private RelativeLayout letter_all_rl1;
		private View sing_line;
		private RoundedImageView zanguo_cover;
		private ImageView zanguo_download, zanguo_paly;
		private TextView zanguo_addtime, zanguo_audioname,
				zanguo_commentnumber, zanguo_playamount, zanguo_timelong,
				zanguo_playstate, zanguo_nickname;
	}

}