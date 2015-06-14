package com.wemanity.scrumbox.android;

import android.content.Context;

public class RotiInformation extends CommonModuleInformation {

	public RotiInformation(Context context){
		super(context);
	}

	@Override
	public String getModuleName() {
		return "roti";
	}

	@Override
	public int getTitleResourceId() {
		return R.string.module_name_roti;
	}

	@Override
	public int getLogoDrawableID() {
		return R.drawable.roti_logo;
	}

	@Override
	public int getModuleColorID() {
		return R.color.Orange;
	}
}
