package com.zm.hsy.https;

public class URLManager {
	public static final String WXappId = "wxecd004c32ff3216e";
	public static final String WXghid = "gh_0b4756d225d4";
	public static final String WXappSecret = "da18bec9a9789837b6ab302582805d18";
	public static final String QQappId = "1105492744";
	public static final String QQappKey = "jSzuFSn6N8I90GyA";
	public static final String ip = "35.25.216.124";
	public static final String notify_url = "http://180.153.63.242:8080/hsyfm/notify_url.jsp";
	public static final String share = "http://www.baidu.com";

	public static final int one = 1;
	public static final int two = 2;
	public static final int three = 3;
	public static final int four = 4;
	public static final int five = 5;
	public static final int six = 6;
	public static final int seven = 7;

	public static final String APP_SERVER_URL = "http://www.wisdomfm.cn/hsyfm";
//	public static final String APP_SERVER_URL = "http://180.153.63.242:8080/hsyfm";
//	public static final String APP_SERVER_URL = "http://192.168.125.125:8080/hsyfm";
//	public static final String APP_SERVER_URL = "http://192.168.1.79:8080/hsyfm";
	/**广告图路径*/
	public static final String Ads_URL = APP_SERVER_URL + "/fmAds/";
	/**封面图路径*/
	public static final String COVER_URL = APP_SERVER_URL + "/fmCover/";
	/**论坛图路径*/
	public static final String TopicCOVER_URL = APP_SERVER_URL + "/fmTopic/";
	/**活动封面图路径*/
	public static final String VariationCOVER_URL = APP_SERVER_URL + "/fmActivity/";
	/**头像路径*/
	public static final String Head_URL = APP_SERVER_URL + "/fmHead/";
	/**推送图片路径*/
	public static final String Push_URL = APP_SERVER_URL + "/fmPush/";
	/** 声音路径*/
	public static final String AUDIO_URL = APP_SERVER_URL + "/fmAudio/";
	/** 视频路径*/
	public static final String Video_URL = APP_SERVER_URL + "/fmVideo/";

	/**.背景音乐路径*/
	public static final String fmSound = APP_SERVER_URL + "/fmSound/";

	/** 0.1.登陆*/
	public static final String Login = APP_SERVER_URL + "/appLogin";
	/** 0.1.2.第三方登陆*/
	public static final String ThirdLogin = APP_SERVER_URL + "/appThirdLogin";
	
	/** 0.2.注册-user.mobile+user.password+yzCode必须有
	 * user.head+user.blurb可空
	 * user.belong=1  先写死*/
	public static final String Register = APP_SERVER_URL + "/appRegister";
	/** 0.4.注册获取验证码*/
	public static final String SendRCode = APP_SERVER_URL + "/appSendRCode";
	/** 0.3.忘记密码*/
	public static final String PasswordRecovery = APP_SERVER_URL + "/appPasswordRecovery";
	/** 0.4.忘记密码获取验证码*/
	public static final String SendFCode = APP_SERVER_URL + "/appSendFCode";
	
	
	/** 1.1.首页推荐页面*/
	public static final String InitIndex = APP_SERVER_URL + "/appInitIndex";
	/** 1.1.首页推荐页面--更多*/
	public static final String IndexMore = APP_SERVER_URL + "/appIndexMoreList";
	/** 1.1.首页主播--更多*/
	public static final String UserByBelong = APP_SERVER_URL + "/appGetUserByBelong";
	/** 1.1.1搜索页面--热词关键字*/
	public static final String KeywordList = APP_SERVER_URL + "/getKeywordList";
	/** 1.1.2搜索页面--搜索*/
	public static final String SearchResultList = APP_SERVER_URL + "/getSearchResultList";
	/** 1.1.3活动列表*/
	public static final String ActivityList = APP_SERVER_URL + "/activityList";
	/** 1.1.4参加活动*/
	public static final String ActivitySign = APP_SERVER_URL + "/addActivitySign";
	/** 1.1.4.1进入活动报名页*/
	public static final String gotoActivitySign = APP_SERVER_URL + "/gotoActivitySign";
	/** 1.1.4.2活动的详情*/
	public static final String getActivityInfo = APP_SERVER_URL + "/getActivityInfo";
	/** 1.1.5发布活动*/
	public static final String addActivity = APP_SERVER_URL + "/addActivity";
	/** 1.2.获取广告图片*/
	public static final String adsList = APP_SERVER_URL + "/adsList";
	
