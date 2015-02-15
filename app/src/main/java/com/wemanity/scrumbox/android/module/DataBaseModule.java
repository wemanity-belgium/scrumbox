package com.wemanity.scrumbox.android.module;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.wemanity.scrumbox.android.db.dao.DaoMaster;
import com.wemanity.scrumbox.android.db.dao.DaoSession;
import com.wemanity.scrumbox.android.db.dao.impl.DailyDao;
import com.wemanity.scrumbox.android.db.dao.impl.DailyOccurrenceDao;
import com.wemanity.scrumbox.android.db.dao.impl.MemberDao;
import com.wemanity.scrumbox.android.db.dao.impl.ParticipantDao;
import com.wemanity.scrumbox.android.db.dao.impl.ParticipationDao;
import com.wemanity.scrumbox.android.db.dao.impl.RoleDao;
import com.wemanity.scrumbox.android.gui.base.FragmentSwitchListener;
import com.wemanity.scrumbox.android.gui.MainActivity;


public class DataBaseModule extends AbstractModule {

    private Application application;

    private DaoMaster daoMaster;

    private DaoSession daoSession;

    public DataBaseModule(Application application){
        this.application = application;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this.application, "scrumbox-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    @Override
    protected void configure() {
    }

    @Provides
    public DaoMaster getDaoMaster(){
        return daoMaster;
    }

    @Provides
    public DailyDao getDailyDao() {
        return daoSession.getDailyDao();
    }

    @Provides
    public RoleDao getRoleDao() {
        return daoSession.getRoleDao();
    }

    @Provides
    public MemberDao getMemberDao() {
        return daoSession.getMemberDao();
    }

    @Provides
    public ParticipantDao getParticipantDao() {
        return daoSession.getParticipantDao();
    }

    @Provides
    public DailyOccurrenceDao getDailyOccurrenceDao() {
        return daoSession.getDailyOccurrenceDao();
    }

    @Provides
    public ParticipationDao getParticipationDao() {
        return daoSession.getParticipationDao();
    }

}
