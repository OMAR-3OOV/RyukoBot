package system;

import com.sun.istack.Nullable;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.Commands.Administration.*;
import system.Commands.Administration.PrivateChattingBot.SendPrivateMessageCommand;
import system.Commands.Administration.Suggest.SuggestCommand;
import system.Commands.Administration.moderatorCategory.SuggestModerator;
import system.Commands.Administration.moderatorCategory.channelsAddonsCommands.permissionChannelCommand;
import system.Commands.Administration.moderatorCategory.informationCommand;
import system.Commands.Administration.moderatorCategory.reactionMessageCommand;
import system.Commands.Administration.moderatorCategory.rolesCommand;
import system.Commands.FunCategory.CountriesCommand;
import system.Commands.FunCategory.animeCommand;
import system.Commands.Games.eventsGame;
import system.Commands.Games.rpcGame;
import system.Commands.NsfwCategory.NekoCommand;
import system.Commands.NsfwCategory.Rule34Command;
import system.Commands.NsfwCategory.SafeBooruCommand;
import system.Commands.informationCategory.profileCommand;
import system.Commands.minecraftCategory.HypixelCommand;
import system.Commands.minecraftCategory.MinecraftNameListCommand;
import system.Commands.minecraftCategory.OptifineVersionsListCommand;
import system.Commands.minecraftCategory.skywarsCommand;
import system.Commands.informationCategory.readCommand;
import system.Commands.Administration.bannedCommand;
import system.Commands.testcommand;
import system.Objects.Category;
import system.Objects.Command;
import system.Objects.TextUtils.MessageUtils;
import system.Objects.Utils.Administration.BannedUtils.BannedElapsedTimes;
import system.Objects.Utils.Administration.BannedUtils.BannedUtils;
import system.Objects.Utils.ProfileConfigUtils.ProfileBuilder;

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
        final ProfileBuilder profile = new ProfileBuilder(event.getAuthor());

        if (commands.containsKey(invoke)) {
            final List<String> args = Arrays.asList(split).subList(1, split.length);

            if (!commands.get(commands.get(invoke).getInVoke()).Lockdown() || event.getAuthor().getId().equalsIgnoreCase("304609934967046144") | event.getAuthor().getId().equalsIgnoreCase("814242905908576327")) {

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
                        bannedUtils.setBannedWithTime(bannedUtils.getUser(), 1, BannedElapsedTimes.HOURS);
                    } else if (profile.getAutoBanned() >= 3) {
                        bannedUtils.setBannedWithTime(bannedUtils.getUser(), 1, BannedElapsedTimes.WEEKS);
                    } else if (profile.getAutoBanned() >= 5) {
                        bannedUtils.setBannedWithTime(bannedUtils.getUser(), 1, BannedElapsedTimes.YEARS);
                    } else {
                        bannedUtils.setBannedWithTime(bannedUtils.getUser(), 5, BannedElapsedTimes.MINUTE);
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
