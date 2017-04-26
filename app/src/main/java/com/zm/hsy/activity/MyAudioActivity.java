package com.zm.hsy.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.App;
import com.zm.hsy.R;
import com.zm.hsy.adapter.MyAudioYifabuAdapter;
import com.zm.hsy.adapter.MyVideoYifabuAdapter;
import com.zm.hsy.entity.AudioList;
import com.zm.hsy.entity.VideoList;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.AudioPlayer;
import com.zm.hsy.util.CustomProgressDialog;
import com.zm.hsy.util.FileSizeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
/**
 * 我的录音c 0待发布1已发布 Tag 1录音3视频 toptv 我的声音 我的视频
 */
public class MyAudioActivity extends Activity implements OnClickListener {
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    JSONArray myAudiol = obj.getJSONArray("myAudioList");
                    myaudio = new ArrayList<AudioList>();
                    for (int i = 0; i < myAudiol.length(); i++) {
                        AudioList au = new AudioList();
                        JSONObject myAudioobj = myAudiol.getJSONObject(i);
                        String nickname = myAudioobj.getString("nickname");
                        String count = myAudioobj.getString("count");
                        au.setNickname(nickname);
                        au.setCount(count);

                        JSONObject audio = myAudioobj.getJSONObject("audio");
                        au.setAddTime(audio.getString("addTime"));
                        au.setAudioAlbumId(audio.getString("audioAlbumId"));
                        au.setAudioName(audio.getString("audioName"));
                        au.setCover(audio.getString("cover"));
                        au.setPath(audio.getString("path"));
                        au.setCommentNumber(audio.getString("commentNumber"));
                        au.setStatus(audio.getString("status"));
                        au.setTimeLong(audio.getString("timeLong"));
                        au.setPlayAmount(audio.getString("playAmount"));
                        au.setId(audio.getString("id"));
                        myaudio.add(au);
                    }
                    madapter = new MyAudioYifabuAdapter(MyAudioActivity.this,
                            myaudio);
                    viewp_yifabu.setAdapter(madapter);
                    madapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    stopProgressDialog();
                }

            } else if (msg.what == URLManager.two) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    JSONArray myAudiol = obj.getJSONArray("myVideoList");
                    myvideo = new ArrayList<VideoList>();
                    for (int i = 0; i < myAudiol.length(); i++) {
                        VideoList vl = new VideoList();
                        JSONObject myAudioobj = myAudiol.getJSONObject(i);
                        String nickname = myAudioobj.getString("nickname");
                        vl.setNickname(nickname);

                        JSONObject video = myAudioobj.getJSONObject("video");
                        vl.setAddTime(video.getString("addTime"));
                        vl.setVideoAlbumId(video.getString("videoAlbumId"));
                        vl.setVideoName(video.getString("videoName"));
                        vl.setCover(video.getString("cover"));
                        vl.setPath(video.getString("path"));
                        vl.setCommentNumber(video.getString("commentNumber"));
                        vl.setStatus(video.getString("status"));
                        vl.setTimeLong(video.getString("timeLong"));
                        vl.setPlayAmount(video.getString("playAmount"));
                        vl.setId(video.getString("id"));
                        myvideo.add(vl);
                    }
                    mvadapter = new MyVideoYifabuAdapter(MyAudioActivity.this,
                            myvideo);
                    viewp_yifabu.setAdapter(mvadapter);
                    mvadapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    stopProgressDialog();
                }
            } else if (msg.what == URLManager.three) {
                try {
                    JSONObject obj = (JSONObject) msg.obj;
                    String code = obj.getString("code");
                    if (code.equals("2")) {
                        addyiifabu();
                    }
                } catch (Exception e) {
                    stopProgressDialog();
                }
            }
            stopProgressDialog();
        }

    };
    private MyAudioYifabuAdapter madapter;
    private MyVideoYifabuAdapter mvadapter;
    private ArrayList<AudioList> myaudio;
    private ArrayList<VideoList> myvideo;
    private String c = "0", Tag, toptv, userid;
    private App mapplication;
    private ArrayList<String> allpath;
    private String strUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
