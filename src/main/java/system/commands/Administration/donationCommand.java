package system.commands.Administration;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.objects.Category;
import system.objects.Command;

import java.awt.*;
import java.util.List;

public class donationCommand implements Command {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        EmbedBuilder embed = new EmbedBuilder()
                .setColor(Color.RED.brighter())
                .setTitle("Donation \uD83D\uDCB8")
                .setDescription("**You can help to make bot more better and help me with my life, you can donation in paypal :** https://www.paypal.com/paypalme/safa7gamer");

        event.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getHelp() {
        return "r!donation";
    }

    @Override
    public String getInVoke() {
        return "donation";
    }

    @Override
    public String getDescription() {
        return "Help me to make bot more better";
    }

    @Override
    public Permission getPermission() {
        return Permission.UNKNOWN;
    }

    @Override
    public Category getCategory() {
        return Category.MANAGEMENT;
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
    public Boolean diplayCommand() {
        return true;
    }
}
