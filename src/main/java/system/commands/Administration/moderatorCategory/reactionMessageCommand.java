package system.commands.Administration.moderatorCategory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.objects.Category;
import system.objects.Command;
import system.objects.Utils.BarTaskUtil;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class reactionMessageCommand implements Command {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {
        try {
            String message = args.get(0);

            final Pattern regexLink = Pattern.compile(Message.JUMP_URL_PATTERN.pattern());

            Matcher matcherLink = regexLink.matcher(message);

            EmbedBuilder embed = new EmbedBuilder();
            Message msg;

            if (matcherLink.find()) {
                message = matcherLink.group(3);
                msg = Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(matcherLink.group(1))).getTextChannelById(matcherLink.group(2))).retrieveMessageById(message).complete();

                Guild guild = event.getJDA().getGuildById(matcherLink.group(1));

                assert guild != null;
                TextChannel channel = guild.getTextChannelById(matcherLink.group(2));
                Optional<Object> invite = event.getGuild().getChannels().stream().findAny().map(m -> m.createInvite().setTemporary(true).complete().getUrl());

                embed.setTitle(msg.getId(), msg.getJumpUrl());
                embed.setColor(new Color(255, 172, 172));

                msg.getReactions().forEach(reaction -> {
                    embed.addField(reaction.getReactionEmote().getName() + " [ " + reaction.getCount() + " ] Reaction", "> " + reaction.retrieveUsers().stream().map(IMentionable::getAsMention).collect(Collectors.joining("\n> ")), true);
                });

                embed.addField("Information",
                        "**| Author: **" + msg.getAuthor().getName() + "\n"
                                + "**| Reactions count: **" + msg.getReactions().size() + "\n"
                                + "**| Guild: **" + guild.getName() + " " + invite.get().toString() + "\n"
                                + "**| Channel: **" + channel.getAsMention()
                        , false);
            } else {
                message = message.replace("<", "").replace("@", "").replace("&", "").replace(">", "");
                msg = event.getChannel().retrieveMessageById(message).complete();

                embed.setTitle(msg.getId(), msg.getJumpUrl());
                embed.setColor(new Color(255, 172, 172));

                BarTaskUtil barTaskUtil = new BarTaskUtil(msg, 10);

                barTaskUtil.createTask().forEach((k, v) -> {
                    embed.addField(k.getReactionEmote().getName() + " **" + barTaskUtil.getPercentage().get(k) + "%** [ " + k.getCount() + " ]", "> " + v, true);
                });

                embed.addField("Information",
                        "| Author: " + msg.getAuthor().getName() + "\n"
                                + "| Reactions count: " + msg.getReactions().size() + "\n"
                                + "| Channel: " + msg.getTextChannel().getAsMention()
                        , false);
            }

            event.getChannel().sendMessage(embed.build()).queue();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "r!reaction <message-id>";
    }

    @Override
    public String getInVoke() {
        return "reaction";
    }

    @Override
    public String getDescription() {
        return "to get the reaction message and the people who is add reaction";
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
    public Boolean displayCommand() {
        return true;
    }
}
