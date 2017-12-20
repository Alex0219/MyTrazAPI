package de.fileinputstream.none.api.time;

import java.util.ArrayList;
import java.util.List;

public enum MuteUnit {
	
	 SECOND("Sekunde(n)", 1, "sec"), MINUTE("Minute(n)", 60, "min"), HOUR("Stunde(n)", 3600, "hour"), DAY("Tag(e)", 86400, "day"), WEEK("Woche(n)", 604800, "week");

    private String name;
    private String shortcut;
    private int toSecond;
	
    private MuteUnit(String name, int toSecond, String shortcut) {
        this.name = name;
        this.toSecond = toSecond;
        this.shortcut = shortcut;
    }

    public String getShortcut() {
        return this.shortcut;
    }

    public String getName() {
        return this.name;
    }

    public int getToSecond() {
        return this.toSecond;
    }

    public static List<String> getUnitsAsString() {
        List<String> units = new ArrayList();
        MuteUnit[] arrayOfMuteUnit;
        int j = (arrayOfMuteUnit = values()).length;
        for (int i = 0; i < j; i++) {
            MuteUnit u = arrayOfMuteUnit[i];
            units.add(u.getShortcut().toLowerCase());
        }
        return units;
    }

    public static MuteUnit getUnits(String unit) {
        MuteUnit[] arrayOfMuteUnit;
        int j = (arrayOfMuteUnit = values()).length;
        for (int i = 0; i < j; i++) {
            MuteUnit units = arrayOfMuteUnit[i];
            if (units.getShortcut().toLowerCase().equals(unit.toLowerCase())) {
                return units;
            }
        }
        return null;
    }

}
