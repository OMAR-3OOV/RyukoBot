package system.Commands.NsfwCategory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.kodehawa.lib.imageboards.DefaultImageBoards;
import net.kodehawa.lib.imageboards.entities.BoardImage;
import system.Objects.Category;
import system.Objects.Command;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Rule34Command implements Command {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {
        try {

            List<String> handlers = new ArrayList<>(args);

            EmbedBuilder embed = new EmbedBuilder();

            if (handlers.isEmpty()) {
                BoardImage img = DefaultImageBoards.RULE34.get().blocking().get((int) (Math.random() * DefaultImageBoards.RULE34.get().blocking().size()));

                // Getting Image from url
                URLConnection urlConnection = new URL(img.getURL()).openConnection();
                urlConnection.addRequestProperty("User-Agent", "Ryuko");

                BufferedImage image = ImageIO.read(urlConnection.getInputStream());

                // Getting pixel color by position x and y
                int clr = image.getRGB(image.getMinX(), image.getMinY());
                int red = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue = clr & 0x000000ff;

                embed.setColor(new Color(red, green, blue));
                embed.setTitle("Here is rule34 image for you", img.getURL());
                embed.setImage(img.getURL());
                embed.setFooter("Score: " + img.getScore() + " | Rating: " + img.getRating());

            } else {
                BoardImage img = DefaultImageBoards.RULE34.search(handlers.get(0)).blocking().get((int) (Math.random() * DefaultImageBoards.RULE34.search(handlers.get(0)).blocking().size()));

                // Getting Image from url
                URLConnection urlConnection = new URL(img.getURL()).openConnection();
                urlConnection.addRequestProperty("User-Agent", "Ryuko");

                BufferedImage image = ImageIO.read(urlConnection.getInputStream());

                // Getting pixel color by position x and y
                int clr = image.getRGB(image.getMinX(), image.getMinY());
                int red = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue = clr & 0x000000ff;

                embed.setColor(new Color(red, green, blue));
                embed.setTitle("Here is rule34 image for you", img.getURL());
                embed.setImage(img.getURL());
                embed.setFooter("Score: " + img.getScore() + " | Rating: " + img.getRating());
            }

            event.getChannel().sendMessage(embed.build()).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "r!r34";
    }

    @Override
    public String getInVoke() {
        return "r34";
    }

    @Override
    public String getDescription() {
        return "To get rule34 pictures";
    }

    @Override
    public Permission getPermission() {
        return null;
    }

    @Override
    public Category getCategory() {
        return Category.NSFW;
    }

    @Override
    public Boolean Lockdown() {
        return false;
    }

    @Override
    public Boolean isNsfw() {
        return true;
    }

    @Override
    public Boolean diplayCommand() {
        return true;
    }
}
