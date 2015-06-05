package com.wemanity.scrumbox.android.gui.base;

import com.wemanity.scrumbox.android.db.entity.Entity;

public interface OnEntityChangeListener<T extends Entity>{
    void onEntityChange(EntityAction action, T entity);
}