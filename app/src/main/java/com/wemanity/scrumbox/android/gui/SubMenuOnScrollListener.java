package com.wemanity.scrumbox.android.gui;

import android.widget.AbsListView;

import com.wemanity.scrumbox.android.gui.base.adapter.SubMenuAdapter;

public class SubMenuOnScrollListener implements AbsListView.OnScrollListener{

	private SubMenuAdapter subMenuAdapter;

	public SubMenuOnScrollListener(SubMenuAdapter subMenuAdapter){
		this.subMenuAdapter = subMenuAdapter;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		subMenuAdapter.closeSubMenu();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}
}
