package system.objects.TextUtils;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public enum PlaceHolderUtils {
    SUCCESSFUL("<a:success_ryuko_system:744940329710649384>", "success_ryuko_system", 0, ":successful:"),
    QUESTION("<a:question_ryuko_system:745171340805406772>", "question_ryuko_system", 1, ":question:"),
    LOADING("<a:searching_ryuko_system:744938866502271106>", "searching_ryuko_system", 2, ":loading:"),

    RUKO("<:Ruko:767492175196848158>", "ruko", 3, ":ruko:"),
    BOOSTER("<:booster:768547260828876860>", "booster", 4, ":booster:"),
    ONLINE("<:online_ryuko_hypixel_system:744972031669567638>", "searching_ryuko_system", 5, ":online:"),
    OFFLINE("<:offline_ryuko_hypixel_system:744972031472435240>", "offline_ryuko_hypixel_system", 6, ":offline:"),
    ERROR("<:error:752041143415341077>", "error", 7, ":error:"),
    ENABLED("<:enable_ryuko_system:752039346139431012>  ", "enable_ryuko_system", 8, ":enable:"),
    DISABLED("<:disable_ryuko_system:752039346080579635>", "disable_ryuko_system", 9, ":disable:"),
    ACHIEVEMENT("<:achievement:823238902851698698>", "achievement", 10, ":achievement:"),
    DISCORD("<:discord_ryuko_system:744991581127311420>", "discord_ryuko_system", 11, ":discord:");
    private final String emoji;
    private final String name;
    private final int id;
    private final String code;

    PlaceHolderUtils(String Emoji, String name, int id, String code) {
        this.emoji = Emoji;
        this.name = name;
        this.id = id;
        this.code = code;
    }

    public String getValue() {
        switch (this) {
            case ACHIEVEMENT: return ACHIEVEMENT.emoji;
            case RUKO: return RUKO.emoji;
            case ERROR: return ERROR.emoji;
            case ONLINE: return ONLINE.emoji;
            case OFFLINE: return OFFLINE.emoji;
            case BOOSTER: return BOOSTER.emoji;
            case DISCORD: return DISCORD.emoji;
            case DISABLED: return DISABLED.emoji;
            case ENABLED: return ENABLED.emoji;
            case SUCCESSFUL: return SUCCESSFUL.emoji;
            case LOADING: return LOADING.emoji;
            case QUESTION: return QUESTION.emoji;
        }
        return null;
    }

    public static String replaceEmojis(String string) {
        try {
            final AtomicReference<String> replacement = new AtomicReference<>(string);
            Arrays.stream(PlaceHolderUtils.values()).forEach(value -> {
                final String replacementString = replacement.get();
                assert value.getValue() != null;

                replacement.set(replacementString.replaceAll(value.code, value.getValue()));
            });
            return replacement.get();
        } catch (Exception e) {
            final AtomicReference<String> replacement = new AtomicReference<>(string);
            Arrays.stream(PlaceHolderUtils.values()).forEach(value -> {
                final String replacementString = replacement.get();
                assert value.getValue() != null;

                replacement.set(replacementString.replaceAll(value.code, "`None Emoji`"));
            });
            return replacement.get();
        }
    }

    public String getEmoji() {
        return emoji;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
