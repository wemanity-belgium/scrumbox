package com.wemanity.scrumbox.android.gui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wemanity.scrumbox.android.time.CountDown;
import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.time.TimeFrame;

public class CountDownView extends GridLayout implements View.OnClickListener {

    private View mMainView;
    private TextView mMinute;
    private TextView mSeconde;
    private TextView mMiliseconde;
    private TextView mTimeSign;
    private ImageButton mStartButton;
    private CountDown mCountDown;

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMainView = inflater.inflate(R.layout.countdown_widget, this, true);
        mMinute = (TextView) mMainView.findViewById(R.id.countDownMinuteTextView);
        mSeconde = (TextView) mMainView.findViewById(R.id.countDownsecondeRextView);
        mMiliseconde = (TextView) mMainView.findViewById(R.id.countDownMiliSecondeRextView);
        mTimeSign = (TextView) mMainView.findViewById(R.id.timeSignTextView);
        mStartButton = (ImageButton) mMainView.findViewById(R.id.countDownStartButton);
        mStartButton.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick(View v) {
        if (mCountDown == null) {
            mCountDown = new CountDown(45000, 100) {

                public void onTick(long millisUntilFinished) {
                    applyTimeFrame(new TimeFrame(millisUntilFinished));
                }

                @Override
                public void onFinish(long duration) {
                    applyTimeFrame(new TimeFrame(duration));
                }
            }.start();
            mStartButton.setBackgroundResource(android.R.drawable.ic_media_next);
        } else {
            mCountDown.stop();
            mCountDown = null;
            mStartButton.setBackgroundResource(android.R.drawable.ic_media_play);
        }
    }

    private void applyTimeFrame(TimeFrame timeFrame){
        mTimeSign.setText(timeFrame.getSign().toString());
        mMiliseconde.setText(String.valueOf(timeFrame.getMillis()));
        mSeconde.setText(String.valueOf(timeFrame.getSeconde()));
        mMinute.setText(String.valueOf(timeFrame.getMinute()));
    }


}
