package system.Commands.minecraftCategory;

import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.Objects.Category;
import system.Objects.Command;
import system.Objects.Config;
import system.Objects.TextUtils.MessageUtils;
import system.Objects.Utils.BarTaskUtil;
import system.Objects.Utils.BarUtil;

import java.awt.*;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class HypixelGuildCommand implements IGuildLeveling, Command {


    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {
        /**
         * Url api : https://api.hypixel.net/guild?name={guildName}&key={API_KEY}
         */
        try {
            List<String> handlers = new ArrayList<>(args);
            String APIKEY = Config.get("HYPIXEL-API-KEY");

            event.getChannel().sendTyping().queue();
            Message msg = event.getChannel().sendMessage(new MessageUtils("Checking :loading:").EmojisHolder()).complete();

            if (handlers.isEmpty()) {
                msg.editMessage(new MessageUtils(":error: you should use write guild name! `Usage: " + getHelp()).EmojisHolder()).queue();
                return;
            }

            SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");

            WebUtils.ins.getJSONObject(new StringBuilder().append("https://api.hypixel.net/guild?name=").append(String.join("%20", handlers)).append("&key=").append(APIKEY).toString() ).async(guild -> {

                AtomicInteger onlineCount = new AtomicInteger(0);
                EmbedBuilder embed = new EmbedBuilder();
                Color c = new Color(209, 206, 206);

                for (int i = 0; i < guild.get("guild").get("members").size(); i++) {
                    WebUtils.ins.getJSONObject(new StringBuilder().append("https://api.hypixel.net/status?key=").append(APIKEY).append("&uuid=").append(guild.get("guild").get("members").get(i).get("uuid").asText()).toString()).async(user -> {
                        if (user.get("session").get("online").asBoolean()) {
                            onlineCount.addAndGet(1);
                        }
                    }, error -> System.out.println(error.getMessage()));
                }

                switch (guild.get("guild").get("tagColor").asText()) {
                    case "GRAY":
                        c = new Color(108, 108, 108);
                        break;
                    case "DARK_AQUA":
                        c = new Color(0, 107, 107);
                        break;
                    case "DARK_GREEN":
                        c = new Color(0, 91, 0);
                        break;
                    case "YELLOW":
                        c = new Color(255, 225, 0);
                        break;
                    default:
                        c = new Color(0, 0, 0);
                        break;
                }

                Color finalC = c;
                BarUtil.instance.build(builder -> {
                    builder.setBarIndex(10);
                    builder.setTask((float) IGuildLeveling.getLevel(guild.get("guild").get("exp").asDouble()));
                    builder.setTotal((float) IGuildLeveling.getPercentageToNextLevel(guild.get("guild").get("exp").asDouble()));

                    float total = (builder.getTask() / builder.getTotal()) % builder.getBarIndex();

                    embed.setColor(finalC.getRGB());
                    embed.setTitle(guild.get("guild").get("name").asText() + (guild.get("guild").get("tag")==null?"":" [ " + guild.get("guild").get("tag").asText() + " ]"));
                    embed.setDescription("**Guild Description : **" + (guild.get("guild").get("description")==null?"`Empty description`":guild.get("guild").get("description").asText()));
                    embed.addField("Created", "> " + format.format(new Date(guild.get("guild").get("created").asLong())), true);
                    embed.addField("Members", "> " + guild.get("guild").get("members").size(), true);
                    embed.addBlankField(true);
                    embed.addField("Games Exp", "> " + (guild.get("guild").get("guildExpByGameType")==null?"empty":guild.get("guild").get("guildExpByGameType").size()), true);
                    embed.addField("Guild Level", "> " + IGuildLeveling.getLevel(guild.get("guild").get("exp").asInt()) + "\n> " + builder.displayTaskBar(total, builder.getBarIndex()) + " **[ " + Math.round(IGuildLeveling.getPercentageToNextLevel(guild.get("guild").get("exp").asDouble())*100) + "% ]**", true);
                    embed.addBlankField(true);
                    embed.addField("Online Members", "> " + onlineCount.get(), true);
                });

                msg.editMessage(new MessageUtils(":successful: " + handlers.stream().collect(Collectors.joining(" ")) + " guild has been find!, " + event.getAuthor().getAsMention()).EmojisHolder()).queue();
                msg.editMessage(embed.build()).queue();
            }, error -> msg.editMessage(new MessageUtils(":error: this guild is not exist").EmojisHolder()).queue());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Message : " + e.getMessage());
        }
    }

    @Override
    public String getHelp() {
        return "r!hypixelguild <guild-name>";
    }

    @Override
    public String getInVoke() {
        return "hypixelguild";
    }

    @Override
    public String getDescription() {
        return "to get hypixel guilds information";
    }

    @Override
    public Permission getPermission() {
        return null;
    }

    @Override
    public Category getCategory() {
        return Category.MINECRAFT;
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
