package com.wemanity.scrumbox.android.db.dao.impl;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.wemanity.scrumbox.android.db.dao.DaoSession;
import com.wemanity.scrumbox.android.db.entity.Participant;
import com.wemanity.scrumbox.android.db.entity.Participation;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PARTICIPATION.
*/
public class ParticipationDao extends AbstractDao<Participation, Long> {


    public static final String TABLENAME = "PARTICIPATION";

    /**
     * Properties of entity Participation.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Time = new Property(1, Long.class, "time", false, "TIME");
        public final static Property Participantid = new Property(2, long.class, "participantid", false, "PARTICIPANTID");
        public final static Property Dailyoccurrenceid = new Property(3, long.class, "dailyoccurrenceid", false, "DAILYOCCURRENCEID");
    };

    private DaoSession daoSession;

    private Query<Participation> dailyOccurrence_ParticipationsQuery;

    public ParticipationDao(DaoConfig config) {
        super(config);
    }

    public ParticipationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PARTICIPATION' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'TIME' INTEGER," + // 1: time
                "'PARTICIPANTID' INTEGER NOT NULL ," + // 2: participantid
                "'DAILYOCCURRENCEID' INTEGER NOT NULL );"); // 3: dailyoccurrenceid
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PARTICIPATION'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Participation entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }

        Long time = entity.getTime();
        if (time != null) {
            stmt.bindLong(2, time);
        }
        stmt.bindLong(3, entity.getParticipantid());
        stmt.bindLong(4, entity.getDailyoccurrenceid());
    }

    @Override
    protected void attachEntity(Participation entity) {
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
    public Participation readEntity(Cursor cursor, int offset) {
        Participation entity = new Participation( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // time
                cursor.getLong(offset + 2), // participantid
                cursor.getLong(offset + 3) // dailyoccurrenceid
        );
        return entity;
    }

    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Participation entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTime(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setParticipantid(cursor.getLong(offset + 2));
        entity.setDailyoccurrenceid(cursor.getLong(offset + 3));
    }

    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Participation entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    /** @inheritdoc */
    @Override
    public Long getKey(Participation entity) {
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

    /** Internal query to resolve the "participations" to-many relationship of DailyOccurrence. */
    public List<Participation> _queryDailyOccurrence_Participations(long dailyoccurrenceid) {
        synchronized (this) {
            if (dailyOccurrence_ParticipationsQuery == null) {
                QueryBuilder<Participation> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Dailyoccurrenceid.eq(null));
                dailyOccurrence_ParticipationsQuery = queryBuilder.build();
            }
        }
        Query<Participation> query = dailyOccurrence_ParticipationsQuery.forCurrentThread();
        query.setParameter(0, dailyoccurrenceid);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getParticipantDao().getAllColumns());
            builder.append(" FROM PARTICIPATION T");
            builder.append(" LEFT JOIN PARTICIPANT T0 ON T.'PARTICIPANTID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }

    protected Participation loadCurrentDeep(Cursor cursor, boolean lock) {
        Participation entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Participant participant = loadCurrentOther(daoSession.getParticipantDao(), cursor, offset);
        if(participant != null) {
            entity.setParticipant(participant);
        }

        return entity;
    }

    public Participation loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();

        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);

        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }

    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Participation> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Participation> list = new ArrayList<Participation>(count);

        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }

    protected List<Participation> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }


    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Participation> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
