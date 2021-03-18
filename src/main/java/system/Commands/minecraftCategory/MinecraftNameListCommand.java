package system.Commands.minecraftCategory;

import checkers.units.quals.C;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.Objects.Category;
import system.Objects.Command;
import system.Objects.TextUtils.MessageUtils;

import java.awt.*;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MinecraftNameListCommand implements Command {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {
        String MinecraftName = args.get(0);

        WebUtils.ins.getJSONObject("https://api.mojang.com/users/profiles/minecraft/"+MinecraftName).async(name -> {
            String uuid = name.get("id").asText();
            WebUtils.ins.getJSONArray("https://api.mojang.com/user/profiles/"+uuid+"/names").async(namelist -> {

                EmbedBuilder embed = new EmbedBuilder();
                embed.setDescription("**__Here " + MinecraftName  + "'s Minecraft list!__**");

                embed.setColor(new Color(12, 255, 0));
                for (int i = 0; i < namelist.size(); i++) {
                    if (i > 0) {
                        StringBuilder builder = new StringBuilder();

                        if (namelist.get(i).get("changedToAt").asLong() == 0L) {
                            builder.append("`-`");
                        } else {
                            Date date = new Date(namelist.get(i).get("changedToAt").asLong());
                            DateFormat format = new SimpleDateFormat("EEEE, dd MMM yyyy, HH:mm:ss");
                            builder.append(format.format(date));
                        }
                        embed.addField("> " + namelist.get(i).get("name").asText(), " Date: " + builder.toString(), false);
                    } else {
                        embed.addField("> " + namelist.get(i).get("name").asText(), " First name", false);
                    }
                }

                event.getChannel().sendMessage(embed.build()).queue();
            }, e -> event.getChannel().sendMessage(new MessageUtils(":error: this minecraft name is wrong!, " + e.getMessage()).EmojisHolder()).queue());
        },e -> event.getChannel().sendMessage(new MessageUtils(":error: this minecraft name is wrong!").EmojisHolder()).queue());
    }

    @Override
    public String getHelp() {
        return "r!mc <user>";
    }

    @Override
    public String getInVoke() {
        return "mc";
    }

    @Override
    public String getDescription() {
        return "namemc command";
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
