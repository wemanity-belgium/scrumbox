package com.wemanity.scrumbox.android.bo;

import com.wemanity.scrumbox.android.db.entity.Daily;
import com.wemanity.scrumbox.android.db.entity.Participant;
import com.wemanity.scrumbox.android.db.entity.Participation;

import java.util.ArrayList;
import java.util.List;

public class DailyInProgress {
    private Daily daily;
    private List<Participant> participants = new ArrayList<>();
    private List<Participation> participations = new ArrayList<>();

    private DailyInProgress(Builder builder) {
        participants = builder.participants;
        daily = builder.daily;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Daily getDaily() {
        return daily;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void addParticipant(Participant participant) {
        this.participants.add(participant);
    }

    public void addParticipation(Participant participant, long time) {
        Participation participation = new Participation();
        participation.setParticipant(participant);
        participation.setTime(time);
        this.participations.add(participation);
    }

    public static final class Builder {
        private Daily daily;
        private List<Participant> participants = new ArrayList<>();

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
            this.participants.add(participant);
            return this;
        }

        public DailyInProgress build() {
            return new DailyInProgress(this);
        }
    }
}
