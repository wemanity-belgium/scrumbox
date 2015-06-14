package com.wemanity.scrumbox.android;

import android.content.Context;
import android.graphics.drawable.Drawable;

public abstract class CommonModuleInformation implements ModuleInformations {
	protected Context context;
	protected String title;
	protected Drawable logo;
	protected int color;
	protected CommonModuleInformation(Context context){
		this.context = context;
		this.title = context.getString(getTitleResourceId());
		this.logo = context.getResources().getDrawable(getLogoDrawableID());
		this.color = context.getResources().getColor(getModuleColorID());
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public Drawable getLogoDrawable() {
		return logo;
	}

	@Override
	public int getModuleColor() {
		return color;
	}
}
