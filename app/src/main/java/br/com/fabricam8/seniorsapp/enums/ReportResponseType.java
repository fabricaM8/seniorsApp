package br.com.fabricam8.seniorsapp.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aercio on 1/27/15.
 */
public enum ReportResponseType {

    NONE(0, ""),
    PULAR(1, "Pulou"),
    TOMAR(2, "Tomou"),
    ADIAR(3, "Adiou");

    private final int value;
    private final String name;

    private ReportResponseType(int v, String s) {
        value = v;
        name = s;
    }

    public static ReportResponseType fromInt(int i) {
        for (ReportResponseType d : ReportResponseType.values()) {
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

    public boolean equals(ReportResponseType d) {
        return this.value == d.value;
    }

    public static String[] getStringValues() {
        ReportResponseType[] values = ReportResponseType.values();

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
