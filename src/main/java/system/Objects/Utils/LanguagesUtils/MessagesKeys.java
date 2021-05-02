package system.objects.Utils.LanguagesUtils;

public enum MessagesKeys {

    HELLO_MESSAGE("hello-message"),
    HELP_MESSAGE_ERROR_COMMAND("help-message-error-command"),

    /* Profile Info */

    PROFILE("profile"),
    PROFILE_USERNAME("profile-username"),
    PROFILE_LANGUAGE("profile-language"),
    PROFILE_GAMESCOUNT("profile-gamescount"),
    PROFILE_LEVEL("profile-level"),
    PROFILE_STATS("profile-stats"),
    PROFILE_ID("profile-id"),
    PROFILE_RANK("profile-rank"),
    PROFILE_LTCU("profile-ltcu"),
    PROFILE_RUKO("profile-ruko"),
    PROFILE_CREATED("profile-created"),
    PROFILE_NOTVERIFIED("profile-notverified"),
    PROFILE_ACCOUNT_CREATED("profile-account-created"),
    PROFILE_NOTE_INFO("profile-note-info"),
    PROFILE_NO_COMMANDUSED("profile-no-commandused"),
    PROFILE_BANNED("profile-banned"),

    /* Achievements info */

    ACHIEVEMENT_COLLECTED_TITLE_MESSAGE("achievement-collected-title-message"),
    ACHIEVEMENT_COLLECTED_FOOTER_MESSAGE("achievement-collected-footer-message"),
    ACHIEVEMENT_COLLECTED_DESCRIPTION_MESSAGE("achievement-collected-description-message"),

    ACHIEVEMENT_MESSAGE_TITLE("achievement-message-title"),
    ACHIEVEMENT_MESSAGE_NOTE("achievement-message-note"),
    ACHIEVEMENT_MESSAGE_DESCRIPTION("achievement-message-description"),
    ACHIEVEMENT_MESSAGE_FOOTER("achievement-message-footer"),

    ACHIEVEMENT_STATS("achievements-stats"),
    ACHIEVEMENT_TOTAL_ACHIEVEMENTS_STRING("achievements-total-achievements-string"),
    ACHIEVEMENT_GLOBAL_ACHIEVEMENTS_STRING("achievements-global-achievements-string"),
    ACHIEVEMENT_SECRET_ACHIEVEMENTS_STRING("achievements-secret-achievements-string"),

    /* Verify info */

    VERIFY_MESSAGE("verify-message"),
    VERIFY_PRIVATE_MESSAGE("verify-private-message"),
    VERIFY_VERIFIED("verify-verified"),
    VERIFY_DENIED("verify-denied"),
    VERIFY_DISAPPEAR("verify-disappeared"),
    VERIFY_CANNOT_SENDMESSAGE("verify-cannot-sendmessage"),
    VERIFY_WRONG_CODE("verify-wrong-code"),
    VERIFY_SUCCESSFULLY_CODE("verify-successfully-code"),
    ;

    private final String messageKey;

    MessagesKeys(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey.toString();
    }
}
