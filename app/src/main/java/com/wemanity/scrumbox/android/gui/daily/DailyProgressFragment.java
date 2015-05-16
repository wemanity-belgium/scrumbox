package com.wemanity.scrumbox.android.gui.daily;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.gui.RootFragment;
import com.wemanity.scrumbox.android.gui.base.BaseFragment;
import com.wemanity.scrumbox.android.time.CountDown;
import com.wemanity.scrumbox.android.time.TimeHelper;


import java.util.concurrent.Executor;

import roboguice.fragment.provided.RoboFragment;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;


public class DailyProgressFragment extends BaseFragment implements View.OnClickListener, CountDown.CountDownEventListener{
    public static final String TAG = "DailyProgressFragment";
    private CountDown countDown;
    @InjectView(R.id.dailyStartButton) private Button startButton;
    @InjectView(R.id.dailyTotalTimeValueTextView) private TextView totalTimeValueTextView;
    @InjectView(R.id.dailyParticipantTimeLeftValueTextView) private TextView participantTimeLeftValueTextView;
    @InjectView(R.id.dailyParticipantTimeLeftitleTextView) private TextView participantTimeLeftTitleTextView;
    @InjectView(R.id.dailyTotalTimeTitleTextView) private TextView dailyTotalTimeTitleTextView;
    @InjectView(R.id.participantPictureLayout) private View participantPictureLayout;
    @InjectView(R.id.roleColorLayout) private View roleColorLayout;
    @InjectResource(R.drawable.circle_drawable) Drawable roleColor;
    @InjectResource(R.string.daily_total_time) String totalTimeTile;
    @InjectResource(R.string.daily_participant_time_left) String participantTimeLeft;

    public DailyProgressFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countDown = new CountDown(2000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_progress_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        roleColorLayout.setBackground(roleColor);
        participantTimeLeftTitleTextView.setText(String.format(participantTimeLeft,"Bob"));
        dailyTotalTimeTitleTextView.setText(totalTimeTile);
        dailyTotalTimeTitleTextView.setCompoundDrawables(null,null,null,null);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.dailyStartButton:
                break;
            case R.id.participantPictureLayout:
                countDown.start();
                break;
        }
    }

    @Override
    public void onTick(long time) {
        TimeHelper.setTimeLabel(participantTimeLeftValueTextView, time);
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                return null;
            }


        };
    }

    @Override
    public void onFinish(long duration) {
        TimeHelper.setTimeLabel(totalTimeValueTextView, duration);
    }

    @Override
    public Class<? extends BaseFragment> getPreviusFragment() {
        return RootFragment.class;
    }
}
