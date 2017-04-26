package com.zm.hsy.adapter;

import java.io.IOException;

import com.zm.hsy.R;
import com.zm.hsy.util.Bimp;
import com.zm.hsy.util.PhotoFileUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

@SuppressLint("HandlerLeak")
public class AddTopicGridAdapter extends BaseAdapter {
	private LayoutInflater inflater; // 视图容器
	private int selectedPosition = -1;// 选中的位置
	private boolean shape;

	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	public AddTopicGridAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	public void update() {
		loading();
	}

	public int getCount() {
		return (Bimp.bmp.size() + 1);
	}

	public Object getItem(int arg0) {

		return null;
	}

	public long getItemId(int arg0) {

		return 0;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	/**
	 * ListView Item设置
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		final int coord = position;
		ViewHolder holder = null;
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.activity_addbbs_item_published_grida,
					parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.item_grida_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}


//		if(Bimp.bmp.size()==0){
//			holder.image.setVisibility(View.GONE);
//		}
//		if (position == Bimp.bmp.size()) {
//			if (position == 9) {
//				holder.image.setVisibility(View.GONE);
//			}
//		} else {
		if(Bimp.bmp.size()!=0){
			holder.image.setImageBitmap(Bimp.bmp.get(position));
		}
//		}

		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void loading() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (Bimp.max == Bimp.drr.size()) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
						break;
					} else {
						try {
							String path = Bimp.drr.get(Bimp.max);
							System.out.println(path);
							Bitmap bm = Bimp.revitionImageSize(path);
							Bimp.bmp.add(bm);
							String newStr = path.substring(
									path.lastIndexOf("/") + 1,
									path.lastIndexOf("."));
							PhotoFileUtils.saveBitmap(bm, "" + newStr);
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						} catch (IOException e) {

							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}

}
