package system.commands.Administration;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.CommandManager;
import system.objects.Category;
import system.objects.Command;
import system.objects.Config;
import system.objects.Utils.LanguagesUtils.LanguagesManager;
import system.objects.Utils.LanguagesUtils.MessagesKeys;
import system.objects.Utils.administration.HelpPagesUtil;
import system.objects.Utils.administration.HelpUtil;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class helpCommand implements Command {

    private final CommandManager manager;

    public helpCommand(CommandManager manager) {
        this.manager = manager;
    }

    public static HashMap<User, HelpUtil> helper = new HashMap<>();
    public static HashMap<User, Message> helperMessage = new HashMap<>();
    public static HashMap<User, HelpPagesUtil> helpPage = new HashMap<>();
    public static HashMap<User, Integer> helpCooldown = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        Command command = manager.getCommand(String.join("", args));
        LanguagesManager languagesManager = new LanguagesManager(event.getAuthor());

        if (args.isEmpty()) {
            GenerateAndSendEmbed(event);
            return;
        }

        if (command == null) {
            event.getChannel().sendMessage(languagesManager.getMessage(MessagesKeys.HELP_MESSAGE_ERROR_COMMAND)).queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.orange)
                .setTitle("Help commands?")
                .setDescription("**" + command.getInVoke() +
                        "** Command " + (command.Lockdown() ? "`(Locked \uD83D\uDD12)`" : "") + " \n **Usage:** " + command.getHelp() +
                        " \n**Category:** " + command.getCategory().getName() +
                        " \n**Description:** " + command.getDescription() + ".");
        event.getChannel().sendMessage(embed.build()).queue();
    }

    private void GenerateAndSendEmbed(GuildMessageReceivedEvent event) {

        if (helper.containsKey(event.getAuthor())) {
            event.getMessage().delete().queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(new Color(254, 95, 95))
                .setTitle("Help commands")
                .setFooter("Usage : r!help <command>")
                .setThumbnail("https://i.postimg.cc/G2MN02p0/help.png");
        embed.setImage("https://imgur.com/UUE6taC.png");

        StringBuilder string = embed.getDescriptionBuilder();

        List<Command> cmds = new ArrayList<>();
        HashMap<List<Command>, Command> map = new HashMap<>();

        manager.getCommands().forEach((cmd) -> {
            cmds.add(cmd);
            map.put(cmds, cmd);
        });

        Message msg = event.getChannel().sendMessage(embed.build()).complete();

        msg.addReaction("⬅").queue();

        HelpUtil helpUtil = new HelpUtil(event.getGuild());
        HelpPagesUtil page = new HelpPagesUtil(event.getAuthor(), msg, embed);

        manager.getCategories().forEach((category) -> {
            if (map.values().stream().anyMatch(m -> m.getCategory().equals(category))) {
                if (event.getAuthor().getId().contains("304609934967046144")){
                    helpUtil.addCategory(category, map.values().stream().filter(f -> f.getCategory().equals(category)).collect(Collectors.toList()));
                } else {
                    helpUtil.addCategory(category, map.values().stream().filter(f -> f.getCategory().equals(category) && f.displayCommand()).collect(Collectors.toList()));
                }
            }
        });

        helpUtil.getMap().entrySet().stream().sorted(Comparator.comparingInt(x -> x.getKey().getId())).forEach((key) -> {
            string.append("**").append(key.getKey().getEmoji()).append(" | ⬩ ").append(key.getKey().getName()).append("**").append("\n");
        });

//        helpUtil.getMap().forEach((key, value) -> {
//            string.append("**").append(key.getEmoji()).append(" | ⬩ ").append(key.getName()).append("**").append("\n");
//        });

        helpUtil.getMap().entrySet().stream().sorted(Comparator.comparingInt(x -> x.getKey().getId())).forEach((key) -> {
            msg.addReaction(key.getKey().getEmoji()).queue();
        });

        string.append("\n").append("```yml").append("\n");
        string.append("Tap on the emoji below to display the commands of the category").append("```");

        string.append("\n").append("`").append("Note: The commands which have \uD83D\uDD12 is Locked that mean you can't use it").append("`").append("\n ");

        string.append(" \n> ").append("**__Bot Information__**").append("\n ");
        string.append("**").append("\uD83C\uDF10 | ⬩").append("**").append(" ").append("The bot creator:").append(" ").append(event.getGuild().getMemberById(Config.get("OWNER")).getUser().getName()).append("#").append(event.getGuild().getMemberById(Config.get("OWNER")).getUser().getDiscriminator()).append("\n");
        string.append("**").append("\uD83C\uDF10 | ⬩").append("**").append(" ").append("The bot office:").append(" ").append(event.getJDA().getGuildById(Config.get("OFFICE")).getChannels().stream().findFirst().get().createInvite().setTemporary(true).complete().getUrl()).append("\n");
        string.append("**").append("\uD83C\uDF10 | ⬩").append("**").append(" ").append("First launch:").append(" ").append(event.getJDA().getSelfUser().getTimeCreated().format(DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy")));
        msg.editMessage(embed.build()).queue();

        helpPage.put(event.getAuthor(), page);
        helper.put(event.getAuthor(), helpUtil);
        helperMessage.put(event.getAuthor(), msg);
        helpCooldown.put(event.getAuthor(), 0);

        event.getMessage().delete().queue();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                helpCooldown.entrySet().forEach((k) -> {
                    if (k.getKey().getId().contains(event.getAuthor().getId())) {
                        if (!helper.containsKey(event.getAuthor())) this.cancel();
                        k.setValue(helpCooldown.get(k.getKey()) + 1);
                    }
                });
            }
        }, 0, 1000);
    }

    @Override
    public String getHelp() {
        return "r!help <command>";
    }

    @Override
    public String getInVoke() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "to get help in with any command";
    }

    @Override
    public Permission getPermission() {
        return null;
    }

    @Override
    public Category getCategory() {
        return Category.MANAGEMENT;
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
}
