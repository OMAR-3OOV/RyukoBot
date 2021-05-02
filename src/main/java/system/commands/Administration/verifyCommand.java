package system.commands.Administration;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.objects.Category;
import system.objects.Command;
import system.objects.Utils.LanguagesUtils.LanguagesManager;
import system.objects.Utils.LanguagesUtils.MessagesKeys;
import system.objects.Utils.VerifyUtil;
import system.objects.Utils.profileconfigutils.ProfileBuilder;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

public class verifyCommand implements Command {

    public static HashMap<User, VerifyUtil> verify = new HashMap<>();

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
        final ProfileBuilder profile = new ProfileBuilder(event.getAuthor(), event.getGuild());
        final LanguagesManager languagesManager = new LanguagesManager(event.getAuthor());
        final VerifyUtil verifyUtil = new VerifyUtil(event.getAuthor(), event.getChannel(), null);

        if (verify.containsKey(event.getAuthor())) {
            return;
        }

        if (profile.getVerify()) {
            event.getChannel().sendMessage(languagesManager.getMessage(MessagesKeys.VERIFY_VERIFIED, event.getAuthor(), null)).queue();
            return;
        }

        verifyUtil.getTimer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (verifyUtil.getRsu().getMap().containsKey(event.getAuthor())) {
                    verifyUtil.getRsu().getMap().remove(event.getAuthor());

                    event.getAuthor().openPrivateChannel().queue((privateChannel -> {
                        File file = new File("Image/reactions/disappear.gif");

                        EmbedBuilder embed = new EmbedBuilder();

                        embed.setTitle(languagesManager.getMessage(MessagesKeys.VERIFY_DISAPPEAR, event.getAuthor(), null));
                        embed.setColor(Color.RED);
                        embed.setImage("attachment://disappear.gif");

                        privateChannel.sendMessage(embed.build()).addFile(file).queue();
                    }));
                }
            }
        }, (1000 * 60), (1000 * 60));

        event.getAuthor().openPrivateChannel().queue((privateChannel -> {

            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(languagesManager.getMessage(MessagesKeys.VERIFY_PRIVATE_MESSAGE));
            embed.setColor(Color.RED);
            embed.addField(" ", "**Code :** " + verifyUtil.getRsu().getKey(), false);

            privateChannel.sendMessage(embed.build()).queue(msg -> {
                msg.addReaction("✅").queue();
            }, (error) -> {
                event.getChannel().sendMessage(languagesManager.getMessage(MessagesKeys.VERIFY_CANNOT_SENDMESSAGE, event.getAuthor(), null)).complete().addReaction("❌").queue();
            });
        }));

        Message message = event.getChannel().sendMessage(languagesManager.getMessage(MessagesKeys.VERIFY_MESSAGE, event.getAuthor(), null)).complete();
        verifyUtil.setMessage(message);

        verify.put(event.getAuthor(), verifyUtil);
    }
}
