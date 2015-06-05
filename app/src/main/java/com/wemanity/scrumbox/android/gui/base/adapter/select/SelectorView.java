package com.wemanity.scrumbox.android.gui.base.adapter.select;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.wemanity.scrumbox.android.db.entity.Entity;

public class SelectorView <T extends Entity> extends LinearLayout {

    public interface EntitySelectionChangeListener<T extends Entity>{
        void onEntitySelectionChange(View v, T entity, boolean selected);
    }

    private T linkedEntity;
    private final View layoutView;
    private final CheckBox checkBox;
    private EntitySelectionChangeListener<T> onSelectionChangeListener;

    public SelectorView(Context context, View layoutView) {
        super(context);
        this.setMinimumHeight(100);

        this.setOrientation(HORIZONTAL);
        this.addView(layoutView, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        this.layoutView = layoutView;
        checkBox = new CheckBox(context);
        checkBox.setText(null);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (SelectorView.this.onSelectionChangeListener != null) {
                    SelectorView.this.onSelectionChangeListener.onEntitySelectionChange(SelectorView.this, SelectorView.this.linkedEntity, isChecked);
                }
            }
        });
        checkBox.setGravity(Gravity.CENTER_VERTICAL);
        this.addView(checkBox, new LinearLayout.LayoutParams(100, 100));
    }

    public T getLinkedEntity() {
        return linkedEntity;
    }

    public void setLinkedEntity(T linkedEntity) {
        this.linkedEntity = linkedEntity;
    }

    public boolean isChecked(){
        return checkBox.isChecked();
    }

    public void setChecked(boolean checked){
        checkBox.setChecked(checked);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        checkBox.setOnClickListener(l);
    }

    public EntitySelectionChangeListener<T> getOnSelectionChangeListener() {
        return onSelectionChangeListener;
    }

    public void setOnSelectionChangeListener(EntitySelectionChangeListener<T> onSelectionChangeListener) {
        this.onSelectionChangeListener = onSelectionChangeListener;
    }
}