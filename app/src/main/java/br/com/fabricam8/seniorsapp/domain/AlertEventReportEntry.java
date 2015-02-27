package br.com.fabricam8.seniorsapp.domain;

import android.content.ContentValues;

import java.util.Date;

import br.com.fabricam8.seniorsapp.enums.ReportResponseType;

/**
 * Created by Aercio on 1/27/15.
 */
public class AlertEventReportEntry extends DbEntity {

    // Entity Columns names
    public static final String KEY_EVENT = "event";
    public static final String KEY_ENTITY_CLASS = "entity_class";
    public static final String KEY_ENTITY_ID = "entity_id";
    public static final String KEY_REPORT_DATE = "report_date";
    public static final String KEY_EVENT_RESPONSE = "report_response";

    // Entity attributes
    private long entityId;
    private String event;
    private String entityClass;
    private Date reportDate;
    private ReportResponseType response;

    // ctor
    public AlertEventReportEntry() {
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put(KEY_ENTITY_ID, getEntityId());
        values.put(KEY_ENTITY_CLASS, getEntityClass());
        values.put(KEY_EVENT, getEvent());
        values.put(KEY_EVENT_RESPONSE, getResponse().getValue());
        values.put(KEY_REPORT_DATE, getReportDate().getTime());

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

    public ReportResponseType getResponse() {
        return response;
    }

    public void setResponse(ReportResponseType response) {
        this.response = response;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

}
