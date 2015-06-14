package com.wemanity.scrumbox.android.gui.base;

import com.wemanity.scrumbox.android.db.entity.Entity;

public interface OnEntityChangeListener<E extends Entity>{
    void onEntityChange(EntityAction action, E entity);
}