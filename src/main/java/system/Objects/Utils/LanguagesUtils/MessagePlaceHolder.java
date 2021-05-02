package system.objects.Utils.LanguagesUtils;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.nio.channels.Channel;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public enum MessagePlaceHolder {

    /* user info */
    USERNAME("<username>"),
    ID("<userid>"),
    MENTION("<mention>"),

    /* channel info */
    CHANNELNAME("<channelname>"),
    CHANNELID("<channelid>"),
    CHANNELMENTION("<channelmention>"),

    /* guild info */
    GUILDNAME("<guildname>"),
    GUILDID("<guildid>"),

    /* bot info */
    RUKO("<ruko>"),
    ;

    private final String string;

    MessagePlaceHolder(String string) {
        this.string = string;
    }

    public String getValue(User user, TextChannel channel) {

        if (user != null) {
            switch (this) {
                case USERNAME:
                    return user.getName();
                case ID:
                    return user.getId();
                case MENTION:
                    return user.getAsMention();
            }
        } else {
            return "`user none`";
        }

        if (channel != null) {
            switch (this) {
                case CHANNELNAME:
                    return channel.getName();
                case CHANNELID:
                    return channel.getId();
                case CHANNELMENTION:
                    return channel.getAsMention();
            }
        } else {
            return "`channel none`";
        }

        return "`null`";
    }

    public String getString() {
        return string;
    }

    public static String replace(String message, User user, TextChannel channel) {
        try {
            AtomicReference<String> replace = new AtomicReference<>(message);

            Arrays.stream(MessagePlaceHolder.values()).forEach(value -> {
                final String replacement = replace.get();

                replace.set(replacement.replaceAll(value.string, Objects.requireNonNull(value.getValue(user, channel))));
            });
            return replace.get();
        } catch (Exception e) {
            System.out.println("Error Message: " + e.getMessage());
            e.printStackTrace();
            return "`none`";
        }
    }
}
