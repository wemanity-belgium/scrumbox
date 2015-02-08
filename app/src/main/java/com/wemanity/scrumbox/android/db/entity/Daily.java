package com.wemanity.scrumbox.android.db.entity;

import com.wemanity.scrumbox.android.db.dao.DaoSession;
import com.wemanity.scrumbox.android.db.dao.impl.DailyGreenDao;
import com.wemanity.scrumbox.android.db.dao.impl.ParticipantGreenDao;

import java.util.List;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table DAILY.
 */
public class Daily {

    private Long id;
    private String title;
    private Integer durationbyparticipant;
    private Integer triggermethod;
    private Integer delaybetweenswitch;
    private Boolean random;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient DailyGreenDao myDao;

    private List<Participant> participants;

    public Daily() {
    }

    public Daily(Long id) {
        this.id = id;
    }

    public Daily(Long id, String title, Integer durationbyparticipant, Integer triggermethod, Integer delaybetweenswitch, Boolean random) {
        this.id = id;
        this.title = title;
        this.durationbyparticipant = durationbyparticipant;
        this.triggermethod = triggermethod;
        this.delaybetweenswitch = delaybetweenswitch;
        this.random = random;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDailyDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDurationbyparticipant() {
        return durationbyparticipant;
    }

    public void setDurationbyparticipant(Integer durationbyparticipant) {
        this.durationbyparticipant = durationbyparticipant;
    }

    public Integer getTriggermethod() {
        return triggermethod;
    }

    public void setTriggermethod(Integer triggermethod) {
        this.triggermethod = triggermethod;
    }

    public Integer getDelaybetweenswitch() {
        return delaybetweenswitch;
    }

    public void setDelaybetweenswitch(Integer delaybetweenswitch) {
        this.delaybetweenswitch = delaybetweenswitch;
    }

    public Boolean getRandom() {
        return random;
    }

    public void setRandom(Boolean random) {
        this.random = random;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Participant> getParticipants() {
        if (participants == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ParticipantGreenDao targetDao = daoSession.getParticipantDao();
            List<Participant> participantsNew = targetDao._queryDaily_Participants(id);
            synchronized (this) {
                if(participants == null) {
                    participants = participantsNew;
                }
            }
        }
        return participants;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetParticipants() {
        participants = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
