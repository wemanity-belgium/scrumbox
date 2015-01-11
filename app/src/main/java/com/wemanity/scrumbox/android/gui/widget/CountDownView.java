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

public class CountDownView extends GridLayout implements View.OnClickListener{

    private View mMainView;
    private TextView mTime;
    private ImageButton mStartButton;
    private ImageButton mNextButton;
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
        mMainView = inflater.inflate(R.layout.countdown_layout, this, true);
        mTime = (TextView) mMainView.findViewById(R.id.timeTextView);
        mStartButton = (ImageButton) mMainView.findViewById(R.id.countDownStartButton);
        mStartButton.setOnClickListener(this);
        mNextButton = (ImageButton) mMainView.findViewById(R.id.countDownNextButton);
        mNextButton.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.countDownStartButton:
                if (mCountDown == null) {
                    mCountDown = new CountDown(45000, 100) {

                        public void onTick(TimeFrame millisUntilFinished) {
                            mTime.setText(millisUntilFinished.toString());
                        }

                        @Override
                        public void onFinish(TimeFrame duration) {
                            mTime.setText(duration.toString());
                        }
                    };
                }

                if (!mCountDown.isStart()){
                    mCountDown.start();
                    mStartButton.setBackgroundResource(android.R.drawable.ic_media_pause);
                }else {
                    mCountDown.pause();
                    mStartButton.setBackgroundResource(android.R.drawable.ic_media_play);
                }

                break;
            case R.id.countDownNextButton:
                mCountDown.stop();
                mCountDown = null;
                mStartButton.setBackgroundResource(android.R.drawable.ic_media_play);
                break;
        }
    }
}
