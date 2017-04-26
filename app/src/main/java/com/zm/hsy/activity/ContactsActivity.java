package com.zm.hsy.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.zm.hsy.R;
import com.zm.hsy.entity.PhoneContacts;
import com.zm.hsy.https.Futil;
import com.zm.hsy.tools.pinyinlist.FancyIndexer;
import com.zm.hsy.tools.pinyinlist.FancyIndexer.OnTouchLetterChangedListener;
import com.zm.hsy.tools.pinyinlist.PingyinAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
/** 找朋友--获取手机通讯录 */
public class ContactsActivity extends Activity implements OnClickListener {

	private ExpandableListView lv_content;
	private PingyinAdapter<PhoneContacts> adapter;
	private ArrayList<PhoneContacts> plist = new ArrayList<PhoneContacts>();
	private List<String> phoneList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_contacts);
		findview();
		/** 得到手机通讯录联系人信息 **/
		getPhoneContacts();
		lv_content = (ExpandableListView) findViewById(R.id.lv_content);
		lv_content.setGroupIndicator(null);
		
		/** 加入支持泛型 */
		adapter = new PingyinAdapter<PhoneContacts>(lv_content, plist,
				R.layout.contacts_list) {

			@Override
			public String getItemName(PhoneContacts goodMan) {
				return goodMan.getContactName();
			}

			/** 返回viewholder持有 */
			@Override
			public ViewHolder getViewHolder(PhoneContacts goodMan) {
				/** View holder */
				class DataViewHolder extends ViewHolder implements
						OnClickListener, OnCheckedChangeListener {
					public TextView contactname;
					public TextView phonenumber;
					public CheckBox checkBox;

					public DataViewHolder(PhoneContacts goodMan) {
						super(goodMan);
					}

					/** 初始化 */
					@Override
					public ViewHolder getHolder(View view) {
						contactname = (TextView) view
								.findViewById(R.id.contactname);
						phonenumber = (TextView) view
								.findViewById(R.id.phonenumber);
						checkBox = (CheckBox) view.findViewById(R.id.checkBox);
						/** 在这里设置点击事件 */
						checkBox.setOnCheckedChangeListener(this);
						view.setOnClickListener(this);
						return this;
					}

					/** 显示数据 */
					@Override
					public void show() {
						contactname.setText(getItem().getContactName());
						phonenumber.setText(getItem().getPhoneNumber());
					}

					/** 点击事件 */
					@Override
					public void onClick(View v) {
						Toast.makeText(v.getContext(),
								getItem().getContactName(), Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							phoneList.add(getItem().getPhoneNumber());
						} else {
							phoneList.remove(getItem().getPhoneNumber());
						}
					}
				}
				return new DataViewHolder(goodMan);
			}

			@Override
			public void onItemClick(PhoneContacts goodMan, View view,
					int position) {
				Toast.makeText(view.getContext(),
						position + " " + goodMan.getContactName(),
						Toast.LENGTH_SHORT).show();
			}
		};
		/** 展开并设置adapter */
		adapter.expandGroup();
		FancyIndexer mFancyIndexer = (FancyIndexer) findViewById(R.id.bar);
		mFancyIndexer
				.setOnTouchLetterChangedListener(new OnTouchLetterChangedListener() {

					@Override
					public void onTouchLetterChanged(String letter) {
						for (int i = 0, j = adapter.getKeyMapList().getTypes()
								.size(); i < j; i++) {
							String str = adapter.getKeyMapList().getTypes()
									.get(i);
							if (letter.toUpperCase().equals(str.toUpperCase())) {
								/** 跳转到选中的字母表 */
								lv_content.setSelectedGroup(i);
							}
						}
					}
				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.contacts_top_tv2:// 邀请
			String uriString = "smsto:";
			if (phoneList.size() > 0) {
				for (int i = 0; i < phoneList.size(); i++) {
					String phone = phoneList.get(i);
					uriString = uriString + phone + ";";
					System.out.println("uriString" + uriString);
				}
				Uri smsToUri = Uri.parse(uriString);
				Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
				intent.putExtra("sms_body", getResources().getString(
						R.string.app_share_weixin_txt));
				startActivity(intent);
			} else {
				Futil.showMessage(ContactsActivity.this, "请至少选择一个联系人");
			}

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
	private ImageView back_top;
	private TextView contacts_top_tv2;

	private void findview() {
		contacts_top_tv2 = (TextView) findViewById(R.id.contacts_top_tv2);
		contacts_top_tv2.setOnClickListener(this);

		back_top = (ImageView) findViewById(R.id.back_top);
		back_top.setOnClickListener(this);

	}

	/** 获取库Phon表字段 **/
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER };

	/** 联系人显示名称 **/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;

	/** 电话号码 **/
	private static final int PHONES_NUMBER_INDEX = 1;

	/** 得到手机通讯录联系人信息 **/
	private void getPhoneContacts() {
		ContentResolver resolver = ContactsActivity.this.getContentResolver();

		// 获取手机联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;

				// 得到联系人名称
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);

				PhoneContacts p = new PhoneContacts();
				p.setContactName(contactName);
				p.setPhoneNumber(phoneNumber);
				plist.add(p);
			}

			phoneCursor.close();
		}
	}

	/** 得到手机SIM卡联系人人信息 **/
	private void getSIMContacts() {
		ContentResolver resolver = ContactsActivity.this.getContentResolver();
		// 获取Sims卡联系人
		Uri uri = Uri.parse("content://icc/adn");
		Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
				null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;
				// 得到联系人名称
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);

				// Sim卡中没有联系人头像

				PhoneContacts p = new PhoneContacts();
				p.setContactName(contactName);
				p.setPhoneNumber(phoneNumber);
				plist.add(p);
			}

			phoneCursor.close();
		}
	}

}
