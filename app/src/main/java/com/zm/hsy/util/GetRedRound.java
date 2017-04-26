package com.zm.hsy.util;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.zm.hsy.activity.MainActivity;
import com.zm.hsy.https.Futil;
import com.zm.hsy.https.URLManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 开启层序时候获取是否有红点
 */

public class GetRedRound {
    private String userid;

    public GetRedRound(Context context) {
        userid = Futil.getValue(context, "userid");
    }

    public void getPushMes() {
        Log.i("hond", "进入判断红点方法");
        String strUrl = URLManager.getPushMessageByUser;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user.id", userid);
        map.put("pft", "3");
        Futil.xutils(strUrl, map, handler, URLManager.one);
    }


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Log.i("hond", "进入自定义方法的handler");
            if (msg.what == URLManager.one) {
                JSONObject obj = (JSONObject) msg.obj;
                try {
                    JSONArray messageCentreList = obj
                            .getJSONArray("messageCentreList");
                    if (messageCentreList.length() != 0) {
                        for (int i = 0; i < messageCentreList.length(); i++) {
                            JSONObject messageCentre = messageCentreList
                                    .getJSONObject(i);
                            String num = messageCentre.getString("num");
                            Log.i("hond", "马上要判断if else 了");
                            if (!num.equals("0")){
                                Log.i("hond", "if");
                                MainActivity.hongdian.setVisibility(View.VISIBLE);
                                MainActivity.ishongdian=true;
                                break;
                            }else {
                                Log.i("hond", "else");
//                                MainActivity.hongdian.setVisibility(View.VISIBLE);
//                                MainActivity.ishongdian=true;
//                                break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }

        }
    };
}



