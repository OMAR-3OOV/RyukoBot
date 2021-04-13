package system.objects.TextUtils;

public final class MessageUtils {

    private String message;

    public MessageUtils(String message) {
        this.message = message;
    }

    public String EmojisHolder() {
        return PlaceHolderUtils.replaceEmojis(this.message);
    }

}
