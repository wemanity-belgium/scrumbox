package com.wemanity.scrumbox.android.bo;

import com.wemanity.scrumbox.android.db.entity.Daily;
import com.wemanity.scrumbox.android.db.entity.Participant;
import com.wemanity.scrumbox.android.db.entity.Participation;

import java.util.ArrayList;
import java.util.List;

public class DailyInProgress {
    private Daily daily;
    private List<Participant> participants;
    private List<Participation> participations;

    private DailyInProgress(Builder builder) {
        setDaily(builder.daily);
        setParticipants(builder.participants);
    }

    public Daily getDaily() {
        return daily;
    }

    public void setDaily(Daily daily) {
        this.daily = daily;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public void addParticipant(Participant participant) {
        if (participants == null){
            participants = new ArrayList<>();
        }
        this.participants.add(participant);
    }

    public void addParticipion(Participant participant, int time) {
        if (participations == null){
            participations = new ArrayList<>();
        }
        Participation participation = new Participation();
        participation.setParticipant(participant);
        participation.setPersonaltime(time);
        this.participations.add(participation);
        //participants.remove(participation.get)
    }

    public static Builder create(){
        return new Builder();
    }

    public static final class Builder {
        private Daily daily;
        private List<Participant> participants;

        public Builder() {
        }

        public Builder daily(Daily daily) {
            this.daily = daily;
            return this;
        }

        public Builder participants(List<Participant> participants) {
            this.participants = participants;
            return this;
        }

        public Builder addParticipant(Participant participant) {
            if (participants == null){
                participants = new ArrayList<>();
            }
            this.participants.add(participant);
            return this;
        }

        public DailyInProgress build() {
            return new DailyInProgress(this);
        }
    }
}
