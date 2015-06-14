package com.wemanity.scrumbox.android.gui.daily;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.db.entity.Daily;
import com.wemanity.scrumbox.android.db.entity.DailyOccurrence;
import com.wemanity.scrumbox.android.db.entity.Participant;
import com.wemanity.scrumbox.android.db.entity.Participation;
import com.wemanity.scrumbox.android.gui.ActiveBackButton;
import com.wemanity.scrumbox.android.gui.RootFragment;
import com.wemanity.scrumbox.android.gui.base.BaseFragment;
import com.wemanity.scrumbox.android.time.CountDown;
import com.wemanity.scrumbox.android.time.TimeHelper;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;


public class DailyProgressFragment extends BaseFragment implements ActiveBackButton, View.OnClickListener, CountDown.CountDownEventListener{
    @InjectView(R.id.dailyTitleTextView) private TextView dailyTitleTextView;
    @InjectView(R.id.dailyTotalTimeValueTextView) private TextView totalTimeValueTextView;
    @InjectView(R.id.dailyParticipantTimeLeftValueTextView) private TextView participantTimeLeftValueTextView;
    @InjectView(R.id.dailyParticipantTimeLeftitleTextView) private TextView participantTimeLeftTitleTextView;
    @InjectView(R.id.dailyTotalTimeTitleTextView) private TextView dailyTotalTimeTitleTextView;
    @InjectView(R.id.participantPictureLayout) private ImageSwitcher imageSwitcher;
    @InjectResource(R.drawable.circle_drawable) Drawable roleColor;
    @InjectResource(R.string.daily_total_time) String totalTimeTile;
    @InjectResource(R.string.daily_participant_time_left) String participantTimeLeft;
    @InjectResource(R.drawable.default_profile_avatar) Drawable dedaultProfilAvatar;
    private Daily daily;
    private Iterator<Participant> participantIterator;
    private long totalDuration = 0;
    private CountDown countDown;
    private List<Participation> dailyParticipations;
    private Participant participantInProgress;
    private DailyOccurrence dailyOccurrence = new DailyOccurrence();
    public DailyProgressFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        dailyParticipations = new ArrayList<>();
        if (args != null && args.containsKey("daily")){
            daily = (Daily) args.getSerializable("daily");
            countDown = new CountDown(daily.getDurationbyparticipant() * 1000);
            countDown.setCountDownEventListener(this);
        } else {
            countDown = new CountDown(2000);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_progress_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dailyTotalTimeTitleTextView.setText(totalTimeTile);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getActivity());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return myView;
            }
        });
        imageSwitcher.setImageResource(R.drawable.default_profile_avatar);
        imageSwitcher.setOnClickListener(this);
        initialize();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.participantPictureLayout:
                if (!countDown.isStart()){
                    countDown.start();
                } else {
                    nextParticipant();
                }
                break;
        }
    }

    private void nextParticipant(){
        countDown.stop();
        Participation participation = new Participation();
        participation.setParticipant(participantInProgress);
        participation.setTime(countDown.getDuration());
        dailyParticipations.add(participation);

        if (!participantIterator.hasNext()){
            terminateDaily();
        } else {
            participantInProgress = participantIterator.next();
            participantTimeLeftTitleTextView.setText(String.format(participantTimeLeft, participantInProgress.getMember().getNickname()));
            participantTimeLeftValueTextView.setTextColor(Color.BLACK);
            long timeRecieveFromPreviusParticipant = countDown.getTimeLeft();
            countDown = new CountDown((daily.getDurationbyparticipant() * 1000) + timeRecieveFromPreviusParticipant);
            countDown.setCountDownEventListener(this);
            countDown.start();
        }
    }

    private void initialize(){
        List<Participant> participants = new ArrayList<>(daily.getParticipants());
        Collections.shuffle(participants);
        participantIterator = participants.iterator();
        if (participantIterator.hasNext()){
            participantInProgress = participantIterator.next();
            participantTimeLeftTitleTextView.setText(String.format(participantTimeLeft, participantInProgress.getMember().getNickname()));
            TimeHelper.setTimeLabel(participantTimeLeftValueTextView, daily.getDurationbyparticipant() * 1000);
            TimeHelper.setTimeLabelWithoutSymbol(totalTimeValueTextView, 0);
        }
        dailyOccurrence.setDateexecuted(new Date());
        dailyTitleTextView.setText(daily.getTitle());
    }

    private void terminateDaily(){
        imageSwitcher.setOnClickListener(null);
        new AlertDialog.Builder(this.getActivity())
                .setTitle("Daily terminate!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onTick(long timeLeft, long duration) {
        TimeHelper.setTimeLabel(participantTimeLeftValueTextView, timeLeft);
        totalDuration += duration;
        TimeHelper.setTimeLabelWithoutSymbol(totalTimeValueTextView, totalDuration);
    }

    @Override
    public void onTimeIsUp() {
        participantTimeLeftValueTextView.setTextColor(Color.RED);
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getActivity(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class<? extends BaseFragment> getPreviusFragment() {
        return RootFragment.class;
    }
}
