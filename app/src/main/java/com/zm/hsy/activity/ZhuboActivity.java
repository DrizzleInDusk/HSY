package com.zm.hsy.activity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.zm.hsy.R;
import com.zm.hsy.adapter.ZhubosyAdapter;
import com.zm.hsy.adapter.ZhubovideoAdapter;
import com.zm.hsy.adapter.ZhubozjAdapter;
import com.zm.hsy.entity.Album;
import com.zm.hsy.entity.AudioList;
import com.zm.hsy.entity.User;
import com.zm.hsy.entity.VideoList;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.myview.RoundedImageView;
import com.zm.hsy.util.CustomProgressDialog;
import com.squareup.picasso.Picasso;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/** 主播页 */
public class ZhuboActivity extends Activity implements OnClickListener {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    brStatus = obj.getString("brStatus");
                } catch (JSONException e) {
                    e.printStackTrace();
                    brStatus = "no";
                }
                try {
                    JSONObject data = obj.getJSONObject("userRank");

                    String fans = data.getString("fans");
                    String guanzhu = data.getString("guanzhu");
                    String zan = data.getString("zan");
                    JSONObject userdata = data.getJSONObject("user");
                    User u = new User();
                    u.setFans(fans);
                    u.setGuanzhu(guanzhu);
                    u.setZan(zan);
                    u.setHead(userdata.getString("head"));
                    u.setBlurb(userdata.getString("blurb"));
                    u.setNickname(userdata.getString("nickname"));
                    u.setMembers(userdata.getString("members"));
                    u.setHeadStatus(data.getString("headStatus"));

                    String aalbumCount = obj.getString("audioAlbumCount");
                    String audioCount = obj.getString("audioCount");
                    String valbumCount = obj.getString("videoAlbumCount");
                    String videoCount = obj.getString("videoCount");
                    if (aalbumCount != null && !aalbumCount.equals("")
                            && !aalbumCount.equals("null")) {
                        audioalbum_num.setText("音频专辑(" + aalbumCount + ")");
                    }
                    if (audioCount != null && !audioCount.equals("")
                            && !audioCount.equals("null")) {
                        audio_num.setText("发布的声音(" + audioCount + ")");
                    }
                    if (valbumCount != null && !valbumCount.equals("")
                            && !valbumCount.equals("null")) {
                        videoalbum_num.setText("视频专辑(" + valbumCount + ")");
                    }
                    if (videoCount != null && !videoCount.equals("")
                            && !videoCount.equals("null")) {
                        video_num.setText("发布的视频(" + videoCount + ")");
                    }
                    changeview(u);

                    try {
                        // 音频专辑
                        JSONArray zList = obj.getJSONArray("audioAlbumList");

                        albumList = new ArrayList<Album>();
                        if (zList.length() != 0) {
                            audioalbum_num.setVisibility(View.VISIBLE);
                            for (int i = 0; i < zList.length(); i++) {
                                Album album = new Album();
                                JSONObject z = zList.getJSONObject(i);

                                String cover = z.getString("cover");
                                String albumName = z.getString("albumName");
                                String playAmount = z.getString("playAmount");
                                String episode = z.getString("episode");
                                String id = z.getString("id");
                                String addTime = z.getString("addTime");

                                album.setId(id);
                                album.setAlbumName(albumName);
                                album.setAddTime(addTime);
                                album.setPlayAmount(playAmount);
                                album.setEpisode(episode);
                                album.setCover(cover);
                                albumList.add(album);
                            }

                            zjadapter = new ZhubozjAdapter(context, albumList);
                            audioalbum_viewp.setAdapter(zjadapter);
                            zjadapter.notifyDataSetChanged();
                        } else {
                            audioalbum_num.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    try {
                        JSONArray sList = obj.getJSONArray("audioList");
                        if (sList.length() != 0) {
                            audio_num.setVisibility(View.VISIBLE);
                            audioList = new ArrayList<AudioList>();
                            for (int i = 0; i < sList.length(); i++) {
                                AudioList audio = new AudioList();
                                JSONObject sdata = sList.getJSONObject(i);
                                audio.setAddTime(sdata.getString("addTime"));// 添加时间
                                audio.setAudioAlbumId(sdata
                                        .getString("audioAlbumId"));// 所属专辑id
                                audio.setAudioName(sdata.getString("audioName"));// 音频名称
                                audio.setBlurb(sdata.getString("blurb"));// 音频-简介
                                audio.setCommentNumber(sdata
                                        .getString("commentNumber"));// 评论数量
                                audio.setCover(sdata.getString("cover"));// 封面图
                                audio.setId(sdata.getString("id"));// 音频id
                                audio.setPath(sdata.getString("path"));// 音频路径
                                audio.setPlayAmount(sdata.getString("playAmount"));// 播放数量
                                audio.setStatus(sdata.getString("status"));// 专辑-状态
                                // 1正常上线
                                audio.setTimeLong(sdata.getString("timeLong"));// 音频时长
                                audioList.add(audio);
                            }

                            syadapter = new ZhubosyAdapter(context, audioList);
                            audio_viewp.setAdapter(syadapter);
                            syadapter.notifyDataSetChanged();

                        } else {
                            audio_num.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    try {
                        JSONArray myVideoAlbum = obj.getJSONArray("videoAlbumList");

                        if (myVideoAlbum.length() != 0) {
                            // 视频专辑
                            videoalbum_num.setVisibility(View.VISIBLE);
                            videoalbumList = new ArrayList<Album>();
                            for (int i = 0; i < myVideoAlbum.length(); i++) {
                                Album album = new Album();
                                JSONObject z = myVideoAlbum.getJSONObject(i);

                                String cover = z.getString("cover");
                                String albumName = z.getString("albumName");
                                String playAmount = z.getString("playAmount");
                                String episode = z.getString("episode");
                                String id = z.getString("id");
                                String addTime = z.getString("addTime");

                                album.setId(id);
                                album.setAlbumName(albumName);
                                album.setAddTime(addTime);
                                album.setPlayAmount(playAmount);
                                album.setEpisode(episode);
                                album.setCover(cover);
                                videoalbumList.add(album);
                            }  videoalbum_num.setVisibility(View.GONE);
                            videoalbum_viewp.setVisibility(View.GONE);
                            viadapter = new ZhubozjAdapter(context, videoalbumList);
                            videoalbum_viewp.setAdapter(viadapter);
                            viadapter.notifyDataSetChanged();
                        } else {
                            videoalbum_num.setVisibility(View.GONE);
                            videoalbum_viewp.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) { videoalbum_num.setVisibility(View.GONE);
                        videoalbum_viewp.setVisibility(View.GONE);
                        stopProgressDialog();
                    }
                    try {
                        JSONArray myAudiol = obj.getJSONArray("videoList");
                        System.out.println("myVideoList>>>"+myAudiol);
                        if (myAudiol.length() != 0) {
                            video_num.setVisibility(View.VISIBLE);
                            myvideo = new ArrayList<VideoList>();
                            for (int i = 0; i < myAudiol.length(); i++) {
                                VideoList vl = new VideoList();
                                JSONObject video = myAudiol.getJSONObject(i);
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
                            vadapter = new ZhubovideoAdapter(context,myvideo);
                            video_viewp.setAdapter(vadapter);
                            vadapter.notifyDataSetChanged();
                        } else {
                            video_num.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        stopProgressDialog();
                    }
                    stopProgressDialog();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (msg.what == URLManager.two) {// 关注
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String message = obj.getString("message");
                    String code = obj.getString("code");
                    if (code.equals("2")) {
                        Futil.showMessage(context, message);
                        concem_tv.setText(hasconcem);
                    } else {
                        Futil.showMessage(context, message);
                        hasconcem = null;
                    }
                } catch (JSONException e) {
                    stopProgressDialog();
                    e.printStackTrace();
                }
            } else if (msg.what == URLManager.three) {// 黑名单
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    String message = obj.getString("message");
                    String code = obj.getString("code");
                    Futil.showMessage(context, message);
                    dialog.dismiss();
                    if (code.equals("2")) {
                        gointo();
                    }
                } catch (JSONException e) {
                    stopProgressDialog();
                    e.printStackTrace();
                }
            }
            stopProgressDialog();
        }

    };
    private ArrayList<Album> albumList;
    private ArrayList<Album> videoalbumList;
    private ArrayList<AudioList> audioList;
    private ArrayList<VideoList> myvideo;
    private ImageView back_top;
    private String userid, brStatus, zhuboid;
    private Context context;

    private ZhubozjAdapter zjadapter;
    private ZhubozjAdapter viadapter;
    private ZhubosyAdapter syadapter;
    private ZhubovideoAdapter vadapter;
    private RelativeLayout concem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhubo);
        context = this;
        Intent i = this.getIntent();
        zhuboid = i.getStringExtra("zhuboid");
        userid = Futil.getValue(context, "userid");
        api = WXAPIFactory.createWXAPI(context, URLManager.WXappId, true);
        mTencent = Tencent.createInstance(URLManager.QQappId,context);
        findview();
        gointo();

    }

    private String hasconcem;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.concem://关注
                String trim = concem_tv.getText().toString().trim();
                if (trim.equals("已关注")) {
                    hasconcem = "关注";
                    delConcem();
                } else {
                    hasconcem = "已关注";
                    addConcem();
                }
                break;
            case R.id.zhubo_share:
                openshare();
                break;
            case R.id.zhubo_gengduo:
                openDialog();
                break;
            case R.id.dialog_zbgd_hmd:
                if(!userid.equals("")&&!userid.equals("null")){
                    aodBlackRoster();
                }else{
                    Futil.showMessage(context,"请先登录");
                }
                break;
            case R.id.dialog_zbgd_qx:
                dialog.dismiss();
                break;
            case R.id.dialog_zbgd_fanhui:
                finish();
                break;
            case R.id.back_top:
                finish();
                break;
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

    private RoundedImageView zhubo_head;
    private ImageView zhubo_v, zhubo_gengduo;
    private TextView zhubo_nickname, zhubo_blurb, concem_tv;
    private TextView zhubo_guanzhu_num, zhubo_fans_num, zhubo_zan_num;
    private TextView audioalbum_num, audio_num, videoalbum_num, video_num;
    private ListView audioalbum_viewp, audio_viewp, videoalbum_viewp, video_viewp;

    private void findview() {
        back_top = (ImageView) findViewById(R.id.back_top);
        back_top.setOnClickListener(this);
        concem = (RelativeLayout) findViewById(R.id.concem);
        concem.setOnClickListener(this);
        findViewById(R.id.zhubo_share).setOnClickListener(this);
        audioalbum_viewp = (ListView) findViewById(R.id.zhubo_audioalbum_viewp);
        audioalbum_viewp.setFocusable(false);
        audio_viewp = (ListView) findViewById(R.id.zhubo_audio_viewp);
        audio_viewp.setFocusable(false);
        videoalbum_viewp = (ListView) findViewById(R.id.zhubo_videoalbum_viewp);
        videoalbum_viewp.setFocusable(false);
        video_viewp = (ListView) findViewById(R.id.zhubo_video_viewp);
        video_viewp.setFocusable(false);

        concem_tv = (TextView) findViewById(R.id.concem_tv);
        audioalbum_num = (TextView) findViewById(R.id.zhubo_audioalbum_num);
        audio_num = (TextView) findViewById(R.id.zhubo_audio_num);
        videoalbum_num = (TextView) findViewById(R.id.zhubo_videoalbum_num);
        video_num = (TextView) findViewById(R.id.zhubo_video_num);

        zhubo_head = (RoundedImageView) findViewById(R.id.zhubo_head);
        zhubo_v = (ImageView) findViewById(R.id.zhubo_member);
        zhubo_gengduo = (ImageView) findViewById(R.id.zhubo_gengduo);
        zhubo_gengduo.setOnClickListener(this);
        zhubo_nickname = (TextView) findViewById(R.id.zhubo_nickname);
        zhubo_blurb = (TextView) findViewById(R.id.zhubo_blurb);
        zhubo_guanzhu_num = (TextView) findViewById(R.id.zhubo_guanzhu_num);
        zhubo_fans_num = (TextView) findViewById(R.id.zhubo_fans_num);
        zhubo_zan_num = (TextView) findViewById(R.id.zhubo_zan_num);

    }
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

                sendWx(1,"");
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.share_pyq).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWx(2,"");
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
        if (brStatus.equals("no")) {
            zbgd_hmd.setText("加入黑名单");
        } else {
            zbgd_hmd.setText("取消黑名单");
        }
        dialog.findViewById(R.id.dialog_zbgd_qx).setOnClickListener(this);

    }

    private void aodBlackRoster() {
        startProgressDialog();
        String strUrl = URLManager.aodBlackRoster;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", userid);
        map.put("bid", zhuboid);
        Futil.xutils(strUrl, map, handler, URLManager.three);
    }

    private void addConcem() {
        startProgressDialog();
        String strUrl = URLManager.addUserConcem;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", userid);
        map.put("concemId", zhuboid);
        Futil.xutils(strUrl, map, handler, URLManager.two);
    }

    private void delConcem() {
        startProgressDialog();
        String strUrl = URLManager.delUserConcem;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", userid);
        map.put("concemId", zhuboid);
        Futil.xutils(strUrl, map, handler, URLManager.two);
    }

    private void gointo() {
        startProgressDialog();
        String strUrl = URLManager.UserInfo;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user.id", zhuboid);
        map.put("loginId", userid);
        Futil.xutils(strUrl, map, handler, URLManager.one);

    }

    private void changeview(User u) {
        String head = u.getHead();
        String blurb = u.getBlurb();
        String nickname = u.getNickname();
        String fans = u.getFans();
        String guanzhu = u.getGuanzhu();
        String zan = u.getZan();
        String type = u.getHeadStatus();
        String members = u.getMembers();
        if (members.equals("2")) {
            zhubo_v.setVisibility(View.VISIBLE);
        } else {
            zhubo_v.setVisibility(View.GONE);
        }
        if (!type.equals("http")) {
            head = URLManager.Head_URL + head;
        }
        Picasso.with(context).load(head).resize(400, 400)
                .placeholder(R.mipmap.touxiang).error(R.mipmap.touxiang)
                .centerCrop().into(zhubo_head);
        if (blurb != null && !blurb.equals("") && !blurb.equals("null")) {
            zhubo_blurb.setText(blurb);
        } else {
            zhubo_blurb.setText("这家伙很懒，没有任何介绍");

        }
        if (nickname != null && !nickname.equals("")
                && !nickname.equals("null")) {
            zhubo_nickname.setText(nickname);
        }
        if (fans != null && !fans.equals("") && !fans.equals("null")) {
            zhubo_fans_num.setText(fans);
        }
        if (guanzhu != null && !guanzhu.equals("") && !guanzhu.equals("null")) {
            zhubo_guanzhu_num.setText(guanzhu);
        }
        if (zan != null && !zan.equals("") && !zan.equals("null")) {
            zhubo_zan_num.setText(zan);
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
