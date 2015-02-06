package br.com.fabricam8.seniorsapp.domain;

import android.content.ContentValues;

import java.util.Date;

/**
 * Created by Aercio on 1/27/15.
 */
public class AlertEvent extends DbEntity {

    public static final int FOREVER = -1;

    // Entity Columns names
    public static final String KEY_EVENT = "event";
    public static final String KEY_ENTITY_CLASS = "entity_class";
    public static final String KEY_ENTITY_ID = "entity_id";
    public static final String KEY_NEXT_ALERT = "next_alert";
    public static final String KEY_MAX_ALARMS = "max_alarms";
    public static final String KEY_ALARMS_PLAYED = "alarms_played";

    // Entity attributes
    private long entityId;
    private String event;
    private String entityClass;
    private Date nextAlert;
    private int maxAlarms;
    private int alarmsPlayed;

    // ctor
    public AlertEvent() {
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put(KEY_ENTITY_ID, getEntityId());
        values.put(KEY_ENTITY_CLASS, getEntityClass());
        values.put(KEY_EVENT, getEvent());
        values.put(KEY_MAX_ALARMS, getMaxAlarms());
        values.put(KEY_ALARMS_PLAYED, getAlarmsPlayed());
        values.put(KEY_NEXT_ALERT, getNextAlert().getTime());

        return values;
    }

    // properties

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    public Date getNextAlert() {
        return nextAlert;
    }

    public void setNextAlert(Date nextAlert) {
        this.nextAlert = nextAlert;
    }

    public int getMaxAlarms() {
        return maxAlarms;
    }

    public void setMaxAlarms(int maxAlarms) {
        this.maxAlarms = maxAlarms;
    }

    public int getAlarmsPlayed() {
        return alarmsPlayed;
    }

    public void setAlarmsPlayed(int alarmsPlayed) {
        this.alarmsPlayed = alarmsPlayed;
    }
}
