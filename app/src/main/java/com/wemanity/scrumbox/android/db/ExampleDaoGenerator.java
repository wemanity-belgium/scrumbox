/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wemanity.scrumbox.android.db;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * 
 * Run it as a Java application (not Android).
 * 
 * @author Markus
 */
public class ExampleDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1000, "com.wemanity.scrumbox.android.db");

        addDaily(schema);
        //addNote(schema);
        //addCustomerOrder(schema);

        new DaoGenerator().generateAll(schema, "../scrumbox/app/src/main/java");
    }

    private static void addDaily(Schema schema){
        Entity daily = schema.addEntity("Daily");
        daily.addIdProperty();
        daily.addStringProperty("title");
        daily.addIntProperty("durationbyparticipant");
        daily.addIntProperty("switchmethod");
        daily.addBooleanProperty("random");
        daily.addDateProperty("time");

        Entity role = schema.addEntity("Role");
        role.addIdProperty();
        role.addStringProperty("name");
        role.addIntProperty("color");


        Entity member = schema.addEntity("Member");
        member.addIdProperty();
        member.addDateProperty("inscriptiondate");
        member.addStringProperty("nickname");
        member.addByteArrayProperty("image");

        Entity participant = schema.addEntity("Participant");
        participant.addIdProperty();
        Property memberId = participant.addLongProperty("memberid").notNull().getProperty();
        Property roleId = participant.addLongProperty("roleid").notNull().getProperty();
        Property dailyId = participant.addLongProperty("dailyid").notNull().getProperty();
        participant.addToOne(member, memberId);
        participant.addToOne(role,roleId);
        participant.addToOne(daily, dailyId);
        //Monday Tuesday Wednesday Thursday Friday Saturday Sunday
        participant.addBooleanProperty("excludemonday");
        participant.addBooleanProperty("excludethuesday");
        participant.addBooleanProperty("excludewednesday");
        participant.addBooleanProperty("excludethursday");
        participant.addBooleanProperty("excludefriday");
        participant.addBooleanProperty("excludesaturday");
        participant.addBooleanProperty("excludesunday");

        ToMany dailyToParticipant = daily.addToMany(participant, dailyId);
        dailyToParticipant.setName("participants");
        dailyToParticipant.orderAsc(roleId,memberId);

        Entity dailyOccurrence = schema.addEntity("DailyOccurrence");
        dailyOccurrence.addIdProperty();
        dailyOccurrence.addDateProperty("dateexecuted");
        dailyOccurrence.addLongProperty("totaltime");

        Entity participation = schema.addEntity("Participation");
        participation.addIdProperty();
        participation.addLongProperty("time");
        Property participantId = participation.addLongProperty("participantid").notNull().getProperty();
        Property dailyOccurrenceId = participation.addLongProperty("dailyoccurrenceid").notNull().getProperty();
        participation.addToOne(participant, participantId);

        ToMany dailyOccurrenceToParticipation = dailyOccurrence.addToMany(participation, dailyOccurrenceId);
        dailyOccurrenceToParticipation.setName("participations");

        Entity event = schema.addEntity("event");
        event.addIdProperty();
        event.addDateProperty("date");
        event.addStringProperty("type");
        event.addLongProperty("externalid");
    }

}
