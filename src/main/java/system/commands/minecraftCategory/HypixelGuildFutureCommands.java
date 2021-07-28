package system.commands.minecraftCategory;

import com.fasterxml.jackson.databind.JsonNode;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.objects.Category;
import system.objects.Command;
import system.objects.Config;
import system.objects.TextUtils.MessageUtils;

import java.awt.*;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class HypixelGuildFutureCommands implements Command {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {
        try {

            List<String> handlers = new ArrayList<>(args);
            String APIKEY = Config.get("HYPIXEL-API-KEY");

            EmbedBuilder embed = new EmbedBuilder();
            embed.setDescription("Loading");
            Message message = event.getChannel().sendMessage(embed.build()).complete();

            if (handlers.stream().anyMatch(any -> any.contains("-g"))) {

                List<String> guildname = new ArrayList<>();
                int goallevel = 0;

                for (int i = 0; i < handlers.indexOf("-g"); i++) {
                    guildname.add(handlers.get(i));
                }

                goallevel = Integer.parseInt(handlers.subList(handlers.indexOf("-g") + 1, handlers.size()).stream().findFirst().get());

                WebUtils.ins.getJSONObject("https://api.hypixel.net/guild?name=" + String.join(" ", guildname) + "&key=" + APIKEY).async(GuildObject -> {

                    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
                    HashMap<String, Integer> playerExp = new HashMap<>();
                    JsonNode guild = GuildObject.get("guild");

                    System.out.println(guild.get("name").asText());
                    System.out.println(guild.get("members").size());

                    embed.addField("Total Members: ", String.valueOf(guild.get("members").size()), false);

                    List<Integer> test = new ArrayList<>();
                    HashMap<String, List<Integer>> lists = new HashMap<>();

                    for (int i = 0; i < guild.get("members").size(); i++) {

                        Stream<Map.Entry<String, JsonNode>> de = StreamSupport.stream(Spliterators.spliteratorUnknownSize(guild.get("members").get(i).get("expHistory").fields(), 0), true);

                        System.out.println(de.collect(Collectors.toList()));
                        //System.out.println(i + ": " + de.collect(Collectors.toList()).get(0).getValue().asInt());

//                        Map.Entry<String, JsonNode> in = de.collect(Collectors.toList()).get(1);
//                        Stream<Map.Entry<String, JsonNode>> intstream = Stream.of(in);
//
//                        System.out.println(i + ": " + intstream.collect(Collectors.toList()).stream().map(m->m.getValue().asText()).collect(Collectors.joining("/")));
//                        test.add(intstream.collect(Collectors.toList()).get(1).getValue().asInt());
                    }

                    System.out.println(test.stream().sorted(Comparator.reverseOrder()).mapToInt(in -> in).sum());
                    message.editMessage(embed.build()).queue();
                }, (error) -> {
                    embed.setDescription(new MessageUtils(":error: | this guild is not exist!").EmojisHolder());
                    message.editMessage(embed.build()).queue();
                    System.out.println(error.getMessage());
                });
            } else {
                embed.setDescription(new MessageUtils(":error: | `Usage : r!guildgoal <guildname> -g <goal-level> \n \n ```\nNote: the goal should be higher then the guild level!```").EmojisHolder());
                embed.setColor(new Color(247, 116, 116));

                message.editMessage(embed.build()).queue();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "r!hguild";
    }

    @Override
    public String getInVoke() {
        return "fguild";
    }

    @Override
    public String getDescription() {
        return "some guild future commands";
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
    public Boolean displayCommand() {
        return true;
    }

    private long days(int number) {
        return 86400000L * number;
    }
}