	/**1.2音频 全部页面*/
	public static final String QuanBu = APP_SERVER_URL + "/appGetYinPinQuanBu";


	/**1.2音频 推荐界面*/
	public static final String TuiJian = APP_SERVER_URL
			+ "/appGetYinPinTuiJian";
	/**1.2音频 推荐界面Top50*/
	public static final String Top50 = APP_SERVER_URL
			+ "/appGetAlbumDayTop50";
	/**1.2音频 多条件查询*/
	public static final String Condition = APP_SERVER_URL
			+ "/appGetAlbumByManyCondition";
	/**1.2.音频 通过id界面跳转*/
	public static final String AlbumByWidth = APP_SERVER_URL
			+ "/appGetAlbumByWidth";
	/**1.2.1音频 专辑*/
	public static final String AudioListByAlbumId = APP_SERVER_URL
			+ "/appGetAudioListByAlbumId";
	/**1.2.1后台请求微信*/
	public static final String createWXOrder = APP_SERVER_URL
			+ "/createWXOrder";
	/**1.2.2音频 获取*/
	public static final String GetAudio = APP_SERVER_URL + "/appGetAudio";
	/**1.2.3音频 发布评论*/
	public static final String Comment = APP_SERVER_URL + "/appAddAudioComment";
	/**1.2.4音频 查看评论*/
	public static final String GetComment = APP_SERVER_URL
			+ "/appGetAllAudioComment";
	/**1.2.5音频 订阅*/
	public static final String AddSubscribe = APP_SERVER_URL
			+ "/appAddSubscribe";
	/**1.2.5音频 取消订阅*/
	public static final String DelSubscribe = APP_SERVER_URL
			+ "/appDelSubscribe";
	/**1.2.6音频 点赞/取消点赞*/
	public static final String ClickPraise = APP_SERVER_URL
			+ "/appClickPraise";
	/**1.2.6 电台增加播放量*/
	public static final String upRadioHits = APP_SERVER_URL
			+ "/upRadioHits";

	/**1.2点击更多相关专辑*/
	public static final String MoreTJ = APP_SERVER_URL + "/appGetAudioMoreTJ";
	/**1.2点击更多相关专辑*/
	public static final String MoreTJ2 = APP_SERVER_URL + "/appGetAlbumMoreTJ";
	/**1.2点击更多相关专辑*/
	public static final String AlbumByKeyWord = APP_SERVER_URL + "/appAudioAlbumByKeyWord";


	/**1.3.榜单*/
	public static final String RankList = APP_SERVER_URL + "/appGetRankList";
	/**1.3.1榜单 100条*/
	public static final String ManyRankList = APP_SERVER_URL
			+ "/appGetManyRankList";

	/**1.4.主播表单页*/
	public static final String AnnouncerPage = APP_SERVER_URL
			+ "/appGetAnnouncerPage";
	/**1.4.主播信息页*/
	public static final String UserInfo = APP_SERVER_URL + "/appGetUserInfo?userType=1";
	
	/**1.4.关注*/
	public static final String addUserConcem = APP_SERVER_URL + "/addUserConcem";
	/**1.4.取消关注*/
	public static final String delUserConcem = APP_SERVER_URL + "/delUserConcem";
	
	/**1.5.视频信息页*/
	public static final String GetVideoIndex = APP_SERVER_URL + "/appGetVideoIndex";
	/**1.5.视频播放页*/
	public static final String GetVideoInfo = APP_SERVER_URL + "/appGetVideoInfo";
	/**1.6.广播页面排行榜*/
	public static final String radioRankList = APP_SERVER_URL + "/radioRankList";
	/**1.6.广播推荐*/
	public static final String radioRecommendList = APP_SERVER_URL + "/radioRecommendList";
	/**1.6.本地电台*/
	public static final String radioListByProvince = APP_SERVER_URL + "/radioListByProvince";
	/**1.6.省市电台*/
	public static final String defaultPacList = APP_SERVER_URL + "/defaultPacList";

