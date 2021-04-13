package system.Listener;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import system.commands.Administration.deletedMessageCommand;

import java.util.ArrayList;
import java.util.List;

public class AIEvents extends ListenerAdapter {

    public static final List<Message> MessagesDeleted = new ArrayList<>();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (deletedMessageCommand.readMessage.containsKey(event.getGuild())) {
            if (deletedMessageCommand.readMessage.get(event.getGuild()) == event.getChannel()) {
                deletedMessageCommand.deletedMessage.put(event.getMessage().getIdLong(), event.getMessage());
            }
        }
    }

    @Override
    public void onGuildMessageDelete(@NotNull GuildMessageDeleteEvent event) {

        if (deletedMessageCommand.readMessage.containsKey(event.getGuild())) {
            if (deletedMessageCommand.readMessage.get(event.getGuild()) == event.getChannel()) {
                deletedMessageCommand.deletedMessage.forEach((k, v) -> {
                    if (k.equals(event.getMessageIdLong())) {
                        MessagesDeleted.add(v);
                    }
                });
            }
        }
    }

}
