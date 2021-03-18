package system.Commands.Administration.moderatorCategory.channelsAddonsCommands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import system.Objects.Category;
import system.Objects.Command;
import system.Objects.TextUtils.MessageUtils;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class permissionChannelCommand implements Command {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (!event.getMember().hasPermission(getPermission())) {
            EmbedBuilder noPermission = new EmbedBuilder();
            noPermission.setColor(Color.red.brighter());
            noPermission.setTitle("You don't have permission! (ง •̀_•́)ง");
            noPermission.setDescription("Permission : " + getPermission().getName());

            event.getChannel().sendMessage(noPermission.build()).queue();
            return;
        }

        try {
            // #types: add - remove - reset - read
            String type = args.get(0);
            // #channel : channel_id - channel_name - channel_mention
            String channel = args.get(1);
            // #permission : Discord_Permission_Name
            //String permission = args.get(2);

            if (type == null) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.red.brighter());
                embed.setTitle("I can't find type!  (ง •̀_•́)ง");
                embed.setDescription("Usage : " + getHelp());

                event.getChannel().sendMessage(embed.build()).queue();
                return;
            } else if (type.equalsIgnoreCase("mode")) {
                try {
                    String permission = args.get(2);

                    if (permission.isEmpty()) {
                        EmbedBuilder pemrissionEmpty = new EmbedBuilder();
                        pemrissionEmpty.setColor(Color.red.brighter());
                        pemrissionEmpty.setTitle("You can't use this with about choosing mode! (ง •̀_•́)ง");
                        pemrissionEmpty.setDescription("Usage: r!channelperm mode <channel> <mode> \n Modes: Viewer / global / operators");

                        event.getChannel().sendMessage(pemrissionEmpty.build()).queue();
                        return;
                    }

                    String permissionString = "???";
                    String Below = "";

                    final Pattern regexNotChars = Pattern.compile("\\p{Punct}");
                    final Matcher matcherNotChars = regexNotChars.matcher(channel);

                    final Pattern regexIsChar = Pattern.compile("-?[A-Za-z]+");
                    final Matcher matcherIsChars = regexIsChar.matcher(channel);

                    if (matcherNotChars.find()) {
                        channel = channel.replace("<", "").replace("#", "").replace(">", "");
                    } else if (matcherIsChars.find()) {
                        channel = event.getGuild().getTextChannelsByName(channel, false).get(0).getId();
                    }

                    // TextChannel
                    TextChannel channels = event.getGuild().getTextChannelById(channel);

                    if (permission.contains("viewer")) {
                        for (PermissionOverride permHolder : channels.getPermissionOverrides()) {
                            permHolder.getManager().setDeny(Permission.ALL_CHANNEL_PERMISSIONS).setAllow(Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_READ, Permission.CREATE_INSTANT_INVITE, Permission.MESSAGE_EXT_EMOJI).queue();
                            permissionString = "Viewer";
                            Below = "Now this channel only for view, and no one can send message!";
                        }
                    } else if (permission.contains("global")) {
                        for (PermissionOverride permHolder : channels.getPermissionOverrides()) {
                            permHolder.getManager().setAllow(Permission.MESSAGE_READ, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_MENTION_EVERYONE).queue();
                            permissionString = "Global";
                            Below = "Now this channel is Global chat for everyone!";
                        }
                    } else if (permission.contains("operators")) {
                        for (PermissionOverride permHolder : channels.getPermissionOverrides()) {
                            permHolder.getManager().setDeny(Permission.MESSAGE_READ, Permission.CREATE_INSTANT_INVITE).queue();
                            permissionString = "Operators";
                            Below = "Now this channel only for operators (STAFFS), Member should have `CHANNEL MANAGER` see this channel!";
                        }
                    } else {
                        EmbedBuilder elseType = new EmbedBuilder();
                        elseType.setColor(Color.red.brighter());
                        elseType.setTitle("`" + permission + "`is undefined! (ง •̀_•́)ง");
                        elseType.setDescription("Types : viewer / global / operators");

                        event.getChannel().sendMessage(elseType.build()).queue();
                        return;
                    }

                    EmbedBuilder embed = new EmbedBuilder();
                    assert channels != null;

                    Date date = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");

                    embed.setColor(Color.GREEN.brighter());
                    embed.setTitle(event.getGuild().getTextChannelById(channel).getName() + " Channel");
                    embed.setDescription("Channel mode has been set to `" + permissionString + "`");
                    embed.addField(" ",
                            "**Channel : **" + channels.getAsMention() + "\n" +
                                    "**Mode : **" + permissionString + "\n" +
                                    "**Description : **" + Below
                            , false);

                    embed.setFooter("\uD83D\uDCC6 " + format.format(date));
                    event.getChannel().sendMessage(embed.build()).queue();

                } catch (NullPointerException | NoSuchElementException | NumberFormatException error) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(Color.red.brighter());
                    embed.setTitle("I can't find channel! (ง •̀_•́)ง");
                    embed.setDescription("Make sure you're put correct channel");

                    event.getChannel().sendMessage(embed.build()).queue();
                    error.printStackTrace();
                } catch (Exception exception) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(Color.red.brighter());
                    embed.setTitle("Something went wrong! (ง •̀_•́)ง");
                    embed.setDescription("Error: " + exception.getMessage());

                    event.getChannel().sendMessage(embed.build()).queue();
                    exception.printStackTrace();
                }
            } else if (type.equalsIgnoreCase("reset")) {
                try {
                    final Pattern regexNotChars = Pattern.compile("\\p{Punct}");
                    final Matcher matcherNotChars = regexNotChars.matcher(channel);

                    final Pattern regexIsChar = Pattern.compile("-?[A-Za-z]+");
                    final Matcher matcherIsChars = regexIsChar.matcher(channel);

                    if (matcherNotChars.find()) {
                        channel = channel.replace("<", "").replace("#", "").replace(">", "");
                    } else if (matcherIsChars.find()) {
                        channel = event.getGuild().getTextChannelsByName(channel, false).get(0).getId();
                    }

                    TextChannel channels = event.getGuild().getTextChannelById(channel);

                    for (PermissionOverride permHolder : channels.getPermissionOverrides()) {
                        permHolder.getManager().setAllow(Permission.UNKNOWN).setDeny(Permission.UNKNOWN).queue();
                    }

                    EmbedBuilder embed = new EmbedBuilder();
                    assert channels != null;

                    Date date = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");

                    embed.setColor(Color.GREEN.brighter());
                    embed.setTitle(event.getGuild().getTextChannelById(channel).getName() + " Channel");
                    embed.addField(" ",
                            "**Channel : **" + channels.getAsMention() + "\n" +
                                    "**Description : ** channel permission has been reset"
                            , false);

                    embed.setFooter("\uD83D\uDCC6 " + format.format(date));
                    event.getChannel().sendMessage(embed.build()).queue();

                } catch (NullPointerException | NoSuchElementException | NumberFormatException error) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(Color.red.brighter());
                    embed.setTitle("I can't find channel! (ง •̀_•́)ง");
                    embed.setDescription("Make sure you're put correct channel");

                    event.getChannel().sendMessage(embed.build()).queue();
                    error.printStackTrace();
                } catch (Exception exception) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(Color.red.brighter());
                    embed.setTitle("Something went wrong! (ง •̀_•́)ง");
                    embed.setDescription("Error: " + exception.getMessage());

                    event.getChannel().sendMessage(embed.build()).queue();
                    exception.printStackTrace();
                }
            } else if (type.equalsIgnoreCase("read")) {
                try {

                    final Pattern regexNotChars = Pattern.compile("\\p{Punct}");
                    final Matcher matcherNotChars = regexNotChars.matcher(channel);

                    final Pattern regexIsChar = Pattern.compile("-?[A-Za-z]+");
                    final Matcher matcherIsChars = regexIsChar.matcher(channel);

                    if (matcherNotChars.find()) {
                        channel = channel.replace("<", "").replace("#", "").replace(">", "");
                    } else if (matcherIsChars.find()) {
                        channel = event.getGuild().getTextChannelsByName(channel, false).get(0).getId();
                    }

                    // TextChannel
                    TextChannel channels = event.getGuild().getTextChannelById(channel);
                    //ArrayList<ServerTextChannel> list = new ArrayList<>(Integer.parseInt(args.get(0)));

                    EmbedBuilder embed = new EmbedBuilder();
                    assert channels != null;

                    int Slowmode = channels.getSlowmode();
                    String SlowmodeFormat = " ";
                    if (Slowmode >= 3600) {
                        Slowmode = (channels.getSlowmode() / (60 * 60));
                        SlowmodeFormat = Slowmode + "h";
                    } else if (Slowmode >= 120) {
                        Slowmode = (channels.getSlowmode() / 60);
                        SlowmodeFormat = Slowmode + "min";
                    } else if (Slowmode <= 60) {
                        Slowmode = channels.getSlowmode();
                        SlowmodeFormat = Slowmode + "sec";
                    }

                    embed.setColor(Color.GREEN.brighter());
                    embed.setTitle(event.getGuild().getTextChannelById(channel).getName() + " Channel");
                    embed.setDescription(new MessageUtils(
                            "**⬩ NSFW :** " + (channels.isNSFW() ? "Enabled **:enable:**" : "Disabled **:disable:**") + "\n" +
                                    "**⬩ Slowmode :** " + SlowmodeFormat
                    ).EmojisHolder());
                    if (channels.getManager().getChannel().getPermissionOverrides().stream().noneMatch(any -> any.getManager().getAllowedPermissions().isEmpty() && any.getManager().getDeniedPermissions().isEmpty())) {

                        String isNullEnabled = "NOT LONGER";
                        String isNullDisabled = "NOT LONGER";

                        if (channels.getManager().getChannel().getPermissionOverrides().stream().noneMatch(any -> any.getManager().getAllowedPermissions().isEmpty())) {
                            isNullEnabled = channels.getManager().getChannel().getPermissionOverrides().stream().filter(f -> Objects.requireNonNull(f.getManager().getRole()).isPublicRole()).map(map -> map.getManager().getAllowedPermissions().stream().map(Permission::getName).collect(Collectors.joining(" ✅\n> ⬩ "))).collect(Collectors.joining(" ✅\n")) + " ✅";
                        }
                        if (!channels.getManager().getChannel().getPermissionOverrides().stream().allMatch(any1 -> any1.getManager().getDeniedPermissions().isEmpty() && any1.getManager().getDeniedPermissions().isEmpty())) { // name is null
                            isNullDisabled = channels.getManager().getChannel().getPermissionOverrides().stream().filter(f -> Objects.requireNonNull(f.getManager().getRole()).isPublicRole()).map(map -> map.getManager().getDeniedPermissions().stream().map(Permission::getName).collect(Collectors.joining(" ❌\n> ⬩ "))).collect(Collectors.joining(" ❌\n")) + " ❌";
                        }

                        embed.addField("**Permissions:**", "\n | Enabled \n> ⬩ " + isNullEnabled + " \n | Disabled \n> ⬩ " + isNullDisabled, false);
                    } else {
                        embed.addField("**Permissions:**", "NOT LONGER", false);
                    }
                    event.getChannel().sendMessage(embed.build()).queue();

                } catch (NullPointerException | NoSuchElementException | IndexOutOfBoundsException | NumberFormatException e) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(Color.red.brighter());
                    embed.setTitle("I can't find channel! (ง •̀_•́)ง");
                    embed.setDescription("Make sure you're put correct channel, " + e.getMessage());

                    event.getChannel().sendMessage(embed.build()).queue();
                    e.printStackTrace();
                }
            } else {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.red.brighter());
                embed.setTitle("I can't find type!  (ง •̀_•́)ง");
                embed.setDescription("**Types :** \n" +
                        "> `r!" + getInVoke() + "`" + " add <permission> <channel>" + "\n" +
                        "> `r!" + getInVoke() + "`" + " remove <permission> <channel>" + "\n" +
                        "> `r!" + getInVoke() + "`" + " reset <channel>" + "\n" +
                        "> `r!" + getInVoke() + "`" + " read <channel>" + "\n"
                );

                event.getChannel().sendMessage(embed.build()).queue();
            }
        } catch (IndexOutOfBoundsException | NullPointerException | NoSuchElementException e) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red.brighter());
            embed.setTitle("I can't find it!  (ง •̀_•́)ง");
            StringBuilder string = embed.getDescriptionBuilder();

            string.append("**Usage : **" + getHelp()).append("\n");
            string.append("**Types:** mode / resst / read").append("\n");
            string.append("**Modes:** viewer / global / operators").append("\n");

            event.getChannel().sendMessage(embed.build()).queue();
            e.printStackTrace();
        } catch (InsufficientPermissionException e) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red.brighter());
            embed.setTitle("Missing Permission! (＃￣ω￣)");
            embed.setDescription(e.getMessage());

            event.getChannel().sendMessage(embed.build()).queue();
            e.printStackTrace();
        }
    }

    // # command args0 args1 args2
    // # command type channel permission
    @Override
    public String getHelp() {
        return "r!" + getInVoke() + " <type> <channel>";
    }

    @Override
    public String getInVoke() {
        return "channelperm";
    }

    @Override
    public String getDescription() {
        return "mode/remove/reset/read channel permissions";
    }

    @Override
    public Permission getPermission() {
        return Permission.MANAGE_CHANNEL;
    }

    @Override
    public Category getCategory() {
        return Category.MODERATOR;
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
}
