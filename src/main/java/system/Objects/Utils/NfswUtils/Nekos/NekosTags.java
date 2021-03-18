package system.Objects.Utils.NfswUtils.Nekos;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public enum NekosTags {

    NEKO("neko"),
    NGIF("ngif"),
    PAT("pat"),
    HUG("hug"),
    FOX_GIRL("fox_girl"),
    WAIFU("waifu"),
    TICKLE("tickle"),
    CLASSIC("classic"),
    KEMONOMIMI("kemonomimi"),
    POKE("poke"),
    SMUG("smug"),
    LIZARD("lizard"),
    FEED("feed"),
    WALLPAPER("wallpaper"),
    AVATAR("avatar"),
    HOLO("holo"),
    BAKA("baka"),
    KISS("kiss"),
    GECG("gecg"),
    SLAP("slap");

    private final String tag_name;

    NekosTags(String name) {
        this.tag_name = name;
    }

    public String getTag() {
        return tag_name;
    }

    public String value() {
        switch (this) {
            case AVATAR: return AVATAR.tag_name;
            case HUG: return HUG.tag_name;
            case PAT: return PAT.tag_name;
            case BAKA: return BAKA.tag_name;
            case FEED: return FEED.tag_name;
            case GECG: return GECG.tag_name;
            case HOLO: return HOLO.tag_name;
            case KISS: return KISS.tag_name;
            case NEKO: return NEKO.tag_name;
            case NGIF: return NGIF.tag_name;
            case POKE: return POKE.tag_name;
            case SLAP: return SLAP.tag_name;
            case SMUG: return SMUG.tag_name;
            case WAIFU: return WAIFU.tag_name;
            case LIZARD: return LIZARD.tag_name;
            case TICKLE: return TICKLE.tag_name;
            case CLASSIC: return CLASSIC.tag_name;
            case FOX_GIRL: return FOX_GIRL.tag_name;
            case WALLPAPER: return WALLPAPER.tag_name;
            case KEMONOMIMI: return KEMONOMIMI.tag_name;
        }
        return null;
    }

    public static NekosTags getRandomTag() {
        return values()[(int) (Math.random()*values().length)];
    }

    public static Boolean isNekoTag(String tag) {
        return Arrays.stream(values()).anyMatch(a -> a.getTag().contains(tag));
    }

    public static String replaceTag(String tag) {
        AtomicReference<String> replacement = new AtomicReference<>(tag);

        Arrays.stream(NekosTags.values()).forEach(value -> {
            final String replacementTag = replacement.get();
            assert value.value() != null;
            replacement.set(replacementTag.replaceAll(value.toString(), value.value()));
        });

        return replacement.get();
    }
}
