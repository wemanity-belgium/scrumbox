package com.wemanity.scrumbox.android;

import android.app.Application;

public class ScrumApplication extends Application {
    private ModuleInformations daily;
    private ModuleInformations member;
    private ModuleInformations roti;

    @Override
    public void onCreate() {
        super.onCreate();
        daily = new DailyInformation(this);
        member = new MemberInformation(this);
        roti = new RotiInformation(this);
    }

    public ModuleInformations getDailyModule(){
        return daily;
    }

    public ModuleInformations getMemberModule(){
        return member;
    }

    public ModuleInformations getRotiModule(){
        return roti;
    }

    public ModuleInformations getScrumPokerModule(){
        return null;
    }
}
