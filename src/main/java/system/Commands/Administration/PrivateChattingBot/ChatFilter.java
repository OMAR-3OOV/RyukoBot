package system.Commands.Administration.PrivateChattingBot;

import net.dv8tion.jda.api.entities.User;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public enum ChatFilter {

    NAME("<name>", "Name"),
    TAG("<tag>", "Tag"),
    ID("<id>", "Id"),
    CREATED("<created>", "Time Created"),
    AVATAR("<avatar>", "Avatar"),
    MENTION("<mention>", "Mention");

    private final String filter;
    private final String name;

    ChatFilter(String filter, String name) {
        this.filter = filter;
        this.name = name;
    }

    public String getFilter() {
        return filter;
    }

    public String getName() {
        return name;
    }

    public String Value(User user) {
        switch (this) {
            case NAME: return user.getName();
            case TAG: return user.getDiscriminator();
            case ID: return user.getId();
            case CREATED: return user.getTimeCreated().format(DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy"));
            case AVATAR: return user.getAvatarUrl();
            case MENTION: return user.getAsMention();
        }
        return null;
    }

    public static String filter(User user, String string) {
        final AtomicReference<String> replacement = new AtomicReference<>(string);

        Arrays.stream(ChatFilter.values()).forEach(filter -> {
            final String replacementString = replacement.get();
            replacement.set(replacementString.replaceAll(filter.filter, filter.Value(user)));
        });

        return replacement.get();
    }

}
