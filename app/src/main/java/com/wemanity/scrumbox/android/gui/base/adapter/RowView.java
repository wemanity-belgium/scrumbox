package com.wemanity.scrumbox.android.gui.base.adapter;

import de.greenrobot.dao.Property;

public class RowView {
    private int viewId;
    private Property entityProperty;
    public RowView(int viewId, Property entityProperty){
        this.viewId = viewId;
        this.entityProperty = entityProperty;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewIds) {
        this.viewId = viewIds;
    }

    public Property getEntityProperty() {
        return entityProperty;
    }

    public void setEntityProperty(Property entityProperty) {
        this.entityProperty = entityProperty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RowView rowView = (RowView) o;

        if (viewId != rowView.viewId) return false;
        if (entityProperty != null ? !entityProperty.equals(rowView.entityProperty) : rowView.entityProperty != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = viewId;
        result = 31 * result + (entityProperty != null ? entityProperty.hashCode() : 0);
        return result;
    }
}
