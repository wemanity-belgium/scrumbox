package com.wemanity.scrumbox.android.gui.base.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.greenrobot.dao.Property;

public class RowVewHelper {
    public List<RowView> convert(int[] viewIds, Property[] entityProperties){
        if (viewIds.length != entityProperties.length){

        }
        List<RowView> rowViews = new ArrayList<>(viewIds.length);
        boolean added = true;
        for(int i = 0; i<viewIds.length-1; i++){
            rowViews.add(new RowView(viewIds[i], entityProperties[i]));
        }
        return rowViews;
    }
}
