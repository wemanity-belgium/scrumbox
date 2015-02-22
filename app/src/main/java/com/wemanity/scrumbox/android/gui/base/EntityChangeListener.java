package com.wemanity.scrumbox.android.gui.base;

import com.wemanity.scrumbox.android.db.entity.Entity;

public interface EntityChangeListener<T extends Entity> {

    void onEntityChange(T entity);
}
