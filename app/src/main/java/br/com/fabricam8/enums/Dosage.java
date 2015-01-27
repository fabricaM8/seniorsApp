package br.com.fabricam8.enums;

/**
 * Created by Aercio on 1/27/15.
 */
public enum Dosage {

    GOTA ("Gotas"),
    AMPOLA ("Ampola"),
    COLHER ("Colher"),
    DRAGEA ("Drágea"),
    SUPOSITORIO ("Supositório"),
    ENVELOPE ("Envelope");


    private final String name;

    private Dosage(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    public String toString(){
        return name;
    }
}
