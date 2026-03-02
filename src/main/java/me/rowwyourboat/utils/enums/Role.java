package me.rowwyourboat.utils.enums;

public enum Role {
    STANDARD("standard", false),
    MEDIC("medic", true),
    SPARK("spark", true);

    private final String name;
    private final boolean special;

    Role(String name, boolean special) {
        this.name = name;
        this.special = special;
    }

    public String getName() {
        return this.name;
    }

    public boolean isSpecial() {
        return this.special;
    }
}
