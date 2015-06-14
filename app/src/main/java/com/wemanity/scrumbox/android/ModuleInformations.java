package com.wemanity.scrumbox.android;

import android.graphics.drawable.Drawable;

public interface ModuleInformations {
	String getModuleName();
	int getTitleResourceId();
	String getTitle();
	Drawable getLogoDrawable();
	int getLogoDrawableID();
	int getModuleColorID();
	int getModuleColor();
}
