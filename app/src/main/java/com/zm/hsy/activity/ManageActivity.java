package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;

/**
 * 管理中心
 *
 * @author Kkan
 *
 */
public class ManageActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					String audioAlbumCount = obj.getString("audioAlbumCount");
					glzx_audio_tv2.setText("专辑("+audioAlbumCount+")");
				} catch (JSONException e) {
					glzx_audio_tv2.setText("专辑(0)");
					stopProgressDialog();
				}
			}else if (msg.what == URLManager.two) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					String videoAlbumCount = obj.getString("videoAlbumCount");
					glzx_video_tv2.setText("专辑("+videoAlbumCount+")");
				} catch (JSONException e) {
					glzx_audio_tv2.setText("专辑(0)");
					stopProgressDialog();
				}
		}else if (msg.what == URLManager.three) {
			JSONObject obj = (JSONObject) msg.obj;
			try {
				String vStatus = obj.getString("vStatus");
				if(vStatus.equals("2")){
					glzx_jv_tv2.setText("已认证");
					glzx_jv_rl.setEnabled(false);
				}else{
					glzx_jv_tv2.setText("未认证");
					glzx_jv_rl.setEnabled(true);
				}
			} catch (JSONException e) {
				stopProgressDialog();
				glzx_jv_tv2.setText("未认证");
				glzx_jv_rl.setEnabled(true);
			}
		}else  if (msg.what == URLManager.five) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					JSONObject data = obj.getJSONObject("userRank");
					JSONObject userdata = data.getJSONObject("user");
					glzx_hsy_tv2.setText("当前积分:"+userdata.getString("score"));
				} catch (JSONException e) {
					e.printStackTrace();
					glzx_hsy_tv2.setText("当前无积分");
					stopProgressDialog();
				}
			}
			stopProgressDialog();
		}

	};

	private String userid;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage);
		context=this;
		userid = Futil.getValue(context, "userid");
		findview();
        gointo();
        getvStatus();
		getAudioAlbum();
		getVideoAlbum();
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

	private void gointo() {
		startProgressDialog();
		String strUrl = URLManager.Myown;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("user.id", userid);
		Futil.xutils(strUrl, map, handler, URLManager.five);

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.glzx_audio_rl://音频专辑
			ActivityJumpControl.getInstance(this).gotoMyAublmActivity("1");
			break;
		case R.id.glzx_video_rl://视频专辑
			ActivityJumpControl.getInstance(this).gotoMyAublmActivity("2");
			break;
		case R.id.glzx_shouyi_rl:
			ActivityJumpControl.getInstance(this).gotoEarningsActivity();
			break;
		case R.id.glzx_hsy_rl:
			ActivityJumpControl.getInstance(this).gotoGoumaijifenActivity();
			break;
		case R.id.glzx_jv_rl:
			String shield= Futil.getValue(context,"shield");
			if(shield.equals("1")){
				ActivityJumpControl.getInstance(this).gotoAddVActivity();
			}else{
				Futil.showMessage(context,context.getString(R.string.shield_remind));
			}
			break;
		case R.id.back_top:
			finish();
			break;

		}
	}

	private ImageView back_top;
	private TextView glzx_audio_tv2, glzx_video_tv2,glzx_hsy_tv2,glzx_jv_tv2;
	private RelativeLayout glzx_audio_rl, glzx_video_rl, glzx_shouyi_rl,
			 glzx_hsy_rl,glzx_jv_rl;

	private void findview() {
		glzx_audio_tv2 = (TextView) findViewById(R.id.glzx_audio_tv2);
		glzx_video_tv2 = (TextView) findViewById(R.id.glzx_video_tv2);
		glzx_hsy_tv2 = (TextView) findViewById(R.id.glzx_hsy_tv2);
		glzx_jv_tv2 = (TextView) findViewById(R.id.glzx_jv_tv2);
		glzx_audio_rl = (RelativeLayout) findViewById(R.id.glzx_audio_rl);
		glzx_audio_rl.setOnClickListener(this);
		glzx_video_rl = (RelativeLayout) findViewById(R.id.glzx_video_rl);
		glzx_video_rl.setOnClickListener(this);
		glzx_shouyi_rl = (RelativeLayout) findViewById(R.id.glzx_shouyi_rl);
		glzx_shouyi_rl.setOnClickListener(this);
		glzx_hsy_rl = (RelativeLayout) findViewById(R.id.glzx_hsy_rl);
		glzx_hsy_rl.setOnClickListener(this);
		glzx_jv_rl = (RelativeLayout) findViewById(R.id.glzx_jv_rl);
		glzx_jv_rl.setOnClickListener(this);
		glzx_jv_rl.setEnabled(false);

		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

	}

	private void getAudioAlbum() {
		startProgressDialog();
		String strUrl = URLManager.MyAudioAlbum;;
		HashMap<String, String> map = new HashMap<String, String>();
		if (!userid.equals("")) {
			map.put("user.id", userid);
		}
		Futil.xutils(strUrl, map, handler, URLManager.one);
	}
	private void getVideoAlbum() {
		startProgressDialog();
		String strUrl = URLManager.MyVideoAlbum;;
		HashMap<String, String> map = new HashMap<String, String>();
		if (!userid.equals("")) {
			map.put("user.id", userid);
		}
		Futil.xutils(strUrl, map, handler, URLManager.two);
	}
	private void getvStatus() {
		startProgressDialog();
		String strUrl = URLManager.jiaV;;
		HashMap<String, String> map = new HashMap<String, String>();
		if (!userid.equals("")) {
			map.put("user.id", userid);
		}
		Futil.xutils(strUrl, map, handler, URLManager.three);
	}

	/**
	 * 等待页
	 */
	private CustomProgressDialog progressDialog;

	private void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = CustomProgressDialog.createDialog(context);
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
