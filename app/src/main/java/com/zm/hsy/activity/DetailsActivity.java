package com.zm.hsy.activity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.open.SocialConstants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.App;
import com.zm.hsy.R;
import com.zm.hsy.adapter.DetailsListAdapter;
import com.zm.hsy.entity.Album;
import com.zm.hsy.entity.AudioList;
import com.zm.hsy.entity.User;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.myview.WordWrapViewYuan;
import com.zm.hsy.util.ActivityJumpControl;
import com.zm.hsy.util.CustomProgressDialog;
import com.squareup.picasso.Picasso;
/** 专辑详情页 */
public class DetailsActivity extends Activity implements OnClickListener {
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject object = (JSONObject) msg.obj;
                try {
                    JSONArray audioList = object.getJSONArray("audioList");
                    albumList = new ArrayList<AudioList>();
                    for (int i = 0; i < audioList.length(); i++) {
                        AudioList audio = new AudioList();
                        JSONObject data = audioList.getJSONObject(i);
                        audio.setAddTime(data.getString("addTime"));// 添加时间
                        audio.setAudioAlbumId(data.getString("audioAlbumId"));// 所属专辑id
                        audio.setAudioName(data.getString("audioName"));// 音频名称
                        audio.setBlurb(data.getString("blurb"));// 音频-简介
                        audio.setCommentNumber(data.getString("commentNumber"));// 评论数量
                        audio.setCover(data.getString("cover"));// 封面图
                        audio.setId(data.getString("id"));// 音频id
                        audio.setPath(data.getString("path"));// 音频路径
                        audio.setPlayAmount(data.getString("playAmount"));// 播放数量
                        audio.setStatus(data.getString("status"));// 专辑-状态 1正常上线
                        audio.setTimeLong(data.getString("timeLong"));// 音频时长
                        albumList.add(audio);
                    }
                    duoshaoji.setText("共" + audioList.length() + "集");

                    adapter = new DetailsListAdapter(DetailsActivity.this,
                            albumList);
                    mlistView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                    stopProgressDialog();
                    duoshaoji.setText("共0集");
                }
                try {
                    JSONObject audioAlbum = object.getJSONObject("audioAlbum");
                    Album album = new Album();
                    album.setAlbumName(audioAlbum.getString("albumName"));
                    album.setCover(audioAlbum.getString("cover"));
                    album.setLabel(audioAlbum.getString("label"));
                    album.setBlurb(audioAlbum.getString("blurb"));

                    JSONObject user = object.getJSONObject("user");
                    User detuser = new User();
                    detuser.setHead(user.getString("head"));
                    detuser.setNickname(user.getString("nickname"));

                    change(album, detuser);
                } catch (Exception e) {
                    stopProgressDialog();
                }
            } else if (msg.what == URLManager.two) {
                JSONObject object = (JSONObject) msg.obj;
                try {
                    String code = object.getString("code");
                    String message = object.getString("message");
                    if (code.equals("2")) {
                        Futil.showMessage(context, message);
                    } else {
                        Futil.showMessage(context, message);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            stopProgressDialog();
        }
    };
    private ArrayList<AudioList> albumList;
    private DetailsListAdapter adapter;
    private ListView mlistView;
    private ImageView back, share, cover, gengduo;
    private RoundedImageView touxiang;
    private TextView text_top, user_name, top_blurb, top_label1, top_label2,
            top_label3, duoshaoji;
    private RelativeLayout top_rl1, top_rl2, top_rl3;
    private String audioAlbumId = "";
    private String userid;
    private Context context;
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        context = this;
        // 获取布局填充器
        mInflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        userid = Futil.getValue(context, "userid");
        Intent i = this.getIntent();
        audioAlbumId = i.getStringExtra("audioAlbumId");
        System.out.println("audioAlbumId---" + audioAlbumId);
        gointo(audioAlbumId);
        api = WXAPIFactory.createWXAPI(context, URLManager.WXappId, true);
        mTencent = Tencent.createInstance(URLManager.QQappId, context);
        findview();
    }

    public static boolean detonact = false;

    @Override
    protected void onResume() {
        detonact = true;
        setcontextaudio(App.getPlaycode() == -1);
        JPushInterface.onResume(this);
        MobclickAgent.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        detonact = false;
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
    }

    private RoundedImageView playbar_touxiang;
    private ImageView playbar_right;
    private static ImageView playbar_play;
    private TextView playbar_name, details_dingyue_tv;
    private RelativeLayout details_dingyue, details_tuijian, details_xiazai;
    private WordWrapViewYuan details_label;

    private void findview() {
        details_dingyue_tv = (TextView) findViewById(R.id.details_dingyue_tv);
        details_dingyue = (RelativeLayout) findViewById(R.id.details_dingyue);
        details_dingyue.setOnClickListener(this);
        details_tuijian = (RelativeLayout) findViewById(R.id.details_tuijian);
        details_tuijian.setOnClickListener(this);
        details_xiazai = (RelativeLayout) findViewById(R.id.details_xiazai);
        details_xiazai.setOnClickListener(this);
        /** 后台播放信息 */
        playbar_touxiang = (RoundedImageView) findViewById(R.id.playbar_touxiang);
        playbar_right = (ImageView) findViewById(R.id.playbar_right);
        playbar_play = (ImageView) findViewById(R.id.playbar_play);
        playbar_name = (TextView) findViewById(R.id.playbar_name);

        mlistView = (ListView) findViewById(R.id.details_content_view);
        mlistView.setFocusable(false);
        back = (ImageView) findViewById(R.id.back_top);
        gengduo = (ImageView) findViewById(R.id.details_gengduo);
        share = (ImageView) findViewById(R.id.details_share);

        duoshaoji = (TextView) findViewById(R.id.tv_duoshaoji);
        cover = (ImageView) findViewById(R.id.details_cover);
        touxiang = (RoundedImageView) findViewById(R.id.details_user_touxiang);
        text_top = (TextView) findViewById(R.id.details_text_top);
        user_name = (TextView) findViewById(R.id.details_user_name);
        top_blurb = (TextView) findViewById(R.id.details_top_blurb);
        details_label = (WordWrapViewYuan) findViewById(R.id.details_label);
        back.setOnClickListener(this);
        findViewById(R.id.details_share).setOnClickListener(this);
        findViewById(R.id.details_gengduo).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.details_dingyue: {
                if (userid.equals("") || userid == null) {
                    Futil.showMessage(context, "请先登录");
                    break;
                }
                String strUrl = URLManager.AddSubscribe;
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("userSubscribe.userId", userid);// 用户
                map.put("userSubscribe.audioAlbumId", audioAlbumId);// 专辑id
                Futil.xutils(strUrl, map, handler, URLManager.two);
            }
            break;

            case R.id.details_xiazai:
//			ActivityJumpControl.getInstance((Activity) context)
//					.gotoMoreTjActivity(id);
                break;
            case R.id.details_tuijian:
                ActivityJumpControl.getInstance((Activity) context)
                        .gotoMoreTjActivity(audioAlbumId, "1");
                System.out.println("---------");
                break;
            case R.id.details_gengduo:
                openDialog();
                break;
            case R.id.details_share:
                openshare();
                break;
            case R.id.back_top:
                finish();
                break;
            case R.id.dialog_zbgd_hmd:
                ActivityJumpControl.getInstance(this).gotoFeedbackActivity();
                finish();
                break;
            case R.id.dialog_zbgd_qx:
                dialog.dismiss();
                break;
            case R.id.dialog_zbgd_fanhui:
                finish();
                break;
        }
    }

    private void gointo(String audioAlbumId) {
        startProgressDialog();
        //
        String strUrl = URLManager.AudioListByAlbumId;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("audioAlbum.id", audioAlbumId);
        Futil.xutils(strUrl, map, handler, URLManager.one);
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

    /**
     * 后台播放信息
     */
    private String id = null;
    private String AlbumId = null;
    private String mmsh = null;
    private String stationid = null;
    private String stationname = null;
    private int isstation;

    private void setcontextaudio(boolean ss) {
        isstation = App.getIsstation();
        AudioList contextaudio = App.getContextAudioList();
        if (isstation == 0) {
            if (contextaudio != null) {
                id = contextaudio.getId();
                AlbumId = contextaudio.getAudioAlbumId();
                String name = contextaudio.getAudioName();
                playbar_name.setText(name);
                String cover = contextaudio.getCover();
                cover = URLManager.COVER_URL + cover;
                Picasso.with(DetailsActivity.this).load(cover).resize(400, 400).placeholder(R.color.touming)
                        .error(R.mipmap.ic_launcher).into(playbar_touxiang);
            }
        } else if (isstation == 1) {
            mmsh = App.getPlayerpath();
            stationname = App.getStationname();
            stationid = App.getStationid();
            Picasso.with(DetailsActivity.this).load(R.mipmap.yyp).placeholder(R.color.touming)
                    .error(R.mipmap.ic_launcher).into(playbar_touxiang);
        }
        int pcode = App.getPlaycode();
        System.out.println("pcode------" + pcode);
        if (pcode == 1) {
            playbar_play.setSelected(ss);
        } else if (pcode == 0) {
            playbar_play.setSelected(!ss);
        } else {
            playbar_play.setSelected(!ss);
        }
        setonlistener();
    }

    private int playcode;

    private void setonlistener() {

        playbar_play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (playbar_play.isSelected()) {
                    MainActivity.mVV.pause();
                    playcode = 1;
                    playbar_play.setSelected(false);
                } else {
                    // 发起一次播放任务,当然您不一定要在这发起
                    if (!MainActivity.mVV.isPlaying() && (MainActivity.mPlayerStatus != MainActivity.PLAYER_STATUS.PLAYER_IDLE)) {
                        MainActivity.mVV.resume();
                    } else {
                        MainActivity.mEventHandler.sendEmptyMessage(MainActivity.UI_EVENT_PLAY);//UI 事件  播放
                    }
                    playcode = 0;
                    playbar_play.setSelected(true);
                }
                App.setPlaycode(playcode);
            }
        });
        playbar_name.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isstation == 0) {
                    ActivityJumpControl.getInstance(DetailsActivity.this)
                            .gotoDetailsPlayActivity(id);
                } else if (isstation == 1) {
                    ActivityJumpControl.getInstance(DetailsActivity.this)
                            .gotoRadioStationPlayerActivity(mmsh, stationname, stationid);
                }

            }
        });
    }

    public static Handler muhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 10086) {
                playbar_play.setSelected(false);
            }
        }
    };
    private AlertDialog dialog;

    private void openshare() {
        dialog = new AlertDialog.Builder(this)
                .setTitle("title").setMessage("message").create();
        dialog.show();
        dialog.setContentView(R.layout.dialog_share);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.mystyle);  //添加动画

        dialog.findViewById(R.id.share_qq).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.share_kj).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.share_wx).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                sendWx(1, "");
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.share_pyq).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWx(2, "");
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.share_qx).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * @param i(1:分享到微信好友，2分享到朋友圈)
     */
    IWXAPI api;

    private void sendWx(final int i, String bitmapurl) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = URLManager.share;
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "这是消息标题";
        msg.description = "这是消息内容";
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        if (i == 1) req.scene = SendMessageToWX.Req.WXSceneSession;//
        if (i == 2) req.scene = SendMessageToWX.Req.WXSceneTimeline;//
        api.sendReq(req);
        /*
        Picasso.with(context).load(bitmapurl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                // 设置想要的大小
                int newWidth = 99;
                int newHeight = 99;
                // 计算缩放比例
                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;
                // 取得想要缩放的matrix参数
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                // 得到新的图片
                Bitmap newBitMap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                        matrix, true);
                msg.thumbData = Bitmap2Bytes(newBitMap);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = msg;
                if (i == 1) req.scene = SendMessageToWX.Req.WXSceneSession;//
                if (i == 2) req.scene = SendMessageToWX.Req.WXSceneTimeline;//
                api.sendReq(req);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });*/
    }

    Tencent mTencent;

    public void share() {
        Bundle bundle = new Bundle();
        //这条分享消息被好友点击后的跳转URL。
//        bundle.putString(SocialConstants.PARAM_TARGET_URL, "http://60.174.234.45:8080/headlines/article/appArticleInfo?aid="+aid+"&uid=0");
        bundle.putString(SocialConstants.PARAM_TARGET_URL, URLManager.share);
        //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最少必须有一个是有值的。
        bundle.putString(SocialConstants.PARAM_TITLE, "这是消息标题");
        //分享的图片URL
//        bundle.putString(SocialConstants.PARAM_IMAGE_URL, imgUrl);
        //分享的消息摘要，最长50个字
        bundle.putString(SocialConstants.PARAM_SUMMARY, "这是消息内容");
        //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        bundle.putString(SocialConstants.PARAM_APPNAME, "慧思语");
        //标识该消息的来源应用，值为应用名称+AppId。
        bundle.putString(SocialConstants.PARAM_APP_SOURCE, "慧思语" + URLManager.QQappId);

        mTencent.shareToQQ(this, bundle, new IUiListener() {
            @Override
            public void onComplete(Object o) {
            }

            @Override
            public void onError(UiError uiError) {
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private void openDialog() {
        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.setContentView(R.layout.dialog_zbgengduo);

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setAttributes(lp);

        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (ActionBar.LayoutParams.WRAP_CONTENT); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);

        TextView zbgd_hmd = (TextView) dialog.findViewById(R.id.dialog_zbgd_hmd);
        TextView zbgd_fanhui = (TextView) dialog.findViewById(R.id.dialog_zbgd_fanhui);
        zbgd_hmd.setOnClickListener(this);
        zbgd_fanhui.setOnClickListener(this);
        zbgd_hmd.setText("投诉");
        dialog.findViewById(R.id.dialog_zbgd_qx).setOnClickListener(this);

    }

    private void change(Album album, User detuser) {

        String albumName = album.getAlbumName();// 专辑名称
        String blurb = album.getBlurb();// 专辑简介
        String label = album.getLabel();// 专辑标签
        String cover = album.getCover();// 专辑封面

        String nickname = detuser.getNickname();// 用户名称
        String head = detuser.getHead();// 用户头像

        text_top.setText(albumName);
        user_name.setText(nickname);
        top_blurb.setText(blurb);

        // 分割字符串
        if (label != null && !label.equals("") && !label.equals("null")) {
            String a[] = label.split(",");
            details_label.removeAllViews();
            for (int i = 0; i < a.length; i++) {
                final String value = a[i].trim();
                TextView textview = (TextView) mInflater.inflate(
                        R.layout.details_playpage_label_item, null);
                textview.setText(value);
                textview.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityJumpControl.getInstance((Activity) context)
                                .gotoXGAlbumActivity(value);
                    }
                });
                details_label.addView(textview);
            }
        } else {
            details_label.removeAllViews();
            TextView textview = (TextView) mInflater.inflate(
                    R.layout.details_playpage_label_item, null);
            textview.setText("暂无简介");
            details_label.addView(textview);
        }
        // if(Cover != null)
        // holder.details_cover
        // holder.details_user_touxiang
    }
}
