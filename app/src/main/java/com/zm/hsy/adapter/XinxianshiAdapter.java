package com.zm.hsy.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zm.hsy.R;
import com.zm.hsy.entity.Fresh;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.ActivityJumpControl;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Adapter 新鲜事
 * 
 * @author Administrator
 * 
 */
public class XinxianshiAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private ArrayList<Fresh> freshArrayList;

	public XinxianshiAdapter(Context context, ArrayList<Fresh> freshArrayList) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.freshArrayList = freshArrayList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return freshArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.activity_xinxianshi_item, null);
			holder = new ViewHolder();
			holder.guanzhu_item_audio = (TextView) convertView
					.findViewById(R.id.guanzhu_item_audio);
			holder.guanzhu_item_nickname = (TextView) convertView
					.findViewById(R.id.guanzhu_item_nickname);
			holder.guanzhu_item_fans = (TextView) convertView
					.findViewById(R.id.guanzhu_item_fans);
			holder.guanzhu_item_zuiduo = (TextView) convertView
					.findViewById(R.id.guanzhu_item_zuiduo);
			holder.guanzhu_item_head = (RoundedImageView) convertView
					.findViewById(R.id.guanzhu_item_head);
			holder.guanzhu_item_cutandadd_press = (ImageView) convertView
					.findViewById(R.id.guanzhu_item_cutandadd_press);


			holder.xxsalbum_item_cover = (ImageView) convertView
					.findViewById(R.id.xxsalbum_item_cover);
			holder.xxsalbum_item_albumName = (TextView) convertView
					.findViewById(R.id.xxsalbum_item_albumName);
			holder.xxsalbum_item_playAmount = (TextView) convertView
					.findViewById(R.id.xxsalbum_item_playAmount);

			holder.xxsaudio_item_audioName = (TextView) convertView
					.findViewById(R.id.xxsaudio_item_audioName);
			holder.xxsaudio_item_playAmount = (TextView) convertView
					.findViewById(R.id.xxsaudio_item_playAmount);
			holder.xxsaudio_item_cover = (RoundedImageView) convertView
					.findViewById(R.id.xxsaudio_item_cover);

			holder.xxsalbum_item = (LinearLayout) convertView
					.findViewById(R.id.xxsalbum_item);
			holder.xxsaudio_item = (LinearLayout) convertView
					.findViewById(R.id.xxsaudio_item);
			holder.guanzhu_item = (LinearLayout) convertView
					.findViewById(R.id.guanzhu_item);

			holder.xxsitem_cover = (RoundedImageView) convertView
					.findViewById(R.id.xxsitem_cover);
			holder.xxsitem_name = (TextView) convertView
					.findViewById(R.id.xxsitem_name);
			holder.xxsitem_tv = (TextView) convertView
					.findViewById(R.id.xxsitem_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String nickname = freshArrayList.get(position).getGname();
		String albumName = freshArrayList.get(position).getAlbumName();
		String audioName = freshArrayList.get(position).getAudioName();
		String wname = freshArrayList.get(position).getWname();
		String whead = freshArrayList.get(position).getWhead();
		String wtype = freshArrayList.get(position).getWheadStatus();
		if (!wtype.equals("http")) {
			whead = URLManager.Head_URL + whead;
		}
		Picasso.with(context).load(whead).resize(400, 400)
				.placeholder(R.mipmap.details_img3)
				.error(R.mipmap.details_img3).into(holder.xxsitem_cover);
		holder.xxsitem_name.setText(wname);
		if(!albumName.equals("null")&&albumName!=null){
			holder.xxsitem_tv.setText("订阅了1张专辑");
			holder.xxsalbum_item.setVisibility(View.VISIBLE);
			holder.xxsaudio_item.setVisibility(View.GONE);
			holder.guanzhu_item.setVisibility(View.GONE);
			String payamount = freshArrayList.get(position).getAlbumPlay();
			holder.xxsalbum_item_albumName.setText(albumName);
			if (payamount != null && !payamount.equals("")
					&& !payamount.equals("null")) {
				int p = Integer.parseInt(payamount);
				if (p >= 10000) {
					int pp = p / 10000;
					BigDecimal bd = new BigDecimal(pp + "");
					bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);

					holder.xxsalbum_item_playAmount.setText(bd + "万人");
				} else if (p < 10000 && p > 0) {
					holder.xxsalbum_item_playAmount.setText(p + "人");
				} else {
					holder.xxsalbum_item_playAmount.setText("0人");
				}
			} else {
				holder.xxsalbum_item_playAmount.setText("0人");
			}

			String cover = freshArrayList.get(position).getAlbumCover();// 封面图
			cover = URLManager.COVER_URL + cover;
			Picasso.with(context).load(cover).resize(400, 400)
					.placeholder(R.mipmap.letter_item_img1)
					.error(R.mipmap.letter_item_img1)
					.into(holder.xxsalbum_item_cover);
			holder.xxsalbum_item.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg1) {
					ActivityJumpControl.getInstance((Activity) context)
							.gotoDetailsActivity(freshArrayList.get(position).getAlbumId());
				}
			});
		}
		if(!audioName.equals("null")&&audioName!=null){
			holder.xxsalbum_item.setVisibility(View.GONE);
			holder.xxsaudio_item.setVisibility(View.VISIBLE);
			holder.guanzhu_item.setVisibility(View.GONE);
			holder.xxsitem_tv.setText("赞了1个声音");


			String cover = freshArrayList.get(position).getAudioCover();// 封面图
			String byName = freshArrayList.get(position).getByName();// 封面图
			cover = URLManager.COVER_URL + cover;
			Picasso.with(context).load(cover).resize(400, 400)
					.placeholder(R.mipmap.details_img3)
					.error(R.mipmap.details_img3).into(holder.xxsaudio_item_cover);

			holder.xxsaudio_item_audioName.setText(audioName);
			holder.xxsaudio_item_playAmount.setText(" By "+byName);
			holder.xxsaudio_item.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg1) {
					ActivityJumpControl.getInstance((Activity) context)
							.gotoDetailsPlayActivity(
									freshArrayList.get(position).getAudioId());
				}
			});
		}
		if(!nickname.equals("null")&&nickname!=null){
			holder.xxsalbum_item.setVisibility(View.GONE);
			holder.xxsaudio_item.setVisibility(View.GONE);
			holder.guanzhu_item.setVisibility(View.VISIBLE);
			holder.xxsitem_tv.setText("关注了1个人");

			String ifgz = freshArrayList.get(position).getConcemStatus();// 关注标识
			if (ifgz.equals("0")) {
			holder.guanzhu_item_cutandadd_press.setSelected(true);
			} else {
				holder.guanzhu_item_cutandadd_press.setSelected(false);
			}
			String concemFans = freshArrayList.get(position).getFans();// 粉丝
			String audioCount = freshArrayList.get(position).getAudioCount();// 声音
			String belong = freshArrayList.get(position).getBelong();
			holder.guanzhu_item_zuiduo.setVisibility(View.GONE);
			System.out.println("belong>>>>"+belong);
			if(belong!=null&&!belong.equals("null")&&!belong.equals("")){
				holder.guanzhu_item_zuiduo.setVisibility(View.VISIBLE);
				holder.guanzhu_item_zuiduo.setText(belong);
			}else{
				holder.guanzhu_item_zuiduo.setVisibility(View.INVISIBLE);
			}
			holder.guanzhu_item_audio.setText("声音  " + audioCount);
			holder.guanzhu_item_nickname.setText(nickname);
			holder.guanzhu_item_fans.setText("粉丝  " + concemFans);
			String cover = freshArrayList.get(position).getGhead();// 封面图
			String type = freshArrayList.get(position).getGheadStatus();
			if (!type.equals("http")) {
				cover = URLManager.Head_URL + cover;
			}
			Picasso.with(context).load(cover).resize(400, 400)
					.placeholder(R.mipmap.details_img3)
					.error(R.mipmap.details_img3).into(holder.guanzhu_item_head);
			holder.guanzhu_item.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					ActivityJumpControl.getInstance((Activity)context).gotoZhuboActivity(freshArrayList.get(position).getGid());
				}
			});
		}
		return convertView;
	}

	class ViewHolder {
		private LinearLayout xxsalbum_item, xxsaudio_item, guanzhu_item;
		private RoundedImageView guanzhu_item_head;
		private ImageView guanzhu_item_cutandadd_press;
		private TextView guanzhu_item_audio, guanzhu_item_nickname, guanzhu_item_fans, guanzhu_item_zuiduo;

		private ImageView xxsalbum_item_cover;
		private TextView xxsalbum_item_albumName,xxsalbum_item_playAmount;

		private RoundedImageView xxsaudio_item_cover;
		private TextView xxsaudio_item_audioName,xxsaudio_item_playAmount;

		private RoundedImageView xxsitem_cover;
		private TextView xxsitem_name,xxsitem_tv;
	}

}
