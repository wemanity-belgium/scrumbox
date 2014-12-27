package com.wemanity.scrumbox.android;

public enum Sign {
    NEGATIVE("-"),
    POSITIVE("+"),
    NA("");

    private String mSymbole;

    Sign(String symbole){
        mSymbole = symbole;
    }

    @Override
    public String toString(){
        return mSymbole;
    }
}