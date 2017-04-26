package com.zm.hsy.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import cn.qqtheme.framework.picker.FilePicker;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.adapter.AddbbsTopicGridViewAdapter;
import com.zm.hsy.entity.Filedata;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.Bimp;
import com.zm.hsy.util.CustomProgressDialog;
import com.zm.hsy.util.FileUtil;
import com.zm.hsy.util.MyPopupWindow;
import com.zm.hsy.util.PhotoFileUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/** 发帖 */
public class AddBBSTopicActivity extends Activity implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String message = obj.getString("message");
                    if (message.equals("发贴成功！")) {
                        popup=new MyPopupWindow(context,true,"发贴成功！");
                        popup.show();
                        stopProgressDialog();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                popup.dismiss();
                                finish();
                            }
                        },2000);
                    } else {
                        Toast.makeText(context, "" + message,
                                Toast.LENGTH_LONG).show();
                        stopProgressDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("发布帖子——返回信息" + obj);
                    stopProgressDialog();
                }
            } else if (msg.what == URLManager.two) {//资源分数
                stopProgressDialog();
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    JSONArray scorearray = obj
                            .getJSONArray("communityTopicScoreList");
                    for (int i = 0; i < scorearray.length(); i++) {
                        JSONObject scoreobj = scorearray.getJSONObject(i);
                        scoreids.add(scoreobj.getString("id"));
                        scores.add(scoreobj.getString("score"));
                    }
                    setGridView(score_gview, scores);
                    mylistener();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    };

    private List<String> scores = new ArrayList<String>();
    private List<String> scoreids = new ArrayList<String>();
    private AddbbsTopicGridViewAdapter gridadapter;
    private String userid, communityid;
    private String scoreid = "0";
    private GridView score_gview;
    private Context context;
    private MyPopupWindow popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbbs_topic);
        context = this;
        score_gview = (GridView) findViewById(R.id.score_gview);

        Intent i = this.getIntent();
        communityid = i.getStringExtra("communityid");
        userid = Futil.getValue(context, "userid");

        findview();
        getscore();

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

    protected void onRestart() {
//        adapter.update();
        super.onRestart();
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

    private AlertDialog dialog;
    private static final int PHOTO_CARMERA = 1;// 相机
    private static final int PHOTO_PICK = 2;// 相册
    private static final int PHOTO_CUT = 3;// 裁剪
    private ImageView imgIcon;// 初始化图片
    public static String SDPATH = Environment.getExternalStorageDirectory()
            + "/hsyfm/recordimg/";
    public static File destDir = new File(SDPATH);

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_top:
                finish();
                break;
            case R.id.add_bbstopic:
                getet();
                break;
            case R.id.upload_res:
//                onFilePicker();
                getfileanddir();
                break;
            case R.id.reply_addtupian:
//                ActivityJumpControl.getInstance(context)
//                        .gotoPhotoalbumActivity();
                openDialog();
                break;
            case R.id.filepathup:
                System.out.println("oldpath>>>" + oldpath);
                System.out.println("myfilepath>>>" + myfilepath);
                System.out.println("newpath>>>" + newpath);
                if (oldpath.equals(myfilepath)) {
                    dialog.dismiss();
                } else {
                    filepathname.setText(newpath);
                    fileTempList = Futil.gethsyFiles(newpath);
                    adapter = new FiledataAdapter(context, fileTempList);
                    file_viewp.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private ArrayList<Filedata> fileTempList;
    private FiledataAdapter adapter;
    private ListView file_viewp;
    private String myfilepath = Environment.getExternalStorageDirectory() + "/hsyfm";
    private TextView filepathname, filepathup;
    private String oldpath = "";
    private String newpath = "";

    private void getfileanddir() {
        oldpath = myfilepath;
        fileTempList = Futil.gethsyFiles(myfilepath);
        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.setContentView(R.layout.dialog_hsyfiles);

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setAttributes(lp);

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);
        filepathname = (TextView) dialog.findViewById(R.id.filepathname);
        filepathname.setText(myfilepath);
        filepathup = (TextView) dialog.findViewById(R.id.filepathup);
        filepathup.setOnClickListener(this);
        file_viewp = (ListView) dialog.findViewById(R.id.file_viewp);
        adapter = new FiledataAdapter(context, fileTempList);
        file_viewp.setAdapter(adapter);
    }

    //资源分数
    private void mylistener() {
        score_gview
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        gridadapter.setCheckItem(position);
                        scoreid = scores.get(position);
                    }
                });

    }

    /**
     * 设置GirdView参数，绑定数据
     */
    @SuppressWarnings("unused")
    private void setGridView(GridView gview, List<String> items) {
        int size = items.size();
        int length = 70;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        gview.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gview.setColumnWidth(itemWidth); // 设置列表项宽
        gview.setHorizontalSpacing(5); // 设置列表项水平间距
        gview.setStretchMode(GridView.NO_STRETCH);
        gview.setNumColumns(size); // 设置列数量=列表集合数
        gridadapter = new AddbbsTopicGridViewAdapter(getApplicationContext(),
                scores);
        gview.setAdapter(gridadapter);
    }


    private ImageView back_top, addtupian, upload_res;
    private TextView add_bbstopic;
    private GridView noScrollgridview;
    //    private GridAdapter adapter;
    private EditText title_et, content_et;

    private void findview() {
        findViewById(R.id.upload_res).setOnClickListener(this);
        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);

        noScrollgridview.setFocusable(false);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));

        title_et = (EditText) findViewById(R.id.topic_title_et);
        content_et = (EditText) findViewById(R.id.topic_content_et);
        imgIcon = (ImageView) findViewById(R.id.addimg);

        back_top = (ImageView) findViewById(R.id.back_top);
        add_bbstopic = (TextView) findViewById(R.id.add_bbstopic);
        addtupian = (ImageView) findViewById(R.id.reply_addtupian);
        back_top.setOnClickListener(this);
        add_bbstopic.setOnClickListener(this);
        addtupian.setOnClickListener(this);

    }


    private void getet() {
        int n = title_et.getText().length();
        if (!userid.equals("")) {
            if (n >= 0) {
                String str1 = title_et.getText().toString().trim();
                String str2 = content_et.getText().toString().trim();
                addTopic(str1, str2);
            } else {
                Toast.makeText(context, "请限制社区名称长度",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "请先登录", Toast.LENGTH_LONG)
                    .show();
        }

    }

    private void addTopic(String title_et, String content_et) {
        startProgressDialog();
        HashMap<String, File> mapfile = new HashMap<String, File>();
        String strUrl = URLManager.BuildTopic;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user.id", userid);
        map.put("community.id", communityid);
        map.put("communityTopic.topicId", "0");// 发帖固定为0
        map.put("communityTopic.title", title_et);
        map.put("communityTopic.content", content_et);
        map.put("communityTopic.parentId", "0");// 发帖固定为0
        map.put("communityTopic.score", scoreid);// 资源
        if (imgFile.exists()) {
            mapfile.put("pic", imgFile);
        }
        if (currentFile != null) {
            String cpath = currentFile.getPath();
            String type = cpath.substring(cpath.indexOf(".") + 1, cpath.length());
            System.out.println("type>>>" + type);
            if (!type.equals("png") && !type.equals("jpg") && !type.equals("mp3") && !type.equals("mp4")) {
                Futil.showMessage(context, "附件格式不正确");
                return;
            }
            map.put("type", type);
            mapfile.put("attachment", currentFile);// 附件
        }
        Futil.xutils(strUrl, map, mapfile, handler, URLManager.one);


    }

    private void getscore() {
        String strUrl = URLManager.GetScore;
        HashMap<String, String> map = new HashMap<String, String>();

        Futil.xutils(strUrl, map, handler, URLManager.two);
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
        if (issdkard()) {
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
        } else {
            Futil.showMessage(context, "请检查sd卡是否插入");
            return;
        }
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

    private File currentFile = null;

    private void onFilePicker() {
        //noinspection MissingPermission
        FilePicker picker = new FilePicker(this, FilePicker.FILE);
        picker.setShowHideDir(false);
        //picker.setAllowExtensions(new String[]{".apk"});
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
                if (FileUtil.getFile(currentPath) != null) {
                    currentFile = FileUtil.getFile(currentPath);
                } else {
                    Futil.showMessage(context, "文件不存在");
                }
            }
        });
        picker.show();
    }

    /**
     * 选择文件
     * ArrayList<Filedata> fileTempList
     */

    public class FiledataAdapter extends BaseAdapter {
        private ArrayList<Filedata> fileTempList;
        private Context context;
        private LayoutInflater minflater;

        public FiledataAdapter(Context context, ArrayList<Filedata> fileTempList) {
            this.minflater = LayoutInflater.from(context);
            this.context = context;
            this.fileTempList = fileTempList;
        }

        @Override
        public int getCount() {
            if (null != fileTempList) {
                return fileTempList.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return fileTempList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {

            ViewHolder holder = null;
            if (convertView == null) {
                convertView = minflater.inflate(R.layout.dialog_hsyfiles_item, null);
                holder = new ViewHolder();
                holder.filename = (TextView) convertView
                        .findViewById(R.id.filename);
                holder.chickfile_ll = (LinearLayout) convertView
                        .findViewById(R.id.chickfile_ll);
                holder.filetypeimg = (ImageView) convertView
                        .findViewById(R.id.filetypeimg);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final String fpath = fileTempList.get(position).getFpath();
            String type = fileTempList.get(position).getType();
            String fname = fpath.substring((myfilepath.length() + 1), fpath.length());

            oldpath = fpath.substring(0, fpath.lastIndexOf("/"));
            newpath = oldpath.substring(0, oldpath.lastIndexOf("/"));
            holder.filename.setText(fname);
            if (type.equals("mp3")) {
                holder.chickfile_ll.setVisibility(View.VISIBLE);
                holder.filetypeimg.setImageResource(R.mipmap.mp3img);
            } else if (type.equals("mp4")) {
                holder.chickfile_ll.setVisibility(View.VISIBLE);
                holder.filetypeimg.setImageResource(R.mipmap.mp4img);
            } else if (type.equals("jpg")) {
                holder.chickfile_ll.setVisibility(View.VISIBLE);
                holder.filetypeimg.setImageResource(R.mipmap.jpgimg);
            } else if (type.equals("png")) {
                holder.chickfile_ll.setVisibility(View.VISIBLE);
                holder.filetypeimg.setImageResource(R.mipmap.pngimg);
            } else if (type.equals("filedir")) {
                holder.chickfile_ll.setVisibility(View.VISIBLE);
                holder.filetypeimg.setImageResource(R.mipmap.wenjianimg);
            } else {
                holder.chickfile_ll.setVisibility(View.GONE);
            }
            holder.chickfile_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File file = new File(fpath);
                    if (file.isDirectory()) {
                        filepathname.setText(fpath);
                        fileTempList = Futil.gethsyFiles(fpath);
                        adapter = new FiledataAdapter(context, fileTempList);
                        file_viewp.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        currentFile = file;
                        dialog.dismiss();
                    }
                }
            });
            return convertView;
        }


        class ViewHolder {
            TextView filename;
            LinearLayout chickfile_ll;
            ImageView filetypeimg;
        }
    }

