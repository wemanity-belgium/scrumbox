package com.wemanity.scrumbox.android.gui.base.adapter;

import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.Property;

public class AdapterVewHelper {
    public static List<AdapterView> convert(int[] viewIds, String[] propetiesName){
        if (viewIds.length != propetiesName.length){

        }
        List<AdapterView> adapterViews = new ArrayList<>(viewIds.length);
        for(int i = 0; i<viewIds.length-1; i++){
            adapterViews.add(new AdapterView(viewIds[i], propetiesName[i]));
        }
        return adapterViews;
    }
}
