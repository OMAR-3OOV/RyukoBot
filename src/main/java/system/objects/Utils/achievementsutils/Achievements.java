package system.objects.Utils.achievementsutils;

public enum Achievements {

    // Secret achievements
    DMSME(0, Achievementypes.SECRETY,"achievement-dmsme-name", "direct-message", "achievement-dmsme-description"),
    LOVESME(1, Achievementypes.SECRETY, "achievement-lovesme-name", "loves-me", "achievement-lovesme-description"),
    THREE_HEART_AT_ONCE(3, Achievementypes.SECRETY, "achievement-threehearts-name", "three-hearts", "achievement-threehearts-description"),
    DAILY_COMMAND_FOR_7DAYS(4, Achievementypes.SECRETY, "achievement-dailyforweek-name", "daily-week", "achievement-dailyforweek-description"),
    PAY_TO_ME(5, Achievementypes.SECRETY, "achievement-paytome-name", "pay-me", "achievement-paytome-description"),
    THREE_MESSAGE_IN_FIVE_SECONDS(6, Achievementypes.SECRETY, "achievement-threemessageinfivesecond-name", "three-messages-five-seconds", "achievement-threemessageinfivesecond-description"),
    REACTION_MESSAGE_IN_DM(7, Achievementypes.SECRETY, "achievement-reactionmessageindm-name", "reaction-message-dm", "achievement-reactionmessageindm-description"),

    // GUI Achievements
    USE_FIRST_COMMAND(8, Achievementypes.GLOBAL, "achievement-usefirstcommand-name", "first-command", "achievement-usefirstcommand-description"),
    VERIFIED(9, Achievementypes.GLOBAL, "achievement-verified-name", "verified-account", "achievement-verified-description"),
    REACH_RUKO_10000(10, Achievementypes.GLOBAL, "achievement-reach10000ruko-name", "reach-ruko-10000", "achievement-reach10000ruko-description"),
    REACH_RUKO_100000(11, Achievementypes.GLOBAL, "achievement-reach100000ruko-name", "reach-ruko-100000", "achievement-reach100000ruko-description"),
    RPC_WINSTREAK_THREE(12, Achievementypes.GLOBAL, "achievement-rpcwinstreakthree-name", "rpc-winstreak-three", "achievement-rpcwinstreakthree-description"),
    RPC_WINSTREAK_FIVE(13, Achievementypes.GLOBAL, "achievement-rpcwinstreakfive-name", "rpc-winstreak-five", "achievement-rpcwinstreakfive-description"),
    RPC_WINSTREAK_TEN(14, Achievementypes.GLOBAL, "achievement-rpcwinstreakten-name", "rpc-winstreak-ten", "achievement-rpcwinstreakten-description"),
    SEND_100_MESSAGE_IN_RYUKOSERVER(15, Achievementypes.GLOBAL, "achievement-send100messageinryukoserver-name", "send-100-message", "achievement-send100messageinryukoserver-description");

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
