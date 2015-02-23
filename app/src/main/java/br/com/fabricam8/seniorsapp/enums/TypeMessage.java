package br.com.fabricam8.seniorsapp.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by laecy_000 on 22/02/2015.
 */
public enum TypeMessage {

    NONE(0, ""),
    CORRER(1, "correr(s)"),
    NADAR(2, "nadas(s)"),
    MALHAR(3, "malhar(s)");

    private final int value;
    private final String name;

    private TypeMessage(int v, String s) {
        value = v;
        name = s;
    }
    public static TypeMessage fromInt(int i) {
        for (TypeMessage d : TypeMessage.values()) {
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
    public boolean equals(TypeMessage d) {
        return this.value == d.value;
    }

    public static String[] getStringValues()
    {

        TypeMessage[] values = TypeMessage.values();

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
