package com.wemanity.scrumbox.android.gui;

import android.widget.EditText;

import com.wemanity.scrumbox.android.R;
import com.wemanity.scrumbox.android.db.dao.impl.MemberDao;
import com.wemanity.scrumbox.android.util.StringUtils;

import de.greenrobot.dao.query.WhereCondition;

public class ViewUtils {
	public static boolean checkEditText(EditText editText, String errorMessage){
		String text = StringUtils.clear(editText);
		if (text.isEmpty()){
			setErrorEditText(editText, errorMessage);
			return false;
		}
		return true;
	}

	public static void setErrorEditText(EditText editText, String errorMessage){
		editText.setError(errorMessage);
		editText.setSelection(0, editText.getText().length());
	}
}
