package br.com.fabricam8.seniorsapp.domain;

import android.content.ContentValues;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by laecy_000 on 22/02/2015.
 */
public class Exercise extends DbEntity {

    // Entity Columns names
    public static final String KEY_START_DATE = "start_date";
    public static final String KEY_END_DATE = "end_date";
    public static final String KEY_MONDAY    = "monday";
    public static final String KEY_SUNDAY    = "sunday";
    public static final String KEY_TUESDAY   = "tuesday";
    public static final String KEY_WEDNESDAY    = "wednesday";
    public static final String KEY_THURSDAY = "thursday";
    public static final String KEY_FRIDAY = "friday";
    public static final String KEY_SATURDAY = "saturday";


    // Entity attributes
    private Date startDate;             // data inicial
    private Date endDate;               // data final
    private boolean repeatOnMonday;     // Se repete ou não nesse dia
    private boolean repeatOnTuesday;    // Se repete ou não nesse dia
    private boolean repeatOnWednesday;  // Se repete ou não nesse dia
    private boolean repeatOnThursday;   // Se repete ou não nesse dia
    private boolean repeatOnFriday;     // Se repete ou não nesse dia
    private boolean repeatOnSaturday;   // Se repete ou não nesse dia
    private boolean repeatOnSunday;     // Se repete ou não nesse dia

    // constructors
    public Exercise() {
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(KEY_CLOUD_ID, getCloudId());
        values.put(KEY_NAME, getName());
        values.put(KEY_START_DATE, getStartDate().getTime());

        if(getEndDate()== null)
        {
            values.put(KEY_END_DATE," ");
        }
        values.put(KEY_SUNDAY, isRepeatOnSunday());
        values.put(KEY_MONDAY, isRepeatOnMonday());
        values.put(KEY_TUESDAY, isRepeatOnTuesday());
        values.put(KEY_WEDNESDAY, isRepeatOnWednesday());
        values.put(KEY_THURSDAY, isRepeatOnThursday());
        values.put(KEY_FRIDAY, isRepeatOnFriday());
        values.put(KEY_SATURDAY, isRepeatOnSaturday());
        return values;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isRepeatOnMonday() {
        return repeatOnMonday;
    }

    public void setRepeatOnMonday(boolean repeatOnMonday) {
        this.repeatOnMonday = repeatOnMonday;
    }

    public boolean isRepeatOnTuesday() {
        return repeatOnTuesday;
    }

    public void setRepeatOnTuesday(boolean repeatOnTuesday) {
        this.repeatOnTuesday = repeatOnTuesday;
    }

    public boolean isRepeatOnWednesday() {
        return repeatOnWednesday;
    }

    public void setRepeatOnWednesday(boolean repeatOnWednesday) {
        this.repeatOnWednesday = repeatOnWednesday;
    }

    public boolean isRepeatOnThursday() {
        return repeatOnThursday;
    }

    public void setRepeatOnThursday(boolean repeatOnThursday) {
        this.repeatOnThursday = repeatOnThursday;
    }

    public boolean isRepeatOnFriday() {
        return repeatOnFriday;
    }

    public void setRepeatOnFriday(boolean repeatOnFriday) {
        this.repeatOnFriday = repeatOnFriday;
    }

    public boolean isRepeatOnSaturday() {
        return repeatOnSaturday;
    }

    public void setRepeatOnSaturday(boolean repeatOnSaturday) {
        this.repeatOnSaturday = repeatOnSaturday;
    }

    public boolean isRepeatOnSunday() {
        return repeatOnSunday;
    }

    public void setRepeatOnSunday(boolean repeatOnSunday) {
        this.repeatOnSunday = repeatOnSunday;
    }


    public int getNumOfAlarms() {
        if (getEndDate() == null)
            return AlertEvent.FOREVER;

        int days = (int)((startDate.getTime() - endDate.getTime()) / (1000 * 60 * 60 * 24));
        // todo verify marked days
        return days;
    }


    @Override
    public int hashCode() {
        return (int) getID() * getStartDate().hashCode() * getName().length();
    }
}




