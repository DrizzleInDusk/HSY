package com.zm.hsy.util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.zm.hsy.activity.AddBBSTopicActivity;
import com.zm.hsy.activity.AddVActivity;
import com.zm.hsy.activity.AlarmActivity;
import com.zm.hsy.activity.BBSActivity;
import com.zm.hsy.activity.BBSCardActivity;
import com.zm.hsy.activity.BBSCommunityActivity;
import com.zm.hsy.activity.BBSFameHallActivity;
import com.zm.hsy.activity.BBSGuanliActivity;
import com.zm.hsy.activity.BangdanTitleListview;
import com.zm.hsy.activity.BangdingActivity;
import com.zm.hsy.activity.BangdingSjActivity;
import com.zm.hsy.activity.BangdingSjnextActivity;
import com.zm.hsy.activity.BangzhuActivity;
import com.zm.hsy.activity.BuildAublmActivity;
import com.zm.hsy.activity.BuildBBSActivity;
import com.zm.hsy.activity.BuildVariationActivity;
import com.zm.hsy.activity.CommunityPhotosActivity;
import com.zm.hsy.activity.ContactsActivity;
import com.zm.hsy.activity.DaShangActivity;
import com.zm.hsy.activity.DetailsActivity;
import com.zm.hsy.activity.DetailsPlayActivity;
import com.zm.hsy.activity.DsjiluActivity;
import com.zm.hsy.activity.EarningsActivity;
import com.zm.hsy.activity.FeedbackActivity;
import com.zm.hsy.activity.FindFriendActivity;
import com.zm.hsy.activity.ForgetPWActivity;
import com.zm.hsy.activity.GotoVariationActivity;
import com.zm.hsy.activity.GoumaijifenActivity;
import com.zm.hsy.activity.GuanYuActivity;
import com.zm.hsy.activity.ImageGridActivity;
import com.zm.hsy.activity.InformationActivity;
import com.zm.hsy.activity.JoinVariationActivity;
import com.zm.hsy.activity.LianxiActivity;
import com.zm.hsy.activity.LoginActivity;
import com.zm.hsy.activity.ManageActivity;
import com.zm.hsy.activity.MessageActivity;
import com.zm.hsy.activity.MessageAudioActivity;
import com.zm.hsy.activity.MoreAlbumActivity;
import com.zm.hsy.activity.MoreHotCommunityActivity;
import com.zm.hsy.activity.MoreHotTopicActivity;
import com.zm.hsy.activity.MoreMyCommunityActivity;
import com.zm.hsy.activity.MoreTjActivity;
import com.zm.hsy.activity.MoreZhuboActivity;
import com.zm.hsy.activity.MyAublmActivity;
import com.zm.hsy.activity.MyAudioActivity;
import com.zm.hsy.activity.MyAudioCompileActivity;
import com.zm.hsy.activity.MyConcemFansActivity;
import com.zm.hsy.activity.MyPHSActivity;
import com.zm.hsy.activity.MyPushMessageActivity;
import com.zm.hsy.activity.MytieActivity;
import com.zm.hsy.activity.PhotoActivity;
import com.zm.hsy.activity.PhotoalbumActivity;
import com.zm.hsy.activity.PublishRecordActivity;
import com.zm.hsy.activity.RadioStationActivity;
import com.zm.hsy.activity.RadioStationPlayerActivity;
import com.zm.hsy.activity.RecordActivity;
import com.zm.hsy.activity.RingActivity;
import com.zm.hsy.activity.RingF2ListActivity;
import com.zm.hsy.activity.SearchActivity;
import com.zm.hsy.activity.SettingActivity;
import com.zm.hsy.activity.SettingCleanActivity;
import com.zm.hsy.activity.ShezhizfbActivity;
import com.zm.hsy.activity.SoundtrackActivity;
import com.zm.hsy.activity.TabVF2Activity;
import com.zm.hsy.activity.TabVF3Activity;
import com.zm.hsy.activity.TaskActivity;
import com.zm.hsy.activity.Top50Activity;
import com.zm.hsy.activity.TuisongActivity;
import com.zm.hsy.activity.VarbmrsActivity;
import com.zm.hsy.activity.VariationActivity;
import com.zm.hsy.activity.VariationMesActivity;
import com.zm.hsy.activity.VideoPlayingActivity;
import com.zm.hsy.activity.VideoTapeActivity;
import com.zm.hsy.activity.VideoviewActivity;
import com.zm.hsy.activity.XGAlbumActivity;
import com.zm.hsy.activity.XinxianshiActivity;
import com.zm.hsy.activity.YinpintitleListview;
import com.zm.hsy.activity.YinsiActivity;
import com.zm.hsy.activity.YinsiHmdActivity;
import com.zm.hsy.activity.YinsiTyActivity;
import com.zm.hsy.activity.YinsiXxsActivity;
import com.zm.hsy.activity.ZhuboActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 所有页面跳转总控制
 * 
 * @author bcc
 */
