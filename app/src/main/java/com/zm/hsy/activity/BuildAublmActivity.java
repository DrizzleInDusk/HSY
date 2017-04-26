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
import com.zm.hsy.adapter.BuildAublmAdapter;
import com.zm.hsy.entity.AlbumType;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.CustomProgressDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 创建专辑
 */
public class BuildAublmActivity extends Activity implements OnClickListener {

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == URLManager.one) {// 第一行标签
				JSONObject obj = (JSONObject) msg.obj;
				try {
					JSONArray albumTypeListArray = obj
							.getJSONArray("albumTypeList");
					if (albumTypeListArray.length() != 0) {
						ischic1=false;
						hsv_1.setVisibility(View.VISIBLE);
						for (int i = 0; i < albumTypeListArray.length(); i++) {
							JSONObject albumobj = albumTypeListArray
									.getJSONObject(i);
							AlbumType albumType = new AlbumType();
							albumType.setTypeName(albumobj
									.getString("typeName"));
							albumType.setId(albumobj.getString("id"));
							albumTypeList1.add(albumType);
						}
						gview1dapter = new BuildAublmAdapter(context,
								albumTypeList1);
						setGridView(gview1, albumTypeList1, gview1dapter);
						gview1dapter.notifyDataSetChanged();
						goTow();
					} else {
						ischic1=true;
						ischic2=true;
						ischic3=true;
						ischic4=true;
						hsv_1.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					ischic1=true;
					ischic2=true;
					ischic3=true;
					ischic4=true;
				}
			} else if (msg.what == URLManager.two) {// 第二行标签
				JSONObject obj = (JSONObject) msg.obj;
				changeList1.clear();
				albumTypeList2.clear();
				try {
					JSONArray albumTypeListArray = obj
							.getJSONArray("albumTypeList");
					if (albumTypeListArray.length() != 0) {
						ischic2=false;
						hsv_2.setVisibility(View.VISIBLE);
						for (int i = 0; i < albumTypeListArray.length(); i++) {
							JSONObject albumobj = albumTypeListArray
									.getJSONObject(i);
							AlbumType albumType = new AlbumType();
							albumType.setTypeName(albumobj
									.getString("typeName"));
							albumType.setId(albumobj.getString("id"));
							changeList1.add(albumType);
						}
						albumTypeList2.addAll(changeList1);
						gview2dapter = new BuildAublmAdapter(context,
								albumTypeList2);
						setGridView(gview2, albumTypeList2, gview2dapter);
						gview2dapter.notifyDataSetChanged();
					} else {
						ischic2=true;
						ischic3=true;
						ischic4=true;
						hsv_2.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					ischic2=true;
					ischic3=true;
					ischic4=true;
					hsv_2.setVisibility(View.GONE);
				}

			} else if (msg.what == URLManager.three) {// 第三行标签
				JSONObject obj = (JSONObject) msg.obj;
				changeList2.clear();
				albumTypeList3.clear();
				try {
					JSONArray albumTypeListArray = obj
							.getJSONArray("albumTypeList");
					if (albumTypeListArray.length() != 0) {
						ischic3=false;
						hsv_3.setVisibility(View.VISIBLE);
						for (int i = 0; i < albumTypeListArray.length(); i++) {
							JSONObject albumobj = albumTypeListArray
									.getJSONObject(i);
							AlbumType albumType = new AlbumType();
							albumType.setTypeName(albumobj
									.getString("typeName"));
							albumType.setId(albumobj.getString("id"));
							changeList2.add(albumType);
						}
						albumTypeList3.addAll(changeList2);
						gview3dapter = new BuildAublmAdapter(context,
								albumTypeList3);
						setGridView(gview3, albumTypeList3, gview3dapter);
						gview3dapter.notifyDataSetChanged();
					} else {
						ischic3=true;
						ischic4=true;
						hsv_3.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					ischic3=true;
					ischic4=true;
					hsv_3.setVisibility(View.GONE);
				}
			} else if (msg.what == URLManager.four) {// 第四行标签
				JSONObject obj = (JSONObject) msg.obj;
				changeList3.clear();
				albumTypeList4.clear();
				try {
					JSONArray albumTypeListArray = obj
							.getJSONArray("albumTypeList");
					if (albumTypeListArray.length() != 0) {
						ischic4=false;
						hsv_4.setVisibility(View.VISIBLE);
						for (int i = 0; i < albumTypeListArray.length(); i++) {
							JSONObject albumobj = albumTypeListArray
									.getJSONObject(i);
							AlbumType albumType = new AlbumType();
							albumType.setTypeName(albumobj
									.getString("typeName"));
							albumType.setId(albumobj.getString("id"));
							changeList3.add(albumType);
						}
						albumTypeList4.addAll(changeList3);
						gview4dapter = new BuildAublmAdapter(context,
								albumTypeList4);
						setGridView(gview4, albumTypeList4, gview4dapter);
						gview4dapter.notifyDataSetChanged();
					} else {
						ischic4=true;
						hsv_4.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					ischic4=true;
					hsv_4.setVisibility(View.GONE);
				}

			} else if (msg.what == URLManager.five) {// 创建专辑
				JSONObject obj = (JSONObject) msg.obj;
				try {
					String code = obj.getString("code");
					if (code.equals("2")) {
						String message = obj.getString("message");
						Futil.showMessage(context, "" + message);
						finish();	
					} else {
						String message = obj.getString("message");
						Futil.showMessage(context, "" + message);
					}
				} catch (JSONException e) {

				}

			}
			stopProgressDialog();
		}

	};
	private AlertDialog dialog;
	private static final int PHOTO_CARMERA = 1;// 相机
	private static final int PHOTO_PICK = 2;// 相册
	private static final int PHOTO_CUT = 3;// 裁剪
	private RoundedImageView imgIcon;// 初始化图片
	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/hsyfm/recordimg/";
	public static File destDir = new File(SDPATH);
	private String userid, Tag;
	private Context context;
	private ArrayList<AlbumType> changeList1;
	private ArrayList<AlbumType> changeList2;
	private ArrayList<AlbumType> changeList3;
	private ArrayList<AlbumType> albumTypeList1;
	private ArrayList<AlbumType> albumTypeList2;
	private ArrayList<AlbumType> albumTypeList3;
	private ArrayList<AlbumType> albumTypeList4;
	private BuildAublmAdapter gview1dapter;
	private BuildAublmAdapter gview2dapter;
	private BuildAublmAdapter gview3dapter;
	private BuildAublmAdapter gview4dapter;

	private String albumUrl = null;
	private boolean ischic1 =false;
	private boolean ischic2 =false;
	private boolean ischic3 =false;
	private boolean ischic4 =false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_build_aublm);
		context = this;
		changeList1 = new ArrayList<AlbumType>();
		changeList2 = new ArrayList<AlbumType>();
		changeList3 = new ArrayList<AlbumType>();
		albumTypeList1 = new ArrayList<AlbumType>();
		albumTypeList2 = new ArrayList<AlbumType>();
		albumTypeList3 = new ArrayList<AlbumType>();
		albumTypeList4 = new ArrayList<AlbumType>();

		Intent i = this.getIntent();
		userid = Futil.getValue(BuildAublmActivity.this, "userid");
		Tag = i.getStringExtra("Tag");
		if (Tag.equals("1")) {// 声音
			albumUrl = URLManager.GetAudioAlbumType;
		} else if (Tag.equals("3")) {// 视频
			albumUrl = URLManager.GetVideoAlbumType;
		}

		if (issdkard()) {
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
		} else {
			Futil.showMessage(context, "请检查sd卡是否插入");
		}
		findview();
		getAlbumType();
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

	private String imgpath = null;
	private String individual = null;// 1公开2私密

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.build_aublm:// 创建专辑
			if (imgpath != null) {
				getet();
			} else {
				Futil.showMessage(context, "请选择封面");
			}
			break;
		case R.id.type_gongkai:// 公开
			type_gongkai.setSelected(true);
			type_simi.setSelected(false);
			individual = "1";
			System.out.println("individual--" + individual);
			break;
		case R.id.type_simi:// 私密
			type_gongkai.setSelected(false);
			type_simi.setSelected(true);
			individual = "3";
			System.out.println("individual--" + individual);
			break;
		case R.id.build_aublm_cover:// 选择头像
			openDialog();
			break;
		case R.id.back_top:
			finish();
			break;

		}
	}

	private ImageView back_top;
	private TextView build_aublm;
	private RadioButton type_gongkai;
	private RadioButton type_simi;
	private EditText name_et,jianjie_et;
	private TextView yixuanze;
	private GridView gview1, gview2, gview3, gview4;
	private HorizontalScrollView hsv_1, hsv_2, hsv_3, hsv_4;

	private void findview() {

		// 封面图片
		imgIcon = (RoundedImageView) findViewById(R.id.build_aublm_cover);
		imgIcon.setOnClickListener(this);
		type_gongkai = (RadioButton) findViewById(R.id.type_gongkai);// 公开
		type_gongkai.setOnClickListener(this);
		type_simi = (RadioButton) findViewById(R.id.type_simi);// 私密
		type_simi.setOnClickListener(this);
		type_gongkai.setSelected(true);
		individual = "1";
		// 专辑名
		name_et = (EditText) findViewById(R.id.build_aublm_name_et);
		// 专辑名
		jianjie_et = (EditText) findViewById(R.id.build_aublm_jianjie_et);
		// 已选择标签
		yixuanze = (TextView) findViewById(R.id.build_aublm_yixuanze_tv);
		// 添加标签
		hsv_1 = (HorizontalScrollView) findViewById(R.id.hsv_1);
		hsv_2 = (HorizontalScrollView) findViewById(R.id.hsv_2);
		hsv_3 = (HorizontalScrollView) findViewById(R.id.hsv_3);
		hsv_4 = (HorizontalScrollView) findViewById(R.id.hsv_4);
		gview1 = (GridView) findViewById(R.id.build_album_gview1);
		gview2 = (GridView) findViewById(R.id.build_album_gview2);
		gview3 = (GridView) findViewById(R.id.build_album_gview3);
		gview4 = (GridView) findViewById(R.id.build_album_gview4);

		// 创建专辑
		build_aublm = (TextView) findViewById(R.id.build_aublm);
		build_aublm.setOnClickListener(this);
		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

	}

	private String parentId = "0";
	private int tag = 1;

	/* 获取专辑分类 */
	private void getAlbumType() {
		startProgressDialog();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("parentId", parentId);
		Futil.xutils(albumUrl, map, handler, tag);
	}

	private void getet() {
		String str1 = name_et.getText().toString().trim();
		String str2 = jianjie_et.getText().toString().trim();
		if (str1.length() == 0) {
			Futil.showMessage(context, "请输入专辑名");
		} else if (str2.length() == 0) {
			Futil.showMessage(context, "请输入专辑简介");
		}else{

			if(!typeId.equals("")){
				if(ischic1&&ischic2&&ischic3&&ischic4){
					addaublm(str1,str2);
				}else{
					Futil.showMessage(context, "请选择完整标签");
				}
			}else{
				Futil.showMessage(context, "请选择标签");
			}
		}
	}

	private String buildAlbumUrl;

	/* 创建专辑 */
	private void addaublm(String nameet,String blurbet) {
		startProgressDialog();
		if (Tag.equals("1")) {// 声音
			HashMap<String, String> map = new HashMap<String, String>();
			HashMap<String, File> mapfile = new HashMap<String, File>();
			buildAlbumUrl = URLManager.AddAudioAlbum;
			map.put("audioAlbum.albumName", nameet);
			map.put("audioAlbum.blurb", blurbet);
			map.put("uid", userid);
			map.put("audioAlbum.status", individual);
			map.put("typeId", typeId);
			mapfile.put("cover", imgFile);
			Futil.xutils(buildAlbumUrl, map, mapfile, handler, URLManager.five);
		} else if (Tag.equals("3")) {// 视频
			HashMap<String, String> map = new HashMap<String, String>();
			HashMap<String, File> mapfile = new HashMap<String, File>();
			buildAlbumUrl = URLManager.AddVideoAlbum;
			map.put("videoAlbum.albumName", nameet);
			map.put("videoAlbum.blurb", blurbet);
			map.put("videoAlbum.albumTypeId", typeId);
			mapfile.put("cover", imgFile);
			map.put("videoAlbum.userId", userid);
			Futil.xutils(buildAlbumUrl, map, mapfile, handler, URLManager.five);
		}

	}

	private String Id1 = "";
	private String t1 = "";
	private String t2 = "";
	private String t3 = "";
	private String t4 = "";
	private String Id2 = "";
	private String Id3 = "";
	private String Id4 = "";
	private String typeId="";
	private String tvtext;

	public void goTow() {
		// gview1点击事件
		gview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				hsv_2.setVisibility(View.GONE);
				hsv_3.setVisibility(View.GONE);
				hsv_4.setVisibility(View.GONE);
				gview1dapter.setCheckItem(position);
				ischic1=true;
				ischic2=false;
				ischic3=false;
				ischic4=false;
				parentId = albumTypeList1.get(position).getId();
				t1 = albumTypeList1.get(position).getTypeName();
				Id1 = parentId;
				typeId = Id1;
				tvtext = t1;
				yixuanze.setText(tvtext);
				Id2 = "";
				Id3 = "";
				Id4 = "";
				t2 = "";
				t3 = "";
				t4 = "";
				tag = 2;
				getAlbumType();
			}
		});
		gview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				hsv_3.setVisibility(View.GONE);
				hsv_4.setVisibility(View.GONE);
				gview2dapter.setCheckItem(position);
				ischic2=true;
				ischic3=false;
				ischic4=false;
				parentId = albumTypeList2.get(position).getId();
				t2 = albumTypeList2.get(position).getTypeName();
				Id2 = parentId;
				typeId = Id1 + "," + Id2;
				tvtext = t1 + "-" + t2;
				yixuanze.setText(tvtext);
				Id3 = "";
				Id4 = "";
				t3 = "";
				t4 = "";
				tag = 3;
				getAlbumType();
			}
		});
		gview3.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				hsv_4.setVisibility(View.GONE);
				gview3dapter.setCheckItem(position);
				ischic3=true;
				ischic4=false;
				parentId = albumTypeList3.get(position).getId();
				t3 = albumTypeList3.get(position).getTypeName();
				Id3 = parentId;
				Id4 = "";
				t4 = "";
				typeId = Id1 + "," + Id2 + "," + Id3;
				tvtext = t1 + "-" + t2 + "-" + t3;
				yixuanze.setText(tvtext);
				tag = 4;
				getAlbumType();
			}
		});
		gview4.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				gview4dapter.setCheckItem(position);
				ischic4=true;
				parentId = albumTypeList4.get(position).getId();
				t4 = albumTypeList4.get(position).getTypeName();
				Id4 = parentId;
				typeId = Id1 + "," + Id2 + "," + Id3 + "," + Id4;
				tvtext = t1 + "-" + t2 + "-" + t3 + "-" + t4;
				yixuanze.setText(tvtext);
			}
		});
	}

	/** 设置GirdView参数，绑定数据 */
	private void setGridView(GridView gview,
			final ArrayList<AlbumType> albumTypeList,
			BuildAublmAdapter gviewadapter) {
		int size = albumTypeList.size();
		int length = 80;
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		int gridviewWidth = (int) (size * (length + 4) * density);
		int itemWidth = (int) (length * density);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
		gview.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
		gview.setColumnWidth(itemWidth); // 设置列表项宽
		gview.setHorizontalSpacing(5); // 设置列表项水平间距
		gview.setStretchMode(GridView.NO_STRETCH);
		gview.setNumColumns(size); // 设置列数量=列表集合数

		gview.setAdapter(gviewadapter);

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
