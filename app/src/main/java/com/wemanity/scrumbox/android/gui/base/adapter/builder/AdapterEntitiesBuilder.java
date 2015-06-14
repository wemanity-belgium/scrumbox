package com.wemanity.scrumbox.android.gui.base.adapter.builder;

import com.wemanity.scrumbox.android.db.entity.Entity;

import java.util.Collection;

public interface AdapterEntitiesBuilder <E extends Entity, B>{
	B entities(E[] entities);
	B entities(Collection<E> entities);
}
