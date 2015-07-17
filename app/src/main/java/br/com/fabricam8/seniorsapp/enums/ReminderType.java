package br.com.fabricam8.seniorsapp.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by laecy_000 on 22/02/2015.
 */
public enum ReminderType {

    NONE(0, ""),
    UMA_HORA_ANTES(1, "Uma hora antes"),
    DUAS_HORAS_ANTES(2, "Duas horas antes"),
    TRES_HORAS_ANTES(3, "Três horas antes"),
    UM_DIA_ANTES(4, "Um dia antes");

    private final int value;
    private final String name;

    private ReminderType(int v, String s) {
        value = v;
        name = s;
    }
    public static ReminderType fromInt(int i)
    {
        for (ReminderType d : ReminderType.values()) {
            if (d.getValue() == i) {
                return d;
            }
        }
        return null;
    }
    public int getValue() {
        return value;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }
    public boolean equals(ReminderType d) {
        return this.value == d.value;
    }

    public static String[] getStringValues()
    {

        ReminderType[] values = ReminderType.values();

        List<String> lstValues = new ArrayList();
        for (int i = 0; i < values.length; i++) {
            if(values[i].toString().length() > 0)
                lstValues.add(values[i].toString());
        }

        return lstValues.toArray(new String[] {});
    }
    @Override
    public String toString() {
        return name;
    }
}
