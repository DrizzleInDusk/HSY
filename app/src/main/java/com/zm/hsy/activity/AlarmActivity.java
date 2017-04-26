package com.zm.hsy.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.loonggg.lib.alarmmanager.clock.AlarmManagerUtil;
import com.zm.hsy.R;
import com.zm.hsy.https.Futil;
import com.zm.hsy.myview.SelectRemindCyclePopup;
import com.zm.hsy.myview.SelectRemindWayPopup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
/** 闹铃 */
public class AlarmActivity extends Activity implements OnClickListener {

    private int mHour = -1;
    private int mMinutes = -1;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        context = this;
        findview();
        String cy = Futil.getValue(context, "cycle");
        String ri = Futil.getValue(context, "ring");
        time = Futil.getValue(context, "time");
        if (!cy.equals("")) {
            alarm_ll.setVisibility(View.VISIBLE);
            alarmonoff.setSelected(false);
            cycle = Integer.parseInt(cy);
            if (cycle == 0) {
                alarm_chongfu_tv.setText("每天");
            } else if (cycle == -1) {
                alarm_chongfu_tv.setText("只响一次");
            } else {
                alarm_chongfu_tv.setText(parseRepeat(cycle, 0));
            }
        }
        if (!ri.equals("")) {
            ring = Integer.parseInt(ri);
            if (ring == 0) {
                alarm_ring_tv.setText("震动");
            } else if (ring == 1) {
                alarm_ring_tv.setText("铃声");
            }
        }
        if (!time.equals("")) {
            alarm_time_tv.setText(time);
        } else {
            // 获取当前时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            if (mHour == -1 && mMinutes == -1) {
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinutes = calendar.get(Calendar.MINUTE);
            }
            alarm_time_tv.setText(mHour + ":" + mMinutes);
        }
    }


    private TextView alarmonoff, alarm_chongfu_tv, alarm_time_tv, alarm_ring_tv;
    private LinearLayout alarm_ll, allLayout;
    private TimePickerView pvTime;

    private void findview() {
        allLayout = (LinearLayout) findViewById(R.id.all_layout);
        alarmonoff = (TextView) findViewById(R.id.alarmonoff);
        alarmonoff.setOnClickListener(this);
        findViewById(R.id.back_top).setOnClickListener(this);
        alarm_time_tv = (TextView) findViewById(R.id.alarm_time_tv);
        alarm_ring_tv = (TextView) findViewById(R.id.alarm_ring_tv);
        alarm_chongfu_tv = (TextView) findViewById(R.id.alarm_chongfu_tv);
        alarm_ll = (LinearLayout) findViewById(R.id.alarm_ll);
        findViewById(R.id.alarm_chongfu).setOnClickListener(this);
        findViewById(R.id.alarm_time).setOnClickListener(this);
        findViewById(R.id.alarm_ring).setOnClickListener(this);
        alarm_ll.setVisibility(View.GONE);
        alarmonoff.setSelected(true);
        pvTime = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                time = getTime(date);
                alarm_time_tv.setText(time);
            }
        });


    }

    private String time;
    private int cycle;
    private int ring;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_top:
                oversel();
                break;
            case R.id.alarmonoff:
                if (alarmonoff.isSelected()) {
                    alarm_ll.setVisibility(View.VISIBLE);
                    alarmonoff.setSelected(false);
                } else {
                    alarm_ll.setVisibility(View.GONE);
                    alarmonoff.setSelected(true);
                }
                break;
            case R.id.alarm_chongfu:
                selectRemindCycle();
                break;
            case R.id.alarm_time:
                pvTime.show();
                break;
            case R.id.alarm_ring:
                selectRingWay();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            oversel();
        }
        return false;
    }

    private void oversel() {
        if (!alarmonoff.isSelected()) {
            setClock();
        } else {
            String cycle = Futil.getValue(context, "cycle");
            if (!cycle.equals("")) {
                int cy = Integer.parseInt(cycle);
                if (cy > 0) {
                    String weeksStr = parseRepeat(cy, 1);
                    String[] weeks = weeksStr.split(",");
                    for (int i = 0; i < weeks.length; i++) {
                        AlarmManagerUtil.cancelAlarm(context, "", i);
                    }
                } else {
                    AlarmManagerUtil.cancelAlarm(context, "", 0);
                }
                Futil.romveValue(context, "cycle", "4");
                Futil.romveValue(context, "time", "4");
                Futil.romveValue(context, "ring", "4");
            }
        }
        finish();
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    private void setClock() {
        if (time != null && time.length() > 0) {
            Futil.saveValue(context, "cycle", cycle + "");
            Futil.saveValue(context, "time", time);
            Futil.saveValue(context, "ring", ring + "");
            String[] times = time.split(":");
            if (cycle == 0) {//是每天的闹钟
                AlarmManagerUtil.setAlarm(this, 0, Integer.parseInt(times[0]), Integer.parseInt
                        (times[1]), 0, 0, "闹钟响了", ring);
            }
            if (cycle == -1) {//是只响一次的闹钟
                AlarmManagerUtil.setAlarm(this, 1, Integer.parseInt(times[0]), Integer.parseInt
                        (times[1]), 0, 0, "闹钟响了", ring);
            } else {//多选，周几的闹钟
                String weeksStr = parseRepeat(cycle, 1);
                String[] weeks = weeksStr.split(",");
                for (int i = 0; i < weeks.length; i++) {
                    AlarmManagerUtil.setAlarm(this, 2, Integer.parseInt(times[0]), Integer
                            .parseInt(times[1]), i, Integer.parseInt(weeks[i]), "闹钟响了", ring);
                }
            }
            Toast.makeText(this, "闹钟设置成功", Toast.LENGTH_LONG).show();
        }

    }


    public void selectRemindCycle() {
        final SelectRemindCyclePopup fp = new SelectRemindCyclePopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindCyclePopupListener(new SelectRemindCyclePopup
                .SelectRemindCyclePopupOnClickListener() {

            @Override
            public void obtainMessage(int flag, String ret) {
                switch (flag) {
                    // 星期一
                    case 0:

                        break;
                    // 星期二
                    case 1:

                        break;
                    // 星期三
                    case 2:

                        break;
                    // 星期四
                    case 3:

                        break;
                    // 星期五
                    case 4:

                        break;
                    // 星期六
                    case 5:

                        break;
                    // 星期日
                    case 6:

                        break;
                    // 确定
                    case 7:
                        int repeat = Integer.valueOf(ret);
                        alarm_chongfu_tv.setText(parseRepeat(repeat, 0));
                        cycle = repeat;
                        fp.dismiss();
                        break;
                    case 8:
                        alarm_chongfu_tv.setText("每天");
                        cycle = 0;
                        fp.dismiss();
                        break;
                    case 9:
                        alarm_chongfu_tv.setText("只响一次");
                        cycle = -1;
                        fp.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
    }


    public void selectRingWay() {
        SelectRemindWayPopup fp = new SelectRemindWayPopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindWayPopupListener(new SelectRemindWayPopup
                .SelectRemindWayPopupOnClickListener() {

            @Override
            public void obtainMessage(int flag) {
                switch (flag) {
                    // 震动
                    case 0:
                        alarm_ring_tv.setText("震动");
                        ring = 0;
                        break;
                    // 铃声
                    case 1:
                        alarm_ring_tv.setText("铃声");
                        ring = 1;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * @param repeat 解析二进制闹钟周期
     * @param flag   flag=0返回带有汉字的周一，周二cycle等，flag=1,返回weeks(1,2,3)
     * @return
     */
    public static String parseRepeat(int repeat, int flag) {
        String cycle = "";
        String weeks = "";
        if (repeat == 0) {
            repeat = 127;
        }
        if (repeat % 2 == 1) {
            cycle = "周一";
            weeks = "1";
        }
        if (repeat % 4 >= 2) {
            if ("".equals(cycle)) {
                cycle = "周二";
                weeks = "2";
            } else {
                cycle = cycle + "," + "周二";
                weeks = weeks + "," + "2";
            }
        }
        if (repeat % 8 >= 4) {
            if ("".equals(cycle)) {
                cycle = "周三";
                weeks = "3";
            } else {
                cycle = cycle + "," + "周三";
                weeks = weeks + "," + "3";
            }
        }
        if (repeat % 16 >= 8) {
            if ("".equals(cycle)) {
                cycle = "周四";
                weeks = "4";
            } else {
                cycle = cycle + "," + "周四";
                weeks = weeks + "," + "4";
            }
        }
        if (repeat % 32 >= 16) {
            if ("".equals(cycle)) {
                cycle = "周五";
                weeks = "5";
            } else {
                cycle = cycle + "," + "周五";
                weeks = weeks + "," + "5";
            }
        }
        if (repeat % 64 >= 32) {
            if ("".equals(cycle)) {
                cycle = "周六";
                weeks = "6";
            } else {
                cycle = cycle + "," + "周六";
                weeks = weeks + "," + "6";
            }
        }
        if (repeat / 64 == 1) {
            if ("".equals(cycle)) {
                cycle = "周日";
                weeks = "7";
            } else {
                cycle = cycle + "," + "周日";
                weeks = weeks + "," + "7";
            }
        }

        return flag == 0 ? cycle : weeks;
    }
}
