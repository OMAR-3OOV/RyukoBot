package system.commands.minecraftCategory;

import me.duncte123.botcommons.web.WebUtils;
import me.kbrewster.exceptions.APIException;
import me.kbrewster.hypixelapi.HypixelAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.objects.Category;
import system.objects.Command;
import system.objects.Config;
import system.objects.TextUtils.MessageUtils;

import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class skywarsCommand implements Command {

    public static final HashMap<User, Message> getMessageSendFromHere = new HashMap<>();
    public static final HashMap<Message, String> checkWhoSendRequest = new HashMap<>();

    public static final HashMap<User, Message> fixBugMessage = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String APIKEY = Config.get("HYPIXEL-API-KEY");
        HypixelAPI hypixel = new HypixelAPI(APIKEY);
        ru.mdashlw.hypixel.HypixelAPI hypixelV2 = new ru.mdashlw.hypixel.HypixelAPI(APIKEY);

        event.getChannel().sendTyping().queue();
        Message msg = event.getChannel().sendMessage(new MessageUtils("Checking :loading:").EmojisHolder()).complete();

        if (args.isEmpty()) {
            msg.editMessage(new MessageUtils(":question: - **there is no argument!**").EmojisHolder()).queue();
            return;
        }

        if (args.get(0).chars().count() > 16) {
            msg.editMessage(new MessageUtils(":question: - **did you forget the limit in minecraft characters name is 16 Chars?**").EmojisHolder()).queue();
            return;
        }

        WebUtils.ins.getJSONObject("https://api.mojang.com/users/profiles/minecraft/" + args.get(0)).async((mojang) -> {
            String uuid = mojang.get("id").asText();
            WebUtils.ins.getJSONObject("https://api.hypixel.net/player?key=" + APIKEY + "&uuid=" + uuid).async((hypixelAPIS) -> {

                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                DecimalFormat decimalFormatWithComma = new DecimalFormat("#,###.00");

                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.cyan);
                try {
                    embed.setAuthor("「 Skywars 」\uD83E\uDCA1  " + hypixel.getPlayer(args.get(0)).getDisplayname(), "https://plancke.io/hypixel/player/stats/" + hypixel.getPlayer(args.get(0)).getDisplayname(), "https://visage.surgeplay.com/face/" + hypixel.getPlayer(args.get(0)).getUuid());
                    embed.addField("**All mode statistics**", "**▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃** \n" + " ", false);
                    embed.addField("**__Skywars Stats__**",
                            "⬩ **Level:** " + decimalFormatWithComma.format(Skywars_ExpToLevel(hypixelV2.retrievePlayerByName(args.get(0)).get().getStats().getSkyWars().getExperience())) + " \n" +
                                    "⬩ **Total Exp:** " + hypixelV2.retrievePlayerByName(args.get(0)).get().getStats().getSkyWars().getExperience() + " \n" +
                                    " \n" +
                                    "⬩ **Kills:** " + hypixel.getPlayer(args.get(0)).getStats().getSkyWars().getKills() + " \n" +
                                    "⬩ **Deaths:** " + hypixel.getPlayer(args.get(0)).getStats().getSkyWars().getDeaths() + " \n" +
                                    "⬩ **Winds:** " + hypixel.getPlayer(args.get(0)).getStats().getSkyWars().getWins() + " \n" +
                                    "⬩ **Losses:** " + hypixel.getPlayer(args.get(0)).getStats().getSkyWars().getLosses() + " \n" +
                                    " \n" +
                                    "⬩ **Last Mode: **" + hypixel.getPlayer(args.get(0)).getStats().getSkyWars().getLastMode()
                            , true);
                    embed.addField("**__InGame Stats__**",
                            "⬩ **Blocks Placed:** " + decimalFormat.format(hypixel.getPlayer(args.get(0)).getStats().getSkyWars().getBlocksPlaced()) + " \n" +
                                    "⬩ **Blocks Broken:** " + decimalFormat.format(hypixel.getPlayer(args.get(0)).getStats().getSkyWars().getBlocksBroken()) + " \n" +
                                    " \n" +
                                    "⬩ **Longest Bow Shot:** " + hypixel.getPlayer(args.get(0)).getStats().getSkyWars().getLongestBowShot() + " \n" +
                                    "⬩ **Arrows Shots:** " + hypixel.getPlayer(args.get(0)).getStats().getSkyWars().getArrowsShot() + " \n" +
                                    "⬩ **Arrows Hits:** " + hypixel.getPlayer(args.get(0)).getStats().getSkyWars().getArrowsHit() + " \n" +
                                    "⬩ **Egg thrown:** " + hypixel.getPlayer(args.get(0)).getStats().getSkyWars().getEggThrown() + " \n" +
                                    "⬩ **Chests Open:** " + decimalFormat.format(hypixel.getPlayer(args.get(0)).getStats().getSkyWars().getChestsOpened()) + " \n" +
                                    "⬩ **Assists:** " + hypixel.getPlayer(args.get(0)).getStats().getSkyWars().getAssists()
                            , true);
                    embed.addField("▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃", " \n **Coins:** " + decimalFormat.format(hypixel.getPlayer(args.get(0)).getStats().getSkyWars().getCoins()) + " **| Souls:** " + decimalFormat.format(hypixel.getPlayer(args.get(0)).getStats().getSkyWars().getSouls()) + "** | Token: **" + decimalFormat.format(hypixel.getPlayer(args.get(0)).getStats().getSkyWars().getCosmeticTokens()), false);
                    embed.setFooter("Usage: r!hypixel " + args.get(0));
                } catch (InterruptedException | ExecutionException | IOException | APIException e) {
                    e.printStackTrace();
                }
                msg.editMessage(new MessageUtils(":successful: Successfully, User was found").EmojisHolder()).queue();
                msg.editMessage(embed.build()).queue();
                msg.addReaction("⏪").queue();
                msg.addReaction("◀️").queue();
                msg.addReaction("\uD83D\uDDD1").queue();
                msg.addReaction("▶️").queue();
                msg.addReaction("⏩").queue();
                msg.addReaction("\uD83D\uDD3D").queue();
                getMessageSendFromHere.put(Objects.requireNonNull(event.getMember()).getUser(), msg);
                checkWhoSendRequest.put(msg, args.get(0));
                fixBugMessage.put(event.getAuthor(), msg);
            }, (error) -> {
                msg.editMessage(new MessageUtils(":error: - `this users doesn't longer in hypixel network`").EmojisHolder()).queue();
            });
        }, (error) -> {
            msg.editMessage(new MessageUtils(":error: **something went wrong with this mojang use**").EmojisHolder()).queue();
        });
    }

    @Override
    public String getHelp() {
        return "r!skywars";
    }

    @Override
    public String getInVoke() {
        return "skywars";
    }

    @Override
    public String getDescription() {
        return "get skywars hypixel stats";
    }

    @Override
    public Permission getPermission() {
        return Permission.UNKNOWN;
    }

    @Override
    public Category getCategory() {
        return Category.MINECRAFT;
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
        return true;
    }

    public static double Skywars_ExpToLevel(double exp) {
        int exps[] = {0, 20, 70, 150, 250, 500, 1000, 2000, 3500, 6000, 10000, 15000};
        if (exp >= 1500) {
            return (exp - 15000) / 10000 + 12;
        } else {
            for (int i = 0; i < exps.length; i++) {
                if (exp < exps[i]) {
                    return 1 + i + (exp - exps[i - 1]) / (exps[i] - exps[i - 1]);
                }
            }
        }
        return exp;
    }
}
