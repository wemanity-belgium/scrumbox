package com.wemanity.scrumbox.android;

import android.content.Context;

public class DailyInformation extends CommonModuleInformation {

	public DailyInformation(Context context){
		super(context);
	}

	@Override
	public String getModuleName() {
		return "daily";
	}

	@Override
	public int getTitleResourceId() {
		return R.string.module_name_daily;
	}

	@Override
	public int getLogoDrawableID() {
		return R.drawable.daily_timer_logo;
	}

	@Override
	public int getModuleColorID() {
		return R.color.DodgerBlue;
	}
}
