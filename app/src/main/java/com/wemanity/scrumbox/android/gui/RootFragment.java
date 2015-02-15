package com.wemanity.scrumbox.android.gui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.gui.base.BaseFragment;
import com.wemanity.scrumbox.android.gui.daily.DailyMainFragment;
import com.wemanity.scrumbox.android.gui.member.MemberMainFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

public class RootFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    public static final String TAG = "RootFragment";
    @InjectView(R.id.moduleListView) private ListView moduleListView;
    @InjectView(R.id.recentActivityListView) private ListView recentActivitiesListView;
    @InjectResource(R.array.list_module) private String[] listModule;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.root_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        moduleListView.setAdapter(createModuleViewAdapter());
        moduleListView.setOnItemClickListener(this);
        recentActivitiesListView.setAdapter(createRecentActivitiesAdapterDumpData());
    }

    private SimpleAdapter createModuleViewAdapter(){
        String[] from = {"moduleName"};
        int[] to = {R.id.moduleNameTextView};
        List<Map<String, String>> fillMaps = new ArrayList<Map<String, String>>();
        for(String moduleName : listModule){
            Map<String,String> moduleData = new HashMap<>(1);
            moduleData.put("moduleName",moduleName);
            fillMaps.add(moduleData);
        }
        return new SimpleAdapter(this.getActivity(),fillMaps, R.layout.module_listview_layout, from,to);
    }

    private SimpleAdapter createRecentActivitiesAdapterDumpData(){
        String[] from = {"recentActivity"};
        int[] to = {R.id.recentActivityTextView};
        List<Map<String, String>> fillMaps = new ArrayList<Map<String, String>>();
        for(int i = 0; i<20;i++){
            Map<String,String> moduleData = new HashMap<>(1);
            moduleData.put("recentActivity","Activity : "+i);
            fillMaps.add(moduleData);
        }
        return new SimpleAdapter(this.getActivity(),fillMaps, R.layout.recent_activities_listview_layout, from,to);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)   {
        switch(listModule[position]){
            case "Daily":
                    //switchFragment(DailyMainFragment.class, DailyMainFragment.TAG);
                getActivity().getFragmentManager().beginTransaction().hide(this).add(R.id.fragment_frame_layout, new DailyMainFragment()).commit();
                //switchFragment(DailyMainFragment.class);
                break;
            case "Member":
                switchFragment(MemberMainFragment.class);
                //getActivity().getFragmentManager().beginTransaction().hide(this).add(R.id.fragment_frame_layout, new MemberMainFragment()).commit();
                break;
        }
    }
}
