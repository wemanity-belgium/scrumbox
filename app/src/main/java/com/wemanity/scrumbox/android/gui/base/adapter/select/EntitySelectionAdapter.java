package com.wemanity.scrumbox.android.gui.base.adapter.select;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.wemanity.scrumbox.android.db.entity.Entity;
import com.wemanity.scrumbox.android.gui.base.adapter.EntityAdapter;
import com.wemanity.scrumbox.android.gui.base.adapter.select.SelectorView;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntitySelectionAdapter<T extends Entity> extends EntityAdapter<T> implements SelectorView.EntitySelectionChangeListener<T> {

    private final Set<T> entitiesSelected;

    public EntitySelectionAdapter(Context context, Collection<T> entities, int layoutId,int[] viewIds, String[] propetiesName){
        super(context, layoutId, entities, viewIds, propetiesName);
        this.entitiesSelected = new HashSet<>();
    }

    public EntitySelectionAdapter(Context context, Collection<T> entities, Collection<T> selectedEntities,int layoutId, int[] viewIds, String[] propetiesName){
        this(context,entities,layoutId,viewIds, propetiesName);
        if (selectedEntities != null && !selectedEntities.isEmpty()) {
            this.entitiesSelected.addAll(selectedEntities);
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SelectorView<T> v;
        if (convertView == null) {
            View layout = mInflater.inflate(mResource, parent, false);
            v = new SelectorView<>(mInflater.getContext(), layout);
            v.setOnSelectionChangeListener(this);
        } else {
            v = (SelectorView)convertView;
        }

        T entity = getItem(position);
        v.setLinkedEntity(entity);
        v.setChecked(entitiesSelected.contains(entity));

        bindView(entity, v);

        return v;
    }

    public Set<T> getSelectedEntities() {
        return Collections.unmodifiableSet(entitiesSelected);
    }

    public void addSelectedEntities(Collection<T> entitiesSelected){
        this.entitiesSelected.addAll(entitiesSelected);
    }

    @Override
    public void onEntitySelectionChange(View v, T entity, boolean selected) {
        if (selected){
            entitiesSelected.add(entity);
        } else {
            entitiesSelected.remove(entity);
        }
    }
}
