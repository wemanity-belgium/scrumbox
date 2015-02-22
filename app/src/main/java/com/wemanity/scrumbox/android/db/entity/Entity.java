package com.wemanity.scrumbox.android.db.entity;

import java.io.Serializable;

import de.greenrobot.dao.Property;

public interface Entity extends Serializable{
    Long getId();
    //String getProperty(Property property);
}
