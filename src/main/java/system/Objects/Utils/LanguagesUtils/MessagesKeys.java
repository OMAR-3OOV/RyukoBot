package system.objects.Utils.LanguagesUtils;

import system.objects.Utils.chatbot.*;

public enum MessagesKeys {

    HELP_MESSAGE_ERROR_COMMAND("help-message-error-command", ":error: this command doesn't exist"),

    /* Profile Info */

    PROFILE("profile", "Profile"),
    PROFILE_USERNAME("profile-username", "Username"),
    PROFILE_LANGUAGE("profile-language", "language"),
    PROFILE_GAMESCOUNT("profile-gamescount", "games count"),
    PROFILE_LEVEL("profile-level", "level"),
    PROFILE_STATS("profile-stats", "stats"),
    PROFILE_ID("profile-id", "id"),
    PROFILE_RANK("profile-rank", "Rank"),
    PROFILE_LTCU("profile-ltcu", "Last command usages"),
    PROFILE_RUKO("profile-ruko", "Rukos"),
    PROFILE_CREATED("profile-created", "Created"),
    PROFILE_NOTVERIFIED("profile-notverified", "Note: Your profile is not verified yet, please make sure to verify your Ryuko account `r!verify`"),
    PROFILE_ACCOUNT_CREATED("profile-account-created", "Your profile has been created"),
    PROFILE_NOTE_INFO("profile-note-info", "Note : there is more information you can setup by tap on the emoji below"),
    PROFILE_NO_COMMANDUSED("profile-no-commandused", "-"),
    PROFILE_BANNED("profile-banned", "BANNED"),

    /* Achievements info */

    ACHIEVEMENT_COLLECTED_TITLE_MESSAGE("achievement-collected-title-message", "You has been collected new achievement's :achievement:"),
    ACHIEVEMENT_COLLECTED_FOOTER_MESSAGE("achievement-collected-footer-message", "\uD83D\uDDD3️ Date :"),
    ACHIEVEMENT_COLLECTED_DESCRIPTION_MESSAGE("achievement-collected-description-message", "``` \n``` > Achievements : \n - <achievement> ``` \n```"),

    ACHIEVEMENT_MESSAGE_TITLE("achievement-message-title", "Your achievements :achievement:"),
    ACHIEVEMENT_MESSAGE_NOTE("achievement-message-note", "You can know how to collect the achievement by look below to achievement description!"),
    ACHIEVEMENT_MESSAGE_DESCRIPTION("achievement-message-description", "```diff\n - NOTE : The secret message can display in Direct message if you collected only!```"),
    ACHIEVEMENT_MESSAGE_FOOTER("achievement-message-footer", "⬅️ BackWard & Forward ➡️"),

    ACHIEVEMENT_STATS("achievements-stats", "\uD83D\uDCCA Your stats :"),
    ACHIEVEMENT_TOTAL_ACHIEVEMENTS_STRING("achievements-total-achievements-string", "• Total achievements"),
    ACHIEVEMENT_GLOBAL_ACHIEVEMENTS_STRING("achievements-global-achievements-string", "• Global achievements"),
    ACHIEVEMENT_SECRET_ACHIEVEMENTS_STRING("achievements-secret-achievements-string", "• Secret achievements"),

    /* Verify info */

    VERIFY_MESSAGE("verify-message", ":successful: Please check your Direct message with ryuko bot and make sure to do everything well."),
    VERIFY_PRIVATE_MESSAGE("verify-private-message", "Please enter this code to confirm your verify!"),
    VERIFY_VERIFIED("verify-verified", "You're already verified " + Emotions.getRandomHappiness()),
    VERIFY_DENIED("verify-denied", ":error: Your request has been denied " + Emotions.getRandomConfusion()),
    VERIFY_DISAPPEAR("verify-disappeared", ":error: Your request has been denied, you didn't enter the code " + Emotions.getRandomConfusion()),
    VERIFY_CANNOT_SENDMESSAGE("verify-cannot-sendmessage", "Request Failed, please make sure to open the Direct message with Ryuko bot!! " + Emotions.getRandomConfusion()),
    VERIFY_WRONG_CODE("verify-wrong-code", "Your request has been denied, Wrong code! " + Emotions.getRandomSadness()),
    VERIFY_SUCCESSFULLY_CODE("verify-successfully-code", ":successful: Your verified now, Thank your for verify your account and to thank you more i will give you an verified achievement. Have fun with me " + Emotions.getRandomMagic()),
    ;

    private final String messageKey;
    private final String En_message;

    MessagesKeys(String messageKey, String En_message) {
        this.messageKey = messageKey;
        this.En_message = En_message;
    }

    public String getMessageKey() {
        return messageKey.toString();
    }

    public String getEn_message() {
        return En_message;
    }
}
