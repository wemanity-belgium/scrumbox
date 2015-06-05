package com.wemanity.scrumbox.android.gui.daily;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.inject.Inject;
import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.db.dao.impl.DailyDao;
import com.wemanity.scrumbox.android.db.entity.Daily;
import com.wemanity.scrumbox.android.db.entity.Member;
import com.wemanity.scrumbox.android.gui.FragmentHelper;
import com.wemanity.scrumbox.android.gui.RootFragment;
import com.wemanity.scrumbox.android.gui.base.BaseFragment;
import com.wemanity.scrumbox.android.gui.base.EntityAction;
import com.wemanity.scrumbox.android.gui.base.OnEntityChangeListener;
import com.wemanity.scrumbox.android.gui.member.MemberEditDialog;
import com.wemanity.scrumbox.android.gui.member.MemberMainFragment;
import com.wemanity.scrumbox.android.gui.member.MemberSwipeAdapter;

import java.util.List;

import roboguice.inject.InjectView;

public class DailyMainFragment extends BaseFragment implements View.OnClickListener, OnEntityChangeListener<Daily> {
    public static final String TAG = "DailyMainFragment";

    @Inject
    private DailyDao dailyDao;

    @InjectView(R.id.dailyListView) private ListView dailyListView;

    @InjectView(R.id.addDailyFAB) private FloatingActionButton dailyFAB;

    @InjectView(R.id.dailyMainLayout) private RelativeLayout dailyLayout;

    private DailySwipeAdapter dailyAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dailyAdapter = new DailySwipeAdapter(getActivity());
        dailyAdapter.setOnEntityChangeListener(new OnEntityChangeListener<Daily>() {
            @Override
            public void onEntityChange(EntityAction action, Daily entity) {
                switch (action){
                    case UPDATE:
                        showEditDialog(entity);
                        break;
                    case DELETE:
                        dailyAdapter.remove(entity);
                        dailyDao.delete(entity);
                        break;
                    case START_DAILY:
                        Bundle args = new Bundle();
                        args.putSerializable("daily", entity);
                        FragmentHelper.switchFragment(getActivity().getFragmentManager(), DailyProgressFragment.class, R.id.fragment_frame_layout, args);
                        break;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_main_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dailyFAB.setOnClickListener(this);
        List<Daily> dailies = dailyDao.queryBuilder().list();

        dailyAdapter.setMode(Attributes.Mode.Single);
        dailyAdapter.clear();
        dailyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SwipeLayout)(dailyListView.getChildAt(position - dailyListView.getFirstVisiblePosition()))).open(true);
            }
        });
        dailyListView.setAdapter(dailyAdapter);
        dailyAdapter.addAll(dailies);
    }

    @Override
    public void onClick(View v) {
       showEditDialog(null);
    }

    private void showEditDialog(Daily daily){
        DailyEditDialog dailyDialog = DailyEditDialog.newInstance(daily);
        dailyDialog.setShowsDialog(false);
        dailyDialog.setOnEntityChangeListener(this);
        dailyDialog.show(getActivity().getFragmentManager(), "editDailyDialog");
    }

    @Override
    public Class<? extends BaseFragment> getPreviusFragment() {
        return RootFragment.class;
    }

    @Override
    public void onEntityChange(EntityAction action, Daily entity) {
        switch (action){
            case INSERT:
                dailyAdapter.add(entity);
                break;
            case UPDATE:
                dailyAdapter.switchObjectById(entity);
                break;
        }
    }

    /*class MyDragListener implements View.OnDragListener {
        //Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
        //Drawable normalShape = getResources().getDrawable(R.drawable.shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //v.setBackgroundDrawable(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //v.setBackgroundDrawable(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup

                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    RelativeLayout container = (RelativeLayout) v;
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //v.setBackgroundDrawable(normalShape);
                default:
                    break;
            }
            return true;
        }
    }
    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }*/
}
