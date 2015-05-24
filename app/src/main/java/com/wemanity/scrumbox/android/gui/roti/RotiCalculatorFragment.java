package com.wemanity.scrumbox.android.gui.roti;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.gui.RootFragment;
import com.wemanity.scrumbox.android.gui.roti.RotiScoreView.RotiScore;
import com.wemanity.scrumbox.android.gui.base.BaseFragment;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

public class RotiCalculatorFragment extends BaseFragment implements RotiScoreView.OnScoreClickListener{

    private double score;

    public double numberOfParticipant;

    private Stack<RotiScore> stackTrace;

    @InjectResource(R.drawable.roti_reset)
    private Drawable rotiResetImage;

    @InjectResource(R.drawable.roti_undo)
    private Drawable rotiUndoImage;

    @InjectView(R.id.rotiCurrentScoreTextView)
    private TextView currentScoreTextView;

    @InjectView(R.id.nbParticipantTextView)
    private TextView nbParticipants;

    @InjectView(R.id.undoButton)
    private ImageButton undoButton;

    @InjectView(R.id.clearButton)
    private ImageButton clearButton;

    @InjectView(R.id.rotiScoreZeroView)
    private RotiScoreView zero;

    @InjectView(R.id.rotiScoreOneView)
    private RotiScoreView one;

    @InjectView(R.id.rotiScoreTwoView)
    private RotiScoreView two;

    @InjectView(R.id.rotiScoreThreeView)
    private RotiScoreView three;

    @InjectView(R.id.rotiScoreFourView)
    private RotiScoreView four;

    @InjectView(R.id.rotiScoreFiveView)
    private RotiScoreView five;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stackTrace = savedInstanceState != null ? (Stack)savedInstanceState.getSerializable("stackTrace") : new Stack<RotiScore>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.roti_calculator_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        zero.setOnScoreClickListener(this);
        one.setOnScoreClickListener(this);
        two.setOnScoreClickListener(this);
        three.setOnScoreClickListener(this);
        four.setOnScoreClickListener(this);
        five.setOnScoreClickListener(this);

        undoButton.setImageDrawable(rotiUndoImage);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoScore();
            }
        });

        clearButton.setImageDrawable(rotiResetImage);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
        if (stackTrace.isEmpty()){
            reset();
        } else {
            restoreScore(stackTrace);
        }
    }

    @Override
    public Class<? extends BaseFragment> getPreviusFragment() {
        return RootFragment.class;
    }

    @Override
    public void OnScoreClick(View v,RotiScore rotiScore) {
        addScore(rotiScore);
    }

    private void addScore(RotiScore rotiScore){
        this.score += rotiScore.getValue();
        this.numberOfParticipant++;
        this.stackTrace.push(rotiScore);
        updateScreen();
    }

    private void undoScore(){
        if (!stackTrace.isEmpty()) {
            RotiScore rotiScore = stackTrace.pop();
            switch (rotiScore) {
                case ZERO:
                    zero.descCount();
                    break;
                case ONE:
                    one.descCount();
                    break;
                case TWO:
                    two.descCount();
                    break;
                case THREE:
                    three.descCount();
                    break;
                case FOUR:
                    four.descCount();
                    break;
                case FIVE:
                    five.descCount();
                    break;
            }
            score -= rotiScore.getValue();
            numberOfParticipant--;
            updateScreen();
        }
    }

    private void restoreScore(Stack<RotiScore> stackTrace){
        Map<RotiScore, Integer> scoreMap = new HashMap<>();
        for (RotiScore rotiScore : stackTrace){
            Integer count = scoreMap.get(rotiScore);
            count = new Integer(count == null ? 1 : count+1);
            scoreMap.put(rotiScore, count);
        }
        for (RotiScore rotiScore : scoreMap.keySet()){
            Integer count = scoreMap.get(rotiScore);
            switch (rotiScore) {
                case ZERO:
                    zero.addToCount(count);
                    break;
                case ONE:
                    one.addToCount(count);
                    break;
                case TWO:
                    two.addToCount(count);
                    break;
                case THREE:
                    three.addToCount(count);
                    break;
                case FOUR:
                    four.addToCount(count);
                    break;
                case FIVE:
                    five.addToCount(count);
                    break;
            }
        }
        updateScreen();
    }

    private void updateScreen(){
        BigDecimal average = new BigDecimal(numberOfParticipant == 0? score : score / numberOfParticipant, new MathContext(2, RoundingMode.HALF_UP));
        currentScoreTextView.setText(average.toString());
        nbParticipants.setText(String.valueOf((int)numberOfParticipant));
    }

    public void reset(){
        numberOfParticipant = 0;
        score = 0;
        stackTrace.clear();
        updateScreen();
        zero.reset();
        one.reset();
        two.reset();
        three.reset();
        four.reset();
        five.reset();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("stackTrace", stackTrace);
    }
}
