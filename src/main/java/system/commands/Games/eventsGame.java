package system.commands.Games;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import system.commands.Games.eventsGames.numbersGame;
import system.objects.Category;
import system.objects.Command;
import system.objects.TextUtils.MessageUtils;
import system.objects.Utils.LastWinnersEvent;

import java.awt.*;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class eventsGame implements Command {

    public String numbersgameVersion = "1.0.0";

    // game option
    public static HashMap<String, User> checkGame = new HashMap<>();
    public static numbersGame game;
    public static HashMap<User, Message> getRoleMap;
    // game -> Setup setting
    public static HashMap<User, Message> setup = new HashMap<>();
    public static HashMap<User, Guild> setup_guildCheck = new HashMap<>();
    public static HashMap<User, EmbedBuilder> setup_steps = new HashMap<>();
    // message
    public static Message message;

    // setup options
    public static int setup_count = 0;
    public static String[] setup_options = {
            "How many digit numbers you want to post? (Pick a number)", // 0
            "What channel that you want the message will send?", // 1
            "What is the reward that you want? (Role)" // 2
    };

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        // r!event args0 args1
        // r!event <Game-Name> <type>

        try {

            String gameName = args.get(0);
            if (gameName.equalsIgnoreCase("lastwinners")) {
                LastWinnersEvent lastWinners = new LastWinnersEvent();
                AtomicInteger numbers = new AtomicInteger();

                List<String> last5winners = new ArrayList<>();
                DateTimeFormatter newformat = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy");

                EmbedBuilder embed = new EmbedBuilder();
                StringBuilder disc = embed.getDescriptionBuilder();

                embed.setColor(new Color(255, 255, 255));
                embed.setTitle("\uD83D\uDD16 Winners list");
                disc.append("\n").append("> \uD83C\uDFC5 Winners : ").append(lastWinners.getProperties().entrySet().size()).append("\n");
                disc.append("```").append("\n");

                lastWinners.getProperties().forEach((key, value) -> {
                    int win = numbers.addAndGet(1);
                    last5winners.add("#" + win + " " + Objects.requireNonNull(event.getGuild().getMemberById(String.valueOf(key))).getUser().getName() + "#" + Objects.requireNonNull(event.getGuild().getMemberById(String.valueOf(key))).getUser().getDiscriminator() + " | " + newformat.format(ZonedDateTime.parse(String.valueOf(value))));
                });

                int totalIndex = last5winners.size();
                int startIndex = totalIndex - 5;

                if (last5winners.size() > 5) {
                    disc.append(String.join("\n", last5winners.subList(startIndex, totalIndex)));
                } else {
                    disc.append(String.join("\n", last5winners));
                }
                disc.append("\n").append("```");

                event.getChannel().sendMessage(embed.build()).queue();
            } else if (gameName.equalsIgnoreCase("numbers")) {
                try {

                    // types: start, end, setup, check
                    String typeName = args.get(1);

                    // Message
                    EmbedBuilder gameMessage = new EmbedBuilder();
                    gameMessage.setColor(Color.lightGray.brighter());
                    gameMessage.setDescription("Loading the game **(ﾉ´ з `)ノ *give me a hug before you starting***");

                    // Date
                    SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");
                    Date date = new Date();

                    if (typeName.equalsIgnoreCase("setup")) {

                        if (setup.containsKey(event.getAuthor()) && (setup_guildCheck.get(event.getAuthor()) == event.getGuild())) {
                            event.getChannel().sendMessage(new MessageUtils(":error: | **You're already editing the setup settings if you want to close the last one please type `no' or `stop` or `done`**").EmojisHolder()).queue();
                            return;
                        }

                        // File System
                        File order = new File("system/Game/");
                        File eventsFile = new File("system/Game/events");
                        File gameFile = new File("system/Game/events/numbersGame");
                        File guildsFile = new File("system/Game/events/numbersGame/Guilds/" + event.getGuild().getId());
                        File file = new File("system/Game/events/numbersGame/Guilds/" + event.getGuild().getId() + "/Settings.properties");
                        FileReader reader = null;


                        EmbedBuilder setupEmbed = new EmbedBuilder();

                        if (!order.exists()) {
                            order.mkdirs();
                        }
                        if (order.exists() && !eventsFile.exists()) {
                            eventsFile.mkdirs();
                        }
                        if (order.exists() && eventsFile.exists() && !gameFile.exists()) {
                            gameFile.mkdirs();
                        }
                        if (order.exists() && eventsFile.exists() && gameFile.exists() && !guildsFile.exists()) {
                            guildsFile.mkdirs();
                        }
                        if (order.exists() && eventsFile.exists() && gameFile.exists() && guildsFile.exists() && !file.exists()) {
                            try {
                                file.createNewFile();
                                reader = new FileReader(file);
                                Properties properties = new Properties();
                                properties.load(reader);

                                properties.setProperty("Digit", "none");
                                properties.setProperty("Channel", "none");
                                properties.setProperty("Reward", "none");

                                setupEmbed.clear();
                                setupEmbed.setColor(Color.cyan.brighter());
                                setupEmbed.setTitle("Setup Number game settings ⚙");
                                setupEmbed.setDescription(
                                        "**Game Settings \uD83C\uDFAE : **" + "\n" +
                                                ("> **⬩ Digit Numbers :** %digit%\n" +
                                                        "> **⬩ Game Channel :** %channel%\n" +
                                                        "> **⬩ Reward Role :** %reward%\n").replace("%digit%", "").replace("%channel%", "").replace("%reward%", "")
                                );
                                setupEmbed.addField("", "If you want to setup the Settings type `Yes` if you want to keep with normally Setting type `No`", false);

                                try {
                                    properties.save(new FileOutputStream(file.getPath()), null);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        File file_ = new File("system/Game/events/numbersGame/Guilds/" + event.getGuild().getId() + "/Settings.properties");
                        if (file_.exists()) {
                            FileReader reader_ = null;
                            try {
                                reader_ = new FileReader(file_);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Properties properties = new Properties();

                            try {
                                properties.load(reader_);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String channelName;
                            String rewardName;

                            if (event.getGuild().getTextChannels().stream().map(ISnowflake::getId).anyMatch(any -> any.contains(properties.getProperty("Channel")))) {
                                channelName = Objects.requireNonNull(event.getGuild().getGuildChannelById(properties.getProperty("Channel"))).getName() + " | " + properties.getProperty("Channel");
                            } else {
                                channelName = "none";
                            }

                            if (event.getGuild().getRoles().stream().map(ISnowflake::getId).anyMatch(any -> any.contains(properties.getProperty("Reward")))) {
                                rewardName = Objects.requireNonNull(event.getGuild().getRoleById(properties.getProperty("Reward"))).getAsMention() + " | " + properties.getProperty("Reward");
                            } else {
                                rewardName = "none";
                            }

                            setupEmbed.clear();
                            setupEmbed.setColor(Color.cyan.brighter());
                            setupEmbed.setTitle("Setup Number game settings ⚙");
                            setupEmbed.setDescription(
                                    "**Game Settings \uD83C\uDFAE : **" + "\n" +
                                            ("> **⬩ Digit Numbers :** %digit%\n" +
                                                    "> **⬩ Game Channel :** %channel%\n" +
                                                    "> **⬩ Reward Role :** %reward%\n").replace("%digit%", properties.getProperty("Digit")).replace("%channel%", channelName).replace("%reward%", rewardName)
                            );
                            setupEmbed.addField("", "If you want to edit any of this Settings type `Yes` if you want to keep this Settings type `No`", false);

                        }
                        Message message = event.getChannel().sendMessage(setupEmbed.build()).complete();
                        setup.put(event.getAuthor(), message);
                        setup_guildCheck.put(event.getAuthor(), event.getGuild());
                        setup_steps.put(event.getAuthor(), setupEmbed);
                    } else if (typeName.equalsIgnoreCase("check")) {
                        if (game != null) {
                            if (checkGame.containsKey("numbers_" + event.getGuild())) {
                                gameMessage.clear();
                                gameMessage.setTitle(event.getGuild().getName() + " connected with `Ryuko -> Numbers`");
                                gameMessage.setDescription(game.getNumbersStart().entrySet().stream().filter(f -> f.getKey().getId().contains(event.getGuild().getId())).map(m -> m.getKey().getName() + " | #" + m.getValue().getGameID() + " | Key : `" + m.getValue().getGameKeyWinner() + "`").collect(Collectors.joining("\n")));
                            } else {
                                gameMessage.clear();
                                gameMessage.setTitle("There is no game longer in Ryuko");
                            }
                        } else {
                            gameMessage.clear();
                            gameMessage.setTitle("There is no game longer in Ryuko");
                        }
                        event.getChannel().sendMessage(gameMessage.build()).queue();
                    } else if (typeName.equalsIgnoreCase("start")) {
                        message = event.getChannel().sendMessage(gameMessage.build()).complete();
                        try {
                            if (game != null) {
                                if (game.getNumbersStart().containsKey(event.getGuild())) {
                                    gameMessage.setColor(Color.RED.brighter());
                                    gameMessage.setDescription("There is already game started " + event.getAuthor().getAsMention());
                                    message.editMessage(gameMessage.build()).queue();
                                    return;
                                }
                            }

                            // generation random game id & game key
                            DecimalFormat df = new DecimalFormat("###");
                            int gameID = generationRandomNumber(10000, 99999);
                            int gameKey = 0;

                            File file_ = new File("system/Game/events/numbersGame/Guilds/" + event.getGuild().getId() + "/Settings.properties");
                            if (args.size() >= 3) {

                                gameKey = generationRandomNumber(100, 999);

                                String role = args.get(2);

                                final Pattern regex = Pattern.compile(Message.MentionType.ROLE.getPattern().pattern());
                                final Matcher matcher = regex.matcher(role);

                                final Pattern regexRoleName = Pattern.compile("-?[A-Za-z]+");
                                final Matcher matcherRoleName = regexRoleName.matcher(role);

                                if (matcher.find()) {
                                    role = role.replace("<", "").replace("@", "").replace("&", "").replace(">", "");
                                } else if (matcherRoleName.find()) {
                                    role = event.getGuild().getRolesByName(role, true).stream().collect(Collectors.toList()).stream().findFirst().get().getId();
                                }

                                game = new numbersGame("numbers_" + event.getGuild(), event.getGuild(), event.getChannel(), df.parse(String.valueOf(gameID)).intValue(), gameKey, message, 3, event.getGuild().getRoleById(role), format.format(date));
                            } else if (file_.exists()) {
                                FileReader reader_ = null;
                                try {
                                    reader_ = new FileReader(file_);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                Properties properties = new Properties();

                                try {
                                    properties.load(reader_);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String digits = "0";

                                if (properties.getProperty("Digit") == "none") {
                                    gameKey = generationRandomNumber(100, 999);
                                    digits = "3";
                                } else if (properties.getProperty("Digit").contains(Integer.toString(1))) {
                                    gameKey = generationRandomNumber(1, 9);
                                    digits = properties.getProperty("Digit");
                                } else if (properties.getProperty("Digit").contains(Integer.toString(2))) {
                                    gameKey = generationRandomNumber(10, 99);
                                    digits = properties.getProperty("Digit");
                                } else if (properties.getProperty("Digit").contains(Integer.toString(3))) {
                                    gameKey = generationRandomNumber(100, 999);
                                    digits = properties.getProperty("Digit");
                                } else if (properties.getProperty("Digit").contains(Integer.toString(4))) {
                                    gameKey = generationRandomNumber(1000, 9999);
                                    digits = properties.getProperty("Digit");
                                } else if (properties.getProperty("Digit").contains(Integer.toString(5))) {
                                    gameKey = generationRandomNumber(10000, 99999);
                                    digits = properties.getProperty("Digit");
                                }

                                TextChannel channelName;
                                Role rewardName = null;

                                if (event.getGuild().getTextChannels().stream().map(ISnowflake::getId).anyMatch(any -> any.contains(properties.getProperty("Channel")))) {
                                    channelName = event.getGuild().getTextChannelById(properties.getProperty("Channel"));
                                } else {
                                    channelName = event.getChannel();
                                }

                                if (event.getGuild().getRoles().stream().map(ISnowflake::getId).anyMatch(any -> any.contains(properties.getProperty("Reward")))) {
                                    rewardName = event.getGuild().getRoleById(properties.getProperty("Reward"));
                                } else {
                                    if (args.size() <= 2) {
                                        getRoleMap = new HashMap<>();
                                        gameMessage.setTitle("Before starting ヽ(°〇°)ﾉ");
                                        gameMessage.setDescription("Type the Reward role to complete the steps!");
                                        gameMessage.setColor(Color.ORANGE);

                                        message.delete().queue();
                                        Message message = event.getChannel().sendMessage(gameMessage.build()).complete();
                                        getRoleMap.put(event.getAuthor(), message);
                                        return;
                                    }
                                }

                                game = new numbersGame("numbers_" + event.getGuild(), event.getGuild(), channelName, df.parse(String.valueOf(gameID)).intValue(), gameKey, message, Integer.parseInt(digits), rewardName, format.format(date));
                            }
                            numbersGame.numbersStart.put(game.getGuild(), game);
                            numbersGame numbers = game.getNumbersStart().get(event.getGuild());

                            gameMessage.setTitle("Lets began (*・ω・)ﾉ");
                            gameMessage.setColor(Color.GREEN);
                            gameMessage.setFooter("\uD83D\uDD30 Created by : " + event.getAuthor().getName() + " | \uD83D\uDCC5 Date : " + format.format(date));
                            gameMessage.setDescription(
                                    "Game starting, have fun (o^ ^o)" +
                                            "\n **if you don't know what should you do, just send a random number including**\n" +
                                            "> **⬩ Digit Count \uD83D\uDCCA : **" + numbers.getMaxNumberCount() + " `which mean xxx`\n" +
                                            "> **⬩ Reward \uD83C\uDFC6 : **" + numbers.getReward().getAsMention() + "\n" +
                                            "> **⬩ Channel \uD83D\uDCE8 :** " + numbers.getTextChannel().getAsMention() + "\n" +
                                            "> **⬩ Game ID \uD83C\uDFF7 :** #" + numbers.getGameID() + "\n"
                            );
                            System.out.println("Key : " + numbers.getGameKeyWinner());

                            if (!event.getMessage().getTextChannel().getId().contains(game.getTextChannel().getId())) {
                                game.getTextChannel().sendMessage(gameMessage.build()).queue();
                            }

                            game.getMessage().editMessage(gameMessage.build()).queue();
                            checkGame.put(numbers.getGameName(), event.getMessage().getAuthor());

                        } catch (NullPointerException | NumberFormatException e) {

                            message.delete().queue();

                            EmbedBuilder embed = new EmbedBuilder();
                            embed.setColor(Color.red.brighter());
                            embed.setTitle("I can't find this role!  (ง •̀_•́)ง");
                            embed.setDescription("> **Usage : r!event numbers start `<role>`**");

                            event.getChannel().sendMessage(embed.build()).queue();
                            e.printStackTrace();
                        } catch (NoSuchElementException | IndexOutOfBoundsException e) {

                            message.delete().queue();

                            EmbedBuilder embed = new EmbedBuilder();
                            embed.setColor(Color.red.brighter());
                            embed.setTitle("I can't find it!  (ง •̀_•́)ง");
                            embed.setDescription("You don't use reward, you should use reward or the winner\n \n> **Usage : r!event numbers start `<role>`**");

                            event.getChannel().sendMessage(embed.build()).queue();
                            e.printStackTrace();
                        } catch (HierarchyException e) {

                            message.delete().queue();

                            EmbedBuilder embed = new EmbedBuilder();
                            embed.setColor(Color.red.brighter());
                            embed.setTitle("Missing Permission! (°ロ°)!");
                            embed.setDescription("This role is higher than bot role");

                            event.getChannel().sendMessage(embed.build()).queue();
                            e.printStackTrace();
                        } catch (Exception e) {

                            message.delete().queue();

                            EmbedBuilder error = new EmbedBuilder();
                            error.setColor(Color.red.brighter());
                            error.setTitle("Something went wrong!  (ง •̀_•́)ง");
                            error.setDescription("Report this error in Ryuko discord server\n \n**Error :** " + e.getMessage());

                            event.getChannel().sendMessage(error.build()).queue();
                            e.printStackTrace();
                        }
                    } else if (typeName.equalsIgnoreCase("end") || typeName.equalsIgnoreCase("stop")) {

                        if (game == null || (!game.getNumbersStart().containsKey(event.getGuild()))) {
                            gameMessage.setColor(Color.RED.brighter());
                            gameMessage.setDescription("Game not found " + event.getAuthor().getAsMention());
                            event.getChannel().sendMessage(gameMessage.build()).queue();
                        } else if (!checkGame.get(game.getNumbersStart().get(event.getGuild()).getGameName()).getId().contains(event.getAuthor().getId())) {
                            gameMessage.setColor(Color.RED.brighter());
                            gameMessage.setDescription(
                                    "You are not the creator of this event\n" +
                                            "> ⬩ Creator : " + checkGame.get(game.getGameName()).getAsMention() +
                                            "\n " + checkGame.get(game.getGameName()).getId() + " | " + event.getAuthor().getId());
                            event.getChannel().sendMessage(gameMessage.build()).queue();
                        } else {
                            gameMessage.setColor(Color.RED.brighter());
                            gameMessage.setTitle("Event Canceled (｡•́︿•̀｡)");
                            gameMessage.setDescription(
                                    "**Game has been closed by ** \n" +
                                            "> **⬩ Game ID \uD83C\uDFF7 : ** " + game.getNumbersStart().get(event.getGuild()).getGameID() + "\n" +
                                            "> **⬩ Number \uD83D\uDD22 : ** " + game.getNumbersStart().get(event.getGuild()).getGameKeyWinner() + "\n" +
                                            "> **⬩ Reward \uD83C\uDFC6 : " + game.getNumbersStart().get(event.getGuild()).getReward().getAsMention() + "**\n" +
                                            "> **⬩ Creator \uD83D\uDD30 : ** " + checkGame.get(game.getGameName()).getAsMention() + "\n" +
                                            "> **⬩ Date \uD83D\uDCC5 : ** " + game.getNumbersStart().get(event.getGuild()).getDate()
                            );

                            game.getNumbersStart().get(event.getGuild()).getMessage().editMessage(gameMessage.build()).queue();

                            if (!event.getMessage().getTextChannel().getId().contains(game.getNumbersStart().get(event.getGuild()).getTextChannel().getId())) {
                                game.getNumbersStart().get(event.getGuild()).getTextChannel().sendMessage(gameMessage.build()).queue();
                            }

                            checkGame.remove(game.getNumbersStart().get(event.getGuild()).getGameName());
                            game.getNumbersStart().remove(event.getGuild());
                        }

                    } else {
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setColor(Color.red.brighter());
                        embed.setTitle("I can't find it!  (ง •̀_•́)ง");
                        embed.setDescription("Usage : r!event number <type>\n Types : start / end / check / setup");

                        event.getChannel().sendMessage(embed.build()).queue();
                        return;
                    }
                } catch (NullPointerException | NoSuchElementException | IndexOutOfBoundsException | NumberFormatException e) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(Color.red.brighter());
                    embed.setTitle("I can't find it!  (ง •̀_•́)ง");
                    embed.setDescription("Usage : r!event number <type>\n Types : start / end / check / setup");

                    event.getChannel().sendMessage(embed.build()).queue();
                    e.printStackTrace();
                } catch (InsufficientPermissionException e) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(Color.red.brighter());
                    embed.setTitle("Missing Permission! (＃￣ω￣)");
                    embed.setDescription(e.getMessage());

                    event.getChannel().sendMessage(embed.build()).queue();
                    e.printStackTrace();
                } catch (Exception e) {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(Color.red.brighter());
                    error.setTitle("Something went wrong!  (ง •̀_•́)ง");
                    error.setDescription("Report this error in Ryuko discord server\n \n**Error :** " + e.getMessage());

                    event.getChannel().sendMessage(error.build()).queue();
                    e.printStackTrace();
                }
            }

        } catch (IndexOutOfBoundsException e) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(Color.red.brighter());
            error.setTitle("Something went wrong!  (ง •̀_•́)ง");
            error.setDescription("Game argument is null\n> Usage: `r!event <game>`");

            event.getChannel().sendMessage(error.build()).queue();
            e.printStackTrace();
        } catch (Exception e) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(Color.red.brighter());
            error.setTitle("Something went wrong!  (ง •̀_•́)ง");
            error.setDescription("Report this error in Ryuko discord server\n \n**Error :** " + e.getMessage());

            event.getChannel().sendMessage(error.build()).queue();
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "r!event <game>";
    }

    @Override
    public String getInVoke() {
        return "event";
    }

    @Override
    public String getDescription() {
        return "You can make an event in your server to have fun or to give players rewards";
    }

    @Override
    public Permission getPermission() {
        return Permission.ADMINISTRATOR;
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

    private int generationRandomNumber(int minimum, int maximum) {
        Random random = new Random();
        return random.nextInt(((maximum - minimum) + 1)) + minimum;
    }
}