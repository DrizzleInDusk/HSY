package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.entity.Music;
import com.zm.hsy.https.Futil;
import com.zm.hsy.util.CustomProgressDialog;
import java.util.ArrayList;
import cn.jpush.android.api.JPushInterface;
/** 选择本地音乐 */
public class SoundtrackActivity extends Activity implements OnClickListener {


	private String userid;
	private String path;

	private Context context;
	private ArrayList<String> fileList;
	private SoundtrackAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_soundtrack);
		context=this;
		userid = Futil.getValue(context, "userid");
		findview();
		//检测SD卡是否存在
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			path = Environment.getExternalStorageDirectory()+"";
			new getsoundTask().execute();
		}else{
			Futil.showMessage(context,"没有SD卡");
			finish();
		}
	}


	@Override
	protected void onResume() {
		MobclickAgent.onResume(this);
		JPushInterface.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
		MobclickAgent.onPause(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_top:
			finish();
			break;

		}
	}
	private ImageView back_top;
	private ListView st_sound_viewp;

	private void findview() {
		st_sound_viewp = (ListView) findViewById(R.id.st_sound_viewp);
		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

	}

//	class getsoundTask extends AsyncTask< ArrayList<String>,  ArrayList<String>,  ArrayList<String>> {
//		@Override
//		protected ArrayList<String> doInBackground(ArrayList<String>... arrayLists) {
//			return Futil.getAllFiles(path,1);
//		}
	class getsoundTask extends AsyncTask<ArrayList<Music>,  ArrayList<Music>,  ArrayList<Music>> {
		@Override
		protected ArrayList<Music> doInBackground(ArrayList<Music>... arrayLists) {

			return Futil.getMusicFile(context);
		}

		@Override
		protected void onPreExecute() {
			startProgressDialog();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute( ArrayList<Music> result) {
			stopProgressDialog();
			adapter=new SoundtrackAdapter(context,result);
			st_sound_viewp.setAdapter(adapter);
			super.onPostExecute(result);
		}
	}

	/**
	 * 等待页
	 */
	private CustomProgressDialog progressDialog;

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(this);
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

	public class SoundtrackAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater minflater;
		private ArrayList<Music> fileList;

		public SoundtrackAdapter(Context context, ArrayList<Music> fileList) {
			super();
			this.minflater = LayoutInflater.from(context);
			this.fileList = fileList;
			this.context = context;
		}

		@Override
		public int getCount() {
			return fileList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				convertView = minflater
						.inflate(R.layout.activity_soundtrack_item, null);
				holder = new ViewHolder();
				holder.st_item_name = (TextView) convertView
						.findViewById(R.id.st_item_name);
				holder.st_item_add = (ImageView) convertView
						.findViewById(R.id.st_item_add);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final String filename = fileList.get(position).getName();
			final String filepath = fileList.get(position).getUrl();
			holder.st_item_name.setText(filename);
			holder.st_item_add.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					RecordActivity.path=filepath;
					RecordActivity.soundName=filename;
					finish();
				}
			});
			stopProgressDialog();
			return convertView;
		}

		class ViewHolder {
			private ImageView st_item_add;
			private TextView st_item_name;

		}

	}
}
