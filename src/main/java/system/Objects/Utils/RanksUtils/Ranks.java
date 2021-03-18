package system.Objects.Utils.RanksUtils;

import org.jetbrains.annotations.Contract;

import java.awt.*;

public enum Ranks {

    OWNERSHIP("ownership", "Ownership", new Color(229,0,0), "RyukoBot creator", 0),
    ACCESSIBLE("adminstration", "Administration", new Color(245, 175, 78), "Administration rank can use locked commands and some future things", 1),
    SPECIAL("special", "Special", new Color(255, 186, 245), "Special rank have some future things and its for the people who famous", 2),
    MEMBER("member", "Member", new Color(112, 112, 112), "Every ryuko user have this role", 3),
    PUNCHED("punched", "Punched", new Color(0, 0, 0), "The people who have this rank they can't use bot commands", 4);

    private final String name;
    private final String prefix;
    private final Color color;
    private final String description;
    private final int id;

    @Contract(pure = true)
    Ranks(String name, String prefix, Color color, String description, int id) {
        this.name = name;
        this.prefix = prefix;
        this.color = color;
        this.description = description;
        this.id = id;
    }

    @Contract(pure = true)
    public String getName() {
        return name;
    }

    @Contract(pure = true)
    public String getPrefix() {
        return prefix;
    }

    @Contract(pure = true)
    public Color getColor() {
        return color;
    }

    @Contract(pure = true)
    public String getDescription() {
        return description;
    }

    @Contract(pure = true)
    public int getId() {
        return id;
    }
}
