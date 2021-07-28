package system.listener;

import com.fasterxml.jackson.databind.*;
import me.duncte123.botcommons.web.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import system.objects.Utils.guildconfigutils.*;

import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

public class AIEvents extends ListenerAdapter {

    public static final List<Message> MessagesDeleted = new ArrayList<>();
    private HashMap<Message.Attachment, EmbedBuilder> embeds = new HashMap<Message.Attachment, EmbedBuilder>();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        GuildsBuilder guildsBuilder = new GuildsBuilder(event.getGuild());

        if ((guildsBuilder.getProperties().getProperty("animeFinder") != null) && guildsBuilder.getProperties().getProperty("animeFinder").equals(event.getChannel().getId())) {

            if (event.getAuthor().isBot()) return;
            if (event.getAuthor().isSystem()) return;

            Queue<Message.Attachment> attachmentQueue = new LinkedList<>(event.getMessage().getAttachments());

            try {

                Message.Attachment attachment = attachmentQueue.remove();
                System.out.println(attachment);

                embeds.put(attachment, new EmbedBuilder());

                embeds.forEach((attachments, embeded) -> {
                    WebUtils.ins.getJSONObject("https://api.trace.moe/search?anilistInfo&url=<url>".replace("<url>", attachments.getUrl())).async(anime -> {
                        EmbedBuilder embed = embeded;

                        JsonNode docs = anime.get("result").get(0);
                        JsonNode anilist = docs.get("anilist");

                        String name = (anilist.get("title").isNull() ? "Title undefined" : anilist.get("title").get("english").asText());
                        String episode = (docs.get("episode").isNull() ? "`none`" : docs.get("episode").asText());
                        String nfsw = (anilist.get("isAdult").asBoolean() ? "Yes" : "No");

                        if ((anilist.get("isAdult").asBoolean() == true) && event.getChannel().isNSFW() == false) {
                            event.getChannel().sendMessage("It's nfsw episode please make sure to turn on the nfsw option on the channel!").queue();
                            return;
                        }

                        // Getting Image from url
                        URLConnection urlConnection = null;
                        try {
                            urlConnection = new URL(Objects.requireNonNull(docs.get("image").asText())).openConnection();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        urlConnection.addRequestProperty("User-Agent", "Ryuko");

                        BufferedImage image = null;
                        try {
                            image = ImageIO.read(urlConnection.getInputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // Getting pixel color by position x and y
                        int clr = image.getRGB(image.getMinX(), image.getMinY());
                        int red = (clr & 0x00ff0000) >> 16;
                        int green = (clr & 0x0000ff00) >> 8;
                        int blue = clr & 0x000000ff;

                        embed.setColor(new Color(red, green, blue));
                        embed.setTitle(name);
                        embed.setImage(docs.get("image").asText());

                        embed.addField("Information", "> Episode : " + episode + "\n> Nfsw : " + nfsw, false);

                        event.getChannel().sendMessage(embed.build()).queue(done -> {
                            System.out.println("done");
                            embeds.remove(attachments);
                        });

                    }, (error) -> {
                        event.getChannel().sendMessage(event.getAuthor().getAsMention() + " please try to send it again while there is no one using me!").queue();
                        event.getMessage().delete().queue();
                    });
                });
            } catch (Exception ignored) {

            }

        }
    }
}
