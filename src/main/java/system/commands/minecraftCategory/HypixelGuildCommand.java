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
import system.objects.Utils.BarUtil;

import java.awt.*;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;
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

            WebUtils.ins.getJSONObject(new StringBuilder().append("https://api.hypixel.net/guild?name=").append(String.join("%20", handlers)).append("&key=").append(APIKEY).toString()).async(guild -> {

                int MembersCount = 0;
                AtomicInteger requests = new AtomicInteger(0);

                AtomicInteger onlineCount = new AtomicInteger(0);
                EmbedBuilder embed = new EmbedBuilder();
                Color c = new Color(209, 206, 206);

                for (int i = 0; i < guild.get("guild").get("members").size(); i++) {
                    MembersCount = guild.get("guild").get("members").size();
                    WebUtils.ins.getJSONObject(new StringBuilder().append("https://api.hypixel.net/status?key=").append(APIKEY).append("&uuid=").append(guild.get("guild").get("members").get(i).get("uuid").asText()).toString()).async(user -> {
                        if (user.get("session").get("online").asBoolean()) {
                            onlineCount.addAndGet(1);
                        }
                        requests.addAndGet(1);
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
                        c = new Color(255, 255, 255);
                        break;
                }

                StringBuilder desc = embed.getDescriptionBuilder();

                desc.append("**Guild Description : **").append((guild.get("guild").get("description") == null ? "`Empty description`" : guild.get("guild").get("description").asText())).append("\n ");
                desc.append("**Members : **").append("```yml").append("\n");

                List<JsonNode> members = new ArrayList<>();
                for (int r = 0; r < guild.get("guild").get("members").size(); r++) {
                    members.add(guild.get("guild").get("members").get(r));
                    WebUtils.ins.getJSONObject("https://api.minetools.eu/profile/" + guild.get("guild").get("members").get(r).get("uuid").asText()).async((minecraft) -> {
                        desc.append(minecraft.get("raw").get("name").asText()).append(", ");
                    },e -> System.out.println("I can't get this minecraft account"));
                }

                Color finalC = c;
                int finalMembersCount = MembersCount;

                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {

                        if (requests.get() == finalMembersCount) {
                            BarUtil.instance.build(builder -> {
                                builder.setBarIndex(10);
                                builder.setTask((float) IGuildLeveling.getLevel(guild.get("guild").get("exp").asDouble()));
                                builder.setTotal((float) IGuildLeveling.getPercentageToNextLevel(guild.get("guild").get("exp").asDouble()));

                                float total = (builder.getTotal() * 10) % builder.getBarIndex();

                                desc.append("```");
                                embed.setColor(finalC.getRGB());
                                embed.setTitle(guild.get("guild").get("name").asText() + (guild.get("guild").get("tag") == null ? "" : " [ " + guild.get("guild").get("tag").asText() + " ]"));
                                embed.addField("Created", "> " + format.format(new Date(guild.get("guild").get("created").asLong())), true);
                                embed.addField("Members", "> " + guild.get("guild").get("members").size(), true);
                                embed.addBlankField(true);
                                embed.addField("Games Exp", "> " + (guild.get("guild").get("guildExpByGameType") == null ? "empty" : guild.get("guild").get("guildExpByGameType").size()), true);
                                embed.addField("Guild Level", "> **" + IGuildLeveling.getLevel(guild.get("guild").get("exp").asInt()) + "**\n> " + builder.displayTaskBar(total, builder.getBarIndex()) + " **[ " + Math.round(IGuildLeveling.getPercentageToNextLevel(guild.get("guild").get("exp").asDouble()) * 100) + "% ]**", true);
                                embed.addBlankField(true);
                                embed.addField("Online Members", "> " + onlineCount.get(), true);
                                embed.addField("Preferred Games", "> " + (guild.get("guild").get("preferredGames").isNull() ? "empty" : guild.get("guild").get("preferredGames").size()), true);
                                embed.addBlankField(true);

                                Map<String, Integer> expGames = new HashMap<>();
                                expGames.put("Duels", guild.get("guild").get("guildExpByGameType").get("DUELS").asInt());
                                expGames.put("Walls-3", guild.get("guild").get("guildExpByGameType").get("WALLS3").asInt());
                                expGames.put("Quakecraft", guild.get("guild").get("guildExpByGameType").get("QUAKECRAFT").asInt());
                                expGames.put("GingErbread", guild.get("guild").get("guildExpByGameType").get("GINGERBREAD").asInt());
                                expGames.put("Bedwars", guild.get("guild").get("guildExpByGameType").get("BEDWARS").asInt());
                                expGames.put("Battle Ground", guild.get("guild").get("guildExpByGameType").get("BATTLEGROUND").asInt());
                                expGames.put("Skywars", guild.get("guild").get("guildExpByGameType").get("SKYWARS").asInt());
                                expGames.put("Super Smash", guild.get("guild").get("guildExpByGameType").get("SUPER_SMASH").asInt());
                                expGames.put("Survival Games", guild.get("guild").get("guildExpByGameType").get("SURVIVAL_GAMES").asInt());
                                expGames.put("Pit", guild.get("guild").get("guildExpByGameType").get("PIT").asInt());
                                expGames.put("Legacy", guild.get("guild").get("guildExpByGameType").get("LEGACY").asInt());
                                expGames.put("PaintBall", guild.get("guild").get("guildExpByGameType").get("PAINTBALL").asInt());
                                expGames.put("Murder Mystery", guild.get("guild").get("guildExpByGameType").get("MURDER_MYSTERY").asInt());
                                expGames.put("Prototype", guild.get("guild").get("guildExpByGameType").get("PROTOTYPE").asInt());
                                expGames.put("Speed Uhc", guild.get("guild").get("guildExpByGameType").get("SPEED_UHC").asInt());
                                expGames.put("Vampirez", guild.get("guild").get("guildExpByGameType").get("VAMPIREZ").asInt());
                                expGames.put("Housing", guild.get("guild").get("guildExpByGameType").get("HOUSING").asInt());
                                expGames.put("Build Battle", guild.get("guild").get("guildExpByGameType").get("BUILD_BATTLE").asInt());
                                expGames.put("UHC", guild.get("guild").get("guildExpByGameType").get("UHC").asInt());
                                expGames.put("Tnt Games", guild.get("guild").get("guildExpByGameType").get("TNTGAMES").asInt());
                                expGames.put("Skyblock", guild.get("guild").get("guildExpByGameType").get("SKYBLOCK").asInt());
                                expGames.put("Walls", guild.get("guild").get("guildExpByGameType").get("WALLS").asInt());
                                expGames.put("MCGO", guild.get("guild").get("guildExpByGameType").get("MCGO").asInt());
                                expGames.put("Arena", guild.get("guild").get("guildExpByGameType").get("ARENA").asInt());

                                Integer maxExp = Collections.max(expGames.entrySet().stream().map(m -> m.getValue()).collect(Collectors.toList()));

                                embed.addField("Best exp game", "> " + expGames.entrySet().stream().map(m -> (m.getValue().equals(maxExp) ? "**" + m.getKey() + "** " : "")).collect(Collectors.joining()) + maxExp + " exp", true);
                            });
                            msg.editMessage(new MessageUtils(":successful: " + handlers.stream().collect(Collectors.joining(" ")) + " guild has been find!, " + event.getAuthor().getAsMention()).EmojisHolder()).queue();
                            msg.editMessage(embed.build()).queue();

                            this.cancel();
                        }
                    }
                }, 0, 1000);

            }, error -> msg.editMessage(new MessageUtils(":error: this guild is not exist").EmojisHolder()).queue());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Message : " + e.getMessage());
        }
    }

    @Override
    public String getHelp() {
        return "r!guild <guild-name>";
    }

    @Override
    public String getInVoke() {
        return "guild";
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
