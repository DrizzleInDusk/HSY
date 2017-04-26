package com.zm.hsy.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.pay.H5PayDemoActivity;
import com.zm.hsy.pay.PayResult;
import com.zm.hsy.pay.SignUtils;
import com.zm.hsy.util.BeanTu;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import cn.jpush.android.api.JPushInterface;
/** 打赏页 */
public class DaShangActivity extends Activity implements OnClickListener {
	private String userid, audioid,zhuboid;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashang);
		context = this;
		Intent i = this.getIntent();
		audioid = i.getStringExtra("audioid");
		zhuboid = i.getStringExtra("zhuboid");
		userid = Futil.getValue(context, "userid");
		findview();
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

	private String RMB = "0";

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.back_top:
			finish();
			break;
		case R.id.rmbzdy:
			openrmbDialog();
			break;
		case R.id.rmb2:
			RMB = "2";
			openDialog();
			break;
		case R.id.rmb3:
			RMB = "3";
			openDialog();
			break;
		case R.id.rmb5:
			RMB = "5";
			openDialog();
			break;
		case R.id.rmb10:
			RMB = "10";
			openDialog();
			break;
		case R.id.rmb50:
			RMB = "50";
			openDialog();
			break;

		}
	}

	private ImageView back_top;
	private TextView rmbzdy, rmb2, rmb3, rmb5, rmb10, rmb50;

	private void findview() {

		rmbzdy = (TextView) findViewById(R.id.rmbzdy);
		rmbzdy.setOnClickListener(this);

		rmb2 = (TextView) findViewById(R.id.rmb2);
		rmb2.setOnClickListener(this);

		rmb3 = (TextView) findViewById(R.id.rmb3);
		rmb3.setOnClickListener(this);

		rmb5 = (TextView) findViewById(R.id.rmb5);
		rmb5.setOnClickListener(this);

		rmb10 = (TextView) findViewById(R.id.rmb10);
		rmb10.setOnClickListener(this);

		rmb50 = (TextView) findViewById(R.id.rmb50);
		rmb50.setOnClickListener(this);

		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

	}

	private AlertDialog dialog;
	private int TAG = 0;
	private RelativeLayout pay_wx, pay_zfb;

	private void openDialog() {
		dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		dialog.setContentView(R.layout.dialog_pay);
		ImageView dialog_pay_dim = (ImageView) dialog
				.findViewById(R.id.dialog_pay_dim);// 关闭
		pay_wx = (RelativeLayout) dialog.findViewById(R.id.pay_wx_rl);// 微信支付
		pay_zfb = (RelativeLayout) dialog.findViewById(R.id.pay_zfb_rl);// 支付宝支付
		ImageView pay_submit = (ImageView) dialog.findViewById(R.id.pay_submit);// 确定

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		dialogWindow.setAttributes(lp);

		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.4
		p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65
		dialogWindow.setAttributes(p);
		dialog_pay_dim.setOnClickListener(new OnClickListener() {// 关闭

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		pay_wx.setOnClickListener(new OnClickListener() {// 微信支付

			@Override
			public void onClick(View v) {
				change(pay_wx);
				TAG = 1;
			}

		});
		pay_zfb.setOnClickListener(new OnClickListener() {// 支付宝支付

			@Override
			public void onClick(View v) {
				change(pay_zfb);
				TAG = 2;
			}

		});

		pay_submit.setOnClickListener(new OnClickListener() {// 确定

					@Override
					public void onClick(View v) {
						if (TAG == 1) {
							int r=Integer.parseInt(RMB);
							RMB=""+(r*100);
							goHTWX();
							dialog.dismiss();
						} else if (TAG == 2) {
							alipaypay();
							dialog.dismiss();
						} else {
							Futil.showMessage(context, "请选择支付方式");
						}
						TAG=0;
					}
				});

	}
	public void goHTWX() {
		 
		String url = URLManager.createWXOrder;
//		String url = URLManager.createWXOrder+"?fee="+RMB+"&describe=pay"+"&attach=audio-"+ userid + "," + zhuboid + "," + audioid + ",weixin";
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("fee", RMB);
		map.put("describe", "打赏主播");
		map.put("attach","audio-" + userid + "," + zhuboid + "," + audioid + ",weixin");
		Futil.xutils(url, map, handler, URLManager.one);
	}
	private String appid;
	private String noncestr;
	private String mypackage;
	private String partnerid;
	private String payMoney;
	private String paySign;
	private String prepayid;
	private String timestamp;
	private IWXAPI api;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == URLManager.one) {
				JSONObject obj = (JSONObject) msg.obj;
				try {
					appid = obj.getString("appid");
					noncestr = obj.getString("noncestr");
					mypackage = obj.getString("package");
					partnerid = obj.getString("partnerid");
					payMoney = obj.getString("payMoney");
					paySign = obj.getString("paySign");
					prepayid = obj.getString("prepayid");
					timestamp = obj.getString("timestamp");
					goWXPlay();
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}

	};
	private void goWXPlay() {
		api = WXAPIFactory.createWXAPI(this, URLManager.WXappId);
		api.registerApp (appid);
		PayReq req = new PayReq();
		req.appId = appid;
		req.partnerId = partnerid;
		req.prepayId = prepayid;
		req.nonceStr = noncestr;
		req.timeStamp = timestamp;
		req.packageValue = mypackage;
		req.sign = paySign;
		req.extData = "app data"; // optional
		// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
		api.sendReq(req);
	}
	ArrayList<RelativeLayout> list = new ArrayList<RelativeLayout>();

	private void change(RelativeLayout rl) {
		for (int i = 0; i < list.size(); i++) {
			list.remove(i).setSelected(false);
		}
		rl.setSelected(true);
		list.add(rl);
	}

	private AlertDialog rmbdialog;

	private void openrmbDialog() {
		rmbdialog = new AlertDialog.Builder(context).create();
		View v = LayoutInflater.from(this).inflate(R.layout.dialog_rmbzdy,
				null, false);
		rmbdialog.setView(v);
		rmbdialog.show();
		ImageView rmbzdy_dim = (ImageView) rmbdialog
				.findViewById(R.id.dialog_rmbzdy_dim);// 关闭

		final EditText rmbzdy_et = (EditText) rmbdialog
				.findViewById(R.id.dialog_rmbzdy_et);//

		ImageView rmbzdy_submit = (ImageView) rmbdialog
				.findViewById(R.id.dialog_rmbzdy_submit);// 确定

		Window dialogWindow = rmbdialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		dialogWindow.setAttributes(lp);

		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		// BeanTu.dip2px(context, 220);
		p.height = BeanTu.dip2px(context, 250); // 高度设置为屏幕的0.4
		// p.height = (int) (d.getHeight()*0.4); // 高度设置为屏幕的0.4
		p.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.65
		dialogWindow.setAttributes(p);
		rmbzdy_dim.setOnClickListener(new OnClickListener() {// 关闭

					@Override
					public void onClick(View v) {
						RMB = "";
						rmbdialog.dismiss();
					}
				});

		rmbzdy_submit.setOnClickListener(new OnClickListener() {// 确定

					@Override
					public void onClick(View v) {
						String rmbzdy = rmbzdy_et.getText().toString().trim();
						if (rmbzdy != null && !rmbzdy.equals("0")
								&& !rmbzdy.equals("")) {
							int r = Integer.valueOf(rmbzdy).intValue();
							if (r > 0 && r < 201) {
								RMB = rmbzdy;
								openDialog();
								rmbdialog.dismiss();
							} else {
								Futil.showMessage(context, "请输入1-200的整数");
							}
						} else {
							Futil.showMessage(context, "请输入1-200的整数");
						}
					}
				});

	}

	// 商户PID
	public static final String PARTNER = "2088421194392281";
	// 商户收款账号
	public static final String SELLER = "18072366998@189.cn";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBANu1ePQxG3mWdGs/sD/VYzde5Nz1AaMyb/8J3imy0Q4EqekGZq1yGCiePy7jW3/Lme0o2Oeqx766cLrO/YOMs9+wXQvgAewC5RLWtNKVUEuekWAB3vjaDoqJygLn1u0XuBfzdSWZIPgW5EKZ3CM9AG8VjEHOODjmiuJudfd0r2sJAgMBAAECgYB9GYNZZ2zBeo2nU4hDkHkB2iJuPYNFLT7f9Ppuu8aVOMqWSHLGZ6Sh3BkV1ZcP2Ro4E34NywwY6YeulpV7Be6UbXpmvf6++y/iuKIwt91Fre1rO187IbH70dO6WInE2gkJGWa71G5zL4KsRU+Yheh7QdyKiNKP8j5XWAnoNkO4QQJBAPAGKKF02bTdgGxMQ+tOsNxRCWq6wnG3kojM9H+Yxq3b5gseZoBsyTOoNW5QlwQ9m4e/tT3QbVo0ShLqjVhJmE0CQQDqVShIsbLu1+u0xKQb7c8KPG8B+ySljbsx+W7OhAiWDh3b06af5DxLAT8H9GNvAXCAi9b5vsBYu5+H6Jp9IPutAkB2wBVKDC3Jtezbp7/So1QNLGqkS1H4QQJWfFBa8JMbYTgnOBsuXqQM0qPcuDJ+/pv8RXS96GixwD0FrtXPYFnhAkBgoxvI1teW3h7LXqjbB7hxXTjeVUboq0l+s4H50sODtnCj3mYB3Grs96eZzTRQbASMTp8qQXsrXLzOnKNRZQ/FAkBWU0K9+kIVqqs2FkIt/hMBDZYBBzgFa4nIMs3VcAyksC4KbCRw99Qa0TgCn3BqB5/zW79NymzLoo+XQI3yqOF7";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	private static final int SDK_PAY_FLAG = 1;
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				System.out.println("--payResult-" + payResult);
				/**
				 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
				 * detail
				 * .htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&docType=1)
				 * 建议商户依赖异步通知
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息
				System.out.println("--resultInfo-" + resultInfo);

				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Futil.showMessage(context, "打赏成功谢谢您的支持！");
					RMB = "";
				} else {
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Futil.showMessage(context, "支付结果确认中");

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Futil.showMessage(context, "已取消本次打赏....");
					}
				}
				break;
			}
			default:
				break;
			}
		};
	};

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void alipaypay() {
		if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
				|| TextUtils.isEmpty(SELLER)) {
			new AlertDialog.Builder(this)
					.setTitle("警告")
					.setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									//
									finish();
								}
							}).show();
			return;
		}
		// subject 用户userid, body 音频audioid
		String orderInfo = getOrderInfo("打赏主播", "audio-" + userid + "," + zhuboid + "," + audioid + ",zhifubao", RMB);
//		String orderInfo = getOrderInfo("打赏主播", "audio-" + userid + "," + zhuboid + "," + audioid + ",zhifubao", "0.01");

		/**
		 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
		 */
		String sign = sign(orderInfo);
		try {
			/**
			 * 仅需对sign 做URL编码
			 */
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		/**
		 * 完整的符合支付宝参数规范的订单信息
		 */
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();
		Log.w("payInfo>>>",payInfo);
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(DaShangActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
	 * 
	 * @param v
	 */
	public void h5Pay(View v) {
		Intent intent = new Intent(this, H5PayDemoActivity.class);
		Bundle extras = new Bundle();
		/**
		 * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
		 * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
		 * 商户可以根据自己的需求来实现
		 */
		String url = "http://m.taobao.com";
		// url可以是一号店或者淘宝等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
		extras.putString("url", url);
		intent.putExtras(extras);
		startActivity(intent);
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	private String getOrderInfo(String subject, String body, String price) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + URLManager.notify_url + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	private String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
}
