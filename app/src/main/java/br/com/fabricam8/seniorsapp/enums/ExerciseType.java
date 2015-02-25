package br.com.fabricam8.seniorsapp.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by laecy_000 on 22/02/2015.
 */
public enum ExerciseType {

    NONE(0, ""),
    CORRER(1, "correr"),
    NADAR(2, "nadar"),
    ANDAR(3, "andar"),
    MALHAR(4, "malhar");

    private final int value;
    private final String name;

    private ExerciseType(int v, String s) {
        value = v;
        name = s;
    }
    public static ExerciseType fromInt(int i) {
        for (ExerciseType d : ExerciseType.values()) {
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
    public boolean equals(ExerciseType d) {
        return this.value == d.value;
    }

    public static String[] getStringValues()
    {

        ExerciseType[] values = ExerciseType.values();

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
