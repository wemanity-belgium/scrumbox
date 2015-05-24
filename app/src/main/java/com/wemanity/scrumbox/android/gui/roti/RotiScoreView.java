package com.wemanity.scrumbox.android.gui.roti;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.readystatesoftware.viewbadger.BadgeView;
import com.wemanity.scrumbox.android.R;

public class RotiScoreView extends FrameLayout implements View.OnClickListener{
    public enum RotiScore {
        ZERO(0),
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5);

        private int value;
        private RotiScore(int value){
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        static RotiScore valueOf(int value){
            switch (value){
                case 0:
                    return ZERO;
                case 1:
                    return ONE;
                case 2:
                    return TWO;
                case 3:
                    return THREE;
                case 4:
                    return FOUR;
                case 5:
                    return FIVE;
                default:
                    return ZERO;
            }
        }
    }

    public interface OnScoreClickListener {
        public void OnScoreClick(View v, RotiScore rotiScore);
    }

    private ImageButton imageButton;

    private int count;

    private RotiScore rotiScore;

    private BadgeView badgeView;

    private OnScoreClickListener onScoreClickListener = new OnScoreClickListener(){

        @Override
        public void OnScoreClick(View v, RotiScore rotiScore) {

        }
    };

    public RotiScoreView(Context context) {
        this(context, null);
    }

    public RotiScoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotiScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        count = 0;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RotiScoreView);
        int scoreValue = ta.getInt(0, 0);
        this.rotiScore = RotiScore.valueOf(scoreValue);
        Drawable drawable = ta.getDrawable(1);
        imageButton = new ImageButton(context);
        imageButton.setPadding(20,20,20,20);
        imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageButton.setAdjustViewBounds(true);
        imageButton.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        imageButton.setImageDrawable(drawable);
        imageButton.setOnClickListener(this);
        addView(imageButton, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        badgeView = new BadgeView(context, imageButton);
        badgeView.setBadgeMargin(30);
        updateBadge();
    }

    public void descCount(){
        count--;
        updateBadge();
    }

    public void incCount(){
        count++;
        updateBadge();
    }

    public void addToCount(int toAdd){
        count += toAdd;
        updateBadge();
    }

    public void reset(){
        if (count != 0) {
            count = 0;
            updateBadge();
        }
    }

    public void setOnScoreClickListener(OnScoreClickListener onScoreClickListener) {
        this.onScoreClickListener = onScoreClickListener;
    }

    public OnScoreClickListener getOnScoreClickListener() {
        return onScoreClickListener;
    }

    @Override
    public void onClick(View v) {
        incCount();
        onScoreClickListener.OnScoreClick(this, rotiScore);
    }

    private void updateBadge(){
        if (count>0){
            badgeView.setText(String.valueOf(count));
            badgeView.show(true);
        } else {
            badgeView.hide(true);
        }

    }
}
