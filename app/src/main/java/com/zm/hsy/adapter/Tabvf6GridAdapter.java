package com.zm.hsy.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.zm.hsy.R;
import com.zm.hsy.entity.User;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;
import com.zm.hsy.util.ActivityJumpControl;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Adapter 主播
 *
 * @author Administrator
 */
public class Tabvf6GridAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater minflater;
    private List<User> userlist;
    private ImageView iv;

    public Tabvf6GridAdapter(Context context, List<User> userlist) {
        super();
        this.minflater = LayoutInflater.from(context);
        this.userlist = userlist;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (null != userlist) {
            return userlist.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int arg0) {
        return userlist.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = minflater
                    .inflate(R.layout.tab_pager_zhubo_item1, null);
            holder = new ViewHolder();

            holder.user1_name = (TextView) convertView
                    .findViewById(R.id.user1_name);

            holder.user1_head = (ImageView) convertView
                    .findViewById(R.id.user1_head);

            holder.hasfocus = (ImageView) convertView
                    .findViewById(R.id.hasfocus);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final User user = userlist.get(position);
        final String id = user.getId();
        holder.user1_name.setText(user.getNickname());

        String ifgz = user.getIfgz();
        if (ifgz.equals("0")) {
            holder.hasfocus.setSelected(false);
        } else {
            holder.hasfocus.setSelected(true);
        }
        String type = user.getHeadStatus();
        String head = user.getHead();
        final String concemId = user.getId();
        if (!type.equals("http")) {
            head = URLManager.Head_URL + head;
        }
        Picasso.with(context).load(head).resize(400, 400).error(R.mipmap.zhubo_title1_img2).into(holder.user1_head);

        holder.user1_head.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                System.out.println("点击了主播>>>id>>"+id);
                ActivityJumpControl.getInstance((Activity) context)
                        .gotoZhuboActivity(id);
            }
        });
        iv = holder.hasfocus;
        holder.hasfocus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {

                String strUrl = URLManager.addUserConcem;
                String userid = Futil.getValue(context, "userid");
                if (userid != null && !userid.equals("")) {
                    RequestParams params = new RequestParams(strUrl);
                        params.addBodyParameter("uid", userid);
                        params.addBodyParameter("concemId", concemId);
                    x.http().post(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            v.setSelected(true);
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String message = jsonObject.getString("message");
                                String code = jsonObject.getString("code");
                                if (code.equals("2")) {
                                    Toast.makeText(context, "" + message,
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context, "" + message,
                                            Toast.LENGTH_LONG).show();
                                }
                                Log.v("String转换json成功", "当前获取json字符>>>" + jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.v("String转换json失败", "当前获取String字符>>>" + result);
                            }

                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            if (ex instanceof HttpException) { // 网络错误
                                HttpException httpEx = (HttpException) ex;
                                int responseCode = httpEx.getCode();
                                String responseMsg = httpEx.getMessage();
                                String errorResult = httpEx.getResult();
                                Log.v("网络错误", "当前获取字符>>>" + httpEx);
                            } else { // 其他错误
                                // ...
                                Log.v("其他错误", "当前获取字符>>>" + ex.getMessage());
                            }
                           Futil. showMessage(x.app(), "请检查网络");
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {

                        }
                    });
                }else{

                    Futil.showMessage(context, "请先登录");
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        private ImageView user1_head;
        private ImageView hasfocus;
        private TextView user1_name;

    }

}
