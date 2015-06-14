package com.wemanity.scrumbox.android.gui.base.adapter.builder;

import com.wemanity.scrumbox.android.db.entity.Entity;

public interface EntityAbapterBuilder<E extends Entity, R> extends AdapterActivityBuilder<AdapterLayoutIdBuilder<AdapterEntitiesBuilder<E, AdapterViewIdsBuilder<AdapterPropertiesBuilder<R>>>>> {
}
