package com.wemanity.scrumbox.android.gui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.db.entity.Entity;
import com.wemanity.scrumbox.android.gui.FragmentHelper;
import com.wemanity.scrumbox.android.gui.daily.DailyEditDialog;

import roboguice.fragment.RoboFragment;
import roboguice.fragment.provided.RoboDialogFragment;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

public abstract class AbstractEntityEditDialog<T extends Entity> extends BaseFragment implements View.OnClickListener{

    @InjectView(R.id.negativeButton)
    private TextView cancelButton;

    @InjectView(R.id.positiveButton)
    private TextView saveButton;

    @InjectResource(R.string.action_save)
    private String save;

    @InjectResource(R.string.action_insert)
    private String insert;

    private OnEntityChangeListener<T> onEntityChangeListener;

    private boolean insertMode;

    private T entity;

    @Override
    @SuppressWarnings("unchecked")
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey("entity")) {
            entity = (T) args.get("entity");
        }
        insertMode = (entity == null);
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    protected abstract int getLayoutId();

    @Override
    public final void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView(view, savedInstanceState);
        if (!insertMode){
            bindEntity(view,savedInstanceState,entity);
        }
        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        saveButton.setText(insertMode ? insert : save);
        //getDialog().setTitle(getTitle(insertMode, entity));
    }

    protected abstract String getTitle(boolean insertMode, T entity);

    protected abstract void initializeView(View view, Bundle savedInstanceState);

    protected abstract void bindEntity(View view, Bundle savedInstanceState, T entity);

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.positiveButton:
                if (valideInput()){
                    if (insertMode) {
                        entity = insert();
                    } else{
                        update(entity);
                    }
                    notifyEntityChangeListener();
	                FragmentHelper.switchFragment(getActivity(), getPreviusFragment(), R.id.fragmentFrameLayout);
                }
                break;
            case R.id.negativeButton:
	            FragmentHelper.switchFragment(getActivity(),getPreviusFragment(), R.id.fragmentFrameLayout);
                break;
        }
    }

    protected abstract boolean valideInput();

    protected abstract T insert();

    protected abstract void update(T entity);

    private void notifyEntityChangeListener(){
        if (onEntityChangeListener != null){
            onEntityChangeListener.onEntityChange(insertMode? EntityAction.CREATE : EntityAction.REPLACE, entity);
        }
    }

    public OnEntityChangeListener<T> getOnEntityChangeListener() {
        return onEntityChangeListener;
    }

    public void setOnEntityChangeListener(OnEntityChangeListener<T> onEntityChangeListener) {
        this.onEntityChangeListener = onEntityChangeListener;
    }

}