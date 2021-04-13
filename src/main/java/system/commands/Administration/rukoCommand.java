package system.commands.Administration;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.Objects.Category;
import system.Objects.Command;
import system.Objects.TextUtils.MessageUtils;
import system.Objects.Utils.boosterutils.Booster;
import system.Objects.Utils.profileconfigutils.ProfileBuilder;
import system.Objects.Utils.coinsManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.List;

public class rukoCommand implements Command {

    public static HashMap<User, Message> rukoAction = new HashMap<>();
    private ProfileBuilder profile = new ProfileBuilder();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        try {
            coinsManager ruko = new coinsManager(event.getAuthor());
            String nickname = " ";

            if (!event.getGuild().getMembersByNickname(Objects.requireNonNull(event.getMember()).getNickname(), true).isEmpty()) {
                nickname = " ";
            }

            if (Objects.requireNonNull(event.getMember()).getNickname() != null) {
                nickname = " (" + Objects.requireNonNull(event.getMember()).getNickname() + ")";
            }

            EmbedBuilder embed = new EmbedBuilder();

            // Getting Image from url
            URLConnection urlConnection = new URL(Objects.requireNonNull(event.getAuthor().getAvatarUrl())).openConnection();
            urlConnection.addRequestProperty("User-Agent", "Ryuko");

            BufferedImage image = ImageIO.read(urlConnection.getInputStream());

            // Getting pixel color by position x and y
            int clr = image.getRGB(image.getMinX(), image.getMinY());
            int red =   (clr & 0x00ff0000) >> 16;
            int green = (clr & 0x0000ff00) >> 8;
            int blue =   clr & 0x000000ff;

            String boostertext = " ";
            Booster.loadBoosters();

            if (!ruko.getProperties().getProperty("booster").contains("0")) {
                boostertext = "> **Ruko Booster \uD83D\uDE80 :** " + Booster.getBooster().get(Integer.parseInt(ruko.getProperties().getProperty("Booster"))).getName() + " x" + Booster.getBooster().get(Integer.parseInt(ruko.getProperties().getProperty("Booster"))).getCombo() + "\n";
            }

            embed.setColor(new Color(red, green, blue));
            embed.setThumbnail(event.getAuthor().getAvatarUrl());
            embed.setAuthor(ruko.getUser().getName() + nickname, ruko.getUser().getAvatarUrl(),ruko.getUser().getAvatarUrl());
            embed.setDescription(new MessageUtils("\n" +
                    "> **All Ruko Gains \uD83D\uDCB0 :** " + ruko.getGains() + "\n" +
                    "> **Ruko :ruko: : **" + ruko.getRukoUser() + "\n \n" +
                    boostertext +
                    "⬩ To **Pay** for someone \uD83D\uDCB8 **|** To **Donation** for server \uD83D\uDCB0"
            ).EmojisHolder());

            Message message = event.getChannel().sendMessage(embed.build()).complete();

            message.addReaction("\uD83D\uDCB8").queue();
            message.addReaction("\uD83D\uDCB0").queue();

            rukoAction.put(ruko.getUser(), message);

        } catch (NullPointerException | NoSuchElementException | IndexOutOfBoundsException e) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red.brighter());
            embed.setTitle("I can't find it!  (ง •̀_•́)ง");
            embed.setDescription("Usage :" +
                    "\n > r!ruko pay <amount> <user>" +
                    "\n > r!ruko donation <amount>");

            event.getChannel().sendMessage(embed.build()).queue();
            e.printStackTrace();
        } catch (NumberFormatException e) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red.brighter());
            embed.setTitle("I can't find the amount!  (ง •̀_•́)ง");
            embed.setDescription("Make sure that you putting the amount `r!ruko pay <amount> <user> | r!ruko donation <amount>");

            event.getChannel().sendMessage(embed.build()).queue();
            e.printStackTrace();
        } catch (Exception e) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(Color.red.brighter());
            error.setTitle("Something went wrong!  (ง •̀_•́)ง");
            error.setDescription("Report this error in Ryuko discord server\n \n**Error :** " + e.getMessage());

            event.getChannel().sendMessage(error.build()).queue();
            e.printStackTrace();
        }

    }

    @Override
    public String getHelp() {
        return "r!ruko";
    }

    @Override
    public String getInVoke() {
        return "ruko";
    }

    @Override
    public String getDescription() {
        return "Ruko is the coins bot system you can collect when you active";
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
        return profile.getRukoSystem();
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
