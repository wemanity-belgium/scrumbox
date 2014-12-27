package com.wemanity.scrumbox.android.time;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/**
 * Schedule a countdown until a time in the future, with
 * regular notifications on intervals along the way.
 *
 * Example of showing a 30 second countdown in a text field:
 *
 * <pre class="prettyprint">
 * new CountDownTimer(30000, 1000) {
 *
 *     public void onTick(long millisUntilFinished) {
 *         mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
 *     }
 *
 *     public void onFinish() {
 *         mTextField.setText("done!");
 *     }
 *  }.start();
 * </pre>
 *
 * The calls to {@link #onTick(long)} are synchronized to this object so that
 * one call to {@link #onTick(long)} won't ever occur before the previous
 * callback is complete.  This is only relevant when the implementation of
 * {@link #onTick(long)} takes an amount of time to execute that is significant
 * compared to the countdown interval.
 */
public abstract class CountDown {

    /**
     * Millis since epoch when alarm should stop.
     */
    private final long mMillisInFuture;

    /**
     * The interval in millis that the user receives callbacks
     */
    private final long mCountdownInterval;

    private long mStopTimeInFuture;
    private long mStartTime;
    private long mStopTime;

    /**
     * boolean representing if the timer was cancelled
     */
    private boolean mStop = false;

    /**
     * @param millisInFuture The number of millis in the future from the call
     *   to {@link #start()}
     * @param countDownInterval The interval along the way to receive
     *   {@link #onTick(long)} callbacks.
     */
    public CountDown(long millisInFuture, long countDownInterval) {
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
    }

    /**
     * Cancel the countdown.
     */
    public synchronized final void stop() {
        mStop = true;
        mHandler.removeMessages(MSG);
        onFinish(mStopTime - mStartTime);
    }

    /**
     * Start the countdown.
     */
    public synchronized final CountDown start() {

        if (mMillisInFuture <= 0) {
            onFinish(0);
            return this;
        }
        mStartTime = SystemClock.elapsedRealtime();
        mStopTimeInFuture = mStartTime + mMillisInFuture;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return this;
    }


    /**
     * Callback fired on regular interval.
     * @param millisUntilFinished The amount of time until finished.
     */
    public abstract void onTick(long millisUntilFinished);

    public abstract void onFinish(long duration);

    private static final int MSG = 1000;

    // handles counting down
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (CountDown.this) {
                if (mStop) {
                    return;
                }
                mStopTime = SystemClock.elapsedRealtime();
                final long millisLeft = mStopTimeInFuture - mStopTime;

                long lastTickStart = SystemClock.elapsedRealtime();
                onTick(millisLeft);
                // take into account user's onTick taking time to execute
                long delay = lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime();

                // special case: user's onTick took more than interval to
                // complete, skip to next interval
                while (delay < 0) delay += mCountdownInterval;

                sendMessageDelayed(obtainMessage(MSG), delay);

            }
        }
    };
}