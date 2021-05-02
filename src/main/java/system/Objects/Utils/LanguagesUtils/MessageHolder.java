package system.objects.Utils.LanguagesUtils;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class MessageHolder {

    private final String message;
    private final User user;
    private final TextChannel channel;

    public MessageHolder(String message, User user, TextChannel channel) {
        this.message = message;
        this.user = user;
        this.channel = channel;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public TextChannel getChannel() {
        return channel;
    }

    public String toHolder() {
        return MessagePlaceHolder.replace(this.message, this.user, this.channel);
    }
}
