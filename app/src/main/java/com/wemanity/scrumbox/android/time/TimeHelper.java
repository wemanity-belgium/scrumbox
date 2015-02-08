package com.wemanity.scrumbox.android.time;

import android.widget.TextView;

import com.wemanity.scrumbox.android.Sign;

public class TimeHelper {
    private static final long MILLIS_IN_MINUTE = 60000;
    private static final long MILLIS_IN_SECONDE = 1000;
    public static final String timeLabel = "%s %02d:%02d:%03d";


    public static void setTimeLabel(TextView textView, long time){
        Time formatedTime = formatTime(time);
        textView.setText(String.format(timeLabel, formatedTime.getSign(), formatedTime.getMinute(), formatedTime.getSeconde(), formatedTime.getMillis()));
    }

    public static Time formatTime(long time){
        Sign sign = time > 0 ? Sign.POSITIVE : Sign.NEGATIVE;
        time  = Math.abs(time);
        long totalMillis = time;
        long minute = time / MILLIS_IN_MINUTE;
        time  = (time % MILLIS_IN_MINUTE);
        long millis = time % MILLIS_IN_SECONDE;
        long seconde = time / MILLIS_IN_SECONDE;
        return new Time.Builder()
                .sign(sign)
                .totalMillis(totalMillis)
                .minute(minute)
                .seconde(seconde)
                .millis(millis)
                .build();
    }
}