public class ActivityJumpControl {
	private static ActivityJumpControl mInstance = null;
	private Activity mOwner = null;
	private ArrayList<Activity> mArrayList;
	private static final String TAG="actname";

	private ActivityJumpControl(Activity owner) {
		mArrayList = new ArrayList<Activity>();
		mOwner = owner;
	}

	public static ActivityJumpControl getInstance(Activity owner) {
		if (mInstance == null) {
			mInstance = new ActivityJumpControl(owner);
		} else {
			mInstance.mOwner = owner;
		}
		return mInstance;
	}

	/**********************************************************************************
	 * 全局设置使用
	 * 
	 * ********************************************************************************/

	// 管理用户窗口和回退事件处理
	public void pushActivity(Activity activity) {
		mArrayList.add(activity);
	}

	public void popActivity(Activity activity) {

		mArrayList.remove(activity);
		activity = null;
	}

	public void popAllActivity() {
		while (mArrayList.size() > 0) {
			Activity ac = mArrayList.get(mArrayList.size() - 1);
			ac.finish();
			popActivity(ac);
		}
	}

	/** 登陆 */
	public void gotoLoginActivity() {
		Log.i(TAG, "gotoLoginActivity: ");
		Intent intent = new Intent(mOwner, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}

	/** Tag1忘记密码Tag2注册 */
	public void gotoForgetPWActivity(String Tag, String TopName) {
		Log.i(TAG, "gotoForgetPWActivity: ");
		Intent intent = new Intent(mOwner, ForgetPWActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("TopName", TopName);
		intent.putExtra("Tag", Tag);
		mOwner.startActivity(intent);
	}

	/** 个人资料 */
	public void gotoInformationActivity() {
		Log.i(TAG, "gotoInformationActivity: ");
		Intent intent = new Intent(mOwner, InformationActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}

	/** 搜索 */
	public void gotoSearchActivity() {
		Log.i(TAG, "gotoSearchActivity: ");
		Intent intent = new Intent(mOwner, SearchActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}

	/** 设置页 */
	public void gotoSettingActivity() {
		Log.i(TAG, "gotoSettingActivity: ");
		Intent intent = new Intent(mOwner, SettingActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}
	/** 闹铃 */
	public void gotoAlarmActivity() {
		Log.i(TAG, "gotoAlarmActivity: ");
		Intent intent = new Intent(mOwner, AlarmActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}
	/** 推送设置*/
	public void gotoTuisongActivity() {
		Log.i(TAG, "gotoTuisongActivity: ");
		Intent intent = new Intent(mOwner, TuisongActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}
	/** 隐私设置*/
	public void gotoYinsiActivity() {
		Log.i(TAG, "gotoYinsiActivity: ");
		Intent intent = new Intent(mOwner, YinsiActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}
	/** 隐私通用*/
	public void gotoYinsiTyActivity() {
		Log.i(TAG, "gotoYinsiTyActivity: ");
		Intent intent = new Intent(mOwner, YinsiTyActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}
	/** 隐私黑名单*/
	public void gotoYinsiHmdActivity() {
		Log.i(TAG, "gotoYinsiHmdActivity: ");
		Intent intent = new Intent(mOwner, YinsiHmdActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}
	/** 隐私新鲜事*/
	public void gotoYinsiXxsActivity() {
		Log.i(TAG, "gotoYinsiXxsActivity: ");
		Intent intent = new Intent(mOwner, YinsiXxsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}
	/** 帮助中心 */
	public void gotoBangzhuActivity() {
		Log.i(TAG, "gotoBangzhuActivity: ");
		Intent intent = new Intent(mOwner, BangzhuActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}
	/** 关于 */
	public void gotoGuanYuActivity() {
		Log.i(TAG, "gotoGuanYuActivity: ");
		Intent intent = new Intent(mOwner, GuanYuActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}
	/** 联系我们 */
	public void gotoLianxiActivity() {
		Log.i(TAG, "gotoLianxiActivity: ");
		Intent intent = new Intent(mOwner, LianxiActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}

	/**首页更多*/
	public void gotoMoreAlbumActivity(String rid, String itemname) {
		Log.i(TAG, "gotoMoreAlbumActivity: ");
		Intent intent = new Intent(mOwner, MoreAlbumActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("rid", rid);
		intent.putExtra("itemname", itemname);
		mOwner.startActivity(intent);
	}
	/**主播更多*/
	public void gotoMoreZhuboActivity(String belong,String name) {
		Log.i(TAG, "gotoMoreZhuboActivity: ");
		Intent intent = new Intent(mOwner, MoreZhuboActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("belong", belong);
		intent.putExtra("name", name);
		mOwner.startActivity(intent);
	}

	/** 清除缓存 */
	public void gotoSettingCleanActivity() {
		Log.i(TAG, "gotoSettingCleanActivity: ");
		Intent intent = new Intent(mOwner, SettingCleanActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}

	/** 录音 */
	public void gotoRecordActivity() {
		Log.i(TAG, "gotoRecordActivity: ");
		Intent intent = new Intent(mOwner, RecordActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}
	/** 选择本地音乐 */
	public void gotoSoundtrackActivity() {
		Log.i(TAG, "gotoSoundtrackActivity: ");
		Intent intent = new Intent(mOwner, SoundtrackActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}

	/** 录视频 */
	public void gotoVideoTapeActivity() {
		Log.i(TAG, "gotoVideoTapeActivity: ");
		Intent intent = new Intent(mOwner, VideoTapeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}

	/** 播放本地视频窗口 */
	public void gotoVideoviewActivity(String path, String addtime, String Tag) {
		Log.i(TAG, "gotoVideoviewActivity: ");
		Intent intent = new Intent(mOwner, VideoviewActivity.class);
		intent.putExtra("path", path);
		intent.putExtra("addtime", addtime);
		intent.putExtra("Tag", Tag);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}

	/**
	 * 我的录音c 0待发布1已发布 Tag 1录音3视频 toptv 我的声音 我的视频
	 */
	public void gotoMyAudioActivity(String c, String Tag, String toptv) {
		Log.i(TAG, "gotoMyAudioActivity: ");
		Intent intent = new Intent(mOwner, MyAudioActivity.class);
		intent.putExtra("c", c);
		intent.putExtra("Tag", Tag);
		intent.putExtra("toptv", toptv);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}

	/**
	 * 创建专辑
	 */
	public void gotoBuildAublmActivity(String Tag) {
		Log.i(TAG, "gotoBuildAublmActivity: ");
		Intent intent = new Intent(mOwner, BuildAublmActivity.class);
		intent.putExtra("Tag", Tag);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}

	/** 声音视频发布 1录音3视频 */
	public void gotoPublishRecordActivity(String key, String Tag) {
		Log.i(TAG, "gotoPublishRecordActivity: ");
		Intent intent = new Intent(mOwner, PublishRecordActivity.class);
		intent.putExtra("key", key);
		intent.putExtra("Tag", Tag);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}

	/** 编辑已发布声音 */
	public void gotoMyAudioCompileActivity(String audioid) {
		Log.i(TAG, "gotoMyAudioCompileActivity: ");
		Intent intent = new Intent(mOwner, MyAudioCompileActivity.class);
		intent.putExtra("audioid", audioid);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}

	/** 消息中心 */
	public void gotoMessageActivity() {
		Log.i(TAG, "gotoMessageActivity: ");
		Intent intent = new Intent(mOwner, MessageActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}
	/** 消息中心--1我的音频 2我的社区 */
	public void gotoMessageAudioActivity(String Tag) {
		Log.i(TAG, "gotoMessageAudioActivity: ");
		Intent intent = new Intent(mOwner, MessageAudioActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("Tag", Tag);
		mOwner.startActivity(intent);
	}
	
	/** 消息中心-查看某个消息 */
	public void gotoMyPushMessageActivity(String pft,String fuid,String nickname) {
		Log.i(TAG, "gotoMyPushMessageActivity: ");
		Intent intent = new Intent(mOwner, MyPushMessageActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("fuid", fuid);
		intent.putExtra("nickname", nickname);
		intent.putExtra("pft", pft);
		mOwner.startActivity(intent);
	}
	
	/** 管理中心 */
	public void gotoManageActivity() {
		Log.i(TAG, "gotoManageActivity: ");
		Intent intent = new Intent(mOwner, ManageActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}
	
	/** 管理中心-加V认证 */
	public void gotoAddVActivity() {
		Log.i(TAG, "gotoAddVActivity: ");
		Intent intent = new Intent(mOwner, AddVActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}

	/** 我的专辑 1音频2视频 */
	public void gotoMyAublmActivity(String Tag) {
		Log.i(TAG, "gotoMyAublmActivity: ");
		Intent intent = new Intent(mOwner, MyAublmActivity.class);
		intent.putExtra("Tag", Tag);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}
	
	/** 收益管理 */
	public void gotoEarningsActivity() {
		Log.i(TAG, "gotoEarningsActivity: ");
		Intent intent = new Intent(mOwner, EarningsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}
	/** 打赏记录 */
	public void gotoDsjiluActivity() {
		Log.i(TAG, "gotoDsjiluActivity: ");
		Intent intent = new Intent(mOwner, DsjiluActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}
	/** 绑定支付宝*/
	public void gotoShezhizfbActivity(String code,String payment,String name) {
		Log.i(TAG, "gotoShezhizfbActivity: ");
		Intent intent = new Intent(mOwner, ShezhizfbActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("code", code);
		intent.putExtra("payment", payment);
		intent.putExtra("name", name);
		mOwner.startActivity(intent);
	}

	/** 我的PHS--1.赞过 2.订阅 3.播放历史 */
	public void gotoMyPHSActivity(String PHS, String toptv) {
		Log.i(TAG, "gotoMyPHSActivity: ");
		Intent intent = new Intent(mOwner, MyPHSActivity.class);
		intent.putExtra("PHS", PHS);
		intent.putExtra("toptv", toptv);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}

	/** 我的CF-- 1.粉丝 2.关注 */
	public void gotoMyConcemFansActivity(String CF, String toptv) {
		Log.i(TAG, "gotoMyConcemFansActivity: ");
		Intent intent = new Intent(mOwner, MyConcemFansActivity.class);
		intent.putExtra("CF", CF);
		intent.putExtra("toptv", toptv);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}

	/** 意见反馈 */
	public void gotoFeedbackActivity() {
		Log.i(TAG, "gotoFeedbackActivity: ");
		Intent intent = new Intent(mOwner, FeedbackActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}

	/** 找朋友 */
	public void gotoFindFriendActivity() {
		Log.i(TAG, "gotoFindFriendActivity: ");
		Intent intent = new Intent(mOwner, FindFriendActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}

	/** 找朋友--获取手机通讯录 */
	public void gotoContactsActivity() {
		Log.i(TAG, "gotoContactsActivity: ");
		Intent intent = new Intent(mOwner, ContactsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}

	/** z账号绑定 */
	public void gotoBangdingActivity() {
		Log.i(TAG, "gotoBangdingActivity: ");
		Intent intent = new Intent(mOwner, BangdingActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}
	/** z账号绑定---手机 */
	public void gotoBangdingSjActivity() {
		Log.i(TAG, "gotoBangdingSjActivity: ");
		Intent intent = new Intent(mOwner, BangdingSjActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}
	/** z账号绑定---手机 */
	public void gotoBangdingSjnextActivity(String phonenum) {
		Log.i(TAG, "gotoBangdingSjnextActivity: ");
		Intent intent = new Intent(mOwner, BangdingSjnextActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("phonenum", phonenum);
		mOwner.startActivity(intent);
	}

	/** 订阅_新鲜事 */
	public void gotoXinxianshiActivity() {
		Log.i(TAG, "gotoXinxianshiActivity: ");
		Intent intent = new Intent(mOwner, XinxianshiActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}

	/*** 电台列表1本地2省市 3国家4网络*/
	public void gotoRadioStationActivity(String tag,String province) {
		Log.i(TAG, "gotoRadioStationActivity: ");
		Intent intent = new Intent(mOwner, RadioStationActivity.class);
		intent.putExtra("tag", tag);
		intent.putExtra("province", province);//省份
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/*** 电台播放页 */
	public void gotoRadioStationPlayerActivity(String mmsh, String stationname, String id) {
		Log.i(TAG, "gotoRadioStationPlayerActivity: ");
		Intent intent = new Intent(mOwner, RadioStationPlayerActivity.class);
		intent.putExtra("mmsh", mmsh);
		intent.putExtra("stationname", stationname);
		intent.putExtra("id", id);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/*** 任务页 */
	public void gotoTaskActivity() {
		Log.i(TAG, "gotoTaskActivity: ");
		Intent intent = new Intent(mOwner, TaskActivity.class);
		// intent.putExtra("id", id);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/*** 活动页 */
	public void gotoVariationActivity() {
		Log.i(TAG, "gotoVariationActivity: ");
		Intent intent = new Intent(mOwner, VariationActivity.class);
		// intent.putExtra("id", id);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/*** 创建活动页 */
	public void gotoBuildVariationActivity() {
		Log.i(TAG, "gotoBuildVariationActivity: ");
		Intent intent = new Intent(mOwner, BuildVariationActivity.class);
		// intent.putExtra("id", id);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/*** 参加活动页 */
	public void gotoJoinVariationActivity(String toptv, String variationid) {
		Log.i(TAG, "gotoJoinVariationActivity: ");
		Intent intent = new Intent(mOwner, JoinVariationActivity.class);
		intent.putExtra("variationid", variationid);
		intent.putExtra("toptv", toptv);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}
	/*** 参加活动页 */
	public void gotoGotoVariationActivity( String variationid) {
		Log.i(TAG, "gotoGotoVariationActivity: ");
		Intent intent = new Intent(mOwner, GotoVariationActivity.class);
		intent.putExtra("variationid", variationid);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/*** 铃声页 */
	public void gotoRingActivity() {
		Log.i(TAG, "gotoRingActivity: ");
		Intent intent = new Intent(mOwner, RingActivity.class);
		// intent.putExtra("id", id);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/*** 铃声页Ringf2列表页 */
	public void gotoRingF2ListActivity(String toptv) {
		Log.i(TAG, "gotoRingF2ListActivity: ");
		Intent intent = new Intent(mOwner, RingF2ListActivity.class);
		intent.putExtra("toptv", toptv);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/*** 社区首页 */
	public void gotoBBSActivity() {
		Log.i(TAG, "gotoBBSActivity: ");
		Intent intent = new Intent(mOwner, BBSActivity.class);
		// intent.putExtra("id", id);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}
	/*** 更多我的社区 */
	public void gotoMoreMyCommunityActivity() {
		Log.i(TAG, "gotoMoreMyCommunityActivity: ");
		Intent intent = new Intent(mOwner, MoreMyCommunityActivity.class);
		// intent.putExtra("id", id);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}
	/*** 更多热门社区 */
	public void gotoMoreHotCommunityActivity() {
		Log.i(TAG, "gotoMoreHotCommunityActivity: ");
		Intent intent = new Intent(mOwner, MoreHotCommunityActivity.class);
		// intent.putExtra("id", id);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}
	/*** 更多热门帖子 */
	public void gotoMoreHotTopicActivity() {
		Log.i(TAG, "gotoMoreHotTopicActivity: ");
		Intent intent = new Intent(mOwner, MoreHotTopicActivity.class);
		// intent.putExtra("id", id);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/** 社区页 */
	public void gotoBBSCommunityActivity(String id) {
		Log.i(TAG, "gotoBBSCommunityActivity: ");
		Intent intent = new Intent(mOwner, BBSCommunityActivity.class);
		intent.putExtra("id", id);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}
	/** 社区相册页 */
	public void gotoCommunityPhotosActivity(String communityid) {
		Log.i(TAG, "gotoCommunityPhotosActivity: ");
		Intent intent = new Intent(mOwner, CommunityPhotosActivity.class);
		intent.putExtra("communityid", communityid);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/** 社区帖子详情页 */
	public void gotoBBSCardActivity(String id) {
		Log.i(TAG, "gotoBBSCardActivity: ");
		Intent intent = new Intent(mOwner, BBSCardActivity.class);
		intent.putExtra("id", id);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/** 发帖 */
	public void gotoAddBBSTopicActivity(String id) {
		Log.i(TAG, "gotoAddBBSTopicActivity: ");
		Intent intent = new Intent(mOwner, AddBBSTopicActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("communityid", id);
		mOwner.startActivity(intent);

	}

	/** 名人堂 */
	public void gotoBBSFameHallActivity(String id) {
		Log.i(TAG, "gotoBBSFameHallActivity: ");
		Intent intent = new Intent(mOwner, BBSFameHallActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("communityid", id);
		mOwner.startActivity(intent);

	}

	/** 名人堂版主管理 */
	public void gotoBBSGuanliActivity(String id) {
		Log.i(TAG, "gotoBBSGuanliActivity: ");
		Intent intent = new Intent(mOwner, BBSGuanliActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("communityid", id);
		mOwner.startActivity(intent);

	}

	/** 创建社区 */
	public void gotoBuildBBSActivity() {
		Log.i(TAG, "gotoBuildBBSActivity: ");
		Intent intent = new Intent(mOwner, BuildBBSActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/** 选择系统相册 */
	public void gotoPhotoalbumActivity() {
		Log.i(TAG, "gotoPhotoalbumActivity: ");
		Intent intent = new Intent(mOwner, PhotoalbumActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/** 选择照片 */
	public void gotoImageGridActivity(Serializable imageList) {
		Log.i(TAG, "gotoImageGridActivity: ");
		Intent intent = new Intent(mOwner, ImageGridActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("imagelist", imageList);
		mOwner.startActivity(intent);

	}

	/** 修改照片 */
	public void gotoPhotoActivity(int id) {
		Log.i(TAG, "gotoPhotoActivity: ");
		Intent intent = new Intent(mOwner, PhotoActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("ID", id);
		mOwner.startActivity(intent);

	}

	/** 首页音频内容跳转页 */
	public void gotoTabVF2Activity(String Tag, String TopName) {
		Log.i(TAG, "gotoTabVF2Activity: ");
		Intent intent = new Intent(mOwner, TabVF2Activity.class);
		intent.putExtra("TopName", TopName);
		intent.putExtra("Tag", Tag);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/** 首页音频内容跳转页 */
	public void gotoTop50Activity(String Tag) {
		Log.i(TAG, "gotoTop50Activity: ");
		Intent intent = new Intent(mOwner, Top50Activity.class);
		intent.putExtra("albumTypeId", Tag);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/** 首页视频内容跳转页 */
	public void gotoTabVF3Activity(String Tag, String TopName) {
		Log.i(TAG, "gotoTabVF3Activity: ");
		Intent intent = new Intent(mOwner, TabVF3Activity.class);
		intent.putExtra("TopName", TopName);
		intent.putExtra("Tag", Tag);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/** 专辑播放详情页 */
	public void gotoDetailsPlayActivity(String audioid) {
		Log.i(TAG, "gotoDetailsPlayActivity: ");
		Intent intent = new Intent(mOwner, DetailsPlayActivity.class);
		intent.putExtra("id", audioid);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}
	/** 专辑播放详情页  更多推荐 */
	public void gotoMoreTjActivity(String audioid,String tag) {
		Log.i(TAG, "gotoMoreTjActivity: ");
		Intent intent = new Intent(mOwner, MoreTjActivity.class);
		intent.putExtra("id", audioid);
		intent.putExtra("tag", tag);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}
	/** 专辑播放详情页 相关专辑*/
	public void gotoXGAlbumActivity(String value) {
		Log.i(TAG, "gotoXGAlbumActivity: ");
		Intent intent = new Intent(mOwner, XGAlbumActivity.class);
		intent.putExtra("value", value);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/** 视频播放详情页 */
	public void gotoVideoPlayActivity(String videoid) {
		Log.i(TAG, "gotoVideoPlayActivity: ");
//		Intent intent = new Intent(mOwner, VideoPlayActivity.class);
		Intent intent = new Intent(mOwner, VideoPlayingActivity.class);
		intent.putExtra("videoid", videoid);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/** 专辑详情页 */
	public void gotoDetailsActivity(String audioAlbumId) {
		Log.i(TAG, "gotoDetailsActivity: ");
		Intent intent = new Intent(mOwner, DetailsActivity.class);
		intent.putExtra("audioAlbumId", audioAlbumId);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/** 标题栏+listview页 */
	public void gotoYinpintitleListview(String audioId, String tv, String Tag) {
		Log.i(TAG, "gotoYinpintitleListview: ");
		Intent intent = new Intent(mOwner, YinpintitleListview.class);
		intent.putExtra("audioId", audioId);
		intent.putExtra("toptv", tv);
		intent.putExtra("Tag", Tag);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}
	/** 打赏页 */
	public void gotoDaShangActivity(String audioid,String zhuboid) {
		Log.i(TAG, "gotoDaShangActivity: ");
		Intent intent = new Intent(mOwner, DaShangActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("audioid", audioid);
		intent.putExtra("zhuboid", zhuboid);
		mOwner.startActivity(intent);
	}

	/** 榜单详情 标题栏+listview页 tv 标签名字 Tag audio user video */
	public void gotoBangdanTitleListview(String rankType, String toptv,
			String Tag) {
		Log.i(TAG, "gotoBangdanTitleListview: ");
		Intent intent = new Intent(mOwner, BangdanTitleListview.class);
		intent.putExtra("toptv", toptv);
		intent.putExtra("Tag", Tag);
		intent.putExtra("rankType", rankType);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/** 主播页 */
	public void gotoZhuboActivity(String zhuboid) {
		Log.i(TAG, "gotoZhuboActivity: ");
		Intent intent = new Intent(mOwner, ZhuboActivity.class);
		intent.putExtra("zhuboid", zhuboid);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}
	/** 购买积分 */
	public void gotoGoumaijifenActivity() {
		Log.i(TAG, "gotoGoumaijifenActivity: ");
		Intent intent = new Intent(mOwner, GoumaijifenActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);

	}

	/** 社区发帖信息 */
	public void gotoMytieActivity(String title,String type) {
		Log.i(TAG, "gotoMytieActivity: ");
		Intent intent = new Intent(mOwner, MytieActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("title", title);
		intent.putExtra("type", type);
		mOwner.startActivity(intent);

	}

	/*** 活动详细页 */
	public void gotoVariationMesActivity( String variationid) {
		Log.i(TAG, "gotoVariationMesActivity: ");
		Intent intent = new Intent(mOwner, VariationMesActivity.class);
		intent.putExtra("variationid", variationid);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}
	/*** 活动报名人数 */
	public void gotoVarbmrsActivity( String variationid) {
		Log.i(TAG, "gotoVarbmrsActivity: ");
		Intent intent = new Intent(mOwner, VarbmrsActivity.class);
		intent.putExtra("variationid", variationid);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mOwner.startActivity(intent);
	}
}
