package system.commands.Administration.moderatorCategory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import system.Objects.Category;
import system.Objects.Command;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class informationCommand implements Command {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (args.isEmpty()) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red.brighter());
            embed.setTitle("How to user ( ´ ω ` )ノ");
            embed.setDescription("**Usage: ** " + getHelp());

            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }

        if (!Objects.requireNonNull(event.getMember()).hasPermission(getPermission())) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red.brighter());
            embed.setTitle("How to user ( ´ ω ` )ノﾞ");
            embed.setDescription("**Usage: ** " + getHelp());

            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }

        String channelID = args.get(0);
        boolean numberID = true;

        try {
            Double num = Double.parseDouble(channelID);
        } catch (NumberFormatException e) {
            numberID = false;
        }

        try {
            final Pattern regexNotChars = Pattern.compile("\\p{Punct}");
            final Matcher matcherNotChars = regexNotChars.matcher(channelID);

            final Pattern regexIsChar = Pattern.compile("-?[A-Za-z]+");
            final Matcher matcherIsChars = regexIsChar.matcher(channelID);

            if (matcherNotChars.find()) {
                channelID = channelID.replace("<", "").replace("#", "").replace(">", "");
                numberID = true;
            }else if (matcherIsChars.find()) {
                channelID = event.getGuild().getTextChannelsByName(channelID, false).get(0).getId();
                numberID = true;
            }

            if (numberID) {
                // TextChannel
                TextChannel channel = event.getGuild().getTextChannelById(channelID);
                //ArrayList<ServerTextChannel> list = new ArrayList<>(Integer.parseInt(args.get(0)));

                EmbedBuilder embed = new EmbedBuilder();
                assert channel != null;

                embed.setColor(Color.red.brighter());

                try {
                    embed.setDescription("**Channel Description:** " + Objects.requireNonNull(channel.getTopic()).toLowerCase() + ".");
                } catch (NullPointerException | EmptyStackException e) {
                    embed.setDescription("**Channel Description:** Description is empty");
                }
                embed.addField("> " + channel.getName() + " channel",
                        "**⬩ ID : **" + channel.getId() + " \n" +
                                "**⬩ Created : **" + channel.getTimeCreated().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm a")) + " \n" +
                                "**⬩ Category : **" + Objects.requireNonNull(channel.getManager().getChannel().getParent()).getName() + " \n" +
                                "**⬩ Members : **All " + channel.getManager().getChannel().getMembers().size() + " ( **Members** " + channel.getManager().getChannel().getMembers().stream().filter(channel::canTalk).collect(Collectors.toList()).stream().filter(member -> !member.getUser().isBot()).count() + " | **Bot** " + channel.getManager().getChannel().getMembers().stream().filter(channel::canTalk).collect(Collectors.toList()).stream().filter(member -> member.getUser().isBot()).count() + " )"
                        , false);
                embed.addField("> Permissions ",
                        "**⬩ Users :** " + channel.getManager().getChannel().getPermissionOverrides().stream().filter(f-> !f.isRoleOverride()).count() + " - ( **" + channel.getManager().getChannel().getPermissionOverrides().stream().filter(f-> !f.isRoleOverride()).filter(m-> !Objects.requireNonNull(m.getMember()).getUser().isBot()).count() + "** Members **" + channel.getManager().getChannel().getPermissionOverrides().stream().filter(f1-> !f1.isRoleOverride()).filter(m1-> Objects.requireNonNull(m1.getMember()).getUser().isBot()).count() + "** Bots )\n" +
                                "**⬩ Roles :** " + channel.getManager().getChannel().getRolePermissionOverrides().size() + " - ( **" + channel.getManager().getChannel().getRolePermissionOverrides().stream().filter(f-> !Objects.requireNonNull(f.getRole()).isManaged()).count() + "** Role **" + channel.getManager().getChannel().getRolePermissionOverrides().stream().filter(f-> !Objects.requireNonNull(f.getRole()).isHoisted()).count() + "** Bots Role )"
                        , false);

                event.getChannel().sendMessage(embed.build()).queue();

            } else {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.red.brighter());
                embed.setDescription("**I can't find this channel for some reason!  ╮( ˘_˘ )╭**");

                event.getChannel().sendMessage(embed.build()).queue();
            }
        } catch (InsufficientPermissionException e) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red.brighter());
            embed.setTitle("Missing Permission! (＃￣ω￣)");
            embed.setDescription(e.getMessage());

            event.getChannel().sendMessage(embed.build()).queue();
            e.printStackTrace();
        } catch (Exception e) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red.brighter());
            embed.setTitle("I can't find it!  (ง •̀_•́)ง");

            event.getChannel().sendMessage(embed.build()).queue();
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "r!"+getInVoke()+" <channel>";
    }

    @Override
    public String getInVoke() {
        return "channelinfo";
    }

    @Override
    public String getDescription() {
        return "get a information channel";
    }

    @Override
    public Permission getPermission() {
        return Permission.MANAGE_CHANNEL;
    }

    @Override
    public Boolean Lockdown() {
        return false;
    }

    @Override
    public Boolean isNsfw() {
        return false;
    }

    @Override
    public Boolean diplayCommand() {
        return false;
    }

    @Override
    public Category getCategory() {
        return Category.INFORMATION;
    }
}
