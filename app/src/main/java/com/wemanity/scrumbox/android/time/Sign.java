package com.wemanity.scrumbox.android.time;

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

    public static Sign getSign(long numericValue){
        return numericValue > 0 ? Sign.POSITIVE : Sign.NEGATIVE;
    }
}