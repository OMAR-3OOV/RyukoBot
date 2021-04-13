package system.Objects.Utils.achievementsutils;

public enum Achievementypes {


    SECRETY(0, "secrety", "Secrety", "Secret is so rare achievement that users can achieved!"),
    GLOBAL(1, "global", "Global", "Everyone can collected global achievements");

    private final int id;
    private final String key;
    private final String name;
    private final String description;

    Achievementypes(int id, String key, String name, String description) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }
}
