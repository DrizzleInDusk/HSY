package com.zm.hsy.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;
import com.zm.hsy.util.FileUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/** 声音视频发布 1录音3视频 */
@SuppressLint("SimpleDateFormat")
public class PublishRecordActivity extends Activity implements OnClickListener {

	private AlertDialog dialog;
	private static final int PHOTO_CARMERA = 1;// 相机
	private static final int PHOTO_PICK = 2;// 相册
	private static final int PHOTO_CUT = 3;// 裁剪
	private RoundedImageView imgIcon;// 初始化图片
	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/hsyfm/recordimg/";
	public static File destDir = new File(SDPATH);
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {// 上传音频
				JSONObject object = (JSONObject) msg.obj;
				try {
					String code = object.getString("code");
					if (code.equals("2")) {

						ArrayList<String> sKey = Futil.loadKeyArray(
								context, Tag);
						System.out.println("sKey.size()---" + sKey.size());
						sKey.remove(key);

						System.out.println("key---" + key);
						Futil.saveKeyArray(context, sKey,
								Tag);
						Futil.romveValue(context, key, Tag);


							Futil.deleteFile(mFile);

						ActivityJumpControl.getInstance((Activity)
								context)
								.gotoMyAudioActivity("1", Tag, toptv);
						finish();
					}
				} catch (Exception e) {
					stopProgressDialog();
				}
				stopProgressDialog();
			} else if (msg.what == URLManager.two) {// 获取音频专辑
				JSONObject obj = (JSONObject) msg.obj;
				try {
					JSONArray myAudioAlbum = obj.getJSONArray("audioAlbumList");
					dialog(myAudioAlbum);
				} catch (JSONException e) {
					JSONArray myAudioAlbum = null;
					dialog(myAudioAlbum);
					stopProgressDialog();
				}
			} else if (msg.what == URLManager.three) {// 上传视频
				JSONObject object = (JSONObject) msg.obj;
				try {
					String code = object.getString("code");
					if (code.equals("2")) {
						ArrayList<String> sKey = Futil.loadKeyArray(
								context, Tag);
						System.out.println("sKey.size()---" + sKey.size());
						sKey.remove(key);

						System.out.println("key---" + key);
						Futil.saveKeyArray(context, sKey,
								Tag);
						Futil.romveValue(context, key, Tag);
						Futil.deleteFile(mFile);
						ActivityJumpControl.getInstance((Activity)
								context)
								.gotoMyAudioActivity("1", Tag, toptv);
						finish();
					}else{
						String message = object.getString("message");
						Futil.showMessage(context,message);
					}
				} catch (Exception e) {
				}
				stopProgressDialog();
			} else if (msg.what == URLManager.four) {// 获取视频专辑
				JSONObject obj = (JSONObject) msg.obj;
				try {
					JSONArray myVideoAlbum = obj.getJSONArray("videoAlbumList");
					dialog(myVideoAlbum);
				} catch (JSONException e) {
					JSONArray myAudioAlbum = null;
					dialog(myAudioAlbum);
					stopProgressDialog();
				}
			}
		}
	};
	private String userid, key, Tag, toptv;
	private String imgpath = null;
	private String name = null;
	private File mFile;
	private String albumId = null;
	private String timeLong = null;
	private String coverpath = null;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publishrecord);
		context=this;
		findview();
		Intent i = this.getIntent();
		key = i.getStringExtra("key");
		Tag = i.getStringExtra("Tag");// 1录音3视频
		if (Tag.equals("1")) {
			publish_top_tv1.setText("声音信息");
			publish_send.setText("发布声音");
			toptv="我的声音";
		} else if (Tag.equals("3")) {
			publish_top_tv1.setText("视频信息");
			publish_send.setText("发布视频");
			toptv="我的视频";
		}
		name = (String) Futil
				.getValue(context, key + "name");
		timeLong = (String) Futil.getValue(context, key
				+ "time");
		String filePath = (String) Futil.getValue(context,
				key + "path");
		coverpath = (String) Futil.getValue(context, key
				+ "cover");
		try {
			publish_name_et.setText(name);

		} catch (Exception e) {
			e.printStackTrace();
		}
		File file = new File(coverpath);
		if (file.exists()) {
			imgFile = new File(coverpath);
			imgpath = imgFile.getPath();
			Bitmap bm = BitmapFactory.decodeFile(coverpath);
			// 将图片显示到ImageView中
			imgIcon.setImageBitmap(bm);
		}
		if (FileUtil.getFile(filePath) != null) {
			mFile = FileUtil.getFile(filePath);
		} else {
			Futil.showMessage(context, "文件已不在");
		}
		userid = Futil.getValue(context, "userid");
		// 获取布局填充器
		mInflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		if (issdkard()) {

			if (!destDir.exists()) {
				destDir.mkdirs();
			}
		} else {
			Futil.showMessage(context, "请检查sd卡是否插入");
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
		switch (v.getId()) {
		case R.id.publish_send:// 发布声音或者视频
			if (imgpath != null) {
				//进入下一步，是否选择专辑
				getet();
			} else {
				Futil.showMessage(context, "请选择封面");
			}
			break;
		case R.id.publish_cover:// 封面图片
			openDialog();
			break;
		case R.id.publish_album:// 选择专辑
			gointo();
			break;
		case R.id.publish_save:// 保存本地
			Futil.saveValue(context, key + "cover", imgpath);// 音乐封面
			String et = publish_name_et.getText().toString().trim();
			Futil.saveValue(context, key + "name", et);// 音乐名
			System.out.println("发布声音name--" + et);
			System.out.println("发布声音audiocover--" + imgpath);
			ActivityJumpControl.getInstance((Activity) context)
					.gotoMyAudioActivity("0", Tag, toptv);
			finish();
			break;
		case R.id.back_top:
			finish();
			break;
		}
	}

	private ImageView back_top;
	private TextView publish_album, publish_top_tv1,publish_send,publish_save;
	private EditText publish_name_et;

	private void findview() {
		publish_top_tv1 = (TextView) findViewById(R.id.publish_top_tv1);
		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);
		// 封面图片
		imgIcon = (RoundedImageView) findViewById(R.id.publish_cover);
		imgIcon.setOnClickListener(this);
		// 发布声音
		publish_send = (TextView) findViewById(R.id.publish_send);
		publish_send.setOnClickListener(this);
		// 保存声音
		publish_save = (TextView) findViewById(R.id.publish_save);
		publish_save.setOnClickListener(this);

		// 音乐名
		publish_name_et = (EditText) findViewById(R.id.publish_name_et);
		// 选择专辑
		publish_album = (TextView) findViewById(R.id.publish_album);
		publish_album.setOnClickListener(this);

	}

	private LayoutInflater mInflater;

	@SuppressLint("ResourceAsColor")
	private void dialog(JSONArray myAlbum) {
		try {
			dialog = new AlertDialog.Builder(context)
					.create();
			dialog.show();
			dialog.setContentView(R.layout.dialog_aublmlist);
			RelativeLayout tv1 = (RelativeLayout) dialog
					.findViewById(R.id.dialog_content);// 暂无内容
			TextView tv2 = (TextView) dialog.findViewById(R.id.dialog_build);// 创建专辑
			LinearLayout dialog_ll = (LinearLayout) dialog
					.findViewById(R.id.dialog_ll);// 列表
			RelativeLayout quxiao = (RelativeLayout) dialog
					.findViewById(R.id.aublmlist_quxiao);// 取消
			Window dialogWindow = dialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.BOTTOM);
			dialogWindow.setAttributes(lp);

			WindowManager m = getWindowManager();
			Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
			WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
			p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.6
			p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65
			dialogWindow.setAttributes(p);
			//取消创建
			quxiao.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			//创建专辑
			tv2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {//创建专辑
					String shield= Futil.getValue(context,"shield");
					if(shield.equals("1")){
						ActivityJumpControl.getInstance((Activity) context)
								.gotoBuildAublmActivity(Tag);
					}else{
						Futil.showMessage(context,context.getString(R.string.shield_remind));
					}
					dialog.dismiss();
				}
			});
			if (myAlbum.length() > 0) {
				dialog_ll.setVisibility(View.VISIBLE);
				tv1.setVisibility(View.GONE);
				for (int i = 0; i < myAlbum.length(); i++) {
					JSONObject data = myAlbum.getJSONObject(i);
					final String id = data.getString("id");
					final String albumName = data.getString("albumName");
					TextView dtv = (TextView) mInflater.inflate(
							R.layout.textviewgroup, null);
					dtv.setText(albumName);
					dtv.setId(data.getInt("id"));
					dialog_ll.addView(dtv);

					dtv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							publish_album.setText(albumName);
							albumId = id;
							dialog.dismiss();
						}
					});

				}
			} else {
				dialog_ll.setVisibility(View.GONE);
				tv1.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		stopProgressDialog();
	}

	private void getet() {
		String str1 = publish_name_et.getText().toString().trim();
		if (albumId != null) {
			//开始上传
			upload(str1);
		} else {
			Futil.showMessage(context, "请选择专辑");
		}
	}

	private void gointo() {//获取专辑
		if (Tag.equals("1")) {
			startProgressDialog();
			String strUrl = URLManager.MyAudioAlbum;
			HashMap<String, String> map = new HashMap<String, String>();
			if (!userid.equals("")) {
				map.put("user.id", userid);
				Futil.xutils(strUrl, map, handler, URLManager.two);
			}
		} else if (Tag.equals("3")) {
			startProgressDialog();
			String strUrl = URLManager.MyVideoAlbum;
			HashMap<String, String> map = new HashMap<String, String>();
			if (!userid.equals("")) {
				map.put("user.id", userid);
				Futil.xutils(strUrl, map, handler, URLManager.four);
			}
		}

	}

	// 上传文件到服务器
	protected void upload(String str) {
		if (Tag.equals("1")) {
			startProgressDialog();
			String strUrl = URLManager.AddAudio;
			HashMap<String, String> map = new HashMap<String, String>();
			HashMap<String, File> mapfile = new HashMap<String, File>();
			map.put("audio.audioName", str);
			map.put("audio.timeLong", timeLong);
			map.put("audio.blurb", "");
			map.put("audio.audioAlbumId", albumId);
			
			mapfile.put("audioc", imgFile);
			
			mapfile.put("audiop", mFile);
			Futil.xutils(strUrl, map, mapfile, handler, URLManager.one);
		} else if (Tag.equals("3")) {
			startProgressDialog();
			String strUrl = URLManager.AddVideo;
			HashMap<String, String> map = new HashMap<String, String>();
			HashMap<String, File> mapfile = new HashMap<String, File>();
			map.put("video.videoName", str);
			map.put("video.timeLong", timeLong);
			map.put("video.blurb", "");
			map.put("video.videoAlbumId", albumId);
			mapfile.put("cover", imgFile);
			mapfile.put("vpath", mFile);
			map.put("user.id", userid);
			System.out.println("strUrl--"+strUrl);
			System.out.println("videoName--"+str);
			System.out.println("timeLong--"+timeLong);
			System.out.println("videoAlbumId--"+albumId);
			System.out.println("cover--"+imgpath);
			System.out.println("vpath--"+mFile.getPath());
			Futil.xutils(strUrl, map, mapfile, handler, URLManager.three);
		}
	}

	// 创建一个以当前系统时间为名称的文件，防止重复
	private File imgFile = new File(destDir, getPhotoFileName());

	// 使用系统当前日期加以调整作为照片的名称
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("'PNG'_yyyyMMdd_HHmmss");
		return sdf.format(date) + ".png";
	}

	private void openDialog() {
		dialog = new AlertDialog.Builder(context).create();
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
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));
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
			startPhotoZoom(Uri.fromFile(imgFile), 300);
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
			imgIcon.setImageBitmap(bmp);

			saveCropPic(bmp);
		}
	}

	// 把裁剪后的图片保存到sdcard上
	private void saveCropPic(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileOutputStream fis = null;
		bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
		try {
			fis = new FileOutputStream(imgFile);
			imgpath = imgFile.getPath();
			fis.write(baos.toByteArray());
			fis.flush();
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
