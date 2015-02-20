package br.com.fabricam8.seniorsapp.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aercio on 1/27/15.
 */
public enum DosageMeasure {

    NONE(0, ""),
    COMPRIMIDO(1, "comprimido(s)"),
    AMPOLA(2, "ampola(s)"),
    COLHER(3, "colher(es)"),
    CAPSULA(4, "cápsula(s)"),
    GOTA(5, "gota(s)"),
    DRAGEA(6, "drágea(s)"),
    SUPOSITORIO(7, "supositório(s)"),
    ENVELOPE(8, "envelope(s)"),
    ML(9, "ml"),
    UNIDADE(10, "unidade(s)");

    private final int value;
    private final String name;

    private DosageMeasure(int v, String s) {
        value = v;
        name = s;
    }

    public static DosageMeasure fromInt(int i) {
        for (DosageMeasure d : DosageMeasure.values()) {
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

    public boolean equals(DosageMeasure d) {
        return this.value == d.value;
    }

    public static String[] getStringValues() {
        DosageMeasure[] values = DosageMeasure.values();

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
