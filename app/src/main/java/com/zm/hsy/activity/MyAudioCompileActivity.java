package com.zm.hsy.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/** 编辑已发布声音 */
public class MyAudioCompileActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					String audioAlbum = obj.getString("audioAlbum");
					JSONObject audio = obj.getJSONObject("audio");
					audioName = audio.getString("audioName");
					String cover = audio.getString("cover");
					cover = URLManager.COVER_URL + cover;
					Picasso.with(MyAudioCompileActivity.this).load(cover)
							.resize(400, 400)
							.placeholder(R.mipmap.compile_cover_img)
							.error(R.mipmap.compile_cover_img)
							.into(compile_cover);
					compile_aublmnametv.setText(audioAlbum);
					audioname_et.setText(audioName);
				} catch (JSONException e) {
					e.printStackTrace();
					compile_aublmnametv.setText("");
					audioname_et.setText("");
					stopProgressDialog();
				}
			} else if (msg.what == URLManager.two) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					String message = obj.getString("message");
					String code = obj.getString("code");
					if (code.equals("2")) {
						Futil.showMessage(MyAudioCompileActivity.this, ""
								+ message);
						ActivityJumpControl.getInstance(
								MyAudioCompileActivity.this)
								.gotoMyAudioActivity("1","1","我的声音");
						finish();
					} else {
						Futil.showMessage(MyAudioCompileActivity.this, ""
								+ message);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					stopProgressDialog();
				}
			}
			stopProgressDialog();
		}

	};

	private String userid, audioid, audioName;
	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/hsyfm/img";
	public static File destDir = new File(SDPATH);
	private static final int PHOTO_CARMERA = 1;
	private static final int PHOTO_PICK = 2;
	private static final int PHOTO_CUT = 3;
	private boolean issave = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myaudio_compile);

		Intent i = this.getIntent();
		audioid = i.getStringExtra("audioid");
		userid = Futil.getValue(MyAudioCompileActivity.this, "userid");
		
		System.out.println("audioid--" + audioid);
		findview();
		getaudio();

		if (issdkard()) {

			if (!destDir.exists()) {
				destDir.mkdirs();
			}
		} else {
			Futil.showMessage(MyAudioCompileActivity.this, "请检查sd卡是否插入");
		}
	}

	public boolean issdkard() {
		// 首先判断sdcard是否插入
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	protected void onRestart() {

		super.onRestart();
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
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.compile_rl:
			getet();
			break;
		case R.id.compile_cover:
			openDialog();
			break;
		case R.id.back_top:
			finish();
			break;

		}
	}

	private void getaudio() {
		startProgressDialog();
		String strUrl = URLManager.EditAudio;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("audio.id", audioid);
		Futil.xutils(strUrl, map, handler, URLManager.one);
	}

	private ImageView back_top;
	private RoundedImageView compile_cover;
	private EditText audioname_et;
	private TextView compile_aublmnametv;
	private RelativeLayout compile_rl;

	private void findview() {
		compile_cover = (RoundedImageView) findViewById(R.id.compile_cover);
		compile_cover.setOnClickListener(this);

		audioname_et = (EditText) findViewById(R.id.compile_audioname_et);
		compile_aublmnametv = (TextView) findViewById(R.id.compile_aublmnametv);

		compile_rl = (RelativeLayout) findViewById(R.id.compile_rl);
		compile_rl.setOnClickListener(this);

		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

	}

	private String nameet;

	private void getet() {
		startProgressDialog();
		int n = audioname_et.getText().length();
		if (!userid.equals("")) {
			if (n > 0) {
				nameet = audioname_et.getText().toString().trim();
				if (nameet.equals(audioName)) {
					nameet = null;
				}
				changeaudio();
			} else {
				Futil.showMessage(MyAudioCompileActivity.this, "请先输入标题");
				stopProgressDialog();
			}
		} else {
			Futil.showMessage(MyAudioCompileActivity.this, "请先登录");
			stopProgressDialog();
		}
	}

	private void changeaudio() {
		String strUrl = URLManager.UpAudio;
		HashMap<String, String> map = new HashMap<String, String>();
		HashMap<String, File> mapfile = new HashMap<String, File>();
		map.put("audio.id", audioid);
		if (nameet != null) {
			map.put("audio.audioName", nameet);
		}
		if (issave) {
			mapfile.put("audioc", tempFile);
		}
		if (!issave && nameet == null) {
			ActivityJumpControl.getInstance(MyAudioCompileActivity.this)
					.gotoMyAudioActivity("1","1","我的声音");
			finish();
			stopProgressDialog();
		} else {
			System.out.println("audio.audioName----"
					+ map.get("audio.audioName"));
			System.out.println("audioc" + mapfile.get("audioc"));
			Futil.xutils(strUrl, map, mapfile, handler, URLManager.two);
		}

	}

	// 创建一个以当前系统时间为名称的文件，防止重复
	private File tempFile = new File(destDir, getPhotoFileName());

	// 使用系统当前日期加以调整作为照片的名称
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("'PNG'_yyyyMMdd_HHmmss");
		return sdf.format(date) + ".png";
	}

	private AlertDialog dialog;

	private void openDialog() {
		dialog = new AlertDialog.Builder(MyAudioCompileActivity.this).create();
		dialog.show();
		dialog.setContentView(R.layout.dialog_photos);

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		dialogWindow.setAttributes(lp);

		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		p.height = (int) (d.getHeight() * 0.23); // 高度设置为屏幕的0.6
		p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65
		dialogWindow.setAttributes(p);

		RelativeLayout rl1 = (RelativeLayout) dialog
				.findViewById(R.id.dialog_photos_rl1);
		RelativeLayout rl2 = (RelativeLayout) dialog
				.findViewById(R.id.dialog_photos_rl2);
		RelativeLayout rl3 = (RelativeLayout) dialog
				.findViewById(R.id.dialog_photos_rl3);
		rl1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) { // 拍照
				startCamera(dialog);
				dialog.dismiss();
			}
		});
		rl2.setOnClickListener(new OnClickListener() { // 相册

			@Override
			public void onClick(View v) {
				startPick(dialog);
				dialog.dismiss();
			}
		});
		rl3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	protected void startCamera(DialogInterface dialog) {
		dialog.dismiss();
		// 调用系统的拍照功能
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra("camerasensortype", 2); // 调用前置摄像头
		intent.putExtra("autofocus", true); // 自动对焦
		intent.putExtra("fullScreen", false); // 全屏
		intent.putExtra("showActionIcons", false);
		// 指定调用相机拍照后照片的存储路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
		startActivityForResult(intent, PHOTO_CARMERA);
	}

	// 调用系统相册
	protected void startPick(DialogInterface dialog) {
		dialog.dismiss();
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intent, PHOTO_PICK);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PHOTO_CARMERA:
			startPhotoZoom(Uri.fromFile(tempFile), 300);
			break;
		case PHOTO_PICK:
			if (null != data) {
				startPhotoZoom(data.getData(), 300);
			}
			break;
		case PHOTO_CUT:
			if (null != data) {
				setPicToView(data);
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// 调用系统裁剪
	private void startPhotoZoom(Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以裁剪
		intent.putExtra("crop", true);
		// aspectX,aspectY是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX,outputY是裁剪图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		// 设置是否返回数据
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTO_CUT);
	}

	// 将裁剪后的图片显示在ImageView上
	private void setPicToView(Intent data) {
		Bundle bundle = data.getExtras();
		if (null != bundle) {
			final Bitmap bmp = bundle.getParcelable("data");
			compile_cover.setImageBitmap(bmp);

			saveCropPic(bmp);
			Log.i("MyAudioCompileActivity", tempFile.getAbsolutePath());
		}
	}

	// 把裁剪后的图片保存到sdcard上
	private void saveCropPic(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileOutputStream fis = null;
		bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
		try {
			fis = new FileOutputStream(tempFile);
			fis.write(baos.toByteArray());
			fis.flush();
			issave = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != baos) {
					baos.close();
				}
				if (null != fis) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
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

}
