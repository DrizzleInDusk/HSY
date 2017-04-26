package com.zm.hsy.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.adapter.ImageBucketAdapter;
import com.zm.hsy.entity.ImageBucket;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.AlbumHelper;

import java.io.Serializable;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
/** 选择系统相册 */
public class PhotoalbumActivity extends Activity {
	// ArrayList<Entity> dataList;//用来装载数据源的列表
	List<ImageBucket> dataList;
	GridView gridView;
	ImageBucketAdapter adapter;// 自定义的适配器
	AlbumHelper helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	TextView back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_bucket);

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		back = (TextView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}

		});
		initData();
		initView();
	}
	@Override
	protected void onResume() {

		JPushInterface.onResume(this);
		MobclickAgent.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
		MobclickAgent.onPause(this);
	}
	/**
	 * 初始化数据
	 */
	private void initData() {
		// /**
		// * 这里，我们假设已经从网络或者本地解析好了数据，所以直接在这里模拟了10个实体类，直接装进列表中
		// */
		// dataList = new ArrayList<Entity>();
		// for(int i=-0;i<10;i++){
		// Entity entity = new Entity(R.drawable.picture, false);
		// dataList.add(entity);
		// }
		dataList = helper.getImagesBucketList(false);	
		bimap=BitmapFactory.decodeResource(
				getResources(),
				R.mipmap.icon_addpic_unfocused);
	}

	/**
	 * 初始化view视图
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new ImageBucketAdapter(PhotoalbumActivity.this, dataList);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/**
				 * 通知适配器，绑定的数据发生了改变，应当刷新视图
				 */
				ActivityJumpControl.getInstance(PhotoalbumActivity.this)
				.gotoImageGridActivity((Serializable) dataList.get(position).imageList);
				finish();
			}

		});
	}
}
