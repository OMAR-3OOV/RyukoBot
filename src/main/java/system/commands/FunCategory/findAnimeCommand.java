package system.commands.FunCategory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.objects.Category;
import system.objects.Command;
import system.objects.Utils.profileconfigutils.animeFinderUtil;

import java.awt.*;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class findAnimeCommand implements Command {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {
        EmbedBuilder embed = new EmbedBuilder();

        try {
            List<String> handlers = new ArrayList<>(args);

            animeFinderUtil animeFinderUtil = new animeFinderUtil(event.getGuild(), event.getChannel());

            if (handlers.isEmpty() || handlers.get(0).equalsIgnoreCase("start")) {
                if (!animeFinderUtil.has()) {
                    embed.setColor(new Color(156, 255, 157));
                    embed.setTitle("Anime finder has been set on this channel!");
                    embed.setDescription("You just need to send a anime picture and i will try to find it for you :3!");
                    embed.setFooter("Date : " + new SimpleDateFormat("EEEE, dd MMMM yyyy").format(new Date()));

                    animeFinderUtil.setup();
                } else {
                    embed.setColor(new Color(156, 243, 255));
                    embed.setTitle("Anime finder is already set on this channel!");
                    embed.setDescription("If you want to change the channel use `r!findanime clear` !");
                    embed.setFooter("Date : " + new SimpleDateFormat("EEEE, dd MMMM yyyy").format(new Date()));
                }
            } else if (handlers.get(0).equalsIgnoreCase("clear")) {
                if (animeFinderUtil.has()) {

                    embed.setColor(new Color(255, 156, 156));
                    embed.setTitle("Anime finder has been deleted!");
                    embed.setDescription("The function has been deleted! to setup again use `r!findanime`");
                    embed.setFooter("Date : " + new SimpleDateFormat("EEEE, dd MMMM yyyy").format(new Date()));

                    animeFinderUtil.clear();
                } else {
                    embed.setColor(new Color(156, 243, 255));
                    embed.setTitle("Anime finder is not set on this channel!");
                    embed.setDescription("If you want to setup the function use `r!findanime` !");
                    embed.setFooter("Date : " + new SimpleDateFormat("EEEE, dd MMMM yyyy").format(new Date()));
                }
            }
        } catch (Exception error) {
            embed.setColor(new Color(245, 115, 115));
            embed.setDescription("> there is an error on " + error.getMessage() + "!");
        }

        event.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getHelp() {
        return "r!findanime";
    }

    @Override
    public String getInVoke() {
        return "findanime";
    }

    @Override
    public String getDescription() {
        return "find any anime you want by sending the picture";
    }

    @Override
    public Permission getPermission() {
        return null;
    }

    @Override
    public Category getCategory() {
        return Category.FUN;
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
        return false;
    }
}
