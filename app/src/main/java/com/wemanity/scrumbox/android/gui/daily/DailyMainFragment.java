package com.wemanity.scrumbox.android.gui.daily;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.inject.Inject;
import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.db.dao.impl.DailyDao;
import com.wemanity.scrumbox.android.db.entity.Daily;
import com.wemanity.scrumbox.android.gui.FragmentHelper;
import com.wemanity.scrumbox.android.gui.RootFragment;
import com.wemanity.scrumbox.android.gui.SubMenuOnScrollListener;
import com.wemanity.scrumbox.android.gui.base.BaseFragment;
import com.wemanity.scrumbox.android.gui.base.EntityAction;
import com.wemanity.scrumbox.android.gui.base.OnEntityChangeListener;
import com.wemanity.scrumbox.android.gui.base.adapter.SubMenuAdapter;

import java.util.Collection;

import roboguice.inject.InjectView;

public class DailyMainFragment extends BaseFragment implements View.OnClickListener, OnEntityChangeListener<Daily> {
    public static final String TAG = "DailyMainFragment";

    @Inject
    private DailyDao dailyDao;

    @InjectView(R.id.dailyListView)
    private ListView dailyListView;

    @InjectView(R.id.addDailyFAB)
    private FloatingActionButton dailyFAB;

    @InjectView(R.id.dailyMainLayout)
    private RelativeLayout dailyLayout;

    private SubMenuAdapter<Daily> entityAdapter;

    //private DailySwipeAdapter dailyAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        Collection<Daily> dailies = dailyDao.queryBuilder().list();
        /*dailies.add(Daily.newInstance().title("Daily 1").build());
        dailies.add(Daily.newInstance().title("Daily 2").build());
        dailies.add(Daily.newInstance().title("Daily 3").build());
        dailies.add(Daily.newInstance().title("Daily 4").build());
        dailies.add(Daily.newInstance().title("Daily 5").build());
        dailies.add(Daily.newInstance().title("Daily 6").build());
        dailies.add(Daily.newInstance().title("Daily 7").build());
        dailies.add(Daily.newInstance().title("Daily 8").build());
        dailies.add(Daily.newInstance().title("Daily 9").build());
        dailies.add(Daily.newInstance().title("Daily 10").build());
        dailies.add(Daily.newInstance().title("Daily 11").build());
        dailies.add(Daily.newInstance().title("Daily 12").build());*/

        entityAdapter = SubMenuAdapter.<Daily>newBuilder()
                .drawables(new int[]{R.drawable.daily_start,
                        R.drawable.daily_chart,
                        R.drawable.daily_delete,
                        R.drawable.daily_edit})
                .entityActions(new EntityAction[]{EntityAction.START_DAILY, EntityAction.SHOW_CHART, EntityAction.DELETE, EntityAction.SHOW_EDIT_DIALOG})
                .activity(getActivity())
                .layoutId(R.layout.daily_listview_row_layout)
                .entities(dailies)
                .viewIds(new int[]{R.id.dailyListviewTitle})
                .properties(new String[]{"title"})
                .changeListener(this)
                .build();
        dailyListView.setAdapter(entityAdapter);
        dailyListView.setOnScrollListener(new SubMenuOnScrollListener(entityAdapter));
    }

    @Override
    public void onClick(View v) {
       showDailyEditDialog();
    }

    private void showDailyEditDialog(Daily daily){
        Bundle args = new Bundle();
        args.putSerializable("entity", daily);
        FragmentHelper.switchFragment(getActivity(), DailyEditDialog.class, R.id.fragmentFrameLayout, args);
    }

    private void showDailyEditDialog(){
        showDailyEditDialog(null);
    }

    @Override
    public Class<? extends BaseFragment> getPreviusFragment() {
        return RootFragment.class;
    }

    @Override
    public void onEntityChange(EntityAction action, Daily entity) {
        switch (action){
            case CREATE:
                entityAdapter.add(entity);
                break;
            case SHOW_CREATION_DIALOG:
                showDailyEditDialog(null);
                break;
            case SHOW_EDIT_DIALOG:
                showDailyEditDialog(entity);
                break;
            case START_DAILY:
                Bundle args = new Bundle();
                args.putSerializable("daily", entity);
                FragmentHelper.switchFragment(getActivity(), DailyProgressFragment.class, R.id.fragmentFrameLayout, args);
                break;
            case DELETE:
                entityAdapter.remove(entity);
                dailyDao.delete(entity);
                break;
            case REPLACE:
                //entityAdapter.switchObjectById(entity);
                break;
            default:
                throw new RuntimeException("Action not implemented yet : "+ action);
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
