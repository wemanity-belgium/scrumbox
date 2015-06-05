package com.wemanity.scrumbox.android.time;

public enum TriggerMethod {
    MANUAL(0),
    END_OF_TIME(1);

    private int id;

    TriggerMethod(int id){
        this.id = id;
    }

    public TriggerMethod valueOf(int id) throws Exception {
        switch(id){
            case 0:
                return MANUAL;
            case 1:
                return END_OF_TIME;
            default:
                throw new Exception();
        }
    }
}
