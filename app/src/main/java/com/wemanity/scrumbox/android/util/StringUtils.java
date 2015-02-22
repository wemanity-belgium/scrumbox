package com.wemanity.scrumbox.android.util;

import android.widget.EditText;

public class StringUtils {

    public static String clear(String s){
        s = s.trim();
        s = s.replaceAll("\n", "");
        s = s.replaceAll("\r", "");
        return s;
    }

    public static String clear(EditText edit){
        String s = edit.getText().toString();
        return clear(s);
    }
}
