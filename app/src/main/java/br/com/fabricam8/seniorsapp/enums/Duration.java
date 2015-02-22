package br.com.fabricam8.seniorsapp.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aercio on 1/27/15.
 */
public enum Duration {

    NONE(0, ""),
    DIA(1, "dia(s)"),
    SEMANA(2, "semana(s)"),
    MES(3, "mês(es)"),
    ANO(4, "ano(s)");

    private final int value;
    private final String name;

    private Duration(int v, String s) {
        value = v;
        name = s;
    }

    public static Duration fromInt(int i) {
        for (Duration d : Duration.values()) {
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

    public boolean equals(Duration d) {
        return this.value == d.value;
    }

    public static String[] getStringValues() {
        Duration[] values = Duration.values();

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