//			return;
        setContentView(R.layout.activity_myaudio);
        Log.i("sbcs", "MyAudioAct");
        userid = Futil.getValue(MyAudioActivity.this, "userid");
        findview();

        Intent i = this.getIntent();
        c = i.getStringExtra("c");// 0带发布1已发布
        toptv = i.getStringExtra("toptv");
        myaudio_top_tv1.setText(toptv);
        Tag = i.getStringExtra("Tag");// 1录音3视频
        if (Tag.equals("1")) {
            System.out.println("录音");
        } else if (Tag.equals("3")) {
            System.out.println("视频");
        }
        if (c.equals("0")) {
            adddaifabu(Tag);
            myaudio_tv1.setTextColor(Color.parseColor("#494949"));
            myaudio_tv2.setTextColor(Color.parseColor("#1abc9c"));
            viewp_daifabu.setVisibility(View.VISIBLE);
            viewp_yifabu.setVisibility(View.GONE);
            myaudio_iv2.setVisibility(View.VISIBLE);
            myaudio_iv1.setVisibility(View.GONE);
        } else if (c.equals("1")) {
            addyiifabu();
            myaudio_tv1.setTextColor(Color.parseColor("#1abc9c"));
            myaudio_tv2.setTextColor(Color.parseColor("#494949"));
            viewp_daifabu.setVisibility(View.GONE);
            viewp_yifabu.setVisibility(View.VISIBLE);
            myaudio_iv2.setVisibility(View.GONE);
            myaudio_iv1.setVisibility(View.VISIBLE);
            yifabulistener();
        }

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
            case R.id.back_top:
                finish();
                break;
            case R.id.myaudio_tv1:// 已发布
                addyiifabu();//获取已发布视频列表
                myaudio_tv1.setTextColor(Color.parseColor("#1abc9c"));
                myaudio_tv2.setTextColor(Color.parseColor("#494949"));
                viewp_yifabu.setVisibility(View.VISIBLE);
                viewp_daifabu.setVisibility(View.GONE);
                myaudio_iv1.setVisibility(View.VISIBLE);
                myaudio_iv2.setVisibility(View.GONE);
                break;
            case R.id.myaudio_tv2:// 待发布
                adddaifabu(Tag);
                myaudio_tv1.setTextColor(Color.parseColor("#494949"));
                myaudio_tv2.setTextColor(Color.parseColor("#1abc9c"));
                viewp_daifabu.setVisibility(View.VISIBLE);
                viewp_yifabu.setVisibility(View.GONE);
                myaudio_iv2.setVisibility(View.VISIBLE);
                myaudio_iv1.setVisibility(View.GONE);
                break;
        }
    }

    private ImageView back_top, myaudio_iv2, myaudio_iv1;
    private TextView myaudio_tv1, myaudio_tv2, myaudio_top_tv1;
    private ListView viewp_daifabu, viewp_yifabu;

    private void findview() {
        myaudio_top_tv1 = (TextView) findViewById(R.id.myaudio_top_tv1);
        back_top = (ImageView) findViewById(R.id.back_top);
        back_top.setOnClickListener(this);
        // 已发布
        myaudio_tv1 = (TextView) findViewById(R.id.myaudio_tv1);
        myaudio_iv1 = (ImageView) findViewById(R.id.myaudio_iv1);
        myaudio_tv1.setOnClickListener(this);
        // 待发布
        myaudio_tv2 = (TextView) findViewById(R.id.myaudio_tv2);
        myaudio_iv2 = (ImageView) findViewById(R.id.myaudio_iv2);
        myaudio_tv2.setOnClickListener(this);

        viewp_daifabu = (ListView) findViewById(R.id.myaudio_viewp_daifabu);
        viewp_daifabu.setFocusable(false);
        viewp_yifabu = (ListView) findViewById(R.id.myaudio_viewp_yifabu);
        viewp_yifabu.setFocusable(false);

    }

    //获取已发布视频列表
    private void addyiifabu() {
        startProgressDialog();
        HashMap<String, String> map = new HashMap<String, String>();

        if (!userid.equals("")) {
            map.put("user.id", userid);
            if (Tag.equals("1")) {
                strUrl = URLManager.GetMyAudio;
                Futil.xutils(strUrl, map, handler, URLManager.one);
            } else if (Tag.equals("3")) {
                strUrl = URLManager.GetMyVideo;
                Futil.xutils(strUrl, map, handler, URLManager.two);
            }
        }
    }

    private AudioList audio;

    //已发布的listview的监听
    private void yifabulistener() {
        viewp_yifabu.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                ImageView yifabu_statusiv = (ImageView) v
                        .findViewById(R.id.yifabu_statusiv);
                audio = myaudio.get(position);
                yifabu_statusiv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        yifabudialog();
                    }

                });

            }
        });

    }

    private void adddaifabu(String Tag) {
        ArrayList<String> sKey = Futil.loadKeyArray(MyAudioActivity.this, Tag);
        allpath = new ArrayList<String>();
        allpath.clear();
        ArrayList<AudioList> abl = new ArrayList<AudioList>();
        for (int i = 0; i < sKey.size(); i++) {
            AudioList a = new AudioList();
            String key = sKey.get(i);
            String timeLong = (String) Futil.getValue(MyAudioActivity.this, key
                    + "time");
            String audiocover = (String) Futil.getValue(MyAudioActivity.this,
                    key + "cover");
            String audioName = (String) Futil.getValue(MyAudioActivity.this,
                    key + "name");
            String path = (String) Futil.getValue(MyAudioActivity.this, key
                    + "path");
            double filesSize = FileSizeUtil.getFileOrFilesSize(path, 3);
            a.setAddTime(key);
            a.setTimeLong(timeLong);
            a.setAudioName(audioName);
            a.setCover(audiocover);
            a.setPath(path);
            a.setFilesSize(filesSize);
            abl.add(a);
            allpath.add(path);
            //获取视频缩略图
//			if(Tag.equals("3")){
//				Bimp bimp =new Bimp();
//				bimp.getVideoThumbnail(path);
//			}
        }

        MyAudioDaifabuAdapter adapter = new MyAudioDaifabuAdapter(
                MyAudioActivity.this, abl);
        viewp_daifabu.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private AlertDialog dialog;

    private void yifabudialog() {
        dialog = new AlertDialog.Builder(MyAudioActivity.this).create();
        dialog.show();
        dialog.setContentView(R.layout.dialog_yifabu);
        RelativeLayout rl1 = (RelativeLayout) dialog
                .findViewById(R.id.dialog_yifabu_rl1);// 编辑
        RelativeLayout rl2 = (RelativeLayout) dialog
                .findViewById(R.id.dialog_yifabu_rl2);// 下载
        RelativeLayout rl3 = (RelativeLayout) dialog
                .findViewById(R.id.dialog_yifabu_rl3);// 删除
        RelativeLayout rl4 = (RelativeLayout) dialog
                .findViewById(R.id.dialog_yifabu_rl4);// 取消

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setAttributes(lp);

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.4
        p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);
        audioid = audio.getId();
        rl1.setOnClickListener(new OnClickListener() {// 编辑

            @Override
            public void onClick(View v) {
                System.out.println("audioid--" + audioid);
                ActivityJumpControl.getInstance(MyAudioActivity.this)
                        .gotoMyAudioCompileActivity(audioid);
                MyAudioActivity.this.finish();
                dialog.dismiss();
            }
        });
        rl2.setOnClickListener(new OnClickListener() {// 下载

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });
        rl3.setOnClickListener(new OnClickListener() {// 删除

            @Override
            public void onClick(View v) {
                delAudio();
                dialog.dismiss();
            }
        });
        rl4.setOnClickListener(new OnClickListener() {// 取消

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private String audioid;

    private void delAudio() {
        startProgressDialog();
        HashMap<String, String> map = new HashMap<String, String>();
        if (!userid.equals("")) {
            map.put("audio.id", audioid);
            if (Tag.equals("1")) {
                strUrl = URLManager.delAudio;
                Futil.xutils(strUrl, map, handler, URLManager.three);
            }
        }
    }

    /**
     * Adapter 待发布
     *
     * @author Administrator
     */
    private boolean coverse = true;
    private int playcode;

    public class MyAudioDaifabuAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater minflater;
        private ArrayList<AudioList> albumList = new ArrayList<AudioList>();
        private AudioPlayer player;

        public MyAudioDaifabuAdapter(Context context,
                                     ArrayList<AudioList> albumList) {
            super();
            this.minflater = LayoutInflater.from(context);
            this.albumList = albumList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return albumList.size();
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
        public View getView(final int position, View convertView,
                            ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                convertView = minflater.inflate(R.layout.activity_myaudio_item,
                        null);
                holder = new ViewHolder();
                holder.audioName_tv = (TextView) convertView
                        .findViewById(R.id.daifabu_audioName_tv);
                holder.username_tv = (TextView) convertView
                        .findViewById(R.id.daifabu_username_tv);
                holder.timeLong_tv = (TextView) convertView
                        .findViewById(R.id.daifabu_timeLong_tv);
                holder.audiosize_tv = (TextView) convertView
                        .findViewById(R.id.daifabu_audiosize_tv);
                //待发布
                holder.daifabu_publish = (TextView) convertView
                        .findViewById(R.id.daifabu_publish);
                holder.daifabu_addtime = (TextView) convertView
                        .findViewById(R.id.daifabu_addtime_tv);
                holder.daifabu_paly = (ImageView) convertView
                        .findViewById(R.id.daifabu_paly_iv);

                holder.coverivi_iv = (RoundedImageView) convertView
                        .findViewById(R.id.daifabu_coverivi_iv);
                holder.daifabu_ll = (LinearLayout) convertView
                        .findViewById(R.id.daifabu_ll);

                holder.single_line = (View) convertView
                        .findViewById(R.id.daifabu_single_line);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == 0) {
                holder.single_line.setVisibility(View.GONE);
            }
            final String addtime = albumList.get(position).getAddTime();
            final String playerpath = albumList.get(position).getPath();

            String timeLong = albumList.get(position).getTimeLong();
            String audioName = albumList.get(position).getAudioName();
            double filesSize = albumList.get(position).getFilesSize();
            holder.audiosize_tv.setText("" + filesSize + "MB");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                Date d1 = df.parse(addtime);
                Date curDate = new Date(System.currentTimeMillis());
                long diff = curDate.getTime() - d1.getTime();// 这样得到的差值是微秒级别
                long days = diff / (1000 * 60 * 60 * 24);
                if (days >= 1) {
                    holder.daifabu_addtime.setText(days + "天前");
                } else {
                    long hours = diff / (1000 * 60 * 60);
                    if (hours >= 1) {
                        holder.daifabu_addtime.setText(hours + "小时前");
                    } else {
                        holder.daifabu_addtime.setText("刚刚");

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.audioName_tv.setText(audioName);
            holder.username_tv.setText("by");
            holder.timeLong_tv.setText(timeLong);

            String coverpath = albumList.get(position).getCover();// 封面图
            File file = new File(coverpath);
            if (file.exists()) {
                Bitmap bm = BitmapFactory.decodeFile(coverpath);
                // 将图片显示到ImageView中
                holder.coverivi_iv.setImageBitmap(bm);
            }

            holder.coverivi_iv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg1) {
                    opendialog(addtime, position);
                }

            });
            //待发布
            holder.daifabu_publish
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg1) {
                            ActivityJumpControl.getInstance((Activity) context)
                                    .gotoPublishRecordActivity(addtime, Tag);
                            MyAudioActivity.this.finish();
                        }

                    });
            holder.daifabu_ll.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg1) {
                    opendialog(addtime, position);
                }

            });
            return convertView;
        }

        private void opendialog(final String addtime, final int position) {
            dialog = new AlertDialog.Builder(MyAudioActivity.this).create();
            dialog.show();
            dialog.setContentView(R.layout.dialog_daifabu);
            RelativeLayout rl1 = (RelativeLayout) dialog
                    .findViewById(R.id.dialog_audiogl_rl1);// 发布
            RelativeLayout rl2 = (RelativeLayout) dialog
                    .findViewById(R.id.dialog_audiogl_rl2);// 播放
            RelativeLayout rl3 = (RelativeLayout) dialog
                    .findViewById(R.id.dialog_audiogl_rl3);// 删除
            RelativeLayout rl4 = (RelativeLayout) dialog
                    .findViewById(R.id.dialog_audiogl_rl4);// 取消

            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.BOTTOM);
            dialogWindow.setAttributes(lp);

            WindowManager m = getWindowManager();
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.4
            p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65
            dialogWindow.setAttributes(p);
            rl1.setOnClickListener(new OnClickListener() {// 发布

                @Override
                public void onClick(View v) {
                    ActivityJumpControl.getInstance((Activity) context)
                            .gotoPublishRecordActivity(addtime, Tag);
                    MyAudioActivity.this.finish();
                    dialog.dismiss();
                }
            });
            rl2.setOnClickListener(new OnClickListener() {// 播放

                @Override
                public void onClick(View v) {
                    if (Tag.equals("1")) {
                        dialog.dismiss();
                        playdialog(position);
                    } else if (Tag.equals("3")) {
                        dialog.dismiss();
                        String path = allpath.get(position);
                        ActivityJumpControl.getInstance((Activity) context)
                                .gotoVideoviewActivity(path, addtime, Tag);
                    }

                }

            });
            rl3.setOnClickListener(new OnClickListener() {// 删除

                @Override
                public void onClick(View v) {
                    ArrayList<String> sKey = Futil.loadKeyArray(context, Tag);
                    sKey.remove(addtime);
                    String path = Futil.getValue(context, addtime + "path");
                    File file = new File(path);
                    if (file.exists()) { // 判断文件是否存在
                        Futil.deleteFile(file);
                    }
                    Futil.saveKeyArray(context, sKey, Tag);
                    Futil.romveValue(context, addtime, Tag);

                    dialog.dismiss();

                    adddaifabu(Tag);
                }
            });
            rl4.setOnClickListener(new OnClickListener() {// 取消

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        }

        private void playdialog(int position) {
            final String playerpath = allpath.get(position);
            System.out.println("playerpath---" + playerpath);
            dialog = new AlertDialog.Builder(MyAudioActivity.this).create();
            dialog.show();
            dialog.setContentView(R.layout.dialog_daifabuplay);

            LinearLayout dialog_audiorl = (LinearLayout) dialog
                    .findViewById(R.id.dialog_audiorl);// 音频播放
            final ImageView audio_play = (ImageView) dialog
                    .findViewById(R.id.dialog_audio_play);// 播放按钮
            final SeekBar rl2 = (SeekBar) dialog
                    .findViewById(R.id.dialog_audio_seekbar);// seekbar
            RelativeLayout rl3 = (RelativeLayout) dialog
                    .findViewById(R.id.dialog_audio_rl3);// 取消
            rl2.setOnSeekBarChangeListener(new SeekBarChangeEvent());
            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.BOTTOM);
            dialogWindow.setAttributes(lp);

            WindowManager m = getWindowManager();
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
            WindowManager.LayoutParams wp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            wp.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.4
            wp.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65
            dialogWindow.setAttributes(wp);
            audio_play.setOnClickListener(new OnClickListener() {// 播放按钮

                @Override
                public void onClick(View v) {
//							if (MainActivity.player.isplay()) {
//								MainActivity.player.pause();
//								playcode = 1;
//							} else {
//								playcode = 0;
//							}
                    mapplication.setPlaycode(playcode);
                    if (audio_play.isSelected()) {//
                        player.pause();
                        audio_play.setSelected(false);
                    } else {
                        player = new AudioPlayer(rl2);
                        player.playPath(playerpath);
                        player.play();
                        audio_play.setSelected(true);
                    }
                    player.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            player.stop();
                            player=null;
                            dialog.dismiss();
                        }
                    });
                }
            });
            rl2.setOnClickListener(new OnClickListener() {// seekbar

                @Override
                public void onClick(View v) {
                }

            });
            rl3.setOnClickListener(new OnClickListener() {// 取消

                @Override
                public void onClick(View v) {
                    if (player != null) {
                        player.stop();
                        player = null;
                    }
                    dialog.dismiss();
                }
            });

        }

        class SeekBarChangeEvent implements OnSeekBarChangeListener {
            int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
                this.progress = progress * player.mediaPlayer.getDuration()
                        / seekBar.getMax();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
                player.mediaPlayer.seekTo(progress);
            }

        }

        class ViewHolder {
            private LinearLayout daifabu_ll;
            private ImageView daifabu_paly;
            private RoundedImageView coverivi_iv;
            private TextView username_tv, audioName_tv, timeLong_tv,
                    audiosize_tv, daifabu_publish, daifabu_addtime;
            private View single_line;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
