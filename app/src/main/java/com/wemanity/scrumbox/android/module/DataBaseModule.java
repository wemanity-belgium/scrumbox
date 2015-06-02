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


public class DataBaseModule extends AbstractModule {

    private DaoMaster daoMaster;

    private DaoSession daoSession;

    public DataBaseModule(Application application){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(application, "scrumbox-db", null);
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
