package system.objects.Utils.profileconfigutils;

public enum ProfileProperties {

    USERNAME("username", "Username", "invalid"),
    ID("user-id", "User ID", "invalid"),
    PROFILE_TIME_CREATE("profile-time-create", "Profile created", String.valueOf(0)),
    LANGUAGE("language", "Lnaguage", "en"),
    RUKO("ruko", "Ruko", String.valueOf(0)),
    GAINS("gains", "Gains", String.valueOf(0)),
    EXPERIENCE("exp", "Exp", String.valueOf(0)),
    LASTTIME_USE_COMMAND("lasttime-use-command", "Last time command used", String.valueOf(0)),
    GAMES_COUNT("games-count", "Games count", String.valueOf(0)),
    BANNED("banned", "Banned", Boolean.toString(false)),
    AUTO_BANNED("auto-banned", "banned", String.valueOf(0)),
    VERIFY("verify", "Verify", Boolean.toString(false)),
    RANK("rank", "Rank", "Member"),
    CANVAS_FILTER("canvas-filter", "Canvas Picture Filter", String.valueOf(0));

    private final String key;
    private final String name;
    private final String value;

    ProfileProperties(String key, String name, String value) {
        this.key = key;
        this.name = name;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
