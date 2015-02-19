package br.com.fabricam8.seniorsapp.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aercio on 1/27/15.
 */
public enum Periodicity {

    NONE(0, ""),
    DIAx1(1, "1 x ao dia"),
    DIAx2(2, "2 x ao dia"),
    DIAx3(3, "3 x ao dia"),
    DIAx4(4, "4 x ao dia"),
    SEMANAx1(5, "1 x por semana"),
    MESx1(6, "1 x ao mês"),
    DIA_SIM_DIA_NAO(7, "Dia sim, dia não");

    private final int value;
    private final String name;

    private Periodicity(int v, String s) {
        value = v;
        name = s;
    }

    public static Periodicity fromInt(int i) {
        for (Periodicity d : Periodicity.values()) {
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

    public static String[] getStringValues() {
        Periodicity[] values = Periodicity.values();

        List<String> lstValues = new ArrayList();
        for (int i = 0; i < values.length; i++) {
            if(values[i].toString().length() > 0)
                lstValues.add(values[i].toString());
        }

        return lstValues.toArray(new String[] {});
    }

    public boolean equals(Periodicity d) {
        return this.value == d.value;
    }

    @Override
    public String toString() {
        return name;
    }
}
