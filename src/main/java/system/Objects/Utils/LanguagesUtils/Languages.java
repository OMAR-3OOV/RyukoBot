package system.objects.Utils.LanguagesUtils;

import org.jetbrains.annotations.Contract;

public enum Languages {

    ENGLISH(0, "en", "english", "English"),
    FRENCH(1, "fr", "french", "French"),
    ARABIC(2, "ar", "arabic", "Arabic")
    ;

    private final int id;
    private final String code;
    private final String key;
    private final String displayName;

    Languages(int id, String code, String key, String displayName) {
        this.id = id;
        this.code = code;
        this.key = key;
        this.displayName = displayName;
    }

    @Contract(pure = true)
    public int getId() {
        return id;
    }

    @Contract(pure = true)
    public String getCode() {
        return code;
    }

    @Contract(pure = true)
    public String getKey() {
        return key;
    }

    @Contract(pure = true)
    public String getDisplayName() {
        return displayName;
    }

    public static Languages getLanguage(int id) {
        return Languages.values()[id];
    }
}