//
//    @SuppressLint("HandlerLeak")
//    public class GridAdapter extends BaseAdapter {
//        private LayoutInflater inflater; // 视图容器
//        private int selectedPosition = -1;// 选中的位置
//        private boolean shape;
//
//        public boolean isShape() {
//            return shape;
//        }
//
//        public void setShape(boolean shape) {
//            this.shape = shape;
//        }
//
//        public GridAdapter(Context context) {
//            inflater = LayoutInflater.from(context);
//        }
//
//        public void update() {
//            loading();
//        }
//
//        public int getCount() {
//            return (Bimp.bmp.size() + 1);
//        }
//
//        public Object getItem(int arg0) {
//
//            return null;
//        }
//
//        public long getItemId(int arg0) {
//
//            return 0;
//        }
//
//        public void setSelectedPosition(int position) {
//            selectedPosition = position;
//        }
//
//        public int getSelectedPosition() {
//            return selectedPosition;
//        }
//
//        /**
//         * ListView Item设置
//         */
//        public View getView(int position, View convertView, ViewGroup parent) {
//            final int coord = position;
//            ViewHolder holder = null;
//            if (convertView == null) {
//
//                convertView = inflater.inflate(R.layout.activity_addbbs_item_published_grida,
//                        parent, false);
//                holder = new ViewHolder();
//                holder.image = (ImageView) convertView
//                        .findViewById(R.id.item_grida_image);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//
//            if (position == Bimp.bmp.size()) {
//                holder.image.setImageBitmap(BitmapFactory.decodeResource(
//                        getResources(), R.mipmap.icon_addpic_unfocused));
//                if (position == 9) {
//                    holder.image.setVisibility(View.GONE);
//                }
//            } else {
//                holder.image.setImageBitmap(Bimp.bmp.get(position));
//            }
//
//            return convertView;
//        }
//
//        public class ViewHolder {
//            public ImageView image;
//        }
//
//        Handler handler = new Handler() {
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//                    case 1:
//                        adapter.notifyDataSetChanged();
//                        break;
//                }
//                super.handleMessage(msg);
//            }
//        };
//
//        public void loading() {
//            new Thread(new Runnable() {
//                public void run() {
//                    while (true) {
//                        if (Bimp.max == Bimp.drr.size()) {
//                            Message message = new Message();
//                            message.what = 1;
//                            handler.sendMessage(message);
//                            break;
//                        } else {
//                            try {
//                                String path = Bimp.drr.get(Bimp.max);
//                                System.out.println(path);
//                                Bitmap bm = Bimp.revitionImageSize(path);
//                                Bimp.bmp.add(bm);
//                                String newStr = path.substring(
//                                        path.lastIndexOf("/") + 1,
//                                        path.lastIndexOf("."));
//                                PhotoFileUtils.saveBitmap(bm, "" + newStr);
//                                Bimp.max += 1;
//                                Message message = new Message();
//                                message.what = 1;
//                                handler.sendMessage(message);
//                            } catch (IOException e) {
//
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//            }).start();
//        }
//    }
}
