package com.wemanity.scrumbox.android.gui.daily;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.daimajia.swipe.SwipeLayout;
import com.google.inject.Inject;
import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.db.dao.impl.MemberDao;
import com.wemanity.scrumbox.android.db.entity.Daily;
import com.wemanity.scrumbox.android.db.entity.Member;
import com.wemanity.scrumbox.android.gui.base.EntityAction;
import com.wemanity.scrumbox.android.gui.base.OnEntityChangeListener;
import com.wemanity.scrumbox.android.util.StringUtils;

import roboguice.fragment.provided.RoboDialogFragment;
import roboguice.inject.InjectView;

public class DeailyEditDialog  extends RoboDialogFragment implements View.OnClickListener{
    private OnEntityChangeListener<Daily> onEntityChangeListener;

    private boolean insertMode;

    @InjectView(R.id.dailyEditSwipeLayout)
    private SwipeLayout swipeLayout;

    private Daily daily;

    public static DeailyEditDialog newInstance(){
        DeailyEditDialog frag = new DeailyEditDialog();
        return frag;
    }

    public static DeailyEditDialog newInstance(Daily daily){
        DeailyEditDialog frag = new DeailyEditDialog();
        Bundle args = new Bundle();
        args.putSerializable("daily", daily);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey("daily")) {
            daily = (Daily) args.get("daily");
        }
        insertMode = (daily == null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_daily_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.positiveButton).setOnClickListener(this);
        init();
    }

    private void init(){

    }

    @Override
    public void onClick(View v) {
        swipeLayout.open(true);
    }

    private void notifyEntityChangeListener(){
        if (onEntityChangeListener != null){
            onEntityChangeListener.onEntityChange(insertMode? EntityAction.INSERT : EntityAction.UPDATE, daily);
        }
    }

    public OnEntityChangeListener<Daily> getOnEntityChangeListener() {
        return onEntityChangeListener;
    }

    public void setOnEntityChangeListener(OnEntityChangeListener<Daily> onEntityChangeListener) {
        this.onEntityChangeListener = onEntityChangeListener;
    }
}