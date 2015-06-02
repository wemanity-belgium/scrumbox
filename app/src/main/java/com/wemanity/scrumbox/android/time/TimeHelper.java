package com.wemanity.scrumbox.android.time;

import android.widget.TextView;

public class TimeHelper {
    private static final long MILLIS_IN_MINUTE = 60000;
    private static final long MILLIS_IN_SECONDE = 1000;
    public static final String timeLabel = "%1$s %2$02d:%3$02d:%4$03d";
    public static final String timeLabelWithoutSymbole = "%2$02d:%3$02d:%4$03d";

    public static void setTimeLabel(TextView textView, long timestamp){
        textView.setText(formatTimestamp(timestamp,timeLabel));
    }

    public static void setTimeLabelWithoutSymbol(TextView textView, long timestamp){
        textView.setText(formatTimestamp(timestamp, timeLabelWithoutSymbole));
    }

    public static String formatTimestamp(long time, String format){
        Time formattedTime = parseTimestamp(time);
        return String.format(format, formattedTime.getSign(), formattedTime.getMinute(), formattedTime.getSeconde(), formattedTime.getMillis());
    }

    public static Time parseTimestamp(long timestamp){
        Sign sign = Sign.getSign(timestamp);
        timestamp  = Math.abs(timestamp);
        long totalMillis = timestamp;
        long minute = timestamp / MILLIS_IN_MINUTE;
        timestamp  = (timestamp % MILLIS_IN_MINUTE);
        long millis = timestamp % MILLIS_IN_SECONDE;
        long seconde = timestamp / MILLIS_IN_SECONDE;

        return Time.newBuilder()
                .sign(sign)
                .totalMillis(totalMillis)
                .minute(minute)
                .seconde(seconde)
                .millis(millis)
                .build();
    }
}
