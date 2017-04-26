package com.zm.hsy.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.zm.hsy.R;
import com.zm.hsy.entity.User;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.squareup.picasso.Picasso;
import com.zm.hsy.myview.RoundedImageView;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BBSBanzhuguanliAdapter extends BaseAdapter {
	private Context context;
	private String userid;
	private String communityid;
	private Handler handler;
	private LayoutInflater minflater;
	private ArrayList<User> guanliList = new ArrayList<User>();;

	public BBSBanzhuguanliAdapter(Context context, ArrayList<User> guanliList,
			String userid, String communityid, Handler handler) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.guanliList = guanliList;
		this.context = context;
		this.userid = userid;
		this.handler = handler;
		this.communityid = communityid;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return guanliList.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.banzhuguanli_item, null);
			holder = new ViewHolder();
			holder.guanli_name = (TextView) convertView
					.findViewById(R.id.banzhugu_guanli_name);
			holder.guanli_blurb = (TextView) convertView
					.findViewById(R.id.banzhugu_guanli_blurb);
			holder.banzhugu_quxiao = (ImageView) convertView
					.findViewById(R.id.banzhugu_quxiao);

			holder.guanli_head = (RoundedImageView) convertView
					.findViewById(R.id.banzhugu_guanli_head);

			holder.single_line = (View) convertView
					.findViewById(R.id.banzhugu_single_line);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == 0) {
			holder.single_line.setVisibility(View.GONE);
		}
		String blurb = guanliList.get(position).getBlurb();
		final String nickname = guanliList.get(position).getNickname();

		holder.guanli_name.setText(nickname);
		if (blurb != null && !blurb.equals("") &&!blurb.equals("null") ){
			holder.guanli_blurb.setText(blurb);
		} else {
			holder.guanli_blurb.setText("暂无简介");
		}

		String head = guanliList.get(position).getHead();
		String type = guanliList.get(position).getHeadStatus();
		if (!type.equals("http")) {
			head = URLManager.Head_URL + head;
		}
		Picasso.with(context).load(head).resize(400, 400)
				.placeholder(R.mipmap.xionghaiz)
				.error(R.mipmap.xionghaiz).into(holder.guanli_head);

		holder.banzhugu_quxiao.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg1) {
				if (!userid.equals("")) {
					String strUrl = URLManager.DownAdmin;
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("user.id", userid);
					map.put("community.id", communityid);
					map.put("nickname", nickname);
					Futil.xutils(strUrl, map, handler, URLManager.three);
				} else {
					Toast.makeText(context, "请先登录", Toast.LENGTH_LONG).show();
				}
			}

		});
		return convertView;
	}

	class ViewHolder {
		private RoundedImageView guanli_head;
		private ImageView banzhugu_quxiao;
		private TextView guanli_blurb, guanli_name;
		private View single_line;
	}

}
