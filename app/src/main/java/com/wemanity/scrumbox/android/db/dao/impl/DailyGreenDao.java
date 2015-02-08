package com.wemanity.scrumbox.android.db.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.wemanity.scrumbox.android.db.dao.DaoSession;
import com.wemanity.scrumbox.android.db.entity.Daily;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table DAILY.
*/
public class DailyGreenDao extends AbstractDao<Daily, Long> implements com.wemanity.scrumbox.android.db.dao.DailyDao {

    public static final String TABLENAME = "DAILY";

    /**
     * Properties of entity Daily.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Durationbyparticipant = new Property(2, Integer.class, "durationbyparticipant", false, "DURATIONBYPARTICIPANT");
        public final static Property Triggermethod = new Property(3, Integer.class, "triggermethod", false, "TRIGGERMETHOD");
        public final static Property Delaybetweenswitch = new Property(4, Integer.class, "delaybetweenswitch", false, "DELAYBETWEENSWITCH");
        public final static Property Random = new Property(5, Boolean.class, "random", false, "RANDOM");
    };

    private DaoSession daoSession;


    public DailyGreenDao(DaoConfig config) {
        super(config);
    }
    
    public DailyGreenDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'DAILY' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'TITLE' TEXT," + // 1: title
                "'DURATIONBYPARTICIPANT' INTEGER," + // 2: durationbyparticipant
                "'TRIGGERMETHOD' INTEGER," + // 3: triggermethod
                "'DELAYBETWEENSWITCH' INTEGER," + // 4: delaybetweenswitch
                "'RANDOM' INTEGER);"); // 5: random
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'DAILY'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Daily entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        Integer durationbyparticipant = entity.getDurationbyparticipant();
        if (durationbyparticipant != null) {
            stmt.bindLong(3, durationbyparticipant);
        }
 
        Integer triggermethod = entity.getTriggermethod();
        if (triggermethod != null) {
            stmt.bindLong(4, triggermethod);
        }
 
        Integer delaybetweenswitch = entity.getDelaybetweenswitch();
        if (delaybetweenswitch != null) {
            stmt.bindLong(5, delaybetweenswitch);
        }
 
        Boolean random = entity.getRandom();
        if (random != null) {
            stmt.bindLong(6, random ? 1l: 0l);
        }
    }

    @Override
    protected void attachEntity(Daily entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Daily readEntity(Cursor cursor, int offset) {
        Daily entity = new Daily( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // durationbyparticipant
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // triggermethod
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // delaybetweenswitch
            cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0 // random
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Daily entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDurationbyparticipant(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setTriggermethod(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setDelaybetweenswitch(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setRandom(cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Daily entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Daily entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
