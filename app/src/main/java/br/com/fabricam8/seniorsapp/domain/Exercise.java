package br.com.fabricam8.seniorsapp.domain;

import android.content.ContentValues;

import java.util.Calendar;
import java.util.Date;

import br.com.fabricam8.seniorsapp.ExerciseFormActivity;

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
        if (getEndDate() == null
                && getEndDate().compareTo(new Date(0)) == 0)
            return AlertEvent.FOREVER;

        int days = diffInDays(startDate, endDate);
        // todo verify marked days
        return days;
    }


    @Override
    public int hashCode() {
        return (int) getID() * getStartDate().hashCode() * getName().length();
    }


    private int diffInDays(Date d1, Date d2) {
        int MILLIS_IN_DAY = 86400000;

        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        c1.set(Calendar.MILLISECOND, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.HOUR_OF_DAY, 0);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        c2.set(Calendar.MILLISECOND, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.HOUR_OF_DAY, 0);
        return (int) ((c1.getTimeInMillis() - c2.getTimeInMillis()) / MILLIS_IN_DAY);
    }
}




