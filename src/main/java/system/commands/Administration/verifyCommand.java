package system.commands.Administration;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.Objects.Category;
import system.Objects.Command;
import system.Objects.TextUtils.MessageUtils;
import system.Objects.Utils.RandomStringAPI;
import system.Objects.Utils.profileconfigutils.ProfileBuilder;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class verifyCommand implements Command {

    public static HashMap<User, String> verifyCheck = new HashMap<>();
    public static HashMap<User, Message> verifyChecking = new HashMap<>();
    public static HashMap<User, Timer> verifyCompleted = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        setup(event);
    }

    @Override
    public String getHelp() {
        return "r!verify";
    }

    @Override
    public String getInVoke() {
        return "verify";
    }

    @Override
    public String getDescription() {
        return "verify your account discord in bot";
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

    public static void setup(GuildMessageReceivedEvent event) {
        final ProfileBuilder profile = new ProfileBuilder(event.getAuthor());

        if (profile.getVerify()) {
            event.getChannel().sendMessage(new MessageUtils(":error: | **Your account is already verified**").EmojisHolder()).queue();
            return;
        }

        RandomStringAPI rna = new RandomStringAPI(event.getAuthor(), 20);

        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (rna.getMap().containsKey(event.getAuthor())) {
                    rna.getMap().remove(event.getAuthor());

                    event.getAuthor().openPrivateChannel().queue((privateChannel -> {
                        privateChannel.sendMessage("**Verify Code has been disappeared \uD83D\uDE3F**").queue();
                    }));
                }
            }
        }, (1000 * 60), (1000 * 60));

        verifyCompleted.put(event.getAuthor(), timer);

        event.getAuthor().openPrivateChannel().queue((privateChannel -> {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Verify your discord account in bot data");
            embed.setColor(Color.RED);
            embed.addField(" ", "**Code :** " + rna.getKey(), false);

            privateChannel.sendMessage(embed.build()).queue();
            verifyCheck.put(event.getAuthor(), rna.getKey());
        }));

        event.getChannel().sendMessage("Check your private chat, you have 60 seconds only to enter the code, if you didn't enter it the code will disappear").delay(60, TimeUnit.SECONDS).queue();
    }
}
