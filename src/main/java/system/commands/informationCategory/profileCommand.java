package system.commands.informationCategory;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.Objects.Category;
import system.Objects.Command;
import system.Objects.TextUtils.MessageUtils;
import system.Objects.Utils.achievementsutils.AchievementsManager;
import system.Objects.Utils.profileconfigutils.ProfileBuilder;
import system.Objects.Utils.coinsManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class profileCommand implements Command {

    public static HashMap<User, TextChannel> verify = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {
        try {
            String user = null;

            if (!args.isEmpty()) {
                user = args.get(0);
            }

            if (user == null) {
                ProfileBuilder profile = new ProfileBuilder(event.getAuthor());
                coinsManager ruko = new coinsManager(event.getAuthor());

                EmbedBuilder embed = new EmbedBuilder();

                Message message = null;
                if (profile.getTimeCraete().getTime() != 0) {

                    if (profile.getUsername().contains(event.getAuthor().getName())) {
                        profile.setUsername(event.getAuthor().getName());
                    }

                    AchievementsManager achievementsManager = new AchievementsManager(event.getAuthor());

                    // Getting Image from url
                    URLConnection urlConnection = new URL(Objects.requireNonNull(event.getAuthor().getAvatarUrl())).openConnection();
                    urlConnection.addRequestProperty("User-Agent", "Ryuko");

                    BufferedImage image = ImageIO.read(urlConnection.getInputStream());
                    BufferedImage banner = ImageIO.read(new File("image/banner.png"));

                    // Getting pixel color by position x and y
                    int clr = image.getRGB(image.getMinX(), image.getMinY());
                    int red = (clr & 0x00ff0000) >> 16;
                    int green = (clr & 0x0000ff00) >> 8;
                    int blue = clr & 0x000000ff;

                    String isbanned = "";
                    if (profile.getBanned()) {
                        isbanned = " `BANNED`";
                    }

                    embed.setThumbnail(event.getAuthor().getAvatarUrl());
                    embed.setTitle(profile.getUsername() + " Profile" + isbanned, event.getAuthor().getAvatarUrl());

                    embed.setColor(new Color(red, green, blue));
                    embed.addField("\uD83D\uDC64 | ⬩ Username", "> " + profile.getUsername(), true);
                    embed.addBlankField(true);
                    embed.addField("\uD83D\uDCB3 | ⬩ ID", "> " + profile.getUserId(), true);
                    embed.addField("\uD83C\uDF10 | ⬩ Language", "> " + profile.getLanguage(), true);
                    embed.addBlankField(true);
                    embed.addField("\uD83D\uDD30 | ⬩ Rank", "> " + profile.getRank(), true);
                    embed.addField("\uD83C\uDFAE | ⬩ Games count", "> " + String.valueOf(profile.getGamesCount()), true);
                    embed.addBlankField(true);
                    embed.addField("\uD83D\uDDE8️ | ⬩ Last Time command uses", "> " + (profile.getLastTimeCommandUse().getTime()>0?TimeAgo.using(profile.getLastTimeCommandUse().getTime()):"`Didn't use any command yet!`"), true);
                    embed.addField("\uD83D\uDD39 | ⬩ Level", "> " + String.valueOf(profile.getLevel()), true);
                    embed.addBlankField(true);
                    embed.addField(new MessageUtils(":ruko: | ⬩ Ruko").EmojisHolder(), "> " + String.valueOf(profile.getRuko()), true);
                    embed.addField("✅ | ⬩ Verify", "> " + String.valueOf(profile.getVerify()), true);

                    embed.setFooter("\uD83D\uDDD3️ | ⬩ Created : " + new SimpleDateFormat("EEEE, dd MMM yyyy").format(profile.getTimeCraete()));
                    message = event.getChannel().sendMessage(embed.build()).complete();

                    if (!profile.getVerify()) {
                        StringBuilder disc = embed.getDescriptionBuilder();

                        disc.append("```yml").append("\n");
                        disc.append("please make sure to verify your account by using [ r!verify ]").append("\n");
                        disc.append("```").append("\n \n");
                    } else {
                        achievementsManager.buildUsers();
                    }

                } else {

                    Date date = new Date();
                    StringBuilder disc = embed.getDescriptionBuilder();

                    profile.setTimeCraete(date);
                    profile.setLanguage("english");
                    profile.setRuko(ruko.getRukoUser());
                    profile.setRank("Member");

                    embed.setColor(new Color(220, 220, 220));

                    disc.append("> **__Your profile has been created__**").append("\n \n");
                    disc.append("```yml").append("\n");
                    disc.append("Note : there is more information you can setup by tap on the emoji below").append("\n");
                    disc.append("```");

                    embed.addField("\uD83D\uDC64 | ⬩ Username", "> " + profile.getUsername(), true);
                    embed.addBlankField(true);
                    embed.addField("\uD83D\uDCB3 | ⬩ ID", "> " + profile.getUserId(), true);

                    embed.setFooter("\uD83D\uDDD3️ Date : " + new SimpleDateFormat("EEEE, dd MMM yyyy").format(date));
                    message = event.getChannel().sendMessage(embed.build()).complete();
                    message.addReaction("✅").queue();

                    verify.put(event.getAuthor(), event.getChannel());
                }

                message.editMessage(embed.build()).queue();
            } else {

                final Pattern regex = Pattern.compile(Message.MentionType.USER.getPattern().pattern());
                final Matcher matcher = regex.matcher(user);

                if (matcher.find()) {
                    user = user.replace("<", "").replace("!", "").replace("@", "").replace("#", "").replace("&", "").replace(">", "");
                } else if (matcher.find()) {
                    user = Objects.requireNonNull(event.getGuild().getMembersByName(user, true)).stream().map(m -> m.getUser().getId()).collect(Collectors.joining());
                }

                User target = Objects.requireNonNull(event.getGuild().getMemberById(user)).getUser();
                ProfileBuilder profile = new ProfileBuilder(target);
                coinsManager ruko = new coinsManager(target);

                EmbedBuilder embed = new EmbedBuilder();

                Message message = null;
                if (profile.getTimeCraete().getTime() != 0) {

                    if (profile.getUsername().contains(event.getAuthor().getName())) {
                        profile.setUsername(event.getAuthor().getName());
                    }

                    // Getting Image from url
                    URLConnection urlConnection = new URL(Objects.requireNonNull(event.getAuthor().getAvatarUrl())).openConnection();
                    urlConnection.addRequestProperty("User-Agent", "Ryuko");

                    BufferedImage image = ImageIO.read(urlConnection.getInputStream());

                    // Getting pixel color by position x and y
                    int clr = image.getRGB(image.getMinX(), image.getMinY());
                    int red = (clr & 0x00ff0000) >> 16;
                    int green = (clr & 0x0000ff00) >> 8;
                    int blue = clr & 0x000000ff;

                    String isbanned = "";
                    if (profile.getBanned()) {
                        isbanned = " `BANNED`";
                    }

                    embed.setTitle(profile.getUsername() + " Profile" + isbanned, target.getAvatarUrl());

                    embed.setColor(new Color(red, green, blue));
                    embed.addField("\uD83D\uDC64 | ⬩ Username", "> " + profile.getUsername(), true);
                    embed.addBlankField(true);
                    embed.addField("\uD83D\uDCB3 | ⬩ ID", "> " + profile.getUserId(), true);
                    embed.addField("\uD83C\uDF10 | ⬩ Language", "> " + profile.getLanguage(), true);
                    embed.addBlankField(true);
                    embed.addField("\uD83D\uDD30 | ⬩ Rank", "> " + profile.getRank(), true);
                    embed.addField("\uD83C\uDFAE | ⬩ Games count", "> " + String.valueOf(profile.getGamesCount()), true);
                    embed.addBlankField(true);
                    embed.addField("\uD83D\uDDE8️ | ⬩ Last Time command uses", "> " + (profile.getLastTimeCommandUse().getTime()>0?TimeAgo.using(profile.getLastTimeCommandUse().getTime()):"`Didn't use any command yet!`"), true);
                    embed.addField("\uD83D\uDD39 | ⬩ Level", "> " + String.valueOf(profile.getLevel()), true);
                    embed.addBlankField(true);
                    embed.addField(new MessageUtils(":ruko: | ⬩ Ruko").EmojisHolder(), "> " + String.valueOf(profile.getRuko()), true);
                    embed.addField("✅ | ⬩ Verify", "> " + String.valueOf(profile.getVerify()), true);

                    embed.setFooter("\uD83D\uDDD3️ | ⬩ Created : " + new SimpleDateFormat("EEEE, dd MMM yyyy").format(profile.getTimeCraete()) + " | Requested by: " + event.getAuthor().getName());
                    message = event.getChannel().sendMessage(embed.build()).complete();

                    if (!profile.getVerify()) {
                        StringBuilder disc = embed.getDescriptionBuilder();

                        disc.append("```yml").append("\n");
                        disc.append("Note : this user is not verified").append("\n");
                        disc.append("```").append("\n \n");
                    }

                } else {

                    Date date = new Date();
                    StringBuilder disc = embed.getDescriptionBuilder();

                    profile.setTimeCraete(date);
                    profile.setLanguage("english");
                    profile.setRuko(ruko.getRukoUser());
                    profile.setRank("Member");

                    embed.setColor(new Color(220, 220, 220));

                    embed.addField("\uD83D\uDC64 | ⬩ Username", "> " + profile.getUsername(), true);
                    embed.addBlankField(true);
                    embed.addField("\uD83D\uDCB3 | ⬩ ID", "> " + profile.getUserId(), true);
                    embed.addField("\uD83C\uDF10 | ⬩ Language", "> " + profile.getLanguage(), true);
                    embed.addBlankField(true);
                    embed.addField("\uD83D\uDD30 | ⬩ Rank", "> " + profile.getRank(), true);
                    embed.addField("\uD83C\uDFAE | ⬩ Games count", "> " + String.valueOf(profile.getGamesCount()), true);
                    embed.addBlankField(true);

                    if (profile.getLastTimeCommandUse().getTime() == 0L) {
                        embed.addField("\uD83D\uDDE8️ | ⬩ Last Time command uses", "> " + TimeAgo.using(profile.getLastTimeCommandUse().getTime()), true);
                    } else {
                        embed.addField("\uD83D\uDDE8️ | ⬩ Last Time command uses", "> `-`", true);
                    }

                    embed.addField("\uD83D\uDD39 | ⬩ Level", "> " + String.valueOf(profile.getLevel()), true);
                    embed.addBlankField(true);
                    embed.addField(new MessageUtils(":ruko: | ⬩ Ruko").EmojisHolder(), "> " + String.valueOf(profile.getRuko()), true);
                    embed.addField("✅ | ⬩ Verify", "> " + String.valueOf(profile.getVerify()), true);

                    embed.setFooter("\uD83D\uDDD3️ Date : " + new SimpleDateFormat("EEEE, dd MMM yyyy").format(date) + " | Requested by: " + event.getAuthor().getName());

                    if (!profile.getVerify()) {
                        disc.append("> **__").append(" ").append(target.getName()).append(" ").append("profile has been created__**").append("\n \n");
                        disc.append("```yml").append("\n");
                        disc.append("Note : this user is not verified").append("\n");
                        disc.append("```");
                    }

                    message = event.getChannel().sendMessage(embed.build()).complete();
                }

                message.editMessage(embed.build()).queue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "r!profile";
    }

    @Override
    public String getInVoke() {
        return "profile";
    }

    @Override
    public String getDescription() {
        return "check your profile information in Ryuko System";
    }

    @Override
    public Permission getPermission() {
        return null;
    }

    @Override
    public Category getCategory() {
        return Category.INFORMATION;
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
        return true;
    }
}
