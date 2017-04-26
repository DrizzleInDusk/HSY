package com.zm.hsy.adapter;

import java.util.ArrayList;

import com.zm.hsy.R;
import com.zm.hsy.entity.MyMessage;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Adapter 消息
 * 
 * @author Administrator
 * 
 */
public class MypushMessageAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater minflater;
	private ArrayList<MyMessage> messlist;

	public MypushMessageAdapter(Context context, ArrayList<MyMessage> messlist) {
		super();
		this.minflater = LayoutInflater.from(context);
		this.context = context;
		this.messlist = messlist;
	}

	@Override
	public int getCount() {
		return messlist.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = minflater.inflate(R.layout.activity_mypushmess_item,
					null);
			holder = new ViewHolder();
			holder.pmess_content = (TextView) convertView
					.findViewById(R.id.pmess_content);
			holder.pmess_addtime = (TextView) convertView
					.findViewById(R.id.pmess_addtime);
			holder.pmess_head = (RoundedImageView) convertView
					.findViewById(R.id.pmess_head);
			holder.pmess_pic = (ImageView) convertView
					.findViewById(R.id.pmess_pic);
			holder.pmess_picrl = (RelativeLayout) convertView
					.findViewById(R.id.pmess_picrl);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MyMessage myMessage = messlist.get(position);
		String head = myMessage.getHead();
		String headStatus = myMessage.getHeadStatus();
		if (!headStatus.equals("http")) {
			head = URLManager.Head_URL + head;

		}
		Picasso.with(context).load(head).resize(400, 400)
				.error(R.mipmap.mess_img2).centerCrop()
				.into(holder.pmess_head);
		String picture = myMessage.getPicture();
		if (!picture.equals("") && picture != null && !picture.equals("null")) {

			holder.pmess_picrl.setVisibility(View.VISIBLE);
			picture = URLManager.Push_URL + picture;
			Picasso.with(context).load(picture).resize(400, 400)
					.error(R.mipmap.mess_img2).centerCrop()
					.into(holder.pmess_pic);
		} else {
			holder.pmess_picrl.setVisibility(View.GONE);
		}
		holder.pmess_addtime.setText(myMessage.getAddTime());
		holder.pmess_content.setText(myMessage.getContent());

		return convertView;
	}

	class ViewHolder {
		private RoundedImageView pmess_head;
		private ImageView pmess_pic;
		private RelativeLayout pmess_picrl;
		private TextView pmess_content, pmess_addtime;
	}

}
