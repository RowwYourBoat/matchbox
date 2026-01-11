package me.rowwyourboat.game.enums;

public enum Role {
    STANDARD("standard", true),
    MEDIC("medic", false),
    SPARK("spark", false);

    private final String name;
    private final boolean multiple;

    Role(String name, boolean multiple) {
        this.name = name;
        this.multiple = multiple;
    }

    public String getName() {
        return this.name;
    }

    public boolean canHaveMultiple() {
        return this.multiple;
    }
}
