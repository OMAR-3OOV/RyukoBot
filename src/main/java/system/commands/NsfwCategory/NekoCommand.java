package system.commands.NsfwCategory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.Objects.Category;
import system.Objects.Command;
import system.Objects.TextUtils.MessageUtils;
import system.Objects.Utils.nfswutils.Nekos.NekosTags;
import system.Objects.Utils.nfswutils.Nekos.NekosUtil;
import system.Objects.Utils.chatbot.Emotions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class NekoCommand implements Command {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {
        try {

            List<String> handlers = new ArrayList<>(args);

            NekosUtil nekos = new NekosUtil();

            EmbedBuilder embed = new EmbedBuilder();

            if (handlers.isEmpty()) {

                // Getting Image from url
                URLConnection urlConnection = new URL(Objects.requireNonNull(nekos.getRandomImage())).openConnection();
                urlConnection.addRequestProperty("User-Agent", "Ryuko");

                BufferedImage image = ImageIO.read(urlConnection.getInputStream());

                // Getting pixel color by position x and y
                int clr = image.getRGB(image.getMinX(), image.getMinY());
                int red = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue = clr & 0x000000ff;

                embed.setColor(new Color(red, green, blue));
                embed.setTitle("Here is a random `" + nekos.getTagName() + "` for you " + Emotions.getRandomLove());
                embed.setImage(nekos.getRandomImage());
                embed.setFooter(new SimpleDateFormat("EEE, dd MMM yyyy").format(new Date()));
            } else if (handlers.get(0).equalsIgnoreCase("tags")) {
                embed.setTitle("Neko's tags that you can use to get an image");
                embed.setDescription(Arrays.stream(NekosTags.values()).map(NekosTags::getTag).collect(Collectors.joining("\n")));
                embed.setColor(new Color(190, 190, 190));
            } else {

                String tag = handlers.stream().collect(Collectors.joining(""));
                if (!NekosTags.isNekoTag(tag)) {
                    event.getChannel().sendMessage(new MessageUtils(":error: this isn't a tag, " + event.getAuthor().getAsMention()).EmojisHolder()).queue();
                    return;
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

                embed.setColor(new Color(red, green, blue));
                embed.setTitle("Here is `" + tag + "` for you " + Emotions.getRandomLove());
                embed.setImage(nekos.getImage(tag));
                embed.setFooter(new SimpleDateFormat("EEE, dd MMM yyyy").format(new Date()));
            }

            event.getChannel().sendMessage(embed.build()).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "r!neko <tag>";
    }

    @Override
    public String getInVoke() {
        return "neko";
    }

    @Override
    public String getDescription() {
        return "to send some images";
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
