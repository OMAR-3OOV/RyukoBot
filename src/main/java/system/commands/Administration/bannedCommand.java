package system.commands.Administration;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.objects.Category;
import system.objects.Command;
import system.objects.TextUtils.MessageUtils;
import system.objects.Utils.administration.BannedUtils.BannedUtils;

import java.awt.*;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class bannedCommand implements Command {

    public static HashMap<String, User> getUserByTicket = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {
        try {
            String command = args.get(0);

            if (command.isEmpty()) {
                EmbedBuilder embed = new EmbedBuilder();

                embed.setColor(new Color(236, 87, 87));
                embed.setDescription(
                        "**Usage: **" + getHelp() + "\n" +
                                "**Description: **" + getDescription() + "\n" +
                                "**Types: ** <user> / check " + "\n" +
                                "**Elapsed Time: ** second / minute / hour / day / month / year"
                );

                event.getChannel().sendMessage(embed.build()).queue();
                return;
            }

            if (command.equalsIgnoreCase("check")) {
                EmbedBuilder embed = new EmbedBuilder();
                String user = args.get(1);

                final Pattern regex = Pattern.compile(Message.MentionType.USER.getPattern().pattern());
                final Matcher matcher = regex.matcher(user);

                if (matcher.find()) {
                    user = user.replace("<", "").replace("!", "").replace("@", "").replace("#", "").replace("&", "").replace(">", "");
                } else if (matcher.find()) {
                    user = Objects.requireNonNull(event.getGuild().getMembersByName(user, true)).stream().map(m -> m.getUser().getId()).collect(Collectors.joining());
                }

                User target = Objects.requireNonNull(event.getGuild().getMemberById(user)).getUser();


                if (target.isBot() || target.isFake()) {
                    event.getChannel().sendMessage(new MessageUtils(":error: Bots is unbannable!").EmojisHolder()).queue();
                    return;
                }

                BannedUtils bannedUtils = new BannedUtils(target);
                SimpleDateFormat f = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss a");

                if (bannedUtils.getProfile().getBanned()) {
                    embed.setColor(new Color(255, 84, 84));
                    embed.setTitle(bannedUtils.getUser().getName() + " is currently Punched!");
                    embed.addField("Reason", "> " + bannedUtils.getProperties().getProperty("reason"), false);
                    embed.addField("Punched by: ", "> " + event.getJDA().getUserById(bannedUtils.getProperties().getProperty("banned-by")).getName(), false);
                    embed.addField("Banned till: ", "> " + f.format(Long.parseLong(bannedUtils.getProperties().getProperty("time-end"))), false);
                    embed.addField("Time left: ", "> " + printTimeLeft(new Date(), new Date(Long.parseLong(bannedUtils.getProperties().getProperty("time-end")))), false);

                    event.getChannel().sendMessage(embed.build()).queue();
                } else {
                    embed.setColor(new Color(226, 226, 226));
                    embed.setTitle(bannedUtils.getUser().getName() + " isn't accused!");
                    event.getChannel().sendMessage(embed.build()).queue();
                }

            } else {

                final Pattern regex = Pattern.compile(Message.MentionType.USER.getPattern().pattern());
                final Matcher matcher = regex.matcher(command);

                if (matcher.find()) {
                    command = command.replace("<", "").replace("!", "").replace("@", "").replace("#", "").replace("&", "").replace(">", "");
                } else if (matcher.find()) {
                    command = Objects.requireNonNull(event.getGuild().getMembersByName(command, true)).stream().map(m -> m.getUser().getId()).collect(Collectors.joining());
                }

                List<String> handlers = new ArrayList<>(args);
                String number = handlers.get(1);
                String time = handlers.get(2);

                StringBuilder reason = new StringBuilder();
                for (int i = 3; i < handlers.size(); i++) {
                    reason.append(handlers.get(i)).append(" ");
                }

                User target = Objects.requireNonNull(event.getGuild().getMemberById(command)).getUser();

                if (target.getId().contains(event.getAuthor().getId())) {
                    event.getChannel().sendMessage(new MessageUtils(":error: you can't banned yourself!").EmojisHolder()).queue();
                    return;
                }

                if (target.isBot() || target.isFake()) {
                    event.getChannel().sendMessage(new MessageUtils(":error: you can't banned Bot!").EmojisHolder()).queue();
                    return;
                }

                BannedUtils bannedUtils = new BannedUtils(target, event.getAuthor());

                if (bannedUtils.getProfile().getBanned()) {
                    event.getChannel().sendMessage(new MessageUtils(":error: this user is already in blacklist!").EmojisHolder()).queue();
                    return;
                }

                EmbedBuilder embed = new EmbedBuilder();

                Date date = new Date();
                SimpleDateFormat f = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss a");

                embed.setTitle("\uD83D\uDEAB You're added in blacklist");
                embed.setColor(new Color(254, 121, 121));

                long elapsedTime = 0L;

                if (time.equalsIgnoreCase("seconds") || time.equalsIgnoreCase("second") || time.equalsIgnoreCase("s")) {
                    elapsedTime = (date.getTime() + (1000L * Long.parseLong(number)));
                } else if (time.equalsIgnoreCase("minutes") || time.equalsIgnoreCase("minute") || time.equalsIgnoreCase("min")) {
                    elapsedTime = (date.getTime() + (1000L * 60 * Long.parseLong(number)));
                } else if (time.equalsIgnoreCase("hours") || time.equalsIgnoreCase("hour") || time.equalsIgnoreCase("h")) {
                    elapsedTime = (date.getTime() + (1000L * 60 * 60 * Long.parseLong(number)));
                } else if (time.equalsIgnoreCase("days") || time.equalsIgnoreCase("day") || time.equalsIgnoreCase("d")) {
                    elapsedTime = (date.getTime() + (1000L * 60 * 60 * 24 * Long.parseLong(number)));
                } else if (time.equalsIgnoreCase("weeks") || time.equalsIgnoreCase("week") || time.equalsIgnoreCase("w")) {
                    elapsedTime = (date.getTime() + (1000L * 60 * 60 * 24 * 7 * Long.parseLong(number)));
                } else if (time.equalsIgnoreCase("months") || time.equalsIgnoreCase("month") || time.equalsIgnoreCase("m")) {
                    elapsedTime = (date.getTime() + (1000L * 60 * 60 * 24 * 31 * Long.parseLong(number)));
                } else if (time.equalsIgnoreCase("years") || time.equalsIgnoreCase("year") || time.equalsIgnoreCase("y")) {
                    elapsedTime = (date.getTime() + (1000L * 60 * 60 * 24 * 365 * Long.parseLong(number)));
                } else {
                    event.getChannel().sendMessage(new MessageUtils(":error: This is not elapsed time there's only this elapses : `minute / hour / week / month / year`").EmojisHolder()).queue();
                    return;
                }

                long finalElapsedTime = elapsedTime;
                StringBuilder builder = embed.getDescriptionBuilder();

                target.openPrivateChannel().queue(t -> {
                    builder.append("**Expiration date : **").append(printTimeLeft(date, new Date(finalElapsedTime))).append("\n");
                    builder.append("**Your Banned Ticket: **").append("`").append(bannedUtils.getRsa().getKey()).append("`").append("\n");
                    builder.append("**Banned by: **").append(event.getAuthor().getName()).append("\n \n");

                    if (!reason.toString().isEmpty()) {
                        builder.append("**Reason: **").append(reason).append("\n");
                    }

                    builder.append("**You'll get unbanned till: **").append(f.format(new Date(finalElapsedTime)));
                    t.sendMessage(embed.build()).queue();
                    bannedUtils.getProfile().getProfileProperties().setProperty("banned-ticket", bannedUtils.getRsa().getKey());
                    bannedUtils.build(date, new Date(finalElapsedTime), bannedUtils.getRsa().getKey(), reason.toString());
                });

                EmbedBuilder e = new EmbedBuilder();
                e.setTitle(target.getName() + " has been added to blacklist");

                StringBuilder be = e.getDescriptionBuilder();
                be.append("**Banned Ticket: **").append(bannedUtils.getRsa().getKey()).append("\n");
                be.append("**banned till: **").append(f.format(new Date(finalElapsedTime))).append("\n");
                be.append("**banned Time: **").append(printTimeLeft(new Date(), new Date(finalElapsedTime))).append("\n");

                if (!reason.toString().isEmpty()) {
                    be.append("**Reason: **").append(reason).append("\n");
                }

                be.append("**User ID: **").append(target.getId()).append("\n");
                be.append("**Banned by: **").append(event.getAuthor().getName()).append("\n");

                e.setFooter("\uD83D\uDDD3️ Date: " + new SimpleDateFormat("EEEE, dd MMMM yyyy").format(new Date()));

                event.getChannel().sendMessage(e.build()).queue();

                getUserByTicket.put(bannedUtils.getRsa().getKey(), target);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "r!banned";
    }

    @Override
    public String getInVoke() {
        return "banned";
    }

    @Override
    public String getDescription() {
        return "this command will add the user in blacklist!";
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

    public String printTimeLeft(Date dateStart, Date dateEnd) {

        StringBuilder builder = new StringBuilder();
        long different = dateEnd.getTime() - dateStart.getTime();

        long seconds = 1000;
        long minutes = seconds * 60;
        long hours = minutes * 60;
        long days = hours * 24;
        long weeks = days * 7;
        long months = (long) (days * 30);
        long years = days * 365;

        long elapsedY = different / years;

        different = different % years;
        long elapsedM = different / months;

        different = different % months;
        long elapsedW = different / weeks;

        different = different % weeks;
        long elapsedD = different / days;

        different = different % days;
        long elapsedH = different / hours;

        different = different % hours;
        long elapsedMIN = different / minutes;

        different = different % minutes;
        long elapsedS = different / seconds;

        // System.out.println(elapsedY + " years," + elapsedM + " month, " + elapsedM + " weeks, " + elapsedD + "  days, " + elapsedH + " hours");

        builder.append(elapsedY > 0 ? elapsedY + " year" + (elapsedY > 1 ? "s " : " ") : " ");
        builder.append(elapsedM > 0 ? elapsedM + " month" + (elapsedM > 1 ? "s " : " ") : " ");
        builder.append(elapsedW > 0 ? elapsedW + " week" + (elapsedW > 1 ? "s " : " ") : " ");
        builder.append(elapsedD > 0 ? elapsedD + " day" + (elapsedD > 1 ? "s " : " ") : " ");
        builder.append(elapsedH > 0 ? elapsedH + " hour" + (elapsedH > 1 ? "s " : " ") : " ");
        builder.append(elapsedMIN > 0 ? elapsedMIN + " minute" + (elapsedMIN > 1 ? "s " : " ") : " ");
        builder.append(elapsedS > 0 ? elapsedS + " second" + (elapsedS > 1 ? "s " : " ") : " ");

        return builder.toString();
    }
}
