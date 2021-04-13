package system.commands.Administration;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.listener.AIEvents;
import system.objects.Category;
import system.objects.Command;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class deletedMessageCommand implements Command {

    public static HashMap<Guild, TextChannel> readMessage = new HashMap<>();
    public static HashMap<Long, Message> deletedMessage = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {
        try {
            List<String> handlers = new ArrayList<>(args);

            if (handlers.get(0).equalsIgnoreCase("start")) {
                String channel = handlers.get(1);

                Pattern regex = Pattern.compile(Message.MentionType.CHANNEL.getPattern().pattern());
                Matcher matcher = regex.matcher(channel);

                if (matcher.find()) {
                    channel = channel.replace("#","").replace("@","").replace("$","").replace("!","").replace("<","").replace(">","");
                }

                TextChannel textChannel = event.getGuild().getTextChannelById(channel);

                assert textChannel != null;

                readMessage.put(textChannel.getGuild(), textChannel);
                readMessage.get(event.getGuild()).sendMessage("Deleted message `Enabled` this channel under reading!").queue();
                deletedMessage.put(event.getMessageIdLong(), event.getMessage());
                event.getMessage().delete().queue();
            } else if (handlers.get(0).equalsIgnoreCase("stop")) {
                if (readMessage.containsKey(event.getGuild())) {
                    EmbedBuilder embed = new EmbedBuilder();

                    AIEvents.MessagesDeleted.forEach(msg -> {
                        embed.addField(msg.getAuthor().getName() + " | `" + msg.getTimeCreated().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")) + "`", msg.getContentRaw(), false);
                    });

                    event.getChannel().sendMessage(embed.build()).queue();
                    readMessage.get(event.getGuild()).sendMessage("Deleted message reader has been stop").queue();
                    readMessage.remove(event.getGuild());
                    AIEvents.MessagesDeleted.clear();
                } else {
                    readMessage.get(event.getGuild()).sendMessage("Reader is not under working!").queue();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "r!deletedmessage";
    }

    @Override
    public String getInVoke() {
        return "deletedmessage";
    }

    @Override
    public String getDescription() {
        return "get deleted message in channels";
    }

    @Override
    public Permission getPermission() {
        return null;
    }

    @Override
    public Category getCategory() {
        return Category.MANAGEMENT;
    }

    @Override
    public Boolean Lockdown() {
        return true;
    }

    @Override
    public Boolean isNsfw() {
        return false;
    }

    @Override
    public Boolean diplayCommand() {
        return false;
    }
}