	/**1.1.1.社区页*/
	public static final String CommunityIndex = APP_SERVER_URL
			+ "/appGetCommunityIndex";
	/**1.1.1.1我的社区*/
	public static final String GetMoreCommunity = APP_SERVER_URL
			+ "/appGetMoreCommunity";
	/**1.1.1.2热门社区页*/
	public static final String GetMoreHotCommunity = APP_SERVER_URL
			+ "/appGetMoreHotCommunity";
	/**1.1.1.3热门帖子*/
	public static final String GetMoreHotCommunityTopic = APP_SERVER_URL
			+ "/appGetMoreHotCommunityTopic";
	/**1.1.2.社区详情页*/
	public static final String CommunityInfo = APP_SERVER_URL
			+ "/appGetCommunityInfo";
	/**1.1.2.社区相册*/
	public static final String PhotoByCommunity = APP_SERVER_URL
			+ "/getPhotoByCommunity";
	/**1.1.3.社区评论页-判断*/
	public static final String TopicListByTopicId = APP_SERVER_URL
			+ "/appGetTopicListByTopicId";
	/**1.1.4.社区评论页-扣积分*/
	public static final String TopicListByTopicId2 = APP_SERVER_URL
			+ "/appGetTopicListByTopicId2";
	/**1.1.5.社区签到*/
	public static final String Sign = APP_SERVER_URL + "/appUpSign";
	/**1.1.6.加入社区*/
	public static final String Join = APP_SERVER_URL
			+ "/appAddAssocUserCommunity";
	/**1.1.7.创建社区*/
	public static final String BuildBBS = APP_SERVER_URL + "/appAddCommunity";
	/**1.1.8.社区发帖*/
	public static final String BuildTopic = APP_SERVER_URL
			+ "/appAddCommunityTopic";
	/**1.1.9.社区发帖-获取score*/
	public static final String GetScore = APP_SERVER_URL
			+ "/getAllCommunityTopicScore";
	/**1.1.10.社区名人堂*/
	public static final String HallOfFame = APP_SERVER_URL + "/appHallOfFame";
	/**1.1.11.社区名人堂版主提升管理*/
	public static final String UpAdmin = APP_SERVER_URL + "/appUpAdmin";
	/**1.1.12.社区名人堂版主取消管理*/
	public static final String DownAdmin = APP_SERVER_URL + "/appDownAdmin";
	/**1.1.13.社区回贴*/
	public static final String AddCommunityTopic = APP_SERVER_URL
			+ "/appAddCommunityTopic";
	/**1.1.14.社区贴子置顶*/
	public static final String TopicTop = APP_SERVER_URL + "/appAddTopicTop";

	/**2.1.订阅 新鲜事*/
	public static final String GetFreshThings = APP_SERVER_URL + "/appGetFreshThings";
	
	/**4.1.我的*/
	public static final String Myown = APP_SERVER_URL + "/appGetUserInfo?userType=2&loginId=0";
	/**4.1.推送设置
	 * 0所有 1声音评论 2社区评论。3关闭所有
	 * */
	public static final String pushSet = APP_SERVER_URL + "/pushSet";
	/**4.1.通用设置
	 * 0所有，1我关注的，2关注我的，3认证主播，4关闭评论。
	 * */
	public static final String currencySet = APP_SERVER_URL + "/currencySet";
	/**4.1.新鲜事设置
	 * 1订阅专辑,2赞声音，3关注
	 * */
	public static final String freshSet = APP_SERVER_URL + "/freshSet";
	/**4.1.添加/取消黑名单
	 * */
	public static final String aodBlackRoster = APP_SERVER_URL + "/aodBlackRoster";
	/**4.1.黑名单
	 * */
	public static final String blackRosterList = APP_SERVER_URL + "/blackRosterList";
	/**4.1.获取用户设置
	 * 1返回推送设置,2返回通用设置,3返回新鲜事设置
	 * */
	public static final String appUserSet = APP_SERVER_URL + "/appUserSet";
	/**4.1.1更新用户信息*/
	public static final String UpUser = APP_SERVER_URL + "/appUpUser";
	/**4.1.2声音发布*/
	public static final String AddAudio = APP_SERVER_URL + "/appAddAudio";
	/**4.1.3视频发布*/
	public static final String AddVideo = APP_SERVER_URL + "/appAddVideo";
	/**4.1.4获取音频专辑分类*/
	public static final String GetAudioAlbumType = APP_SERVER_URL + "/appGetAudioAlbumType";
	/**4.1.4获取视频专辑分类*/
	public static final String GetVideoAlbumType = APP_SERVER_URL + "/appGetVideoAlbumType";
	/**4.1.5.1创建音频专辑*/
	public static final String AddAudioAlbum = APP_SERVER_URL + "/appAddAudioAlbum";
	/**4.1.5.2创建视频专辑*/
	public static final String AddVideoAlbum = APP_SERVER_URL + "/appAddVideoAlbum";
	
