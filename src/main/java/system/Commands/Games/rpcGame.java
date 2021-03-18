package system.Commands.Games;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.Objects.Category;
import system.Objects.Command;
import system.Objects.TextUtils.MessageUtils;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class rpcGame implements Command {

    /*

    ---------------------
    |         |         | -> S c o r e
    |    0    |    0    | -> WinStreak
    |         |         |
    ---------------------

     */

    public static HashMap<User, Message> game = new HashMap<>();
    public static HashMap<String, Integer> points = new HashMap<>();
    public static HashMap<String, Integer> draw = new HashMap<>();
    public static HashMap<String, Integer> playtimes = new HashMap<>();
    public static HashMap<String, Integer> losetimes = new HashMap<>();

    public static String PLAYER_SELECTED = null;
    public static Properties p;

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (game.containsKey(event.getAuthor())) {
            event.getChannel().sendMessage(new MessageUtils(":error: | You are already playing with me ").EmojisHolder()).complete().editMessage(new MessageUtils(":error: | You are already playing with me ").EmojisHolder()).delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
            return;
        }

        File order = new File("system/GamerpcGame");
        File file = new File("system/Game/rpcGame", event.getAuthor().getId() + ".properties");

        FileReader reader = null;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {

        }

        p = new Properties();

        if (file.exists()) {
            try {
                p.load(reader);
            } catch (IOException e) {

            }
        }

        if (!order.exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();

                p.setProperty("User", event.getAuthor().getName());
                p.setProperty("Id", event.getAuthor().getId());
                p.setProperty("wins", "0");
                p.setProperty("bestwinstreak", "0");
                p.setProperty("draw" , "0");
                p.setProperty("playtimes", "0");
                p.setProperty("losetimes", "0");
            } catch (IOException e) {

            }
            try {
                p.save(new FileOutputStream("system/Game/rpcGame/" + event.getAuthor().getId() + ".properties"), null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        startGame(event);
    }

    @Override
    public String getHelp() {
        return "r!rpc";
    }

    @Override
    public String getInVoke() {
        return "rpc";
    }

    @Override
    public String getDescription() {
        return "game";
    }

    @Override
    public Permission getPermission() {
        return Permission.UNKNOWN;
    }

    @Override
    public Category getCategory() {
        return Category.FUN;
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

    public static void save(User user) {
        try {
            p.save(new FileOutputStream("system/Game/rpcGame/" + user.getId() + ".properties"), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    // start the game
    public static void startGame(GuildMessageReceivedEvent event) {
        String rpc = "◻";
        PLAYER_SELECTED = "◽";

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.ORANGE.brighter());
        StringBuilder string = embedBuilder.getDescriptionBuilder();

        StringBuilder GUI = new StringBuilder();

        // Title Gui
        GUI.append("**\uD83C\uDF20 Rock Paper Scissor \uD83C\uDF20**\n");
        // Material Gui
        GUI.append("^^^^^^^^^\n");
        GUI.append("^+++^+++^ | Score: %score% \n");
        GUI.append("^+P1+^+P2+^ | Last Wins : %winstreak%\n");
        GUI.append("^+++^+++^ | Best Wins: %best-winstreak%\n");
        GUI.append("^^^^^^^^^\n");

        if (points.containsKey(event.getAuthor().getId())) {
            points.put(event.getAuthor().getId(), points.get(event.getAuthor().getId()));
            draw.put(event.getAuthor().getId(), draw.get(event.getAuthor().getId()));
            losetimes.put(event.getAuthor().getId(), losetimes.get(event.getAuthor().getId()));
        }else {
            points.put(event.getAuthor().getId(), 0);
            draw.put(event.getAuthor().getId(), 0);
            losetimes.put(event.getAuthor().getId(), 0);
        }

        string.append(GUI.toString().

                replace("^", "\uD83D\uDFE5").

                replace("+", "⬛").

                replace("P1", PLAYER_SELECTED).

                replace("P2", rpc).

                replace("%score%", String.valueOf(points.get(event.getAuthor().getId()))).

                replace("%winstreak%", rpcGame.p.getProperty("wins")).

                replace("%best-winstreak%", rpcGame.p.getProperty("bestwinstreak")));

        Message message = event.getChannel().sendMessage(embedBuilder.build()).complete();

        message.addReaction("\uD83D\uDD90").

                queue();
        message.addReaction("✊").

                queue();
        message.addReaction("✌").

                queue();
        game.put(event.getChannel().retrieveMessageById(event.getMessageId()).map(Message::getAuthor).complete(), message);

        playtimes.put(event.getAuthor().getId(), 0);
        p.setProperty("playtimes", String.valueOf(playtimes.get(event.getAuthor().getId()+1)));
        playtimes.remove(event.getAuthor().getId());
    }
}