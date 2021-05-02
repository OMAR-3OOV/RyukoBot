package system.objects.Utils.achievementsutils;

public enum Achievementypes {


    SECRETY(0, "secret", "achievement-type-secret-name", "achievement-type-secret-description"),
    GLOBAL(1, "global", "achievement-type-global-name", "achievement-type-global-description");

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
