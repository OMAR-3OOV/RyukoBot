package system.commands.minecraftCategory;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import me.duncte123.botcommons.web.WebUtils;
import me.kbrewster.hypixelapi.HypixelAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.Objects.Category;
import system.Objects.Command;
import system.Objects.Config;
import system.Objects.StringsUtils.StringX;
import system.Objects.TextUtils.MessageUtils;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HypixelCommand implements Command {

    /**
     * HYPIXEL PLAYER API https://api.hypixel.net/player?key=[API KEY]&uuid=[PLAYER UUID]
     * <p>
     * MOJANG PLAYER API https://api.mojang.com/users/profiles/minecraft/<player-name>
     */

    private String historyNames;
    private String guild;
    private String getRank;
    private String getStatus;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        String APIKEY = Config.get("HYPIXEL-API-KEY");
        HypixelAPI hypixelAPI = new HypixelAPI(APIKEY);

        EmbedBuilder embed = new EmbedBuilder();

        embed.setDescription(new MessageUtils("Checking :loading:").EmojisHolder());

        event.getChannel().sendTyping().queue();
        Message msg = event.getChannel().sendMessage(embed.build()).complete();

        if (args.isEmpty()) {
            msg.editMessage(new MessageUtils(":question: - **there is no argument!**").EmojisHolder()).queue();
            return;
        }

        if (args.get(0).chars().count() > 16) {
            msg.editMessage(new MessageUtils(":question: - **The limit of minecraft username is 16 characters only?**").EmojisHolder()).queue();
            return;
        }

        WebUtils.ins.getJSONObject("https://api.mojang.com/users/profiles/minecraft/" + args.get(0)).async((mojang) -> {

            /* Mojang player api */
            String name = mojang.get("name").asText();
            String uuid = mojang.get("id").asText();

            embed.setDescription(new MessageUtils(":successful: Successfully, User was found").EmojisHolder());
            msg.editMessage(embed.build())
                    .delay(5000, TimeUnit.SECONDS)
                    .flatMap(Message::delete).queue();

            msg.getChannel().sendTyping().queue();
            WebUtils.ins.getJSONObject("https://api.minetools.eu/profile/" + uuid).async((minecraft) -> {

                /* Minecraft JSON String */
                String skinType = minecraft.get("decoded").path("textures").path("SKIN").path("metadata").path("model").asText();
                String skinUrl = minecraft.get("decoded").path("textures").path("SKIN").path("url").asText();

                /* Hypixel API JSON */
                WebUtils.ins.getJSONObject("https://api.hypixel.net/player?key=" + APIKEY + "&uuid=" + uuid).async((hypixel) -> WebUtils.ins.getJSONObject("https://api.hypixel.net/guild?key=" + APIKEY + "&player=" + uuid).async((guild) -> {
                    WebUtils.ins.getJSONObject("https://api.hypixel.net/status?key=" + APIKEY + "&uuid=" + uuid).async((status) -> {
                                embed.setDescription(new MessageUtils(":successful: **User was found in Hypixel Network** now we are getting data :loading:").EmojisHolder());
                                msg.editMessage(embed.build()).queue();
                                Date currentDate = new Date();

                                /* HypixelAPI JSON String */
                                double NEWTWORKLEVEL = 0L;
                                long FIRSTJOIN = 0L;
                                long LASTLOGIN = 0L;

                                NEWTWORKLEVEL = hypixel.get("player").get("networkExp").asDouble();
                                FIRSTJOIN = hypixel.get("player").get("firstLogin")!=null ? hypixel.get("player").get("firstLogin").asLong():0L;
                                LASTLOGIN = hypixel.get("player").get("lastLogin")!=null ? hypixel.get("player").get("lastLogin").asLong():0L;

                                /* Calculate  */
                                double exp = (Math.sqrt((2 * NEWTWORKLEVEL) + 30625) / 50) - 2.5;

                                DecimalFormat decimalFormat = new DecimalFormat("#,##0.##");
                                /* Date's */
                                SimpleDateFormat asDate = new SimpleDateFormat("EEEE, dd MMMM yyyy");
                                SimpleDateFormat asTimeYMD = new SimpleDateFormat("EEEE, dd MMMM yyyy");
                                SimpleDateFormat asTimeHMS = new SimpleDateFormat("HH:mm:ss");

                                /* Hypixel Network options */
                                this.guild = guild.get("guild").path("name").isNull()?"":guild.get("guild").path("name").asText();

                                String rank = hypixel.get("player").path("newPackageRank").asText();
                                Color color = Color.BLACK;

                                JsonNode status_ = status.get("session");

                                if (status.get("session").path("online").asBoolean() == false) {
                                    getStatus = new MessageUtils(":offline: Offline").EmojisHolder();
                                } else if (status.get("session").path("online").asBoolean() == true) {
                                    String gameType = status_.path("gameType").asText();
                                    switch (gameType) {
                                        case "MAIN":
                                            gameType = "Hypixel Network Lobby";
                                            break;
                                        case "HOUSING":
                                            if (status_.path("mode").asText().equals("LOBBY")) {
                                                gameType = "Housing Lobby";
                                            } else if (status_.path("map").asText().equals("Base")) {
                                                gameType = "Housing Base";
                                            }
                                            break;
                                        case "TOURNAMENT":
                                            if (status_.path("mode").asText().equals("LOBBY")) {
                                                gameType = "Tournament Lobby";
                                            } else {
                                                gameType = "Tournament ";
                                            }
                                            break;
                                        case "QUAKECRAFT":
                                            switch (status_.path("mode").asText()) {
                                                case "solo":
                                                    gameType = "QuakeCraft | Solo | Map: " + status_.path("map").asText();
                                                    break;
                                                case "teams":
                                                    gameType = "QuakeCraft | Teams | Map: " + status_.path("map").asText();
                                                    break;
                                                default:
                                                    gameType = "QuakeCraft Mode NOT FOUND | Map: " + status_.path("map").asText();
                                                    break;
                                            }
                                            break;
                                        case "WALLS":
                                            switch (status_.path("mode").asText()) {
                                                case "normal":
                                                    gameType = "Walls Normal | Map: " + status_.path("map").asText();
                                                    break;
                                                default:
                                                    gameType = "Walls Mode NOT FOUND | Map: " + status_.path("map").asText();
                                                    break;
                                            }
                                            break;
                                        case "ARENA":
                                            switch (status_.path("mode").asText()) {
                                                case "1v1":
                                                    gameType = "Arena 1vs1 | Map: " + status_.path("map").asText();
                                                    break;
                                                case "2v2":
                                                    gameType = "Arena 2vs2 | Map: " + status_.path("map").asText();
                                                    break;
                                                case "4v4":
                                                    gameType = "Walls 4vs4 | Map: " + status_.path("map").asText();
                                                    break;
                                                default:
                                                    gameType = "Walls Mode NOT FOUND | Map: " + status_.path("map").asText();
                                                    break;
                                            }
                                            break;
                                        case "VAMPIREZ":
                                            switch (status_.path("mode").asText()) {
                                                case "normal":
                                                    gameType = "Vampirez | Map: " + status_.path("map").asText();
                                                    break;
                                                default:
                                                    gameType = "Vampirez Mode NOT FOUND | Map: " + status_.path("map").asText();
                                                    break;
                                            }
                                            break;
                                        case "GINGERBREAD":
                                            switch (status_.path("mode").asText()) {
                                                case "normal":
                                                    gameType = "Turbo Kart Racers | Map: " + status_.path("map").asText();
                                                    break;
                                                default:
                                                    gameType = "TKR Mode NOT FOUND | Map: " + status_.path("map").asText();
                                                    break;
                                            }
                                            break;
                                        case "PAINTBALL":
                                            switch (status_.path("mode").asText()) {
                                                case "normal":
                                                    gameType = "Paintball Warfare | Map: " + status_.path("map").asText();
                                                    break;
                                                default:
                                                    gameType = "Paintball Warfare Mode NOT FOUND | Map: " + status_.path("map").asText();
                                                    break;
                                            }
                                            break;
                                        case "BATTLEGROUND":
                                            switch (status_.path("mode").asText()) {
                                                case "LOBBY":
                                                    gameType = "Warlords Lobby";
                                                    break;
                                                case "ctf_mini":
                                                    gameType = "Warlords | Capture The Flag | Map: " + status_.path("map").asText();
                                                    break;
                                                case "domination":
                                                    gameType = "Warlords | Domination | Map: " + status_.path("map").asText();
                                                    break;
                                                case "team_deathmatch":
                                                    gameType = "Warlords | Team DeathMatch | Map: " + status_.path("map").asText();
                                                    break;
                                                default:
                                                    gameType = "Warlords Mode NOT FOUND | Map: " + status_.path("map").asText();
                                                    break;
                                            }
                                            break;
                                        case "UHC":
                                        case "SPEED_UHC":
                                            switch (status_.path("mode").asText()) {
                                                case "LOBBY":
                                                    gameType = "UHC Lobby";
                                                    break;
                                                case "SOLO":
                                                    gameType = "UHC Champions | Solo | Map: " + status_.path("map").asText();
                                                    break;
                                                case "TEAMS":
                                                    gameType = "UHC Champions | Teams | Map: " + status_.path("map").asText();
                                                    break;
                                                case "solo_normal":
                                                    gameType = "Speed UHC | Solo | Map: " + status_.path("map").asText();
                                                    break;
                                                case "team_normal":
                                                    gameType = "Speed UHC | Teams | Map: " + status_.path("map").asText();
                                                    break;
                                                default:
                                                    gameType = "UHC Mode NOT FOUND | Map: " + status_.path("map").asText();
                                                    break;
                                            }
                                            break;
                                        case "MCGO":
                                            switch (status_.path("mode").asText()) {
                                                case "LOBBY":
                                                    gameType = "Cops And Crims Lobby";
                                                    break;
                                                case "normal":
                                                    gameType = "Cops And Crims | Map: " + status_.path("map").asText();
                                                    break;
                                                case "deathmatch":
                                                    gameType = "Cops And Crims | The DeathMatch | Map: " + status_.path("map").asText();
                                                    break;
                                                default:
                                                    gameType = "Cops And Crims Mode NOT FOUND | Map: " + status_.path("map").asText();
                                                    break;
                                            }
                                            break;
                                        case "ARCADE":
                                            switch (status_.path("mode").asText()) {
                                                case "LOBBY":
                                                    gameType = "Arcade Lobby";
                                                    break;
                                                case "ONEINTHEQUIVER":
                                                case "DAYONE":
                                                case "STARWARS":
                                                case "THROW_OUT":
                                                case "DRAW_THEIR_THING":
                                                case "SOCCER":
                                                case "ENDER":
                                                case "PARTY":
                                                case "DEFENDER":
                                                case "DRAGONWARS2":
                                                case "HOLE_IN_THE_WALL":
                                                    gameType = "Arcade | Game: " + status_.path("map").asText();
                                                    break;
                                                case "FARM_HUNT":
                                                    gameType = "Arcade | Farm Hunt | Map: " + status_.path("map").asText();
                                                    break;
                                                case "ZOMBIES_DEAD_END":
                                                    gameType = "Arcade | Zombies Dead End";
                                                    break;
                                                case "ZOMBIES_BAD_BLOOD":
                                                    gameType = "Arcade | Zombies Bad Blood";
                                                    break;
                                                case "ZOMBIES_ALIEN_ARCADIUM":
                                                    gameType = "Arcade | Zombies Alien Arcadium";
                                                    break;
                                                case "HIDE_AND_SEEK_PROP_HUNT":
                                                    gameType = "Arcade | Hide and Seek Prop Hunt | Map: " + status_.path("map").asText();
                                                    break;
                                                case "HIDE_AND_SEEK_PARTY_POOPER":
                                                    gameType = "Arcade | Hide and Seek Party Pooper | Map: " + status_.path("map").asText();
                                                    break;
                                                case "MINI_WALLS":
                                                    gameType = "Arcade | Mini Walls | Map: " + status_.path("map").asText();
                                                    break;
                                                case "SIMON_SAYS":
                                                    gameType = "Arcade | Hypixel Says | Map: " + status_.path("map").asText();
                                                    break;
                                                default:
                                                    gameType = "Arcade | Game NOT FOUND | Map: " + status_.path("map").asText();
                                                    break;
                                            }
                                            if (status_.path("mode").asText().equals("SCUBA_SIMULATOR")) {
                                                gameType = "Arcade | Scuba Simulator";
                                            }
                                            break;
                                        case "PIT":
                                            if (status_.path("mode").asText().equals("PIT")) {
                                                if (status_.path("map").asText().isEmpty() || status_.path("map").isNull()) {
                                                    gameType = "The Pit";
                                                } else {
                                                    gameType = "The Pit | Map: " + status_.path("map").asText();
                                                }
                                            }
                                            break;
                                        case "TNTGAMES":
                                            switch (status_.path("mode").asText()) {
                                                case "LOBBY":
                                                    gameType = "TNT Games Lobby";
                                                    break;
                                                case "TNTRUN":
                                                    gameType = "TNT Games | TNT Run | Map: " + status_.path("map").asText();
                                                    break;
                                                case "PVPRUN":
                                                    gameType = "TNT Games | TNT PVP Run | Map: " + status_.path("map").asText();
                                                    break;
                                                case "BOWSPLEEF":
                                                    gameType = "TNT Games | TNT Bow Spleef | Map: " + status_.path("map").asText();
                                                    break;
                                                case "TNTAG":
                                                    gameType = "TNT Games | TNT Tag | Map: " + status_.path("map").asText();
                                                    break;
                                                case "CAPTURE":
                                                    gameType = "TNT Games | TNT Wizards | Map: " + status_.path("map").asText();
                                                    break;
                                                default:
                                                    gameType = "TNT Games | GAME NOT LONGER";
                                                    break;
                                            }
                                            break;
                                        case "BEDWARS":
                                            switch (status_.path("mode").asText()) {
                                                case "LOBBY":
                                                    gameType = "Bedwars Lobby";
                                                    break;
                                                case "EIGHT_ONE":
                                                    gameType = "Bedwars | Solo | Map: " + status_.path("map").asText();
                                                    break;
                                                case "EIGHT_TWO":
                                                    gameType = "Bedwars | Doubles | Map: " + status_.path("map").asText();
                                                    break;
                                                case "FOUR_THREE":
                                                    gameType = "Bedwars | 3v3v3v3 | Map: " + status_.path("map").asText();
                                                    break;
                                                case "FOUR_FOUR":
                                                    gameType = "Bedwars | 4v4v4v4 | Map: " + status_.path("map").asText();
                                                    break;
                                                case "TWO_FOUR":
                                                    gameType = "Bedwars | 4v4 | Map: " + status_.path("map").asText();
                                                    break;
                                                case "EIGHT_TWO_ARMED":
                                                    gameType = "Bedwars | Armor Doubles | Map: " + status_.path("map").asText();
                                                    break;
                                                case "FOUR_FOUR_ARMED":
                                                    gameType = "Bedwars | Armor 4v4v4v4 | Map: " + status_.path("map").asText();
                                                    break;
                                            }
                                            break;
                                        case "SUPER_SMASH":
                                            switch (status_.path("mode").asText()) {
                                                case "LOBBY":
                                                    gameType = "Smash Heroes Lobby";
                                                    break;
                                                case "solo_normal":
                                                    gameType = "Smash Heroes | Solo 1v1v1v1 | Map: " + status_.path("map").asText();
                                                    break;
                                                case "2v2_normal":
                                                    gameType = "Smash Heroes | Teams 2v2 | Map: " + status_.path("map").asText();
                                                    break;
                                                case "teams_normal":
                                                    gameType = "Smash Heroes | Teams 2v2v2 | Map: " + status_.path("map").asText();
                                                    break;
                                                default:
                                                    gameType = "Smash Heros | Type **NOT LONGER**";
                                                    break;
                                            }
                                            break;
                                        case "WALLS3":
                                            switch (status_.path("mode").asText()) {
                                                case "LOBBY":
                                                    gameType = "Mega Walls Lobby";
                                                    break;
                                                case "standard":
                                                    gameType = "Mega Walls | Standard | Map: " + status_.path("map").asText();
                                                    break;
                                                case "face_off":
                                                    gameType = "Mega Walls | Face off | Map: " + status_.path("map").asText();
                                                    break;
                                                default:
                                                    gameType = "Mega Walls | Type **NOT LONGER**";
                                                    break;
                                            }
                                            break;
                                        case "BUILD_BATTLE":
                                            switch (status_.path("mode").asText()) {
                                                case "LOBBY":
                                                    gameType = "Build Battle Lobby";
                                                    break;
                                                case "SOLO_NORMAL":
                                                case "SOLO_PRO":
                                                case "TEAMS_NORMAL":
                                                case "GUESS_THE_BUILD":
                                                    gameType = "Build Battle | Mode: " + status_.path("map").asText();
                                                    break;
                                                default:
                                                    gameType = "Build Battle | Type **NOT LONGER**";
                                                    break;
                                            }
                                            break;
                                        case "PROTOTYPE":
                                            switch (status_.path("mode").asText()) {
                                                case "LOBBY":
                                                    gameType = "ProtoType Lobby";
                                                    break;
                                                case "PVP_CTW":
                                                    gameType = "ProtoType | Capture The Wood | Map: " + status_.path("map").asText();
                                                    break;
                                                case "TOWERWARS_SOLO":
                                                    gameType = "ProtoType | TownerWars | Map: " + status_.path("map").asText();
                                                    break;
                                                default:
                                                    gameType = "Prototype | Type **NOT LONGER**";
                                                    break;
                                            }
                                            break;
                                        case "MURDER_MYSTERY":
                                            switch (status_.path("mode").asText()) {
                                                case "LOBBY":
                                                    gameType = "Murder Mystery Lobby";
                                                    break;
                                                case "MURDER_ASSASSINS":
                                                    gameType = "Murder Mystery | Assassins | Map: " + status_.path("map").asText();
                                                    break;
                                                case "MURDER_CLASSIC":
                                                    gameType = "Murder Mystery | Classic | Map: " + status_.path("map").asText();
                                                    break;
                                                case "MURDER_DOUBLE_UP":
                                                    gameType = "Murder Mystery | Double | Map: " + status_.path("map").asText();
                                                    break;
                                                case "MURDER_INFECTION":
                                                    gameType = "Murder Mystery | Infection | Map: " + status_.path("map").asText();
                                                    break;
                                                default:
                                                    gameType = "Murder Mystery | Not Longer | Map: " + status_.path("map").asText();
                                                    break;
                                            }
                                            break;
                                        case "SKYWARS":
                                            switch (status_.path("mode").asText()) {
                                                case "LOBBY":
                                                    gameType = "Skywars Lobby";
                                                    break;
                                                case "solo_normal":
                                                    gameType = "Skywars | Solo Normal | Map: " + status_.path("map").asText();
                                                    break;
                                                case "solo_insane":
                                                    gameType = "Skywars | Solo Insane | Map: " + status_.path("map").asText();
                                                    break;
                                                case "teams_normal":
                                                    gameType = "Skywars | Teams Normal | Map: " + status_.path("map").asText();
                                                    break;
                                                case "teams_insane":
                                                    gameType = "Skywars | Teams Insane | Map: " + status_.path("map").asText();
                                                    break;
                                                case "ranked_normal":
                                                    gameType = "Skywars | Ranked | Map: " + status_.path("map").asText();
                                                    break;
                                                case "mega_normal":
                                                    gameType = "Skywars | Mega Normal | Map: " + status_.path("map").asText();
                                                    break;
                                                case "solo_insane_tnt_madness":
                                                    gameType = "Skywars | Solo insane | Mode: TNT Madness | Map: " + status_.path("map").asText();
                                                    break;
                                                case "solo_insane_lucky":
                                                    gameType = "Skywars | Solo insane | Mode: Lucky Block | Map: " + status_.path("map").asText();
                                                    break;
                                                case "teams_insane_tnt_madness":
                                                    gameType = "Skywars | Teams insane | Mode: TNT Madness | Map: " + status_.path("map").asText();
                                                    break;
                                                case "teams_insane_lucky":
                                                    gameType = "Skywars | Teams insane | Mode: Lucky Block | Map: " + status_.path("map").asText();
                                                    break;
                                                default:
                                                    gameType = "Skywars | Not Longer | Map: " + status_.path("map").asText();
                                                    break;
                                            }
                                            break;
                                        case "SURVIVAL_GAMES":
                                            switch (status_.path("mode").asText()) {
                                                case "LOBBY":
                                                    gameType = "Blitz SG Lobby";
                                                    break;
                                                case "solo_normal":
                                                    gameType = "Blitz SG | Solo Normal | Map: " + status_.path("map").asText();
                                                    break;
                                                case "teams_normal":
                                                    gameType = "Blitz SG | Teams Normal | Map: " + status_.path("map").asText();
                                                    break;
                                                default:
                                                    gameType = "Blitz SG Mode NOT FOUND | Map: " + status_.path("map").asText();
                                                    break;
                                            }
                                            break;
                                        case "DUELS":
                                            switch (status_.path("mode").asText()) {
                                                case "LOBBY":
                                                    gameType = "Duels Lobby";
                                                    break;
                                                case "SW_DUEL":
                                                    gameType = "Duels | Skywars Solo | Map: " + status_.path("map").asText();
                                                    break;
                                                case "SW_DOUBLES":
                                                    gameType = "Duels | Skywars Doubles | Map: " + status_.path("map").asText();
                                                    break;
                                                case "UHC_MEETUP":
                                                    gameType = "Duels | UHC DeathMatch | Map: " + status_.path("map").asText();
                                                    break;
                                                case "UHC_DUEL":
                                                    gameType = "Duels | UHC 1vs1 | Map: " + status_.path("map").asText();
                                                    break;
                                                case "UHC_DOUBLES":
                                                    gameType = "Duels | UHC 2vs2 | Map: " + status_.path("map").asText();
                                                    break;
                                                case "UHC_FOUR":
                                                    gameType = "Duels | UHC 4vs4 | Map: " + status_.path("map").asText();
                                                    break;
                                                case "SUMO_DUEL":
                                                    gameType = "Duels | Sumo | Map: " + status_.path("map").asText();
                                                    break;
                                                case "CLASSIC_DUEL":
                                                    gameType = "Duels | Classic | Map: " + status_.path("map").asText();
                                                    break;
                                                case "BRIDGE_DUEL":
                                                    gameType = "Duels | The Bridge 1vs1 | Map: " + status_.path("map").asText();
                                                    break;
                                                case "BRIDGE_DOUBLES":
                                                    gameType = "Duels | The Bridge 2vs2 | Map: " + status_.path("map").asText();
                                                    break;
                                                case "BRIDGE_FOUR":
                                                    gameType = "Duels | The Bridge 4vs4 | Map: " + status_.path("map").asText();
                                                    break;
                                                case "BRIDGE_2V2V2V2":
                                                    gameType = "Duels | The Bridge 2vs2vs2vs2 | Map: " + status_.path("map").asText();
                                                    break;
                                                case "BRIDGE_3V3V3V3":
                                                    gameType = "Duels | The Bridge 3vs3vs3vs3 | Map: " + status_.path("map").asText();
                                                    break;
                                                case "MW_DUEL":
                                                    gameType = "Duels | MegaWalls Solo | Map: " + status_.path("map").asText();
                                                    break;
                                                case "MW_DOUBLES":
                                                    gameType = "Duels | MegaWalls Doubles | Map: " + status_.path("map").asText();
                                                    break;
                                                case "OP_DUEL":
                                                    gameType = "Duels | Op 1vs1 | Map: " + status_.path("map").asText();
                                                    break;
                                                case "OP_DOUBLES":
                                                    gameType = "Duels | Op 2vs2 | Map: " + status_.path("map").asText();
                                                    break;
                                                case "BOWSPLEEF_DUEL":
                                                    gameType = "Duels | Bow Spleef | Map: " + status_.path("map").asText();
                                                    break;
                                                case "BLITZ_DUEL":
                                                    gameType = "Duels | Blitz Solo | Map: " + status_.path("map").asText();
                                                    break;
                                                case "POTION_DUEL":
                                                    gameType = "Duels | NoDebuff Solo | Map: " + status_.path("map").asText();
                                                    break;
                                                case "BOW_DUEL":
                                                    gameType = "Duels | Bow Solo | Map: " + status_.path("map").asText();
                                                    break;
                                                default:
                                                    gameType = "Duels Mode NOT FOUND | Map: " + status_.path("map").asText();
                                                    break;
                                            }
                                            break;
                                        case "SKYBLOCK":
                                            switch (status_.path("mode").asText()) {
                                                case "dynamic":
                                                    gameType = "Skyblock | Island";
                                                    break;
                                                case "hub":
                                                    gameType = "Skyblock | Hub";
                                                    break;
                                                case "dungeon":
                                                    gameType = "Skyblock | Dungeon";
                                                    break;
                                                case "dungeon_hub":
                                                    if (!status_.path("map").isNull()) {
                                                        gameType = "Skyblock | Dungeon | Map: " + status_.path("map").asText();
                                                    } else {
                                                        gameType = "Skyblock | Dungeon HUB";
                                                    }
                                                    break;
                                                case "farming_1":
                                                    gameType = "Skyblock | The Barn";
                                                    break;
                                                case "farming_2":
                                                    gameType = "Skyblock | Mushroom Desert";
                                                    break;
                                                case "foraging_1":
                                                    gameType = "Skyblock | The Park";
                                                    break;
                                                case "combat_3":
                                                    gameType = "Skyblock | The End";
                                                    break;
                                                case "combat_1":
                                                    gameType = "Skyblock | Spider's Den";
                                                    break;
                                                case "combat_2":
                                                    gameType = "Skyblock | The Nether";
                                                    break;
                                                case "mining_1":
                                                    gameType = "Skyblock | Gold Mine";
                                                    break;
                                                case "mining_2":
                                                    gameType = "Skyblock | Deep Caverns";
                                                    break;
                                                case "mining_3":
                                                    gameType = "Skyblock | Dwarven Mines";
                                                    break;
                                                default:
                                                    gameType = "SkyBlock | Type **NOT LONGER**";
                                                    break;
                                            }
                                            break;
                                    }

                                    getStatus = new MessageUtils(":online: Online - " + gameType).EmojisHolder();

                                }

                                if (hypixel.get("player").path("rank") != null || rank != null || hypixel.get("player").path("packageRank") != null || hypixel.get("player").path("prefix") != null) {
                                    getRank = hypixel.get("player").path("rank").asText();
                                    switch (hypixel.get("player").path("rank").asText()) {
                                        case "YOUTUBER":
                                            if (hypixel.get("player").path("prefix").asText().contains("PIG")) {
                                                getRank = "PIG";
                                                color = Color.pink;
                                            } else {
                                                color = Color.RED;
                                            }
                                            break;
                                        case "ADMIN":
                                            color = Color.RED.darker();
                                            break;
                                        case "DEVELOPER":
                                            color = Color.cyan.darker();
                                            break;
                                        case "MODERATOR":
                                            color = Color.GREEN.darker();
                                            break;
                                        case "HELPER":
                                            color = Color.blue;
                                            break;
                                        default:
                                            if (hypixel.get("player").path("newPackageRank").asText().equals("MVP_PLUS") && (hypixel.get("player").path("monthlyPackageRank").asText().equals("SUPERSTAR") || hypixel.get("player").path("packageRank").asText().equals("SUPERSTAR"))) {
                                                getRank = "MVP++";
                                                color = Color.ORANGE;
                                            } else if (rank.equals("MVP_PLUS") || hypixel.get("player").path("packageRank").asText().equals("MVP_PLUS")) {
                                                if (hypixel.get("player").path("prefix").asText().contains("MOJANG")) {
                                                    getRank = "Mojang";
                                                    color = Color.RED;
                                                } else {
                                                    getRank = "MVP+";
                                                    color = Color.CYAN;
                                                }
                                            } else if (rank.equals("MVP") || hypixel.get("player").path("packageRank").asText().equals("MVP")) {
                                                if (hypixel.get("player").path("prefix").asText().contains("MOJANG")) {
                                                    getRank = "Mojang";
                                                    color = Color.RED;
                                                } else {
                                                    getRank = "MVP";
                                                    color = Color.CYAN;
                                                }
                                            } else if (rank.equals("VIP_PLUS") || hypixel.get("player").path("packageRank").asText().equals("VIP_PLUS")) {
                                                if (hypixel.get("player").path("prefix").asText().contains("MOJANG")) {
                                                    getRank = "Mojang";
                                                    color = Color.RED;
                                                } else {
                                                    getRank = "VIP+";
                                                    color = Color.GREEN;
                                                }
                                            } else if (rank.equals("VIP") || hypixel.get("player").path("packageRank").asText().equals("VIP")) {
                                                if (hypixel.get("player").path("prefix").asText().contains("MOJANG")) {
                                                    getRank = "Mojang";
                                                    color = Color.RED;
                                                } else {
                                                    getRank = "VIP";
                                                    color = Color.GREEN;
                                                }
                                            } else if ((hypixel.get("player").path("packageRank").isNull() || hypixel.get("player").path("newPackageRank").isNull() || hypixel.get("player").path("rank").isNull())) {
                                                if (hypixel.get("player").path("prefix").asText().contains("MOJANG")) {
                                                    getRank = "Mojang";
                                                    color = Color.RED;
                                                } else {
                                                    getRank = "Member";
                                                    color = Color.GRAY;
                                                }
                                            } else {
                                                if (hypixel.get("player").path("prefix").asText().contains("MOJANG")) {
                                                    getRank = "Mojang";
                                                    color = Color.RED;
                                                } else {
                                                    getRank = "Member";
                                                    color = Color.GRAY;
                                                }
                                            }
                                            break;
                                    }
                                }

                                String guildRank = "";

                                if (this.guild.isEmpty()) {
                                    this.guild = "Not longer in guild";
                                } else {
                                    for (int i = 0; i < guild.get("guild").get("members").size(); i++) {
                                        if (guild.get("guild").get("members").get(i).get("uuid").asText().equalsIgnoreCase(uuid)) {
                                            guildRank = " ( `" + guild.get("guild").get("members").get(i).get("rank").asText() + "` )";
                                        }
                                    }
                                }

                                DecimalFormat number = new DecimalFormat("#,###.00");

                                embed.setColor(color);
                                embed.setDescription("** **");
                                embed.addField("```                                                    ```", "", false);
                                embed.setAuthor(name + " [ " + getRank + " ]", "https://plancke.io/hypixel/player/stats/" + name, "https://visage.surgeplay.com/face/" + uuid);
                                //embed.setThumbnail("https://visage.surgeplay.com/head/512/" + uuid);
                                if (FIRSTJOIN == 0) {
                                    embed.addField("**__First Join__**", "> -", true);
                                } else {
                                    embed.addField("**__First Join__**", "> " + asDate.format(FIRSTJOIN), true);
                                }
                                embed.addBlankField(true);
                                if (LASTLOGIN == 0) {
                                    embed.addField("**__Last Join__**", "> -" + " \n> -", true);
                                } else {

                                    Locale LocaleBylanguageTag = Locale.forLanguageTag("en");
                                    TimeAgoMessages message = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
                                    String test = TimeAgo.using(LASTLOGIN, message);

                                    if (status.get("session").path("online").asBoolean()) {
                                        embed.addField("**__Last Join__**", "> " + asTimeYMD.format(LASTLOGIN), true);
                                    } else {
                                        embed.addField("**__Last Join__**", "> " + asTimeYMD.format(LASTLOGIN) + "\n \n> " + printTimeLeft(new Date(), new Date(LASTLOGIN)) + "\n> **" + test + "**", true);
                                    }
                                }
                                embed.addField("**__Guild__**", "> " + this.guild + guildRank, true);
                                embed.addBlankField(true);
                                embed.addField("**__Level__**", "> " + decimalFormat.format(exp), true);
                                embed.addField("**__Karma__**", "> " + decimalFormat.format(hypixel.get("player").path("karma").asInt()), true);
                                embed.addBlankField(true);
                                embed.addField("**__Achievements Points__**", "> " + decimalFormat.format(hypixel.get("player").path("achievementPoints").asInt()), true);

                                if (hypixel.get("player").path("housingMeta").path("firstHouseJoinMs").asLong() == 0 || hypixel.get("player").path("housingMeta").path("firstHouseJoinMs").isNull()) {
                                    embed.addField("**__Housing First Join__**", "> -", true);
                                } else {
                                    embed.addField("**__Housing First Join__**", "> " + asTimeYMD.format(hypixel.get("player").path("housingMeta").path("firstHouseJoinMs").asLong()) + "\n> " + asTimeHMS.format(hypixel.get("player").path("housingMeta").path("firstHouseJoinMs").asLong()), true);
                                }
                                embed.addBlankField(true);
                                embed.addField("**__Parkour Completions__**", "> " + hypixel.get("player").path("parkourCompletions").size(), true);
                                embed.addField("```                                                    ```", " ", false);
                                if (!hypixel.get("player").get("friendRequestsUuid").isNull()) {
                                    embed.addField("Friends Request", String.valueOf(hypixel.get("player").get("friendRequestsUuid").size()), false);
                                }
                                embed.addField("**__Status__**", status.get("session").path("online").asBoolean()?(getStatus + " `" + printTimeLeft(new Date(), new Date(LASTLOGIN)) + "`"):getStatus, false);

                                /* Sorted history name */
                                LinkedHashMap<String, LinkedList<String>> NameList = new LinkedHashMap<>();
                                LinkedList<String> histrorys1 = new LinkedList<>();

                                for (int i = 0; i < hypixel.get("player").path("knownAliases").size(); i++) {
                                    historyNames = hypixel.get("player").path("knownAliases").get(i).asText();
                                    NameList.put(name, histrorys1);
                                    NameList.get(name).add("'" + historyNames + "'");
                                }

                                if (histrorys1.contains("'" + name + "'")) {
                                    embed.addField("Name History", "```diff\n" + String.join("\n", NameList.get(name)).replaceAll("'" + name + "'", "- " + name).replace("'", "") + "```", false);
                                } else {
                                    embed.addField("Name History", "```yaml\n" + "THERE IS NO HISTORY NAME FOR THIS USER```", false);
                                }
                                if (hypixel.get("player").path("socialMedia").isNull() || hypixel.get("player").path("socialMedia").path("links").path("DISCORD").asText().isEmpty() || hypixel.get("player").path("socialMedia").path("links").path("DISCORD").isNull()) {
                                    embed.setFooter(name + " | Discord : " + "DISCORD NOT LONGER", StringX.DISCORD.toString());
                                } else {
                                    embed.setFooter(name + " | Discord : " + hypixel.get("player").path("socialMedia").path("links").path("DISCORD").asText(), StringX.DISCORD.toString());
                                }

                                msg.editMessage(embed.build()).queue();

                                WebUtils.ins.getJSONObject("https://api.hypixel.net/recentgames?key=" + APIKEY + "&uuid=" + uuid).async((log) -> {
                                    msg.addReaction("\uD83E\uDEB5").queue();
                                }, (error) -> {
                                });

                            }, (error) -> {
                                embed.setDescription(new MessageUtils(":error: - `this users doesn't longer in hypixel network`\n" + error.getMessage() + " | " + error.getCause().getLocalizedMessage()).EmojisHolder());
                                msg.editMessage(embed.build()).queue();
                                error.printStackTrace();
                            }
                    );
                }));
            });
        }, (error) -> {
            embed.setDescription(new MessageUtils(":error: - **Something went wrong with this user**").EmojisHolder());
            msg.editMessage(embed.build()).queue();
        });
    }

    @Override
    public String getHelp() {
        return "r!hypixel <player>";
    }

    @Override
    public String getInVoke() {
        return "hypixel";
    }

    @Override
    public String getDescription() {
        return "get information about hypixel player";
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

    public static String toDuration(long duration) {
        StringBuffer res = new StringBuffer();

        Date now = new Date();
        TimeUnit time = TimeUnit.MILLISECONDS;

        if (time.toSeconds(now.getTime() - duration) <= 60) {
            res.append(time.toSeconds(now.getTime() - duration)).append(" ").append("second").append(duration != 1 ? "s" : "").append(" ").append("ago");
        } else if (time.toMinutes(now.getTime() - duration) <= 60) {
            res.append(time.toMinutes(now.getTime() - duration)).append(" ").append("minute").append(duration != 1 ? "s" : "").append(" ").append("ago");
        } else if (time.toHours(now.getTime() - duration) <= 24) {
            res.append(time.toHours(now.getTime() - duration)).append(" ").append("hour").append(duration != 1 ? "s" : "").append(" ").append("ago");
        } else if (time.toDays(now.getTime() - duration) >= 365) {
            res.append(time.toDays((now.getTime() - duration) / 365)).append(" ").append("year").append(duration != 1 ? "s" : "").append(" ").append("ago");
        } else if (time.toDays(now.getTime() - duration) >= 30) {
            res.append(time.toDays((now.getTime() - duration) / 30)).append(" ").append("month").append(duration != 1 ? "s" : "").append(" ").append("ago");
        } else if (time.toDays(now.getTime() - duration) >= 7) {
            res.append(time.toDays((now.getTime() - duration) / 7)).append(" ").append("week").append(duration != 1 ? "s" : "").append(" ").append("ago");
        } else if (time.toDays(now.getTime() - duration) > 0) {
            res.append(time.toDays(now.getTime() - duration)).append(" ").append("day").append(duration != 1 ? "s" : "").append(" ").append("ago");
        }

        if ("".equals(res.toString()))
            return "0 seconds ago";
        else
            return res.toString();
    }

    public String printTimeLeft(Date dateStart, Date dateEnd) {

        StringBuilder builder = new StringBuilder();
        long different = dateStart.getTime() - dateEnd.getTime();

        long seconds = 1000;
        long minutes = seconds * 60;
        long hours = minutes * 60;
        long days = hours * 24;
        long weeks = days * 7;
        long months = (long) (days * 30.4167f);
        long years = days * 365;

        long elapsedY = different / years;

        different = different % years;
        long elapsedM = different / months;

        different = different % months;
        long elapsedW = different / weeks;

        different = different % weeks;
        long elapsedD = different / days;

        different = different % days;
        long elapsedH = different / hours;

        different = different % hours;
        long elapsedMIN = different / minutes;

        // System.out.println(elapsedY + " years," + elapsedM + " month, " + elapsedM + " weeks, " + elapsedD + "  days, " + elapsedH + " hours, " + elapsedMIN + " minutes!");

        builder.append(elapsedY > 0 ? elapsedY + " year" + (elapsedY > 1 ? "s " : " ") : " ");
        builder.append(elapsedM > 0 ? elapsedM + " month" + (elapsedM > 1 ? "s " : " ") : " ");
        builder.append(elapsedW > 0 ? elapsedW + " week" + (elapsedW > 1 ? "s " : " ") : " ");
        builder.append(elapsedD > 0 ? elapsedD + " day" + (elapsedD > 1 ? "s " : " ") : " ");
        builder.append(elapsedH > 0 ? elapsedH + " hour" + (elapsedH > 1 ? "s " : " ") : " ");
        builder.append(elapsedMIN > 0 ? elapsedMIN + " minute" + (elapsedMIN > 1 ? "s " : " ") : " ");

        return builder.toString();
    }

}