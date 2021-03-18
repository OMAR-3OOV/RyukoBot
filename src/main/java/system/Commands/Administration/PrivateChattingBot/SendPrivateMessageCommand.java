package system.Commands.Administration.PrivateChattingBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.Listener.Events;
import system.Objects.Category;
import system.Objects.Command;
import system.Objects.TextUtils.MessageUtils;
import system.Objects.Utils.PrivateChatUtils.PrivateChat;
import system.Objects.Utils.PrivateChatUtils.PrivateChatMode;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SendPrivateMessageCommand implements Command {

    public static HashMap<User, PrivateChat> privatechat = new HashMap<>();
    public static HashMap<User, PrivateChat> getterchat = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {
        try {
            String command = args.get(0);

            if (command.equalsIgnoreCase("open")) {

                String username = args.get(1);

                StringBuilder builder = new StringBuilder();

                for (int i = 2; i < args.size(); i++) {
                    builder.append(args.get(i)).append(" ");
                }

                final Pattern regex = Pattern.compile(Message.MentionType.USER.getPattern().pattern());
                final Matcher matcher = regex.matcher(username);

                final Pattern regexRoleName = Pattern.compile("-?[A-Za-z]+");
                final Matcher matcherRoleName = regexRoleName.matcher(username);

                if (matcher.find()) {
                    username = username.replace("<", "").replace("!", "").replace("@", "").replace("#", "").replace("&", "").replace(">", "");
                } else if (matcherRoleName.find()) {
                    username = Objects.requireNonNull(event.getJDA().getUsersByName(username, true)).stream().map(m -> m.getId()).collect(Collectors.joining());
                }

                User user = event.getJDA().getUserById(username);

                // Private Util

                PrivateChat privateChat = new PrivateChat(event.getAuthor(), user, event.getChannel(), event.getGuild(), event.getMessage());
                PrivateChat getterChat = new PrivateChat(event.getAuthor(), user, event.getChannel(), null);

                if (privateChat.getStarted()) {
                    event.getChannel().sendMessage(new MessageUtils(":error: You're already in private message with a user!").EmojisHolder()).queue();
                    return;
                }

                privateChat.start(builder.toString());

                privateChat.getSenderMessage().addReaction("\uD83D\uDD27").queue();

                privatechat.put(event.getAuthor(), privateChat);
                getterchat.put(user, getterChat);
            } else if (command.equalsIgnoreCase("close")) {
                if (privatechat.containsKey(event.getAuthor())) {
                    event.getChannel().sendMessage(new MessageUtils(":successful: | private has been closed").EmojisHolder()).queue();
                    privatechat.get(event.getAuthor()).end();
                    getterchat.remove(privatechat.get(event.getAuthor()).getGetter());
                    privatechat.remove(event.getAuthor());
                }
            } else if (Arrays.stream(PrivateChatMode.values()).anyMatch(a -> command.equalsIgnoreCase(a.getName()))) {
                if (!privatechat.containsKey(event.getAuthor())) return;
                if (command.equalsIgnoreCase("chat")) {
                    privatechat.get(event.getAuthor()).setMode(PrivateChatMode.CHATTING);
                } else if (command.equalsIgnoreCase("view")) {
                    privatechat.get(event.getAuthor()).setMode(PrivateChatMode.VIEW);
                }

                event.getChannel().sendMessage(new MessageUtils(":successful: | Your private chat mode has been changed to : `" + privatechat.get(event.getAuthor()).getMode().getName() + "`").EmojisHolder()).queue();
            } else if (command.equalsIgnoreCase("filter")) {
                EmbedBuilder embed = new EmbedBuilder();

                embed.setColor(new Color(200, 200, 200));
                embed.setTitle("Private Chat filter ⌨");

                StringBuilder disc = embed.getDescriptionBuilder();

                disc.append("```yml").append("\n");
                disc.append("Filter Name:").append(spaceBetween(5)).append("|").append("     ").append("Filter:").append("\n");
                disc.append(HorizontalLine(30, "=")).append("\n");

                for (int i = 0; i < ChatFilter.values().length; i++) {
                    disc.append(ChatFilter.values()[i].getName())
                            .append(spaceBetween((12 + 5) - ChatFilter.values()[i].getName().length()))
                            .append("|")
                            .append(spaceBetween((12) - (ChatFilter.values()[i].getFilter().length())))
                            .append(ChatFilter.values()[i].getFilter()).append("\n");
                }

                disc.append("```").append(" \n");
                event.getChannel().sendMessage(embed.build()).queue();
            }
        } catch (Exception e) {
            System.out.println("> Error Message : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "r!send <user> <message>";
    }

    @Override
    public String getInVoke() {
        return "send";
    }

    @Override
    public String getDescription() {
        return "send private for someone";
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

    private StringBuilder spaceBetween(int amount) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < amount; i++) {
            builder.append(" ");
        }

        return builder;
    }

    private StringBuilder HorizontalLine(int amount, String line) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < amount; i++) {
            builder.append(line);
        }

        return builder;
    }
}