	/**4.2.我的声音*/
	public static final String GetMyAudio = APP_SERVER_URL + "/appGetMyAudio";
	/**4.2.背景音乐列表*/
	public static final String SoundtrackList = APP_SERVER_URL + "/appGetSoundtrackByName";

	
	/**4.2.1.我的声音--编辑：*/
	public static final String EditAudio = APP_SERVER_URL + "/appEditAudio";
	/**4.2.2.我的声音--编辑--修改*/
	public static final String UpAudio = APP_SERVER_URL + "/appUpAudio";
	/**4.2.3.我的管理-音频专辑*/
	public static final String MyAudioAlbum = APP_SERVER_URL + "/appMyAudioAlbum";
	/**4.2.3.我的管理-视频专辑*/
	public static final String MyVideoAlbum = APP_SERVER_URL + "/appMyVideoAlbum";
	/**4.2.3.我的管理-   +V认证*/
	public static final String jiaV = APP_SERVER_URL + "/jiaVAuthenticationStatus";
	/**4.2.3.我的管理- 认证条件是否满足*/
	public static final String jiaVAuthentication = APP_SERVER_URL + "/jiaVAuthentication";
	/**4.2.3.我的管理-  申请+V*/
	public static final String jiaVCertification = APP_SERVER_URL + "/jiaVCertification";
	/**4.2.3.我的管理-打赏*/
	public static final String profit = APP_SERVER_URL + "/profit";
	/**4.2.3.我的管理-打赏记录*/
	public static final String PayListByUid = APP_SERVER_URL + "/appGetPayListByUid";
	/**4.2.3.我的管理-收益管理里点击账号绑定*/
	public static final String UserAccountInfo = APP_SERVER_URL + "/appGetUserAccountInfo";
	
	/**4.3.我的视频*/
	public static final String GetMyVideo = APP_SERVER_URL + "/appGetMyVideo";
	
	/**4.4.我的关注*/
	public static final String MyConcem = APP_SERVER_URL + "/appMyConcem";
	/**4.5.我的粉丝*/
	public static final String MyFans = APP_SERVER_URL + "/appMyFans";
	/**4.5.给自己的粉丝推送消息*/
	public static final String pushMessageToFan = APP_SERVER_URL + "/pushMessageToFan";
	/**4.6.我赞过的*/
	public static final String MyPraise = APP_SERVER_URL + "/appMyPraise";
	/**4.7我的订阅*/
	public static final String MySubscribe = APP_SERVER_URL + "/appMySubscribe";
	/**2.1订阅_推荐*/
	public static final String SubscribeRecommend = APP_SERVER_URL + "/appSubscribeRecommend";
	/**4.8找朋友*/
	public static final String FindFriend = APP_SERVER_URL + "/findAddressBookFriend";
	/**4.8消息管理-获取所有*/
	public static final String getPushMessageByUser = APP_SERVER_URL + "/getPushMessageByUser";
	/**4.8消息管理-获取某个*/
	public static final String upPushMessageByRead = APP_SERVER_URL + "/upPushMessageByRead";
	/**4.8消息管理-删除*/
	public static final String upPushMessageByDel = APP_SERVER_URL + "/upPushMessageByDel";
	/**4.9意见反馈：*/
	public static final String addOpinion = APP_SERVER_URL + "/addOpinion";
	/**4.10账号绑定：*/
	public static final String bindings = APP_SERVER_URL + "/bindings";
	/**删除音频和音频相关数据：*/
	public static final String delAudio = APP_SERVER_URL + "/appDelAudio";
	/**创建过的社区：*/
	public static final String getMyCommunity = APP_SERVER_URL + "/appGetMyCreatCommunity";
	/**发布过的活动：*/
	public static final String getMyVariation = APP_SERVER_URL + "/appGetMyCreatActivity";
	/**社区发帖信息：*/
	public static final String getMyTopic = APP_SERVER_URL + "/appGetMyCommunityTopic";
	/**删除帖子信息：*/
	public static final String delMyTopic = APP_SERVER_URL + "/appDelMyCommunityTopic";
	/**查看活动报名人员列表：*/
	public static final String getMyVarSignList = APP_SERVER_URL + "/appGetActivitySignList";

}
