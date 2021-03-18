package system.Commands.Administration.PrivateChattingBot;

import net.dv8tion.jda.api.entities.User;

public class PrivateChatFilterManager {

    private final String text;

    public PrivateChatFilterManager(String text) {
        this.text = text;
    }

    public String toFilter(User user) {
        return ChatFilter.filter(user, this.text);
    }
}
