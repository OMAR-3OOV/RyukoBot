package system.Listener;

import me.kbrewster.exceptions.APIException;
import me.kbrewster.hypixelapi.HypixelAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageUpdateEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mdashlw.hypixel.entities.player.stats.SkyWars;
import system.CommandManager;
import system.Commands.Administration.PrivateChattingBot.PrivateChatFilterManager;
import system.Commands.Administration.PrivateChattingBot.SendPrivateMessageCommand;
import system.Commands.Administration.rukoCommand;
import system.Commands.Administration.shopCommand;
import system.Commands.Administration.verifyCommand;
import system.Commands.Games.eventsGame;
import system.Commands.Games.eventsGames.numbersGame;
import system.Commands.Games.rpcGame;
import system.Commands.informationCategory.profileCommand;
import system.Commands.minecraftCategory.skywarsCommand;
import system.Constants;
import system.Objects.Config;
import system.Objects.TextUtils.MessageUtils;
import system.Objects.Utils.ActivityManagar;
import system.Objects.Utils.BoosterUtils.Booster;
import system.Objects.Utils.BoosterUtils.BoosterManagement;
import system.Objects.Utils.LastWinnersEvent;
import system.Objects.Utils.PrivateChatUtils.PrivateChat;
import system.Objects.Utils.PrivateChatUtils.PrivateChatMode;
import system.Objects.Utils.ProfileConfigUtils.ProfileBuilder;
import system.Objects.Utils.RandomStringAPI;
import system.Objects.Utils.coinsManager;
import system.Objects.Versions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class Events extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Class.class);
    private final CommandManager manager;

    // NumbersGame options
    public static HashMap<User, Integer> Steps = new HashMap<>();

    public static String prefix;

    public static HashMap<User, Integer> checkAction = new HashMap<>();
    public static HashMap<User, User> acceptRequest = new HashMap<>();
    public static HashMap<User, Integer> requestAccepted = new HashMap<>();

    public Events(CommandManager manager) {
        this.manager = manager;
    }

    int i = 0;

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        String[] games = {
                "r!help || " + event.getGuildTotalCount() + " Servers || %games% Games run || Version " + Arrays.stream(Versions.VERSIONS).findFirst().get(),
        };
        event.getJDA().getPresence().setActivity(Activity.playing(games[i]));

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                i++;
                if (i > (games.length - 1)) {
                    i = 0;
                }

                event.getJDA().getPresence().setActivity(Activity.playing(games[i].replace("%vc%", String.valueOf(event.getJDA().getAudioManagers().stream().map(m -> m.getConnectedChannel()).count())).replace("%games%", Integer.toString((rpcGame.game.entrySet().size() + eventsGame.checkGame.entrySet().size())))));
            }
        }, 0, (1000 * 5));

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                event.getJDA().getUsers().forEach(user -> {
                    if (CommandManager.commandCooldown.containsKey(user)) {
                        try {
                            CommandManager.commandCooldown.entrySet().forEach(k -> {
                                k.setValue(k.getValue()-1);
                                if (k.getValue() <= 0) {
                                    CommandManager.commandCooldown.remove(user);
                                    CommandManager.cooldownCount.remove(user);
                                }
                            });
                        } catch (Exception ignored) {

                        }
                    }
                });
            }
        }, (1000), 1000);

        LOGGER.info("I'm in " + event.getGuildTotalCount() + " Guild");
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {

        coinsManager ruko = new coinsManager(event.getGuild());

        // Guild file
        {
            if (event.getGuild().isLoaded()) {
                if (!ruko.getGuildFiles().exists()) {
                    ruko.createGuildFile();
                }
            }

            File order = new File("System/Guilds/");
            File file = new File("System/Guilds/" + event.getGuild().getId() + ".properties");

            if (!order.exists()) {
                file.getParentFile().mkdirs();
            }
            FileReader reader = null;
            try {
                reader = new FileReader(file);
            } catch (FileNotFoundException e) {
            }

            Properties p = new Properties();
            if (file.exists()) {
                try {
                    p.load(reader);
                } catch (IOException e) {

                }
            }

            if (!file.exists()) {
                try {
                    file.createNewFile();

                    p.setProperty("Name", event.getGuild().getName());
                    p.setProperty("Id", event.getGuild().getId());
                    p.setProperty("OwnerShipId", event.getGuild().getOwnerId());

                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    p.save(new FileOutputStream("System/Guilds/" + event.getGuild().getId() + ".properties"), null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        event.getJDA().getPresence().setActivity(Activity.playing("r!help || " + event.getJDA().getGuilds().stream().count() + " Servers || Version " + Arrays.stream(Versions.VERSIONS).findFirst().get()));
        LOGGER.info("I entered in " + event.getGuild().getName() + " Guild, ID : " + event.getGuild().getId() + " | The guilds count now is " + event.getJDA().getGuilds().stream().count());
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {

        File file = new File("System/Guilds/" + event.getGuild().getId() + ".properties");

        if (file.exists()) {
            file.delete();
        }

        event.getJDA().getPresence().setActivity(Activity.playing("r!help || " + event.getJDA().getGuilds().stream().count() + " Servers || Version " + Arrays.stream(Versions.VERSIONS).findFirst().get()));
        LOGGER.info("I left from " + event.getGuild().getName() + " Guild, ID : " + event.getGuild().getId() + " | The guilds count now is " + event.getJDA().getGuilds().stream().count());
    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        System.out.println("I have class today so you soon");
    }

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {

        if (event.getAuthor().isBot()) return;
        if (event.getAuthor().isFake()) return;

        if (verifyCommand.verifyCheck.containsKey(event.getAuthor())) {
            if (event.getMessage().getContentRaw().equals(verifyCommand.verifyCheck.get(event.getAuthor()))) {
                final ProfileBuilder profile = new ProfileBuilder(event.getAuthor());

                event.getAuthor().openPrivateChannel().queue((msg) -> msg.sendMessage("Your verify has been accepted ✅").queue());

                verifyCommand.verifyCompleted.get(event.getAuthor()).cancel();

                verifyCommand.verifyCompleted.remove(event.getAuthor());
                verifyCommand.verifyChecking.remove(event.getAuthor());
                verifyCommand.verifyCheck.remove(event.getAuthor());

                profile.setVerify(true);
            } else {
                event.getAuthor().openPrivateChannel().queue((msg) -> msg.sendMessage("Your verify has been denied ❌").queue());

                verifyCommand.verifyCompleted.get(event.getAuthor()).cancel();

                verifyCommand.verifyCompleted.remove(event.getAuthor());
                verifyCommand.verifyChecking.remove(event.getAuthor());
                verifyCommand.verifyCheck.remove(event.getAuthor());
            }
        }


        if (SendPrivateMessageCommand.getterchat.containsKey(event.getAuthor())) {
            PrivateChat getterchat = SendPrivateMessageCommand.getterchat.get(event.getAuthor());
            TextChannel channel = getterchat.getChannel();

            Message message;

            if (event.getMessage().getAttachments().stream().anyMatch(Message.Attachment::isImage) || event.getMessage().getAttachments().stream().anyMatch(Message.Attachment::isVideo) || event.getMessage().getAttachments().stream().anyMatch(Message.Attachment::isSpoiler)) {
                message = channel.sendMessage("> **" + event.getAuthor().getName() + ": **" + event.getMessage().getContentRaw() + "\n " + event.getMessage().getAttachments().stream().map(Message.Attachment::getUrl).collect(Collectors.joining())).complete();
            } else {
                message = channel.sendMessage("> **" + event.getAuthor().getName() + ": **" + event.getMessage().getContentRaw()).complete();
            }

            if (getterchat.lastMessage.containsKey(event.getAuthor())) {
                getterchat.lastMessage.get(getterchat.getGetter()).clearReactions().queue();
            }

            message.addReaction("✏").queue();
            getterchat.lastMessage.put(getterchat.getGetter(), message);
            getterchat.getLastBotMessages().add(event.getMessage());

        }
    }

    @Override
    public void onPrivateMessageUpdate(@NotNull PrivateMessageUpdateEvent event) {
        PrivateChat privateChat = SendPrivateMessageCommand.getterchat.get(event.getAuthor());

        if (privateChat != null) {
            if (privateChat.lastMessage.containsKey(privateChat.getGetter())) {
                privateChat.lastMessage.get(event.getAuthor()).editMessage("> **" + event.getAuthor().getName() + ": **" + event.getMessage().getContentRaw() + " `edited`").queue();
            }
        }
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        if (event.getAuthor().isBot()) return;
        if (event.getAuthor().isFake()) return;

        ActivityManagar activityManagar = new ActivityManagar(event.getGuild(), event.getAuthor());
        ProfileBuilder profile = new ProfileBuilder(event.getAuthor());

        numbersGame numbersGame = eventsGame.game;
        prefix = Constants.PREFIX;

        if (!event.getMessage().getContentRaw().contains(prefix)) {
            if (SendPrivateMessageCommand.privatechat.containsKey(event.getAuthor())) {
                if (SendPrivateMessageCommand.privatechat.get(event.getAuthor()).getMode().getId() == PrivateChatMode.CHATTING.getId()) {
                    if (SendPrivateMessageCommand.privatechat.get(event.getAuthor()).getGuild() == event.getGuild()) {
                        if (SendPrivateMessageCommand.privatechat.get(event.getAuthor()).getChannel() == event.getChannel()) {

                            PrivateChat privateChat = SendPrivateMessageCommand.privatechat.get(event.getAuthor());
                            PrivateChat getterChat = SendPrivateMessageCommand.getterchat.get(event.getAuthor());

                            privateChat.getGetter().openPrivateChannel().queue((u) -> {
                                if (privateChat.editMessage.containsKey(event.getAuthor())) {
                                    privateChat.getLastBotMessages().get(privateChat.getLastBotMessages().size() - 1).editMessage(new PrivateChatFilterManager(event.getMessage().getContentRaw()).toFilter(u.getUser())).queue();

                                    // clear reactions from old message
                                    privateChat.getSenderMessage().clearReactions().queue();

                                    // delete old message
                                    privateChat.getSenderMessage().delete().queue();
                                    // set new message
                                    privateChat.setSenderMessage(event.getMessage());
                                    privateChat.getSenderMessage().addReaction("\uD83D\uDD27").queue();

                                    privateChat.editMessage.remove(event.getAuthor());
                                    return;
                                }

                                if (privateChat.replyMessage.containsKey(event.getAuthor())) {
                                    getterChat.getLastBotMessages().get(getterChat.getLastBotMessages().size() - 1).reply(new PrivateChatFilterManager(event.getMessage().getContentRaw()).toFilter(u.getUser())).queue();

                                    getterChat.lastMessage.get(privateChat.getGetter()).clearReactions().queue();
                                    privateChat.getSenderMessage().clearReactions().queue();
                                    privateChat.setSenderMessage(event.getMessage());

                                    privateChat.getSenderMessage().addReaction("\uD83D\uDD27").queue();

                                    privateChat.replyMessage.remove(event.getAuthor());
                                    return;
                                }

                                Message message;

                                if (event.getMessage().getAttachments().stream().anyMatch(Message.Attachment::isImage) || event.getMessage().getAttachments().stream().anyMatch(Message.Attachment::isVideo) || event.getMessage().getAttachments().stream().anyMatch(Message.Attachment::isSpoiler)) {
                                    message = u.sendMessage(new PrivateChatFilterManager(event.getMessage().getContentRaw()).toFilter(u.getUser()) + "\n " + event.getMessage().getAttachments().stream().map(Message.Attachment::getUrl).collect(Collectors.joining())).complete();
                                } else {
                                    message = u.sendMessage(new PrivateChatFilterManager(event.getMessage().getContentRaw()).toFilter(u.getUser())).complete();
                                }

                                privateChat.getSenderMessage().clearReactions().queue();
                                privateChat.setSenderMessage(event.getMessage());
                                privateChat.getLastBotMessages().add(message);

                                privateChat.getSenderMessage().addReaction("\uD83D\uDD27").queue();
                            });

                            // old code
                            {
//                        SendPrivateMessageCommand.privateEvent.keySet().forEach((user -> {
//                            user.openPrivateChannel().queue((u) -> {
//                                if (editMessage.containsKey(event.getAuthor())) {
//                                    SendPrivateMessageCommand.lastBotMessages.get(SendPrivateMessageCommand.lastBotMessages.size() - 1).editMessage(new PrivateChatFilterManager(event.getMessage().getContentRaw()).toFilter(u.getUser())).queue();
//
//                                    SendPrivateMessageCommand.privateFunctions.get(event.getAuthor()).clearReactions().queue();
//                                    SendPrivateMessageCommand.privateFunctions.put(event.getAuthor(), event.getMessage());
//
//                                    event.getMessage().addReaction("\uD83D\uDD27").queue();
//                                    event.getMessage().addReaction("❌").queue();
//
//                                    editMessage.remove(event.getAuthor());
//                                    return;
//                                }
//
//                                if (replyMessage.containsKey(event.getAuthor())) {
//                                    SendPrivateMessageCommand.lastBotMessages.get(SendPrivateMessageCommand.lastBotMessages.size() - 1).reply(new PrivateChatFilterManager(event.getMessage().getContentRaw()).toFilter(u.getUser())).queue();
//
//                                    lastMessage.get(SendPrivateMessageCommand.privateEvent.keySet().stream().findFirst().get()).clearReactions().queue();
//                                    SendPrivateMessageCommand.privateFunctions.get(event.getAuthor()).clearReactions().queue();
//                                    SendPrivateMessageCommand.privateFunctions.put(event.getAuthor(), event.getMessage());
//
//                                    event.getMessage().addReaction("\uD83D\uDD27").queue();
//                                    event.getMessage().addReaction("❌").queue();
//
//                                    replyMessage.remove(event.getAuthor());
//                                    return;
//                                }
//
//                                Message message;
//
//                                if (event.getMessage().getAttachments().stream().anyMatch(Message.Attachment::isImage) || event.getMessage().getAttachments().stream().anyMatch(Message.Attachment::isVideo) || event.getMessage().getAttachments().stream().anyMatch(Message.Attachment::isSpoiler)) {
//                                    message = u.sendMessage(new PrivateChatFilterManager(event.getMessage().getContentRaw()).toFilter(u.getUser()) + "\n " + event.getMessage().getAttachments().stream().map(Message.Attachment::getUrl).collect(Collectors.joining())).complete();
//                                } else {
//                                    message = u.sendMessage(new PrivateChatFilterManager(event.getMessage().getContentRaw()).toFilter(u.getUser())).complete();
//                                }
//
//                                SendPrivateMessageCommand.privateFunctions.get(event.getAuthor()).clearReactions().queue();
//
//                                SendPrivateMessageCommand.privateFunctions.put(event.getAuthor(), event.getMessage());
//                                SendPrivateMessageCommand.lastBotMessages.add(message);
//
//                                event.getMessage().addReaction("\uD83D\uDD27").queue();
//                                event.getMessage().addReaction("❌").queue();
//                            });
//                        }));
                            }
                        }
                    }
                }
            }
        }

        if (eventsGame.setup != null) {
            if (eventsGame.setup.containsKey(event.getAuthor()) && (eventsGame.setup_guildCheck.get(event.getAuthor()) == event.getGuild())) {
                if (Steps.containsKey(event.getAuthor())) {

                    eventsGame.setup_steps.get(event.getAuthor()).setFooter("Keys: Skip | back | done ");

                    String digitCheck = "";
                    String channelCheck = "";
                    String rewardCheck = "";

                    File file = new File("System/Game/events/numbersGame/Guilds/" + event.getGuild().getId() + "/Settings.properties");
                    if (file.exists()) {
                        FileReader reader = null;
                        try {
                            reader = new FileReader(file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Properties properties = new Properties();

                        try {
                            properties.load(reader);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (event.getMessage().getContentRaw().equalsIgnoreCase("skip")) {
                            Steps.entrySet().stream().filter(f -> f.getKey().equals(event.getAuthor())).forEach((f) -> f.setValue(Steps.get(event.getAuthor()) + 1));
                            event.getMessage().delete().queue();

                            if (Steps.get(event.getAuthor()) == 0) {
                                digitCheck = new MessageUtils(":loading:").EmojisHolder();
                            } else if (Steps.get(event.getAuthor()) == 1) {
                                channelCheck = new MessageUtils(":loading:").EmojisHolder();
                            } else if (Steps.get(event.getAuthor()) == 2) {
                                rewardCheck = new MessageUtils(":loading:").EmojisHolder();
                            }

                            eventsGame.setup_steps.get(event.getAuthor()).clearFields();

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

                            eventsGame.setup_steps.get(event.getAuthor()).setDescription(
                                    "**Game Settings \uD83C\uDFAE : **" + "\n" +
                                            ("> **⬩ Digit Numbers :** %digit% " + digitCheck + "\n" +
                                                    "> **⬩ Game Channel :** %channel% " + channelCheck + "\n" +
                                                    "> **⬩ Reward Role :** %reward% " + rewardCheck + "\n").replace("%digit%", properties.getProperty("Digit")).replace("%channel%", channelName).replace("%reward%", rewardName)
                            );

                            if (Steps.get(event.getAuthor()) <= 2) {
                                eventsGame.setup_steps.get(event.getAuthor()).addField(" ", "` ❓ " + eventsGame.setup_options[Steps.get(event.getAuthor())] + "`", false);
                                eventsGame.setup.get(event.getAuthor()).editMessage(eventsGame.setup_steps.get(event.getAuthor()).build()).queue();
                            } else {
                                eventsGame.setup_steps.get(event.getAuthor()).clearFields();
                                eventsGame.setup_steps.get(event.getAuthor()).addField("", "`✅ The setup has been completed without facing any problem`", false);
                                eventsGame.setup.get(event.getAuthor()).editMessage(eventsGame.setup_steps.get(event.getAuthor()).build()).queue();
                                eventsGame.setup_steps.remove(event.getAuthor());
                                eventsGame.setup.remove(event.getAuthor());
                                Steps.remove(event.getAuthor());
                                Steps.entrySet().stream().filter(f -> f.getKey().equals(event.getAuthor())).forEach((f) -> f.setValue(0));

                            }
                            return;
                        } else if (event.getMessage().getContentRaw().equalsIgnoreCase("back")) {

                            if (Steps.get(event.getAuthor()) == 0) {
                                Steps.entrySet().stream().filter(f -> f.getKey().equals(event.getAuthor())).forEach((f) -> f.setValue((eventsGame.setup_steps.size() + 1)));
                            } else {
                                Steps.entrySet().stream().filter(f -> f.getKey().equals(event.getAuthor())).forEach((f) -> f.setValue(Steps.get(event.getAuthor()) - 1));
                            }

                            event.getMessage().delete().queue();

                            if (Steps.get(event.getAuthor()) == 0) {
                                digitCheck = new MessageUtils(":loading:").EmojisHolder();
                            } else if (Steps.get(event.getAuthor()) == 1) {
                                channelCheck = new MessageUtils(":loading:").EmojisHolder();
                            } else if (Steps.get(event.getAuthor()) == 2) {
                                rewardCheck = new MessageUtils(":loading:").EmojisHolder();
                            }

                            eventsGame.setup_steps.get(event.getAuthor()).clearFields();

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

                            eventsGame.setup_steps.get(event.getAuthor()).setDescription(
                                    "**Game Settings \uD83C\uDFAE : **" + "\n" +
                                            ("> **⬩ Digit Numbers :** %digit% " + digitCheck + "\n" +
                                                    "> **⬩ Game Channel :** %channel% " + channelCheck + "\n" +
                                                    "> **⬩ Reward Role :** %reward% " + rewardCheck + "\n").replace("%digit%", properties.getProperty("Digit")).replace("%channel%", channelName).replace("%reward%", rewardName)
                            );

                            if (Steps.get(event.getAuthor()) <= 2) {
                                eventsGame.setup_steps.get(event.getAuthor()).addField(" ", "` ❓ " + eventsGame.setup_options[Steps.get(event.getAuthor())] + "`", false);
                                eventsGame.setup.get(event.getAuthor()).editMessage(eventsGame.setup_steps.get(event.getAuthor()).build()).queue();
                            } else {
                                eventsGame.setup_steps.get(event.getAuthor()).clearFields();
                                eventsGame.setup_steps.get(event.getAuthor()).addField("", "`✅ The setup has been completed without facing any problem`", false);
                                eventsGame.setup.get(event.getAuthor()).editMessage(eventsGame.setup_steps.get(event.getAuthor()).build()).queue();
                                eventsGame.setup_steps.remove(event.getAuthor());
                                eventsGame.setup.remove(event.getAuthor());
                                Steps.remove(event.getAuthor());
                                Steps.entrySet().stream().filter(f -> f.getKey().equals(event.getAuthor())).forEach((f) -> f.setValue(0));
                            }
                            return;
                        } else if (event.getMessage().getContentRaw().equalsIgnoreCase("done")) {
                            eventsGame.setup_steps.get(event.getAuthor()).clearFields();
                            eventsGame.setup_steps.get(event.getAuthor()).addField("", "`✅ The setup has been completed without facing any problem`", false);
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

                            eventsGame.setup_steps.get(event.getAuthor()).setDescription(
                                    "**Game Settings \uD83C\uDFAE : **" + "\n" +
                                            ("> **⬩ Digit Numbers :** %digit% " + digitCheck + "\n" +
                                                    "> **⬩ Game Channel :** %channel% " + channelCheck + "\n" +
                                                    "> **⬩ Reward Role :** %reward% " + rewardCheck + "\n").replace("%digit%", properties.getProperty("Digit")).replace("%channel%", channelName).replace("%reward%", rewardName)
                            );
                            eventsGame.setup.get(event.getAuthor()).editMessage(eventsGame.setup_steps.get(event.getAuthor()).build()).queue();
                            eventsGame.setup_steps.remove(event.getAuthor());
                            eventsGame.setup.remove(event.getAuthor());
                            Steps.remove(event.getAuthor());
                            Steps.entrySet().stream().filter(f -> f.getKey().equals(event.getAuthor())).forEach((f) -> f.setValue(0));


                            return;
                        }

                        if (Steps.get(event.getAuthor()) == 0) {
                            channelCheck = new MessageUtils(":loading:").EmojisHolder();
                            try {
                                if (Integer.parseInt(event.getMessage().getContentRaw()) <= 0) {
                                    eventsGame.setup_steps.get(event.getAuthor()).clearFields();
                                    eventsGame.setup_steps.get(event.getAuthor()).addField(" ", "` ❓ " + eventsGame.setup_options[Steps.get(event.getAuthor())] + "` \n \n ❌ You can't use 0 number!", false);
                                    eventsGame.setup.get(event.getAuthor()).editMessage(eventsGame.setup_steps.get(event.getAuthor()).build()).queue();
                                } else {
                                    properties.setProperty("Digit", Integer.toString(Integer.parseInt(event.getMessage().getContentRaw())));
                                }
                            } catch (NumberFormatException e) {
                                eventsGame.setup_steps.get(event.getAuthor()).clearFields();
                                eventsGame.setup_steps.get(event.getAuthor()).addField(" ", "` ❓ " + eventsGame.setup_options[Steps.get(event.getAuthor())] + "` \n \n ❌ This is not a number!", false);
                                eventsGame.setup.get(event.getAuthor()).editMessage(eventsGame.setup_steps.get(event.getAuthor()).build()).queue();

                                e.printStackTrace();
                                return;
                            }
                        } else if (Steps.get(event.getAuthor()) == 1) {
                            rewardCheck = new MessageUtils(":loading:").EmojisHolder();
                            try {
                                String channelID = event.getMessage().getContentRaw();

                                final Pattern regexNotChars = Pattern.compile("\\p{Punct}");
                                final Matcher matcherNotChars = regexNotChars.matcher(channelID);

                                final Pattern regexIsChar = Pattern.compile("-?[A-Za-z]+");
                                final Matcher matcherIsChars = regexIsChar.matcher(channelID);

                                if (matcherNotChars.find()) {
                                    channelID = channelID.replace("<", "").replace("#", "").replace(">", "");
                                } else if (matcherIsChars.find()) {
                                    channelID = event.getGuild().getTextChannelsByName(channelID, false).get(0).getId();
                                }

                                TextChannel channel = event.getGuild().getTextChannelById(channelID);

                                properties.setProperty("Channel", channel.getId());
                            } catch (InsufficientPermissionException e) {
                                eventsGame.setup_steps.get(event.getAuthor()).clearFields();
                                eventsGame.setup_steps.get(event.getAuthor()).addField(" ", "` ❓ " + eventsGame.setup_options[Steps.get(event.getAuthor())] + "` \n \n `❌ I don't have permission to control this channel!`", false);
                                eventsGame.setup.get(event.getAuthor()).editMessage(eventsGame.setup_steps.get(event.getAuthor()).build()).queue();

                                e.printStackTrace();
                                return;
                            } catch (NumberFormatException e) {
                                eventsGame.setup_steps.get(event.getAuthor()).clearFields();
                                eventsGame.setup_steps.get(event.getAuthor()).addField(" ", "` ❓ " + eventsGame.setup_options[Steps.get(event.getAuthor())] + "` \n \n `❌ this channel ID is null", false);
                                eventsGame.setup.get(event.getAuthor()).editMessage(eventsGame.setup_steps.get(event.getAuthor()).build()).queue();

                                e.printStackTrace();
                            } catch (Exception e) {
                                eventsGame.setup_steps.get(event.getAuthor()).clearFields();
                                eventsGame.setup_steps.get(event.getAuthor()).addField(" ", "` ❓ " + eventsGame.setup_options[Steps.get(event.getAuthor())] + "` \n \n `❌ I can't find this channel!` \n Report this : " + e.getMessage(), false);
                                eventsGame.setup.get(event.getAuthor()).editMessage(eventsGame.setup_steps.get(event.getAuthor()).build()).queue();

                                e.printStackTrace();
                                return;
                            }
                        } else if (Steps.get(event.getAuthor()) == 2) {
                            try {
                                String role = event.getMessage().getContentRaw();

                                final Pattern regex = Pattern.compile(Message.MentionType.ROLE.getPattern().pattern());
                                final Matcher matcher = regex.matcher(role);

                                final Pattern regexRoleName = Pattern.compile("-?[A-Za-z]+");
                                final Matcher matcherRoleName = regexRoleName.matcher(role);

                                if (matcher.find()) {
                                    role = role.replace("<", "").replace("@", "").replace("&", "").replace(">", "");
                                } else if (matcherRoleName.find()) {
                                    role = event.getGuild().getRolesByName(role, true).stream().collect(Collectors.toList()).stream().findFirst().get().getId();
                                }
                                Role reward = event.getGuild().getRoleById(role);

                                properties.setProperty("Reward", reward.getId());
                            } catch (NullPointerException | NoSuchElementException | IndexOutOfBoundsException | NumberFormatException e) {
                                eventsGame.setup_steps.get(event.getAuthor()).clearFields();
                                eventsGame.setup_steps.get(event.getAuthor()).addField(" ", "` ❓ " + eventsGame.setup_options[Steps.get(event.getAuthor())] + "` \n \n `❌ I can't find this role!`", false);
                                eventsGame.setup.get(event.getAuthor()).editMessage(eventsGame.setup_steps.get(event.getAuthor()).build()).queue();

                                e.printStackTrace();
                                return;
                            } catch (InsufficientPermissionException e) {
                                eventsGame.setup_steps.get(event.getAuthor()).clearFields();
                                eventsGame.setup_steps.get(event.getAuthor()).addField(" ", "` ❓ " + eventsGame.setup_options[Steps.get(event.getAuthor())] + "` \n \n `❌ I don't have permission to control this role!`", false);
                                eventsGame.setup.get(event.getAuthor()).editMessage(eventsGame.setup_steps.get(event.getAuthor()).build()).queue();

                                e.printStackTrace();
                                return;
                            } catch (HierarchyException e) {
                                eventsGame.setup_steps.get(event.getAuthor()).clearFields();
                                eventsGame.setup_steps.get(event.getAuthor()).addField(" ", "` ❓ " + eventsGame.setup_options[Steps.get(event.getAuthor())] + "` \n \n `❌ This role is higher than mine!`", false);
                                eventsGame.setup.get(event.getAuthor()).editMessage(eventsGame.setup_steps.get(event.getAuthor()).build()).queue();

                                e.printStackTrace();
                                return;
                            }
                        }

                        eventsGame.setup_steps.get(event.getAuthor()).clearFields();

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

                        eventsGame.setup_steps.get(event.getAuthor()).setDescription(
                                "**Game Settings \uD83C\uDFAE : **" + "\n" +
                                        ("> **⬩ Digit Numbers :** %digit% " + digitCheck + "\n" +
                                                "> **⬩ Game Channel :** %channel% " + channelCheck + "\n" +
                                                "> **⬩ Reward Role :** %reward% " + rewardCheck + "\n").replace("%digit%", properties.getProperty("Digit")).replace("%channel%", channelName).replace("%reward%", rewardName)
                        );

                        Steps.entrySet().stream().filter(f -> f.getKey().equals(event.getAuthor())).forEach((f) -> f.setValue(Steps.get(event.getAuthor()) + 1));

                        if (Steps.get(event.getAuthor()) <= 2) {
                            eventsGame.setup_steps.get(event.getAuthor()).addField(" ", "` ❓ " + eventsGame.setup_options[Steps.get(event.getAuthor())] + "`", false);
                            eventsGame.setup.get(event.getAuthor()).editMessage(eventsGame.setup_steps.get(event.getAuthor()).build()).queue();
                        } else {
                            eventsGame.setup_steps.get(event.getAuthor()).clearFields();
                            eventsGame.setup_steps.get(event.getAuthor()).addField("", "`✅ The setup has been completed without facing any problem`", false);
                            eventsGame.setup.get(event.getAuthor()).editMessage(eventsGame.setup_steps.get(event.getAuthor()).build()).queue();
                            eventsGame.setup_steps.remove(event.getAuthor());
                            eventsGame.setup.remove(event.getAuthor());
                            Steps.remove(event.getAuthor());
                            Steps.entrySet().stream().filter(f -> f.getKey().equals(event.getAuthor())).forEach((f) -> f.setValue(0));

                        }
                        event.getMessage().delete().queue();

                        try {
                            properties.save(new FileOutputStream(file.getPath()), null);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (event.getMessage().getContentRaw().equalsIgnoreCase("yes") || event.getMessage().getContentRaw().equalsIgnoreCase("continue")) {
                        Steps.put(event.getAuthor(), eventsGame.setup_count);

                        File file = new File("System/Game/events/numbersGame/Guilds/" + event.getGuild().getId() + "/Settings.properties");
                        if (file.exists()) {
                            FileReader reader = null;
                            try {
                                reader = new FileReader(file);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Properties properties = new Properties();

                            try {
                                properties.load(reader);
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

                            eventsGame.setup_steps.get(event.getAuthor()).clearFields();
                            eventsGame.setup_steps.get(event.getAuthor()).addField(" ", "` ❓ " + eventsGame.setup_options[Steps.get(event.getAuthor())] + "`", false);
                            eventsGame.setup_steps.get(event.getAuthor()).setDescription(
                                    "**Game Settings \uD83C\uDFAE : **" + "\n" +
                                            ("> **⬩ Digit Numbers :** %digit% " + new MessageUtils(":loading:").EmojisHolder() + "\n" +
                                                    "> **⬩ Game Channel :** %channel%\n" +
                                                    "> **⬩ Reward Role :** %reward%\n").replace("%digit%", properties.getProperty("Digit")).replace("%channel%", channelName).replace("%reward%", rewardName)
                            );
                            eventsGame.setup.get(event.getAuthor()).editMessage(eventsGame.setup_steps.get(event.getAuthor()).build()).queue();
                            event.getMessage().delete().queue();

                            Steps.entrySet().stream().filter(f -> f.getKey().equals(event.getAuthor())).forEach((f) -> f.setValue(0));
                        }
                    } else if (event.getMessage().getContentRaw().equalsIgnoreCase("no") || event.getMessage().getContentRaw().equalsIgnoreCase("stop")) {
                        event.getMessage().delete().queue();
                        eventsGame.setup.get(event.getAuthor()).delete().queue();
                        eventsGame.setup_steps.remove(event.getAuthor());
                        eventsGame.setup.remove(event.getAuthor());
                    } else {
                        event.getMessage().delete().queue();
                        eventsGame.setup_steps.get(event.getAuthor()).clearFields();
                        eventsGame.setup_steps.get(event.getAuthor()).addField("", "Please type only `Yes` or `No` to complete the setup", false);
                        eventsGame.setup.get(event.getAuthor()).editMessage(eventsGame.setup_steps.get(event.getAuthor()).build()).queue();
                    }
                }
            }
        }

        if (eventsGame.getRoleMap != null) {
            if (eventsGame.getRoleMap.containsKey(event.getAuthor())) {
                try {
                    String role = event.getMessage().getContentRaw();

                    final Pattern regex = Pattern.compile(Message.MentionType.ROLE.getPattern().pattern());
                    final Matcher matcher = regex.matcher(role);

                    final Pattern regexRoleName = Pattern.compile("-?[A-Za-z]+");
                    final Matcher matcherRoleName = regexRoleName.matcher(role);

                    if (matcher.find()) {
                        role = role.replace("<", "").replace("@", "").replace("&", "").replace(">", "");
                    } else if (matcherRoleName.find()) {
                        role = new ArrayList<>(event.getGuild().getRolesByName(role, true)).stream().findFirst().get().getId();
                    }

                    if (event.getGuild().getRoles().contains(event.getGuild().getRoleById(role))) {
                        DecimalFormat df = new DecimalFormat("###");
                        int gameID = generationRandomNumber(10000, 99999);
                        int gameKey = generationRandomNumber(100, 999);

                        SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");
                        Date date = new Date();

                        try {
                            numbersGame n_game = new numbersGame("numbers_" + event.getGuild(), event.getGuild(), event.getChannel(), df.parse(String.valueOf(gameID)).intValue(), gameKey, eventsGame.getRoleMap.get(event.getAuthor()), 3, Objects.requireNonNull(event.getGuild().getRoleById(role)).getManager().getRole(), format.format(date));
                            system.Commands.Games.eventsGames.numbersGame.numbersStart.put(n_game.getGuild(), n_game);
                            numbersGame n1 = n_game.getNumbersStart().get(event.getGuild());

                            EmbedBuilder gameMessage = new EmbedBuilder();
                            gameMessage.setTitle("Lets began (*・ω・)ﾉ");
                            gameMessage.setColor(Color.GREEN);
                            gameMessage.setFooter("\uD83D\uDD30 Created by : " + event.getAuthor().getName() + " | \uD83D\uDCC5 Date : " + format.format(date));
                            gameMessage.setDescription(
                                    "Game starting, have fun (o^ ^o)" +
                                            "\n **if you don't know what should you do, just send a random number including**\n" +
                                            "> **⬩ Digit Count \uD83D\uDCCA : **" + n1.getNumbersStart().get(event.getGuild()).getMaxNumberCount() + " `which mean xxx`\n" +
                                            "> **⬩ Reward \uD83C\uDFC6 : **" + n1.getNumbersStart().get(event.getGuild()).getReward().getAsMention() + "\n" +
                                            "> **⬩ Game ID \uD83C\uDFF7 :** #" + n1.getNumbersStart().get(event.getGuild()).getGameID() + "\n"
                            );

                            eventsGame.getRoleMap.get(event.getAuthor()).editMessage(gameMessage.build()).queue();
                            eventsGame.game = n1;
                            eventsGame.checkGame.put(n1.getNumbersStart().get(event.getGuild()).getGameName(), event.getMessage().getAuthor());
                            eventsGame.getRoleMap.remove(event.getAuthor());

                            System.out.println("Key : " + n1.getGameKeyWinner());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (NullPointerException | NoSuchElementException | IndexOutOfBoundsException | NumberFormatException e) {
                    event.getChannel().sendMessage(new MessageUtils(":error: | " + event.getAuthor().getAsMention() + ", I can't find this role, make sure you write a role, " + e.getMessage()).EmojisHolder()).queue();
                    e.printStackTrace();
                } catch (InsufficientPermissionException e) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(Color.red.brighter());
                    embed.setTitle("Missing Permission! (＃￣ω￣)");
                    embed.setDescription(e.getMessage());

                    event.getChannel().sendMessage(embed.build()).queue();
                    e.printStackTrace();
                } catch (HierarchyException e) {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(Color.red.brighter());
                    embed.setTitle("Missing Permission! (°ロ°)!");
                    embed.setDescription("This role is higher than bot role");

                    event.getChannel().sendMessage(embed.build()).queue();
                    e.printStackTrace();
                }
            }
        }

        if (numbersGame != null) {
            if (numbersGame.getNumbersStart().containsKey(event.getGuild())) {
                if (numbersGame.getNumbersStart().get(event.getGuild()).getTextChannel().getId().contains(event.getMessage().getTextChannel().getId())) {
                    try {
                        if (event.getMessage().getContentRaw().equalsIgnoreCase(String.valueOf(numbersGame.getNumbersStart().get(event.getGuild()).getGameKeyWinner()))) {

                            LastWinnersEvent lastwinner = new LastWinnersEvent();

                            event.getMessage().addReaction("\uD83C\uDF89").queue();
                            numbersGame numbers = numbersGame.getNumbersStart().get(event.getGuild());
                            numbers.getWinner().put(numbers.getGameKeyWinner(), event.getMessage().getAuthor());

                            EmbedBuilder gameMessage = new EmbedBuilder();
                            gameMessage.setColor(Color.orange);
                            gameMessage.setDescription(
                                    "**\uD83E\uDD73 Congratulation (ﾉ´ з `)ノ** \n \n" +
                                            "> **⬩ Winner \uD83C\uDFC5 : ** " + numbers.getWinner().entrySet().stream().map(m -> m.getValue().getAsMention()).collect(Collectors.joining()) + "\n" +
                                            "> **⬩ Game Number \uD83D\uDCCA : ** " + numbers.getGameKeyWinner() + "\n" +
                                            "> **⬩ Creator \uD83D\uDD30 : ** " + eventsGame.checkGame.get(numbers.getGameName()).getAsMention() + "\n" +
                                            "> **⬩ Reward \uD83C\uDFC6 : ** " + numbers.getReward().getAsMention() + "\n" +
                                            "> **⬩ Date \uD83D\uDCC6 : ** " + numbers.getDate()
                            );

                            if (numbers.getReward() != null) {
                                event.getGuild().addRoleToMember(numbers.getWinner().get(numbers.getGameKeyWinner()).getId(), numbers.getReward()).queue();
                            }

                            numbers.getWinner().get(numbers.getGameKeyWinner()).openPrivateChannel().queue((channel) -> {
                                EmbedBuilder winMessage = new EmbedBuilder();
                                winMessage.setColor(Color.GREEN.brighter());
                                winMessage.setDescription(
                                        "\uD83E\uDD73 **Congratulation %user% you Win in the numbers event ٩(｡•́‿•̀｡)۶** \n".replace("%user%", numbers.getWinner().entrySet().stream().map(m -> m.getValue().getAsMention()).collect(Collectors.joining())) +
                                                "> **⬩ Your reward is `@" + numbers.getReward().getName() + "` role** \n" +
                                                "> **⬩ Server : ** " + numbers.getGuild().getName() + "\n" +
                                                "> **⬩ Game Key : ** " + numbers.getGameKeyWinner() + "\n" +
                                                "> **⬩ Game ID : **" + numbers.getGameID() + " `( Important )`"
                                );
                                channel.sendMessage(winMessage.build()).queue();
                            });

                            lastwinner.addWinner(numbers.getWinner().values().stream().findFirst().get());
                            event.getChannel().sendMessage(gameMessage.build()).queue();

                            numbers.getNumbersStart().remove(numbers.getGuild());
                            eventsGame.checkGame.remove(numbers.getGameName());
                        }
                    } catch (HierarchyException e) {
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
            }

        }

        if (checkAction != null) {
            if (checkAction.containsKey(event.getAuthor())) {
                if (checkAction.get(event.getAuthor()).equals(0)) {
                    try {

                        EmbedBuilder embed = new EmbedBuilder();
                        String user = event.getMessage().getContentRaw();

                        final Pattern regex = Pattern.compile(Message.MentionType.USER.getPattern().pattern());
                        final Matcher matcher = regex.matcher(user);

                        final Pattern regexRoleName = Pattern.compile("-?[A-Za-z]+");
                        final Matcher matcherRoleName = regexRoleName.matcher(user);

                        if (matcher.find()) {
                            user = user.replace("<", "").replace("!", "").replace("@", "").replace("#", "").replace("&", "").replace(">", "");
                        } else if (matcherRoleName.find()) {
                            user = Objects.requireNonNull(event.getGuild().getMembersByName(user, true)).stream().map(m -> m.getUser().getId()).collect(Collectors.joining());
                        }

                        if (Objects.requireNonNull(event.getGuild().getMemberById(user)).getId().equals("741944404008239134")) {
                            embed.clearFields();
                            acceptRequest.remove(event.getAuthor());
                            requestAccepted.remove(event.getAuthor());
                            checkAction.remove(event.getAuthor());

                            event.getMessage().delete().queue();
                            rukoCommand.rukoAction.get(event.getAuthor()).editMessage(embed.build()).queue();
                            return;
                        }

                        coinsManager ruko = new coinsManager(event.getAuthor());
                        String nickname = " ";

                        if (Objects.requireNonNull(event.getMember()).getNickname() != null) {
                            nickname = " (" + Objects.requireNonNull(event.getMember()).getNickname() + ")";
                        }

                        if (Objects.requireNonNull(event.getGuild().getMemberById(user)).getUser().equals(event.getAuthor())) {
                            event.getMessage().delete().queue();
                            event.getChannel().sendMessage(event.getAuthor().getAsMention() + " **You can't pay to yourself**").delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
                            return;
                        }

                        // Getting Image from url
                        URLConnection urlConnection = null;
                        try {
                            urlConnection = new URL(Objects.requireNonNull(event.getAuthor().getAvatarUrl())).openConnection();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (urlConnection != null) {
                            urlConnection.addRequestProperty("User-Agent", "Ryuko");
                        }

                        BufferedImage image = null;
                        try {
                            if (urlConnection != null) {
                                image = ImageIO.read(urlConnection.getInputStream());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // Getting pixel color by position x and y
                        int clr = image.getRGB(image.getMinX(), image.getMinY());
                        int red = (clr & 0x00ff0000) >> 16;
                        int green = (clr & 0x0000ff00) >> 8;
                        int blue = clr & 0x000000ff;

                        String boostertext = " ";
                        Booster.loadBoosters();

                        if (!ruko.getProperties().getProperty("Booster").contains("null")) {
                            boostertext = "> **Ruko Booster \uD83D\uDE80 :** " + Booster.getBooster().get(Integer.parseInt(ruko.getProperties().getProperty("Booster"))).getName() + " x" + Booster.getBooster().get(Integer.parseInt(ruko.getProperties().getProperty("Booster"))).getCombo() + "\n";
                        }

                        embed.setColor(new Color(red, green, blue));
                        embed.setThumbnail(event.getAuthor().getAvatarUrl());
                        embed.setAuthor(ruko.getUser().getName() + nickname, ruko.getUser().getAvatarUrl(), ruko.getUser().getAvatarUrl());
                        embed.setDescription(new MessageUtils("\n" +
                                "> **All Ruko Gains \uD83D\uDCB0 :** " + ruko.getGains() + "\n" +
                                "> **Ruko :ruko: : **" + ruko.getRukoUser() + "\n \n" +
                                boostertext +
                                "⬩ To **Pay** for someone \uD83D\uDCB8 **|** To **Donation** for server \uD83D\uDCB0"
                        ).EmojisHolder());

                        embed.clearFields();
                        embed.addBlankField(false);
                        embed.addField("⬩ Information : ", "> ** ⬩ User : **" + Objects.requireNonNull(event.getGuild().getMemberById(user)).getAsMention() + " \n> **⬩ Amount :** Type the amount that you want to pay", false);

                        rukoCommand.rukoAction.get(event.getAuthor()).editMessage(embed.build()).queue();

                        checkAction.remove(event.getAuthor());
                        checkAction.put(event.getAuthor(), 2);
                        acceptRequest.put(event.getAuthor(), Objects.requireNonNull(event.getGuild().getMemberById(user)).getUser());

                        event.getMessage().delete().queue();
                    } catch (NullPointerException e) {
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setColor(Color.red.brighter());
                        embed.setTitle("I can't find this user!  (ง •̀_•́)ง");
                        embed.setDescription("Mention the user or write his name or id!");

                        acceptRequest.remove(event.getAuthor());
                        requestAccepted.remove(event.getAuthor());
                        checkAction.remove(event.getAuthor());

                        rukoCommand.rukoAction.get(event.getAuthor()).delete().queue();
                        event.getMessage().delete().queue();

                        event.getChannel().sendMessage(embed.build()).queue();
                        e.printStackTrace();
                    } catch (Exception e) {
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setColor(Color.red.brighter());
                        embed.setTitle("Something went wrong! (ง •̀_•́)ง \n Report this for developer");
                        embed.setDescription("Error: " + e.getMessage());

                        event.getChannel().sendMessage(embed.build()).queue();
                        e.printStackTrace();
                    }
                } else if (checkAction.get(event.getAuthor()).equals(1)) {

                } else if (checkAction.get(event.getAuthor()).equals(2)) {
                    try {
                        int amount = Integer.parseInt(event.getMessage().getContentRaw());

                        coinsManager ruko = new coinsManager(event.getAuthor());
                        String nickname = " ";

                        if (Objects.requireNonNull(event.getMember()).getNickname() != null) {
                            nickname = " (" + Objects.requireNonNull(event.getMember()).getNickname() + ")";
                        }

                        EmbedBuilder embed = new EmbedBuilder();

                        if (ruko.getRukoUser() < amount) {
                            event.getMessage().delete().queue();
                            event.getChannel().sendMessage(event.getAuthor().getAsMention() + " **You don't have enough Ruko's to pay**").delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
                            return;
                        }

                        if (amount <= 0) {
                            event.getMessage().delete().queue();
                            event.getChannel().sendMessage(event.getAuthor().getAsMention() + " **You can't pay less than 1 amounts of Ruko**").delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue();
                            return;
                        }

                        // Getting Image from url
                        URLConnection urlConnection = null;
                        try {
                            urlConnection = new URL(Objects.requireNonNull(event.getAuthor().getAvatarUrl())).openConnection();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (urlConnection != null) {
                            urlConnection.addRequestProperty("User-Agent", "Ryuko");
                        }

                        BufferedImage image = null;
                        try {
                            if (urlConnection != null) {
                                image = ImageIO.read(urlConnection.getInputStream());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // Getting pixel color by position x and y
                        int clr = image.getRGB(image.getMinX(), image.getMinY());
                        int red = (clr & 0x00ff0000) >> 16;
                        int green = (clr & 0x0000ff00) >> 8;
                        int blue = clr & 0x000000ff;

                        String boostertext = " ";
                        Booster.loadBoosters();

                        if (!ruko.getProperties().getProperty("Booster").contains("null")) {
                            boostertext = "> **Ruko Booster \uD83D\uDE80 :** " + Booster.getBooster().get(Integer.parseInt(ruko.getProperties().getProperty("Booster"))).getName() + " x" + Booster.getBooster().get(Integer.parseInt(ruko.getProperties().getProperty("Booster"))).getCombo() + "\n";
                        }

                        embed.setColor(new Color(red, green, blue));
                        embed.setThumbnail(event.getAuthor().getAvatarUrl());
                        embed.setAuthor(ruko.getUser().getName() + nickname, ruko.getUser().getAvatarUrl(), ruko.getUser().getAvatarUrl());
                        embed.setDescription(new MessageUtils("\n" +
                                "> **All Ruko Gains \uD83D\uDCB0 :** " + ruko.getGains() + "\n" +
                                "> **Ruko :RUKO: : **" + ruko.getRukoUser() + "\n \n" +
                                boostertext +
                                "⬩ To **Pay** for someone \uD83D\uDCB8 **|** To **Donation** for server \uD83D\uDCB0"
                        ).EmojisHolder());

                        embed.clearFields();
                        embed.addBlankField(false);
                        embed.addField("Click to \uD83D\uDD3D to send", "⬩ Information : \n> ⬩ User : " + acceptRequest.get(event.getAuthor()).getAsMention() + " \n> ⬩ Amount : " + amount, false);

                        rukoCommand.rukoAction.get(event.getAuthor()).editMessage(embed.build()).queue();
                        rukoCommand.rukoAction.get(event.getAuthor()).addReaction("\uD83D\uDD3D").queue();
                        rukoCommand.rukoAction.get(event.getAuthor()).addReaction("❌").queue();

                        checkAction.remove(event.getAuthor());
                        checkAction.put(event.getAuthor(), 3);
                        requestAccepted.put(event.getAuthor(), amount);

                        event.getMessage().delete().queue();
                    } catch (NullPointerException | NumberFormatException e) {
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setColor(Color.red.brighter());
                        embed.setTitle("You don't have this cost of ruko's!  (ง •̀_•́)ง");
                        embed.setDescription("write the amount you want to pay it");

                        event.getChannel().sendMessage(embed.build()).queue();
                        e.printStackTrace();
                    } catch (Exception e) {
                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setColor(Color.red.brighter());
                        embed.setTitle("Something went wrong! (ง •̀_•́)ง \n Report this for developer");
                        embed.setDescription("Error: " + e.getMessage());

                        event.getChannel().sendMessage(embed.build()).queue();
                        e.printStackTrace();
                    }
                }
            }
        }

        if (!event.getMessage().getContentRaw().contains(prefix)) {
            activityManagar.addActivity(event, 9, 7, 5, 3, 1);
        }

        String raw = event.getMessage().getContentRaw();

        if (!(event.getAuthor().isBot() || event.getMessage().isWebhookMessage() || event.getAuthor().isFake() || event.getMessage().getContentRaw().startsWith(prefix)))
            return;

        if (raw.equalsIgnoreCase(prefix + "shutdown") && event.getAuthor().getId().equals(Config.get("OWNER"))) {
            event.getMessage().delete().queue();
            event.getJDA().shutdownNow();
            System.exit(0);
        }

        coinsManager ruko = new coinsManager(event.getAuthor());

        int calculation = (int) (activityManagar.getActivity() * (0.25));

        if (raw.equalsIgnoreCase(prefix + "claim")) {
            if (activityManagar.getActivity() > 0) {

                ruko.addRukoUser(calculation);
                profile.setRuko(profile.getRuko() + calculation);
                ruko.saveFile();

                EmbedBuilder embed = new EmbedBuilder();
                embed.setThumbnail(event.getAuthor().getAvatarUrl());
                embed.setColor(Color.green.brighter());
                embed.setTitle("Claiming money \uD83D\uDCB0");
                embed.setDescription(new MessageUtils("\n Your claim has been successful \n \n **Ruko :ruko: **: " + ruko.getRukoUser() + " > " + (ruko.getRukoUser() + calculation) + " `+" + calculation + "`").EmojisHolder());

                event.getChannel().sendMessage(embed.build()).queue();
                activityManagar.resetActivity();
            } else {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + " **You don't have any `Ruko` to claim it**").queue();
            }
        } else if (raw.equalsIgnoreCase(prefix + "activity")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setThumbnail(event.getAuthor().getAvatarUrl());
            embed.setColor(Color.ORANGE.brighter());
            embed.setTitle("Claiming money \uD83D\uDCB0");
            embed.setDescription("\n You have `" + calculation + "` Ruko in Bank, You can claim it by using `r!claim`");

            event.getChannel().sendMessage(embed.build()).queue();
        }

        try {
            manager.handleCommand(event, prefix);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {

        if (event.getUser().isBot() || event.getUser().isFake()) {
            return;
        }

        String checkIdgame = " ";
        String checkIdhypixel = " ";

        PrivateChat privateChat = SendPrivateMessageCommand.privatechat.get(event.getUser());
        PrivateChat getterChat = SendPrivateMessageCommand.getterchat.get(event.getUser());
        final ProfileBuilder profile = new ProfileBuilder(event.getUser());

        if (profileCommand.verify != null) {
            if (profileCommand.verify.containsKey(event.getUser())) {
                if (profileCommand.verify.get(event.getUser()) == event.getChannel()) {
                    switch (event.getReactionEmote().getName()) {
                        case "✅":
                            if (profile.getVerify()) {
                                event.getChannel().sendMessage(new MessageUtils(":error: | **Your account is already verified**").EmojisHolder()).queue();
                                return;
                            }

                            RandomStringAPI rna = new RandomStringAPI(event.getUser(), 20);

                            Timer timer = new Timer();

                            timer.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {
                                    if (rna.getMap().containsKey(event.getUser())) {
                                        rna.getMap().remove(event.getUser());

                                        event.getUser().openPrivateChannel().queue((privateChannel -> {
                                            privateChannel.sendMessage("**Verify Code has been disappeared \uD83D\uDE3F**").queue();
                                        }));
                                    }
                                }
                            }, (1000 * 60), (1000 * 60));

                            verifyCommand.verifyCompleted.put(event.getUser(), timer);

                            event.getUser().openPrivateChannel().queue((privateChannel -> {
                                EmbedBuilder embed = new EmbedBuilder();
                                embed.setTitle("Verify your discord account in bot data");
                                embed.setColor(Color.RED);
                                embed.addField(" ", "**Code :** " + rna.getKey(), false);

                                privateChannel.sendMessage(embed.build()).queue();
                                verifyCommand.verifyCheck.put(event.getUser(), rna.getKey());
                            }));

                            event.getChannel().sendMessage("Check your private chat, you have 60 seconds only to enter the code, if you didn't enter it the code will disappear").delay(60, TimeUnit.SECONDS).queue();
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        if (privateChat != null) {
            if (privateChat.getChannel().getId().contains(event.getChannel().getId())) {
                if (privateChat.getMode().getId() == PrivateChatMode.CHATTING.getId()) {
                    if (privateChat.getStarted()) {
                        switch (event.getReactionEmote().getName()) {
                            case "✏": // reply
                                privateChat.replyMessage.put(event.getUser(), getterChat.lastMessage.get(getterChat.getGetter()));
                                getterChat.lastMessage.get(privateChat.getGetter()).clearReactions().queue();

                                getterChat.lastMessage.get(privateChat.getGetter()).addReaction("❌").queue();
                                event.getReaction().removeReaction(event.getUser()).queue();
                                break;
                            case "❌": // cancel
                                if (privateChat.replyMessage.containsKey(event.getUser())) {
                                    privateChat.replyMessage.remove(event.getUser());

                                    getterChat.lastMessage.get(privateChat.getGetter()).clearReactions().queue();
                                    getterChat.lastMessage.get(privateChat.getGetter()).addReaction("✏").queue();
                                    return;
                                }
                                break;
                            default:
                                event.getReaction().removeReaction(event.getUser()).queue();
                                break;
                        }

                        if (privateChat.getSenderMessage().getId().contains(event.getMessageId())) {
                            switch (event.getReactionEmote().getName()) {
                                case "\uD83D\uDD27": // edit
                                    privateChat.editMessage.put(event.getUser(), privateChat.getLastBotMessages().get(privateChat.getLastBotMessages().size() - 1));
                                    privateChat.getSenderMessage().clearReactions().queue();
                                    privateChat.getSenderMessage().addReaction("❌").queue();
                                    break;
                                case "❌": // cancel
                                    if (privateChat.editMessage.containsKey(event.getUser())) {
                                        privateChat.editMessage.remove(event.getUser());

                                        privateChat.getSenderMessage().clearReactions().queue();

                                        privateChat.getSenderMessage().addReaction("\uD83D\uDD27").queue();
                                        return;
                                    }

                                    if (privateChat.replyMessage.containsKey(event.getUser())) {
                                        privateChat.replyMessage.remove(event.getUser());

                                        privateChat.getSenderMessage().addReaction("✏").queue();
                                        return;
                                    }
                                    break;
                                default:
                                    break;
                            }
                            event.getReaction().removeReaction(event.getUser()).queue();
                        }
                    }
                } else return;
            }
        }

        if (rukoCommand.rukoAction != null) {
            HashMap<User, Message> action = rukoCommand.rukoAction;
            if (action.containsKey(event.getUser())) {

                coinsManager ruko = new coinsManager(event.getUser());
                String nickname = " ";

                if (Objects.requireNonNull(event.getMember()).getNickname() != null) {
                    nickname = " (" + Objects.requireNonNull(event.getMember()).getNickname() + ")";
                }

                EmbedBuilder embed = new EmbedBuilder();

                // Getting Image from url
                URLConnection urlConnection = null;
                try {
                    urlConnection = new URL(Objects.requireNonNull(event.getUser().getAvatarUrl())).openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (urlConnection != null) {
                    urlConnection.addRequestProperty("User-Agent", "Ryuko");
                }

                BufferedImage image = null;
                try {
                    if (urlConnection != null) {
                        image = ImageIO.read(urlConnection.getInputStream());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Getting pixel color by position x and y
                int clr = image.getRGB(image.getMinX(), image.getMinY());
                int red = (clr & 0x00ff0000) >> 16;
                int green = (clr & 0x0000ff00) >> 8;
                int blue = clr & 0x000000ff;

                String boostertext = " ";
                Booster.loadBoosters();

                if (!ruko.getProperties().getProperty("Booster").contains("null")) {
                    boostertext = "> **Ruko Booster \uD83D\uDE80 :** " + Booster.getBooster().get(Integer.parseInt(ruko.getProperties().getProperty("Booster"))).getName() + " x" + Booster.getBooster().get(Integer.parseInt(ruko.getProperties().getProperty("Booster"))).getCombo() + "\n";
                }

                embed.setColor(new Color(red, green, blue));
                embed.setThumbnail(event.getUser().getAvatarUrl());
                embed.setAuthor(ruko.getUser().getName() + nickname, ruko.getUser().getAvatarUrl(), ruko.getUser().getAvatarUrl());
                embed.setDescription(new MessageUtils("\n" +
                        "> **All Ruko Gains \uD83D\uDCB0 :** " + ruko.getGains() + "\n" +
                        "> **Ruko :ruko: : **" + ruko.getRukoUser() + "\n \n" +
                        boostertext +
                        "⬩ To **Pay** for someone \uD83D\uDCB8 **|** To **Donation** for server \uD83D\uDCB0"
                ).EmojisHolder());

                switch (event.getReactionEmote().getName()) {
                    case "\uD83D\uDCB8":
                        embed.clearFields();
                        embed.addBlankField(false);
                        embed.addField("Paying to `-`", "> ⬩ Mention the user that you want to pay for him", false);
                        action.get(event.getUser()).clearReactions().queue();
                        event.getReaction().removeReaction(event.getUser()).queue();
                        action.get(event.getUser()).addReaction("❌").queue();
                        checkAction.put(event.getUser(), 0);
                        break;
                    case "\uD83D\uDCB0":
                        embed.clearFields();
                        embed.addBlankField(false);
                        embed.addField("Donation to server", "> ⬩ Make sure that there is 5% val will go to server", false);
                        action.get(event.getUser()).clearReactions().queue();
                        event.getReaction().removeReaction(event.getUser()).queue();
                        checkAction.put(event.getUser(), 1);
                        break;
                    case "\uD83D\uDD3D":
                        if (checkAction != null) {
                            if (checkAction.get(event.getUser()).equals(3)) {
                                coinsManager ruko1 = new coinsManager(acceptRequest.get(event.getUser()));

                                ruko.removeRukoUser(requestAccepted.get(event.getUser()));
                                ruko1.addRukoUser(requestAccepted.get(event.getUser()));

                                embed.setDescription(new MessageUtils("\n" +
                                        "> **All Ruko Gains \uD83D\uDCB0 :** " + ruko.getGains() + "\n" +
                                        "> **Ruko :ruko: : **" + (ruko.getRukoUser()) + "`-" + requestAccepted.get(event.getUser()) + "`" + "\n \n" +
                                        boostertext +
                                        "⬩ To **Pay** for someone \uD83D\uDCB8 **|** To **Donation** for server \uD83D\uDCB0"
                                ).EmojisHolder());

                                EmbedBuilder requestSuccessful = new EmbedBuilder();
                                requestSuccessful.setDescription(new MessageUtils(
                                        "> **All Ruko Gains \uD83D\uDCB0 :** " + ruko1.getGains() + "\n" +
                                                "> **Ruko :ruko: : **" + ruko1.getRukoUser() + "`+" + requestAccepted.get(event.getUser()) + "`" + "\n \n" +
                                                "⬩ You got Ruko's from " + event.getUser().getAsMention()
                                ).EmojisHolder());

                                ruko.saveFile();
                                ruko1.saveFile();

                                embed.clearFields();
                                embed.addBlankField(false);
                                embed.addField(" ", "✅ payment has been successful", false);

                                Objects.requireNonNull(event.getGuild().getMemberById(acceptRequest.get(event.getUser()).getId())).getUser().openPrivateChannel().flatMap(msg -> msg.sendMessage(requestSuccessful.build())).queue();

                                action.get(event.getUser()).editMessage(embed.build()).queue();
                                action.get(event.getUser()).clearReactions().queue();

                                acceptRequest.remove(event.getUser());
                                acceptRequest.remove(event.getUser());
                                action.remove(event.getUser());
                            } else {
                                event.getReaction().removeReaction(event.getUser()).queue();
                                return;
                            }
                        }
                        break;
                    case "❌":

                        embed.clearFields();
                        action.get(event.getUser()).clearReactions().queue();
                        action.get(event.getUser()).editMessage(embed.build()).queue();

                        acceptRequest.remove(event.getUser());
                        requestAccepted.remove(event.getUser());
                        checkAction.remove(event.getUser());
                        action.remove(event.getUser());

                        event.getReaction().removeReaction(event.getUser()).queue();
                        break;
                    default:
                        event.getReaction().removeReaction(event.getUser()).queue();
                        return;
                }

                if (action.get(event.getUser()) != null || embed.isEmpty()) {
                    action.get(event.getUser()).editMessage(embed.build()).queue();
                }
            }
        }

        if (shopCommand.shopCheck != null) {
            if (shopCommand.shopCheck.containsKey(event.getUser())) {

                coinsManager ruko = new coinsManager(event.getUser());
                EmbedBuilder embed = new EmbedBuilder();

                embed.setColor(Color.ORANGE);
                embed.setTitle("Ruko Market \uD83D\uDED2");

                Message message = shopCommand.shopCheck.get(event.getUser());
                StringBuilder disc = embed.getDescriptionBuilder();

                disc.append(new MessageUtils("**Boosters :booster: :**\n> You can buy booster buy clicking to Reactions\n \n").EmojisHolder());
                Booster.loadBoosters();
                Booster.getBooster().forEach((k, v) -> {
                    Booster booster = new Booster(v);

                    disc.append(new MessageUtils(booster.getEmoji() + " | `" + booster.getName() + "` x" + booster.getCombo() + " ➜ :ruko: " + v.getCost() + "\n").EmojisHolder());
                    message.addReaction(booster.getEmoji()).queue();
                });
                embed.setFooter("⬩ Your Ruko's : " + ruko.getRukoUser());
                message.editMessage(embed.build()).queue();

                switch (event.getReactionEmote().getName()) {
                    case "\uD83D\uDFE7":
                        embed.clearFields();
                        if (ruko.getProperties().getProperty("Booster").contains(Integer.toString(BoosterManagement.BOOSTER_TREBLE.getId()))) {
                            embed.addField(" ", "`❌ Your payment has been refusal ( You already have highest booster )`", false);
                        } else if (ruko.getProperties().getProperty("Booster").contains(Integer.toString(BoosterManagement.BOOSTER_QUADRUPLE.getId()))) {
                            embed.addField(" ", "`❌ Your payment has been refusal ( You already have highest booster )`", false);
                        } else if (ruko.getProperties().getProperty("Booster").contains(Integer.toString(BoosterManagement.BOOSTER_DOUBLE.getId()))) {
                            embed.addField(" ", "`❌ Your payment has been successful ( You already have this booster )`", false);
                        } else {

                            int count = BoosterManagement.BOOSTER_DOUBLE.getCost();

                            if (ruko.getRukoUser() < count) {
                                embed.addField(" ", "`❌ Your payment has been successful ( You don't have enough ruko )`", false);
                            } else {
                                ruko.addProperty("Booster", Integer.toString(BoosterManagement.BOOSTER_DOUBLE.getId()));
                                ruko.removeRukoUser(count);

                                embed.addField(" ", new MessageUtils("`✅ Your payment has been successful ( Booster x2 )` -" + count + " :ruko:").EmojisHolder(), false);
                            }
                        }
                        shopCommand.shopCheck.get(event.getUser()).editMessage(embed.build()).queue();
                        event.getReaction().removeReaction(event.getUser()).queue();
                        break;
                    case "\uD83D\uDFEA":
                        embed.clearFields();
                        if (ruko.getProperties().getProperty("Booster").contains(Integer.toString(BoosterManagement.BOOSTER_DOUBLE.getId()))) {

                            int count = (BoosterManagement.BOOSTER_TREBLE.getCost() - BoosterManagement.BOOSTER_DOUBLE.getCost());

                            if (ruko.getRukoUser() < count) {
                                embed.addField(" ", "`❌ Your payment has been successful ( You don't have enough ruko )`", false);
                            } else {
                                ruko.addProperty("Booster", Integer.toString(BoosterManagement.BOOSTER_TREBLE.getId()));
                                embed.addField(" ", new MessageUtils("`✅ Your payment has been successful ( Booster x3 )` -" + count + " :ruko:").EmojisHolder(), false);
                                ruko.removeRukoUser(count);
                            }
                        } else if (ruko.getProperties().getProperty("Booster").contains(Integer.toString(BoosterManagement.BOOSTER_QUADRUPLE.getId()))) {
                            embed.addField(" ", "`❌ Your payment has been refusal ( You already have highest booster )`", false);
                        } else if (ruko.getProperties().getProperty("Booster").contains(Integer.toString(BoosterManagement.BOOSTER_TREBLE.getId()))) {
                            embed.addField(" ", "`❌ Your payment has been successful ( You already have this booster )`", false);
                        } else {
                            int count = (BoosterManagement.BOOSTER_TREBLE.getCost());

                            if (ruko.getRukoUser() < count) {
                                embed.addField(" ", "`❌ Your payment has been successful ( You don't have enough ruko )`", false);
                            } else {
                                ruko.addProperty("Booster", Integer.toString(BoosterManagement.BOOSTER_TREBLE.getId()));
                                embed.addField(" ", new MessageUtils("`✅ Your payment has been successful ( Booster x3 )` -" + count + " :ruko:").EmojisHolder(), false);
                                ruko.removeRukoUser(count);
                            }
                        }

                        shopCommand.shopCheck.get(event.getUser()).editMessage(embed.build()).queue();
                        event.getReaction().removeReaction(event.getUser()).queue();
                        break;
                    case "\uD83D\uDFE5":
                        embed.clearFields();
                        if (ruko.getProperties().getProperty("Booster").contains(Integer.toString(BoosterManagement.BOOSTER_DOUBLE.getId()))) {
                            int count = (BoosterManagement.BOOSTER_QUADRUPLE.getCost() - BoosterManagement.BOOSTER_DOUBLE.getCost());

                            if (ruko.getRukoUser() < count) {
                                embed.addField(" ", "`❌ Your payment has been successful ( You don't have enough ruko )`", false);
                            } else {
                                ruko.addProperty("Booster", Integer.toString(BoosterManagement.BOOSTER_QUADRUPLE.getId()));
                                embed.addField(" ", new MessageUtils("`✅ Your payment has been successful ( Booster x4 )` -" + count + " :ruko:").EmojisHolder(), false);
                                ruko.removeRukoUser(count);
                            }
                        } else if (ruko.getProperties().getProperty("Booster").contains(Integer.toString(BoosterManagement.BOOSTER_TREBLE.getId()))) {
                            int count = (BoosterManagement.BOOSTER_QUADRUPLE.getCost() - BoosterManagement.BOOSTER_TREBLE.getCost());

                            if (ruko.getRukoUser() < count) {
                                embed.addField(" ", "`❌ Your payment has been successful ( You don't have enough ruko )`", false);
                            } else {
                                ruko.addProperty("Booster", Integer.toString(BoosterManagement.BOOSTER_QUADRUPLE.getId()));
                                embed.addField(" ", new MessageUtils("`✅ Your payment has been successful ( Booster x4 )` -" + count + " :ruko:").EmojisHolder(), false);
                                ruko.removeRukoUser(count);
                            }
                        } else if (ruko.getProperties().getProperty("Booster").contains(Integer.toString(BoosterManagement.BOOSTER_QUADRUPLE.getId()))) {
                            embed.addField(" ", "`❌ Your payment has been successful ( You already have this booster )`", false);
                        } else {
                            int count = (BoosterManagement.BOOSTER_QUADRUPLE.getCost());

                            if (ruko.getRukoUser() < count) {
                                embed.addField(" ", "`❌ Your payment has been successful ( You don't have enough ruko )`", false);
                            } else {
                                ruko.addProperty("Booster", Integer.toString(BoosterManagement.BOOSTER_QUADRUPLE.getId()));
                                embed.addField(" ", new MessageUtils("`✅ Your payment has been successful ( Booster x4 )` -" + count + " :ruko:").EmojisHolder(), false);
                                ruko.removeRukoUser(count);
                            }
                        }

                        shopCommand.shopCheck.get(event.getUser()).editMessage(embed.build()).queue();
                        event.getReaction().removeReaction(event.getUser()).queue();
                        break;
                    case "❌":
                        shopCommand.shopCheck.get(event.getUser()).clearReactions().queue();
                        shopCommand.shopCheck.remove(event.getUser());
                        event.getReaction().removeReaction(event.getUser()).queue();
                        break;
                    default:
                        event.getReaction().removeReaction(event.getUser()).queue();
                        break;
                }
            }
        }
        if (rpcGame.game.containsKey(event.getUser())) {
            checkIdgame = rpcGame.game.get(event.getUser()).getId();
        }
        if (skywarsCommand.fixBugMessage.containsKey(event.getUser())) {
            checkIdhypixel = skywarsCommand.fixBugMessage.get(event.getUser()).getId();
        }

        if (event.getMessageId().contains(checkIdgame)) {
            if (rpcGame.game.containsKey(event.getUser())) {

                String[] rpc = {
                        "✊", // 2 rock
                        "\uD83D\uDD90", // 1 paper
                        "✌" // 3 scissor
                };

                Random random = new Random();
                int nextGame = random.nextInt(3);

                switch (nextGame) {
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }

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
                GUI.append("^+++^+++^ | Best Wins : %best-winstreak%\n");
                GUI.append("^^^^^^^^^\n");

                int points = rpcGame.points.get(event.getUser().getId());

                switch (event.getReactionEmote().getName()) {
                    case "\uD83D\uDD90":
                        // Message editing
                        rpcGame.PLAYER_SELECTED = "\uD83D\uDD90";

                        // Check winner
                        if (rpcGame.PLAYER_SELECTED == rpc[nextGame]) {
                            string.append(GUI.toString().replace("^", "\uD83D\uDFE5").replace("+", "⬛").replace("P1", rpcGame.PLAYER_SELECTED).replace("P2", rpc[nextGame]).replace("%score%", Integer.toString(rpcGame.points.get(event.getUser().getId()))).replace("%winstreak%", rpcGame.p.getProperty("wins")).replace("%best-winstreak%", rpcGame.p.getProperty("bestwinstreak")));
                            string.append("\n\uD83C\uDD93 Draw \uD83C\uDD93");
                            rpcGame.draw.entrySet().stream().filter(f -> f.getKey().contains(event.getUser().getId())).forEach((t) -> {
                                t.setValue((rpcGame.draw.get(event.getUser().getId())) + 1);
                            });
                            rpcGame.p.setProperty("draw", Integer.toString(rpcGame.draw.get(event.getUser().getId())));
                            rpcGame.save(event.getUser());
                        } else if ((rpcGame.PLAYER_SELECTED != rpc[nextGame]) && rpc[nextGame] == rpc[0]) {

                            rpcGame.points.entrySet().stream().filter(f -> f.getKey().contains(event.getUser().getId())).forEach((t) -> {
                                t.setValue(points + 1);
                            });

                            string.append(GUI.toString().replace("^", "\uD83D\uDFE5").replace("+", "⬛").replace("P1", rpcGame.PLAYER_SELECTED).replace("P2", rpc[nextGame]).replace("%score%", Integer.toString(rpcGame.points.get(event.getUser().getId()))).replace("%winstreak%", rpcGame.p.getProperty("wins")).replace("%best-winstreak%", rpcGame.p.getProperty("bestwinstreak")));
                            string.append("\n✅ You won ✅ + 1");
                        } else {
                            if (rpcGame.points.get(event.getUser().getId()) > 0) {
                                rpcGame.p.setProperty("wins", Integer.toString(rpcGame.points.get(event.getUser().getId())));

                                if (Integer.parseInt(rpcGame.p.getProperty("wins")) > Integer.parseInt(rpcGame.p.getProperty("bestwinstreak"))) {
                                    rpcGame.p.setProperty("bestwinstreak", Integer.toString(rpcGame.points.get(event.getUser().getId())));
                                    embedBuilder.addField("", "\n \uD83E\uDD73 Congratulation, you hit new score `" + (Integer.parseInt(rpcGame.p.getProperty("wins")) - Integer.parseInt(rpcGame.p.getProperty("bestwinstreak"))) + " > " + rpcGame.p.getProperty("bestwinstreak") + "`", true);
                                }
                            }

                            try {
                                rpcGame.p.save(new FileOutputStream("System/Game/rpcGame/" + event.getUser().getId() + ".properties"), null);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            rpcGame.points.entrySet().stream().filter(f -> f.getKey().contains(event.getUser().getId())).forEach((t) -> {
                                t.setValue(points - 1);
                            });
                            rpcGame.losetimes.put(event.getUser().getId(), (rpcGame.losetimes.get(event.getUser().getId()) + 1));
                            rpcGame.p.setProperty("losetimes", String.valueOf(rpcGame.losetimes.get(event.getUser().getId())));

                            string.append(GUI.toString().replace("^", "\uD83D\uDFE5").replace("+", "⬛").replace("P1", rpcGame.PLAYER_SELECTED).replace("P2", rpc[nextGame]).replace("%score%", Integer.toString(rpcGame.points.get(event.getUser().getId()))).replace("%winstreak%", rpcGame.p.getProperty("wins")).replace("%best-winstreak%", rpcGame.p.getProperty("bestwinstreak")));
                            string.append("\n☠ You Lost ☠ - 1");

                            if (rpcGame.points.get(event.getUser().getId()) > 0) {

                                rpcGame.points.entrySet().stream().filter(f -> f.getKey().contains(event.getUser().getId())).forEach((t) -> {
                                    t.setValue(0);
                                });

                                rpcGame.points.remove(event.getUser().getId());
                            }
                        }

                        // Message editing
                        rpcGame.game.get(event.getUser()).editMessage(embedBuilder.build()).queue();

                        // Actions
                        event.getReaction().removeReaction(event.getUser()).queue();
                        rpcGame.game.get(event.getUser()).clearReactions().queue();
                        rpcGame.game.remove(event.getUser());
                        break;
                    case "✊":
                        // Message editing
                        rpcGame.PLAYER_SELECTED = "✊";

                        // Check winner
                        if (rpcGame.PLAYER_SELECTED == rpc[nextGame]) {
                            string.append(GUI.toString().replace("^", "\uD83D\uDFE5").replace("+", "⬛").replace("P1", rpcGame.PLAYER_SELECTED).replace("P2", rpc[nextGame]).replace("%score%", Integer.toString(rpcGame.points.get(event.getUser().getId()))).replace("%winstreak%", rpcGame.p.getProperty("wins")).replace("%best-winstreak%", rpcGame.p.getProperty("bestwinstreak")));
                            string.append("\n\uD83C\uDD93 Draw \uD83C\uDD93");
                            rpcGame.draw.entrySet().stream().filter(f -> f.getKey().contains(event.getUser().getId())).forEach((t) -> {
                                t.setValue((rpcGame.draw.get(event.getUser().getId())) + 1);
                            });
                            rpcGame.p.setProperty("draw", Integer.toString(rpcGame.draw.get(event.getUser().getId())));
                            rpcGame.save(event.getUser());
                        } else if ((rpcGame.PLAYER_SELECTED != rpc[nextGame]) && rpc[nextGame] == rpc[2]) {

                            rpcGame.points.entrySet().stream().filter(f -> f.getKey().contains(event.getUser().getId())).forEach((t) -> {
                                t.setValue(points + 1);
                            });

                            string.append(GUI.toString().replace("^", "\uD83D\uDFE5").replace("+", "⬛").replace("P1", rpcGame.PLAYER_SELECTED).replace("P2", rpc[nextGame]).replace("%score%", Integer.toString(rpcGame.points.get(event.getUser().getId()))).replace("%winstreak%", rpcGame.p.getProperty("wins")).replace("%best-winstreak%", rpcGame.p.getProperty("bestwinstreak")));
                            string.append("\n✅ You won ✅ + 1");
                        } else {
                            if (rpcGame.points.get(event.getUser().getId()) > 0) {
                                rpcGame.p.setProperty("wins", Integer.toString(rpcGame.points.get(event.getUser().getId())));

                                if (Integer.parseInt(rpcGame.p.getProperty("wins")) > Integer.parseInt(rpcGame.p.getProperty("bestwinstreak"))) {
                                    rpcGame.p.setProperty("bestwinstreak", Integer.toString(rpcGame.points.get(event.getUser().getId())));
                                    embedBuilder.addField("", "\n \uD83E\uDD73 Congratulation, you hit new score `" + rpcGame.p.getProperty("wins") + " > " + rpcGame.p.getProperty("bestwinstreak") + "`", true);
                                }
                            }

                            try {
                                rpcGame.p.save(new FileOutputStream("System/Game/rpcGame/" + event.getUser().getId() + ".properties"), null);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            rpcGame.points.entrySet().stream().filter(f -> f.getKey().contains(event.getUser().getId())).forEach((t) -> {
                                t.setValue(points - 1);
                            });
                            rpcGame.losetimes.put(event.getUser().getId(), (rpcGame.losetimes.get(event.getUser().getId()) + 1));
                            rpcGame.p.setProperty("losetimes", String.valueOf(rpcGame.losetimes.get(event.getUser().getId())));

                            string.append(GUI.toString().replace("^", "\uD83D\uDFE5").replace("+", "⬛").replace("P1", rpcGame.PLAYER_SELECTED).replace("P2", rpc[nextGame]).replace("%score%", Integer.toString(rpcGame.points.get(event.getUser().getId()))).replace("%winstreak%", rpcGame.p.getProperty("wins")).replace("%best-winstreak%", rpcGame.p.getProperty("bestwinstreak")));
                            string.append("\n☠ You Lost ☠ - 1");

                            if (rpcGame.points.get(event.getUser().getId()) > 0) {

                                rpcGame.points.entrySet().stream().filter(f -> f.getKey().contains(event.getUser().getId())).forEach((t) -> {
                                    t.setValue(0);
                                });

                                rpcGame.points.remove(event.getUser().getId());
                            }
                        }

                        rpcGame.game.get(event.getUser()).editMessage(embedBuilder.build()).queue();

                        // Actions
                        event.getReaction().removeReaction(event.getUser()).queue();
                        rpcGame.game.get(event.getUser()).clearReactions().queue();
                        rpcGame.game.remove(event.getUser());
                        break;
                    case "✌":
                        // Message editing
                        rpcGame.PLAYER_SELECTED = "✌";

                        // Check winner
                        if (rpcGame.PLAYER_SELECTED == rpc[nextGame]) {
                            string.append(GUI.toString().replace("^", "\uD83D\uDFE5").replace("+", "⬛").replace("P1", rpcGame.PLAYER_SELECTED).replace("P2", rpc[nextGame]).replace("%score%", Integer.toString(rpcGame.points.get(event.getUser().getId()))).replace("%winstreak%", rpcGame.p.getProperty("wins")).replace("%best-winstreak%", rpcGame.p.getProperty("bestwinstreak")));
                            string.append("\n\uD83C\uDD93 Draw \uD83C\uDD93");
                            rpcGame.draw.entrySet().stream().filter(f -> f.getKey().contains(event.getUser().getId())).forEach((t) -> {
                                t.setValue((rpcGame.draw.get(event.getUser().getId())) + 1);
                            });
                            rpcGame.p.setProperty("draw", Integer.toString(rpcGame.draw.get(event.getUser().getId())));
                            rpcGame.save(event.getUser());
                        } else if ((rpcGame.PLAYER_SELECTED != rpc[nextGame]) && rpc[nextGame] == rpc[1]) {

                            rpcGame.points.entrySet().stream().filter(f -> f.getKey().contains(event.getUser().getId())).forEach((t) -> {
                                t.setValue(points + 1);
                            });

                            string.append(GUI.toString().replace("^", "\uD83D\uDFE5").replace("+", "⬛").replace("P1", rpcGame.PLAYER_SELECTED).replace("P2", rpc[nextGame]).replace("%score%", Integer.toString(rpcGame.points.get(event.getUser().getId()))).replace("%winstreak%", rpcGame.p.getProperty("wins")).replace("%best-winstreak%", rpcGame.p.getProperty("bestwinstreak")));
                            string.append("\n✅ You won ✅ + 1");
                        } else {
                            if (rpcGame.points.get(event.getUser().getId()) > 0) {
                                rpcGame.p.setProperty("wins", Integer.toString(rpcGame.points.get(event.getUser().getId())));

                                if (Integer.parseInt(rpcGame.p.getProperty("wins")) > Integer.parseInt(rpcGame.p.getProperty("bestwinstreak"))) {
                                    rpcGame.p.setProperty("bestwinstreak", Integer.toString(rpcGame.points.get(event.getUser().getId())));
                                    embedBuilder.addField("", "\n \uD83E\uDD73 Congratulation, you hit new score `" + rpcGame.p.getProperty("wins") + " > " + rpcGame.p.getProperty("bestwinstreak") + "`", true);
                                }
                            }

                            try {
                                rpcGame.p.save(new FileOutputStream("System/Game/rpcGame/" + event.getUser().getId() + ".properties"), null);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            rpcGame.points.entrySet().stream().filter(f -> f.getKey().contains(event.getUser().getId())).forEach((t) -> {
                                t.setValue(points - 1);
                            });

                            rpcGame.losetimes.put(event.getUser().getId(), (rpcGame.losetimes.get(event.getUser().getId()) + 1));
                            rpcGame.p.setProperty("losetimes", String.valueOf(rpcGame.losetimes.get(event.getUser().getId())));

                            string.append(GUI.toString().replace("^", "\uD83D\uDFE5").replace("+", "⬛").replace("P1", rpcGame.PLAYER_SELECTED).replace("P2", rpc[nextGame]).replace("%score%", Integer.toString(rpcGame.points.get(event.getUser().getId()))).replace("%winstreak%", rpcGame.p.getProperty("wins")).replace("%best-winstreak%", rpcGame.p.getProperty("bestwinstreak")));
                            string.append("\n☠ You Lost ☠ - 1");

                            if (rpcGame.points.get(event.getUser().getId()) > 0) {

                                rpcGame.points.entrySet().stream().filter(f -> f.getKey().contains(event.getUser().getId())).forEach((t) -> {
                                    t.setValue(0);
                                });

                                rpcGame.points.remove(event.getUser().getId());
                            }
                        }

                        rpcGame.game.get(event.getUser()).editMessage(embedBuilder.build()).queue();

                        // Actions
                        event.getReaction().removeReaction(event.getUser()).queue();
                        rpcGame.game.get(event.getUser()).clearReactions().queue();
                        rpcGame.game.remove(event.getUser());
                        break;
                    default:
                        event.getReaction().removeReaction(event.getUser()).queue();
                        break;
                }
                profile.setGamesCount(profile.getGamesCount() + 1);
            } else {
                event.getReaction().removeReaction(event.getUser()).queue();
            }
        } else if (event.getMessageId().contains(checkIdhypixel)) {
            if (skywarsCommand.fixBugMessage.containsKey(event.getUser())) {
                if (skywarsCommand.getMessageSendFromHere.containsKey(event.getUser())) {
                    switch (event.getReactionEmote().getName()) {
                        case "⏪":

                            if (selectGUI == 0) {
                                selectGUI = 3;
                            } else if (selectGUI == 1) {
                                selectGUI = 4;
                            } else {
                                selectGUI -= 2;
                            }

                            EmbedBuilder embedLoading_1 = new EmbedBuilder()
                                    .setColor(Color.ORANGE)
                                    .setDescription(new MessageUtils("Checking :loading:").EmojisHolder());
                            skywarsCommand.getMessageSendFromHere.get(event.getUser()).editMessage(embedLoading_1.build()).queue();

                            event.getReaction().removeReaction(event.getUser()).queue();

                            skywarsCommand.getMessageSendFromHere.get(event.getUser()).editMessage(send(event.getUser()).build()).queue();

                            break;
                        case "◀️":

                            if (selectGUI == 0) {
                                selectGUI = 4;
                            } else {
                                selectGUI -= 1;
                            }

                            EmbedBuilder embedLoading = new EmbedBuilder()
                                    .setColor(Color.ORANGE)
                                    .setDescription(new MessageUtils("Checking :loading:").EmojisHolder());
                            skywarsCommand.getMessageSendFromHere.get(event.getUser()).editMessage(embedLoading.build()).queue();

                            event.getReaction().removeReaction(event.getUser()).queue();

                            skywarsCommand.getMessageSendFromHere.get(event.getUser()).editMessage(send(event.getUser()).build()).queue();

                            break;
                        case "\uD83D\uDDD1":
                            skywarsCommand.checkWhoSendRequest.remove(skywarsCommand.getMessageSendFromHere.get(event.getUser()));
                            skywarsCommand.getMessageSendFromHere.get(event.getUser()).delete().queue();
                            skywarsCommand.getMessageSendFromHere.remove(event.getUser());
                            break;
                        case "▶️":

                            if (selectGUI == 4) {
                                selectGUI = 0;
                            } else {
                                selectGUI++;
                            }

                            EmbedBuilder embedLoading_2 = new EmbedBuilder()
                                    .setColor(Color.ORANGE)
                                    .setDescription(new MessageUtils("Checking :loading:").EmojisHolder());
                            skywarsCommand.getMessageSendFromHere.get(event.getUser()).editMessage(embedLoading_2.build()).queue();

                            event.getReaction().removeReaction(event.getUser()).queue();

                            skywarsCommand.getMessageSendFromHere.get(event.getUser()).editMessage(send(event.getUser()).build()).queue();

                            break;
                        case "⏩":

                            if (selectGUI == 4) {
                                selectGUI = 1;
                            } else if (selectGUI == 3) {
                                selectGUI = 0;
                            } else {
                                selectGUI += 2;
                            }

                            EmbedBuilder embedLoading_3 = new EmbedBuilder()
                                    .setColor(Color.ORANGE)
                                    .setDescription(new MessageUtils("Checking :loading:").EmojisHolder());
                            skywarsCommand.getMessageSendFromHere.get(event.getUser()).editMessage(embedLoading_3.build()).queue();

                            event.getReaction().removeReaction(event.getUser()).queue();

                            skywarsCommand.getMessageSendFromHere.get(event.getUser()).editMessage(send(event.getUser()).build()).queue();

                            break;
                        case "\uD83D\uDD3D":
                            String APIKEY = Config.get("HYPIXEL-API-KEY");
                            HypixelAPI hypixel = new HypixelAPI(APIKEY);
                            String name = skywarsCommand.checkWhoSendRequest.get(skywarsCommand.getMessageSendFromHere.get(event.getUser()));

                            event.getReaction().removeReaction(event.getUser()).queue();

                            try {
                                event.getChannel().sendMessage("https://plancke.io/hypixel/player/stats/" + hypixel.getPlayer(name).getDisplayname() + "#SkyWars").queue();
                            } catch (APIException | IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            event.getReaction().removeReaction(event.getUser()).queue();
                            break;
                    }
                } else {
                    event.getReaction().removeReaction(event.getUser()).queue();
                }
            }
        } else return;
    }

    private int selectGUI = 0;
    private final String[] GUIS = {
            "Main Skywars stats", // 0
            "Ranked Skywars stats", // 1
            "Solo Skywars stats", // 2
            "Teams Skywars stats", // 3
            "Mega Skywars stats" // 4
    };

    private final EmbedBuilder send(User user) {
        EmbedBuilder embed = new EmbedBuilder();

        /* Hypixels */
        String APIKEY = Config.get("HYPIXEL-API-KEY");
        HypixelAPI hypixel = new HypixelAPI(APIKEY);
        ru.mdashlw.hypixel.HypixelAPI hypixelV2 = new ru.mdashlw.hypixel.HypixelAPI(APIKEY);
        /* Options */
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        DecimalFormat decimalFormatWithComma = new DecimalFormat("#,###.00");

        SimpleDateFormat formatDate = new SimpleDateFormat("HH'hrs' mm'mins' ss'sec'");

        String name = skywarsCommand.checkWhoSendRequest.get(skywarsCommand.getMessageSendFromHere.get(user));

        if (selectGUI == 0) {
            embed.setColor(Color.cyan);
            try {
                embed.setAuthor("「 Skywars 」\uD83E\uDCA1  " + hypixel.getPlayer(name).getDisplayname(), "https://plancke.io/hypixel/player/stats/" + hypixel.getPlayer(name).getDisplayname(), "https://visage.surgeplay.com/face/" + hypixel.getPlayer(name).getUuid());
                embed.addField("**All mode statistics**", "**▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃** \n" + " ", false);
                embed.addField("**__Skywars Stats__**",
                        "⬩ **Level:** " + decimalFormatWithComma.format(Skywars_ExpToLevel(hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getExperience())) + " \n" +
                                "⬩ **Total Exp:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getExperience() + " \n" +
                                " \n" +
                                "⬩ **Kills:** " + hypixel.getPlayer(name).getStats().getSkyWars().getKills() + " \n" +
                                "⬩ **Deaths:** " + hypixel.getPlayer(name).getStats().getSkyWars().getDeaths() + " \n" +
                                "⬩ **Winds:** " + hypixel.getPlayer(name).getStats().getSkyWars().getWins() + " \n" +
                                "⬩ **Losses:** " + hypixel.getPlayer(name).getStats().getSkyWars().getLosses() + " \n" +
                                " \n" +
                                "⬩ **Last Mode: **" + hypixel.getPlayer(name).getStats().getSkyWars().getLastMode()
                        , true);
                embed.addField("**__InGame Stats__**",
                        "⬩ **Blocks Placed:** " + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getBlocksPlaced()) + " \n" +
                                "⬩ **Blocks Broken:** " + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getBlocksBroken()) + " \n" +
                                " \n" +
                                "⬩ **Longest Bow Shot:** " + hypixel.getPlayer(name).getStats().getSkyWars().getLongestBowShot() + " \n" +
                                "⬩ **Arrows Shots:** " + hypixel.getPlayer(name).getStats().getSkyWars().getArrowsShot() + " \n" +
                                "⬩ **Arrows Hits:** " + hypixel.getPlayer(name).getStats().getSkyWars().getArrowsHit() + " \n" +
                                "⬩ **Egg thrown:** " + hypixel.getPlayer(name).getStats().getSkyWars().getEggThrown() + " \n" +
                                "⬩ **Chests Open:** " + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getChestsOpened()) + " \n" +
                                "⬩ **Assists:** " + hypixel.getPlayer(name).getStats().getSkyWars().getAssists()
                        , true);
                embed.addField("▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃", " \n **Coins:** " + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getCoins()) + " **| Souls:** " + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getSouls()) + "** | Token: **" + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getCosmeticTokens()), false);
                embed.setFooter("Usage: r!hypixel " + name);
            } catch (InterruptedException | ExecutionException | IOException | APIException e) {
                e.printStackTrace();
            }
        } else if (selectGUI == 1) {
            embed.setColor(Color.cyan);
            try {
                embed.setAuthor("「 Skywars 」\uD83E\uDCA1  " + hypixel.getPlayer(name).getDisplayname(), "https://plancke.io/hypixel/player/stats/" + hypixel.getPlayer(name).getDisplayname(), "https://visage.surgeplay.com/face/" + hypixel.getPlayer(name).getUuid());
                embed.addField("**Ranked Mode statistics**", "**▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃** \n" + " ", false);
                embed.addField("**__Skywars Stats__**",
                        "⬩ **Level:** " + decimalFormatWithComma.format(Skywars_ExpToLevel(hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getExperience())) + " \n" +
                                "⬩ **Total Exp:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getExperience() + " \n" +
                                " \n" +
                                "⬩ **Kills:** " + hypixel.getPlayer(name).getStats().getSkyWars().getKillsRanked() + " \n" +
                                "⬩ **Deaths:** " + hypixel.getPlayer(name).getStats().getSkyWars().getDeathsRanked() + " \n" +
                                "⬩ **Winds:** " + hypixel.getPlayer(name).getStats().getSkyWars().getWinsRanked() + " \n" +
                                "⬩ **Losses:** " + hypixel.getPlayer(name).getStats().getSkyWars().getLossesRanked() + " \n" +
                                " \n"
                        , true);
                embed.addField("**__InGame Stats__**",
                        "⬩ **Blocks Placed:** " + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getBlocksPlaced()) + " \n" +
                                "⬩ **Blocks Broken:** " + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getBlocksBroken()) + " \n" +
                                " \n" +
                                "⬩ **Longest Bow Shot:** " + hypixel.getPlayer(name).getStats().getSkyWars().getLongestBowShotRanked() + " \n" +
                                "⬩ **Arrows Shots:** " + hypixel.getPlayer(name).getStats().getSkyWars().getArrowsShotRanked() + " \n" +
                                "⬩ **Arrows Hits:** " + hypixel.getPlayer(name).getStats().getSkyWars().getArrowsHitRanked() + " \n" +
                                "⬩ **Chests Open:** " + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getChestsOpenedRanked()) + " \n" +
                                "⬩ **Assists:** " + hypixel.getPlayer(name).getStats().getSkyWars().getAssistsRanked()
                        , true);
                embed.addField("▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃", " \n **Coins:** " + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getCoins()) + " **| Souls:** " + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getSouls()) + "** | Token: **" + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getCosmeticTokens()), false);
                embed.setFooter("Usage: r!hypixel " + name);
            } catch (InterruptedException | ExecutionException | IOException | APIException e) {
                e.printStackTrace();
            }
        } else if (selectGUI == 2) {
            embed.setColor(Color.cyan);
            try {
                embed.setAuthor("「 Skywars 」\uD83E\uDCA1  " + hypixel.getPlayer(name).getDisplayname(), "https://plancke.io/hypixel/player/stats/" + hypixel.getPlayer(name).getDisplayname(), "https://visage.surgeplay.com/face/" + hypixel.getPlayer(name).getUuid());
                embed.addField("**Solo Mode statistics**", "**▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃** \n" + " ", false);
                embed.addField("**__Insane Stats__**",
                        "⬩ **Kit:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getActiveKit(SkyWars.Type.INSANE) + " \n" +
                                "⬩ **Kills:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getKills(SkyWars.Mode.SOLO_INSANE) + " \n" +
                                "⬩ **Deaths:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getDeaths(SkyWars.Mode.SOLO_INSANE) + " \n" +
                                "⬩ **Winds:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getWins(SkyWars.Mode.SOLO_INSANE) + " \n" +
                                "⬩ **Losses:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getLosses(SkyWars.Mode.SOLO_INSANE) + " \n" +
                                " \n"
                        , true);
                embed.addField("**__Normal Stats__**",
                        "⬩ **Kit:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getActiveKit(SkyWars.Type.NORMAL) + " \n" +
                                "⬩ **Kills:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getKills(SkyWars.Mode.SOLO_NORMAL) + " \n" +
                                "⬩ **Deaths:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getDeaths(SkyWars.Mode.SOLO_NORMAL) + " \n" +
                                "⬩ **Winds:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getWins(SkyWars.Mode.SOLO_NORMAL) + " \n" +
                                "⬩ **Losses:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getLosses(SkyWars.Mode.SOLO_NORMAL) + " \n" +
                                " \n"
                        , true);
                embed.addField("▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃", " \n **Coins:** " + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getCoins()) + " **| Souls:** " + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getSouls()) + "** | Token: **" + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getCosmeticTokens()), false);
                embed.setFooter("Usage: r!hypixel " + name);
            } catch (IOException | APIException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        } else if (selectGUI == 3) {
            embed.setColor(Color.cyan);
            try {
                embed.setAuthor("「 Skywars 」\uD83E\uDCA1  " + hypixel.getPlayer(name).getDisplayname(), "https://plancke.io/hypixel/player/stats/" + hypixel.getPlayer(name).getDisplayname(), "https://visage.surgeplay.com/face/" + hypixel.getPlayer(name).getUuid());
                embed.addField("**Teams Mode statistics**", "**▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃** \n" + " ", false);
                embed.addField("**__Insane Stats__**",
                        "⬩ **Kit:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getActiveKit(SkyWars.Type.INSANE) + " \n" +
                                "⬩ **Kills:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getKills(SkyWars.Mode.DOUBLES_INSANE) + " \n" +
                                "⬩ **Deaths:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getDeaths(SkyWars.Mode.DOUBLES_INSANE) + " \n" +
                                "⬩ **Winds:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getWins(SkyWars.Mode.DOUBLES_INSANE) + " \n" +
                                "⬩ **Losses:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getLosses(SkyWars.Mode.DOUBLES_INSANE) + " \n" +
                                " \n"
                        , true);
                embed.addField("**__Normal Stats__**",
                        "⬩ **Kit:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getActiveKit(SkyWars.Type.NORMAL) + " \n" +
                                "⬩ **Kills:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getKills(SkyWars.Mode.DOUBLES_NORMAL) + " \n" +
                                "⬩ **Deaths:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getDeaths(SkyWars.Mode.DOUBLES_NORMAL) + " \n" +
                                "⬩ **Winds:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getWins(SkyWars.Mode.DOUBLES_NORMAL) + " \n" +
                                "⬩ **Losses:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getLosses(SkyWars.Mode.DOUBLES_NORMAL) + " \n" +
                                " \n"
                        , true);
                embed.addField("▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃", " \n **Coins:** " + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getCoins()) + " **| Souls:** " + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getSouls()) + "** | Token: **" + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getCosmeticTokens()), false);
                embed.setFooter("Usage: r!hypixel " + name);
            } catch (IOException | APIException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        } else if (selectGUI == 4) {
            embed.setColor(Color.cyan);
            try {
                embed.setAuthor("「 Skywars 」\uD83E\uDCA1  " + hypixel.getPlayer(name).getDisplayname(), "https://plancke.io/hypixel/player/stats/" + hypixel.getPlayer(name).getDisplayname(), "https://visage.surgeplay.com/face/" + hypixel.getPlayer(name).getUuid());
                embed.addField("**Mega Mode statistics**", "**▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃** \n" + " ", false);
                embed.addField("**__Insane Stats__**",
                        "⬩ **Kit:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getActiveKit(SkyWars.Type.MEGA) + " \n" +
                                "⬩ **Kills:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getKills(SkyWars.Mode.MEGA) + " \n" +
                                "⬩ **Deaths:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getDeaths(SkyWars.Mode.MEGA) + " \n" +
                                "⬩ **Winds:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getWins(SkyWars.Mode.MEGA) + " \n" +
                                "⬩ **Losses:** " + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().getLosses(SkyWars.Mode.MEGA) + " \n" +
                                " \n"
                        , true);
                embed.addField("▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃▃", " \n **Coins:** " + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getCoins()) + " **| Souls:** " + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getSouls()) + "** | Token: **" + decimalFormat.format(hypixel.getPlayer(name).getStats().getSkyWars().getCosmeticTokens()) + "\n" + "**Ranked Division: **" + hypixelV2.retrievePlayerByName(name).get().getStats().getSkyWars().hasRewards(SkyWars.RankedDivision.GOLD), false);
                embed.setFooter("Usage: r!hypixel " + name);
            } catch (IOException | APIException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return embed;
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

    private int generationRandomNumber(int minimum, int maximum) {
        Random random = new Random();
        return random.nextInt(((maximum - minimum) + 1)) + minimum;
    }
}