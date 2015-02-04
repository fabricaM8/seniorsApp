package br.com.fabricam8.seniorsapp.enums;

/**
 * Created by Aercio on 1/27/15.
 */
public enum Dosage {

    NONE (0, ""),
    GOTA (1, "Gotas"),
    AMPOLA (2, "Ampola"),
    COLHER (3, "Colher"),
    CAPSULA (4, "Cápsula"),
    COMPRIMIDO (5, "Comprimido"),
    DRAGEA (6, "Drágea"),
    SUPOSITORIO (7, "Supositório"),
    ENVELOPE (8, "Envelope");

    private final int value;
    private final String name;

    private Dosage(int v, String s) {
        value = v;
        name = s;
    }

    public int getValue() {
        return value;
    }
    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    public boolean equals(Dosage d){
        return this.value == d.value;
    }

    public static Dosage fromInt(int i) {
        for (Dosage d : Dosage.values()) {
            if (d.getValue() == i) { return d; }
        }
        return null;
    }

    @Override
    public String toString(){
        return name;
    }
}
