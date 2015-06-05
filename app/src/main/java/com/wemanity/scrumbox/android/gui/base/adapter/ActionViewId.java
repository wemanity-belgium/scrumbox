package com.wemanity.scrumbox.android.gui.base.adapter;

import com.wemanity.scrumbox.android.db.entity.Entity;
import com.wemanity.scrumbox.android.gui.base.EntityAction;

public class ActionViewId {
    private int id;
    private EntityAction action;

    public ActionViewId(int id, EntityAction action) {
        this.id = id;
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public EntityAction getAction() {
        return action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionViewId that = (ActionViewId) o;

        if (id != that.id) return false;
        if (action != that.action) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (action != null ? action.hashCode() : 0);
        return result;
    }
}
