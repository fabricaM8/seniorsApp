package br.com.fabricam8.domain;

import java.util.Date;

/**
 * Created by Aercio on 1/27/15.
 */
public abstract class AlertEvent extends DbEntity {

    private int cloudId;

    private String name;

    private Date nextAlert;


    // ctor
    public AlertEvent() {
    }


    public int getCloudId() {
        return cloudId;
    }

    public void setCloudId(int cloudId) {
        this.cloudId = cloudId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getNextAlert() {
        return nextAlert;
    }

    public void setNextAlert(Date nextAlert) {
        this.nextAlert = nextAlert;
    }

}
