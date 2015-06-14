package com.wemanity.scrumbox.android;

import android.content.Context;

public class MemberInformation extends CommonModuleInformation {

	public MemberInformation(Context context){
		super(context);
	}

	@Override
	public String getModuleName() {
		return "member";
	}

	@Override
	public int getTitleResourceId() {
		return R.string.module_name_member;
	}

	@Override
	public int getLogoDrawableID() {
		return R.drawable.member_logo;
	}

	@Override
	public int getModuleColorID() {
		return R.color.DarkViolet;
	}
}
