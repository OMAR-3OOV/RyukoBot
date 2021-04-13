package system.objects.Utils.achievementsutils;

public enum Achievements {

    // Secret achievements
    DMSME(0, Achievementypes.SECRETY,"Direct Message", "direct-message", "Send DM to Ryuko"),
    LOVESME(1, Achievementypes.SECRETY, "You Love Me", "loves-me", "Send `i love you` in DM to Ryuko"),
    THREE_HEART_AT_ONCE(3, Achievementypes.SECRETY, "Three Heart At Once", "three-hearts", "Send three hearts to Ryuko"),
    DAILY_COMMAND_FOR_7DAYS(4, Achievementypes.SECRETY, "Daily Command For Week", "daily-week", "Use Daily command 7 consecutive days"),
    PAY_TO_ME(5, Achievementypes.SECRETY, "Pay To Me", "pay-me", "Pay to Ryuko a ruko's"),
    THREE_MESSAGE_IN_FIVE_SECONDS(6, Achievementypes.SECRETY, "Three Messages In Five Seconds", "three-messages-five-seconds", "Send to Ryuko three message in DM with five seconds"),
    REACTION_MESSAGE_IN_DM(7, Achievementypes.SECRETY, "Reaction Direct Message", "reaction-message-dm", "Add reaction to any message that bot sent you in the DM"),

    // GUI Achievements
    USE_FIRST_COMMAND(8, Achievementypes.GLOBAL, "First Command For You", "first-command", "First command that you used"),
    VERIFIED(9, Achievementypes.GLOBAL, "Account Verified", "verified-account", "You're account has been verified"),
    REACH_RUKO_10000(10, Achievementypes.GLOBAL, "Reach 10000 Ruko's", "reach-ruko-10000", "You reached 10000 ruko's"),
    REACH_RUKO_100000(11, Achievementypes.GLOBAL, "Reach 100000 Ruko's", "reach-ruko-100000", "You reached 100000 ruko's"),
    RPC_WINSTREAK_THREE(12, Achievementypes.GLOBAL, "Win streak Rpc Three Times", "rpc-winstreak-three", "You're achieved three winstreak on rpc game"),
    RPC_WINSTREAK_FIVE(13, Achievementypes.GLOBAL, "Win streak Rpc Five Times", "rpc-winstreak-five", "You're achieved five winstreak on rpc game"),
    RPC_WINSTREAK_TEN(14, Achievementypes.GLOBAL, "Win streak Rpc Ten Times", "rpc-winstreak-ten", "You're achieved ten winstreak on rpc game"),
    SEND_100_MESSAGE_IN_RYUKOSERVER(15, Achievementypes.GLOBAL, "Send 100 Message", "send-100-message", "Sending 100 message in ryuko server");

    private final int achievementId;
    private final Achievementypes type;
    private final String achievementName;
    private final String achievementKey;
    private final String achievementDescription;

    Achievements(int id, Achievementypes type, String name, String key, String description) {
        this.achievementId = id;
        this.type = type;
        this.achievementName = name;
        this.achievementKey = key;
        this.achievementDescription = description;
    }

    public int getAchievementId() {
        return achievementId;
    }

    public Achievementypes getType() {
        return type;
    }

    public String getAchievementName() {
        return achievementName;
    }

    public String getAchievementKey() {
        return achievementKey;
    }

    public String getAchievementDescription() {
        return achievementDescription;
    }
}
