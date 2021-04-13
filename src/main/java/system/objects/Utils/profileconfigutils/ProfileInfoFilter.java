package system.objects.Utils.profileconfigutils;

import system.objects.TextUtils.MessageUtils;

import java.util.*;
import java.util.stream.Collectors;

public enum ProfileInfoFilter {

    USERNAME("username", "Username", "\uD83D\uDC64"),
    ID("user-id","ID" ,"\uD83D\uDCB3"),
    LANGUAGE("language","Language" , "\uD83C\uDF10"),
    RANK("rank","Rank", "\uD83D\uDD30"),
    GAMESCOUNT("games-count","Gamse Count", "\uD83C\uDFAE"),
    LASTTIMECOMMANDUSE("lasttime-use-command","Last Time command uses", "\uD83D\uDDE8️"),
    LEVEL("exp","Level", "\uD83D\uDD39"),
    RUKO("ruko","Ruko", new MessageUtils(":ruko:").EmojisHolder()),
    VERIFY("verify","Verify", "✅"),
    MINECRAFTACCOUNT("minecraft-account","Minecraft Account", "\uD83C\uDF32"),
    PROFILETIMECREATE("profile-time-create","Created", "\uD83D\uDDD3️");

    private final String key;
    private final String displayName;
    private final String emoji;

    public static HashMap<String, String> displayNames = new HashMap<>();

    ProfileInfoFilter(String key, String displayName, String emoji) {
        this.key = key;
        this.displayName = displayName;
        this.emoji = emoji;
    }

    static void createFilters() {
        Arrays.stream(ProfileInfoFilter.values()).forEach((u) -> {
            if (!(displayNames.containsKey(u.key))) {
                displayNames.put(u.key, u.displayName);
            }
        });
    }

    public static HashMap<String, HashMap<String, String>> getFilterDisplayName() {
        final HashMap<String, HashMap<String, String>> fil = new HashMap<>();
        final HashMap<String, String> de = new HashMap<>();

        Arrays.stream(ProfileInfoFilter.values()).forEach((u) -> {
            if (!(fil.containsKey(u.key))) {
                de.put(u.displayName, u.emoji);
                fil.put(u.key, de);
            }
        });

        final HashMap<String, HashMap<String, String>> filter = (fil)
                .entrySet()
                .stream().sorted(Collections.reverseOrder(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (key, value) -> key, LinkedHashMap::new));
        return filter;
    }

    public static HashMap<String, String> getFilterEmoji() {
        final HashMap<String, String> filterEmoji = new HashMap<>();

        Arrays.stream(ProfileInfoFilter.values()).forEach((u) -> {
            if (!(filterEmoji.containsKey(u.key))) {
                filterEmoji.put(u.key, u.emoji);
            }
        });

        final HashMap<String, String> filter = (filterEmoji)
                .entrySet()
                .stream().sorted(Collections.reverseOrder(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (key, value) -> key, LinkedHashMap::new));
        return filter;
    }

    public static HashMap<String, String> getDisplayNames() {
        return displayNames;
    }

    public String getKey() {
        return key;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getDisplayName() {
        return displayName;
    }
}
