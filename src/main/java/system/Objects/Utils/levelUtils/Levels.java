package system.Objects.Utils.levelUtils;

import java.awt.*;

public enum Levels {

    BEGINNER("beginner", "Beginner", new Color(73, 255, 194), 0, 4, 500),
    RUNNER("runner", "Runner", new Color(51, 208, 194), 5, 9, 1000),
    WORTHY("worthy", "Worthy", new Color(37, 131, 213), 10, 19, 5000),
    PLATINUM("platinum", "Platinum", new Color(3, 200, 255), 20, 39, 10000),
    SUPER("super", "Super", new Color(158, 123, 245), 40, 44, 15000),
    CHAMPION("champion", "Champion", new Color(155, 4, 93), 45, 50, 20000);

    private final String key;
    private final String name;
    private final Color color;
    private final int level_from;
    private final int level_tell;
    private final int experience;


    Levels(String key, String name, Color color, int level_from, int level_tell, int experience) {
        this.key = key;
        this.name = name;
        this.color = color;
        this.level_from = level_from;
        this.level_tell = level_tell;
        this.experience = experience;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public int getLevel_from() {
        return level_from;
    }

    public int getLevel_tell() {
        return level_tell;
    }

    public int getExperience() {
        return experience;
    }
}
