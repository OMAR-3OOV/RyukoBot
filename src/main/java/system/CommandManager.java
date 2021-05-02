package system;

import com.sun.istack.Nullable;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.commands.Administration.*;
import system.commands.Administration.PrivateChattingBot.SendPrivateMessageCommand;
import system.commands.Administration.Suggest.SuggestCommand;
import system.commands.Administration.moderatorCategory.SuggestModerator;
import system.commands.Administration.moderatorCategory.channelsAddonsCommands.permissionChannelCommand;
import system.commands.Administration.moderatorCategory.informationCommand;
import system.commands.Administration.moderatorCategory.reactionMessageCommand;
import system.commands.Administration.moderatorCategory.rolesCommand;
import system.commands.FunCategory.CountriesCommand;
import system.commands.FunCategory.animeCommand;
import system.commands.Games.eventsGame;
import system.commands.Games.rpcGame;
import system.commands.NsfwCategory.NekoCommand;
import system.commands.NsfwCategory.Rule34Command;
import system.commands.NsfwCategory.SafeBooruCommand;
import system.commands.informationCategory.AchievementsCommand;
import system.commands.informationCategory.ServerProfileIDCanvas;
import system.commands.informationCategory.profileCommand;
import system.commands.minecraftCategory.*;
import system.commands.informationCategory.readCommand;
import system.commands.Administration.bannedCommand;
import system.commands.testcommand;
import system.objects.Category;
import system.objects.Command;
import system.objects.TextUtils.MessageUtils;
import system.objects.Utils.administration.BannedUtils.BannedElapsedTimes;
import system.objects.Utils.administration.BannedUtils.BannedUtils;
import system.objects.Utils.profileconfigutils.ProfileBuilder;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class CommandManager {

    public static HashMap<User, Integer> commandCooldown = new HashMap<>();
    public static HashMap<User, AtomicInteger> cooldownCount = new HashMap<>();
    private AtomicInteger count;

    public static final Map<String, Command> commands = new HashMap<>();
    private final Map<Integer, Category> categories = new HashMap<>();

    public CommandManager() {
        // Add commands
        addCommand(new helpCommand(this));
        addCommand(new HypixelCommand());
        addCommand(new skywarsCommand());
        addCommand(new informationCommand());
        addCommand(new readCommand());
        addCommand(new rolesCommand());
        addCommand(new permissionChannelCommand());
        addCommand(new verifyCommand());
        addCommand(new animeCommand());
        addCommand(new donationCommand());
        addCommand(new rpcGame());
        addCommand(new eventsGame());
        addCommand(new rukoCommand());
        addCommand(new shopCommand());
        addCommand(new OptifineVersionsListCommand());
        addCommand(new MinecraftNameListCommand());
        addCommand(new SuggestCommand());
        addCommand(new SuggestModerator());
        addCommand(new CountriesCommand());
        addCommand(new SendPrivateMessageCommand());
        addCommand(new bannedCommand());
        addCommand(new profileCommand());
        addCommand(new reactionMessageCommand());
        addCommand(new testcommand());
        addCommand(new NekoCommand());
        addCommand(new Rule34Command());
        addCommand(new SafeBooruCommand());
        addCommand(new HypixelGuildCommand());
        addCommand(new ServerProfileIDCanvas());
        addCommand(new deletedMessageCommand());
        addCommand(new HypixelGuildFutureCommands());
        addCommand(new AchievementsCommand());

        // Add category
        addCategory(Category.MANAGEMENT);
        addCategory(Category.MODERATOR);
        addCategory(Category.INFORMATION);
        addCategory(Category.FUN);
        addCategory(Category.MINECRAFT);
        addCategory(Category.NSFW);
    }

    private void addCommand(Command command) {
        if (!commands.containsKey(command.getInVoke())) {
            commands.put(command.getInVoke(), command);
        }
    }

    private void addCategory(Category category) {
        if (!categories.containsKey(category)) {
            categories.put(category.getId(), category);
        }
    }

    public Collection<Command> getCommands() {
        return commands.values();
    }

    public Command getCommand(String command) {
        return commands.get(command);
    }

    public Collection<Category> getCategories() {
        return categories.values();
    }

    /**
     *
     * @param event returned to be GuildMessageReceivedEvent
     * @param prefix prefix command access
     * @throws FileNotFoundException
     */
    public void handleCommand(@Nullable GuildMessageReceivedEvent event, String prefix) throws FileNotFoundException {
        final String[] split = event.getMessage().getContentRaw().replaceFirst("(?i)" + Pattern.quote(prefix), "").split("\\s+");
        final String invoke = split[0].toLowerCase();
        final ProfileBuilder profile = new ProfileBuilder(event.getAuthor(), event.getGuild());

        if (commands.containsKey(invoke)) {
            final List<String> args = Arrays.asList(split).subList(1, split.length);

            if (!commands.get(commands.get(invoke).getInVoke()).Lockdown() || event.getAuthor().getId().equalsIgnoreCase("304609934967046144") | event.getAuthor().getId().equalsIgnoreCase("814242905908576327") || event.getAuthor().getId().equalsIgnoreCase("427259177514303499")) {

                if (profile.getBanned()) {
                    event.getChannel().sendMessage("⛔ | **You're currently blacklist**, `you can apply for unbanned application in our server`").queue();
                    return;
                }

                if (commandCooldown.containsKey(event.getAuthor())) {
                    cooldownCount.get(event.getAuthor()).addAndGet(1);
                    event.getChannel().sendMessage(new MessageUtils(":error: There's " + commandCooldown.get(event.getAuthor()) + " seconds to use commands again, `Note: if you spam more than " + cooldownCount.get(event.getAuthor()).get() + " times you will get banned` ||" + event.getAuthor().getAsMention() + "||").EmojisHolder()).queue();
                }

                if (cooldownCount.containsKey(event.getAuthor()) && cooldownCount.get(event.getAuthor()).get() >= 5) {
                    BannedUtils bannedUtils = new BannedUtils(event.getAuthor(), event.getJDA().getSelfUser());

                    if (profile.getAutoBanned() >= 1) {
                        bannedUtils.setBannedWithTime(bannedUtils.getUser(), 1, BannedElapsedTimes.HOURS, "Spamming : " + bannedUtils.getProperties().getProperty("auto-banned"));
                    } else if (profile.getAutoBanned() >= 3) {
                        bannedUtils.setBannedWithTime(bannedUtils.getUser(), 1, BannedElapsedTimes.WEEKS, "Spamming : " + bannedUtils.getProperties().getProperty("auto-banned"));
                    } else if (profile.getAutoBanned() >= 5) {
                        bannedUtils.setBannedWithTime(bannedUtils.getUser(), 1, BannedElapsedTimes.YEARS, "Spamming : " + bannedUtils.getProperties().getProperty("auto-banned"));
                    } else {
                        bannedUtils.setBannedWithTime(bannedUtils.getUser(), 5, BannedElapsedTimes.MINUTE, "Spamming : " + bannedUtils.getProperties().getProperty("auto-banned"));
                    }

                    bannedUtils.getProfile().addAutoBanned(1);
                    bannedUtils.getProfile().setBanned(true);

                    commandCooldown.remove(event.getAuthor());
                    cooldownCount.remove(event.getAuthor());
                    return;
                }

                if (!commandCooldown.containsKey(event.getAuthor())) {
                    this.count = new AtomicInteger(0);
                    commands.get(invoke).handle(args, event);
                    commandCooldown.put(event.getAuthor(), 5);
                    cooldownCount.put(event.getAuthor(), this.count);
                }

                if (!invoke.contains("profile")) {
                    profile.setLastTimeCommandUse(new Date());
                }
            } else {
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", this command is locked \uD83D\uDD12").queue();
            }
        }
    }
}
