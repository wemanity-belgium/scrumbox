package com.wemanity.scrumbox.android.time;

import com.wemanity.scrumbox.android.Sign;

public class TimeFrame {

    private static final long MILLIS_IN_MINUTE = 60000;
    private static final long MILLIS_IN_SECONDE = 1000;

    private Sign mSign;
    private long mTotalMillis;
    private long mMinute;
    private long mSeconde;
    private long mMillis;

    public TimeFrame(long totalMillis){
        mSign = totalMillis > 0 ? Sign.POSITIVE : Sign.NEGATIVE;
        totalMillis  = Math.abs(totalMillis);
        mTotalMillis = totalMillis;
        mMinute = totalMillis / MILLIS_IN_MINUTE;
        totalMillis  = (totalMillis % MILLIS_IN_MINUTE);
        mMillis = totalMillis % MILLIS_IN_SECONDE;
        mSeconde = totalMillis / MILLIS_IN_SECONDE;
    }

    public long getTotalMillis() {
        return mTotalMillis;
    }

    public Sign getSign() {
        return mSign;
    }

    public long getMinute() {
        return mMinute;
    }

    public long getSeconde() {
        return mSeconde;
    }

    public long getMillis() {
        return mMillis;
    }

    public String toString(){
        return String.format("%s %02d:%02d:%03d",mSign, mMinute,mSeconde,mMillis);
    }

}
