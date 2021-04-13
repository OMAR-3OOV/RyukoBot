package system.Objects.Utils;

public enum coinsConfig {
    Gains("gains"),
    RUKO("ruko"),
    BOOSTER("booster");

    private final String key;

    coinsConfig(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
