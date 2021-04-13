package system.commands.Administration;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.Objects.Category;
import system.Objects.Command;

import java.io.FileNotFoundException;
import java.util.List;

public class PollCommand implements Command {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {

    }

    @Override
    public String getHelp() {
        return "r!poll <topic> | <vote-1> | <vote-2> | ...";
    }

    @Override
    public String getInVoke() {
        return "poll";
    }

    @Override
    public String getDescription() {
        return "to make your own poll";
    }

    @Override
    public Permission getPermission() {
        return null;
    }

    @Override
    public Category getCategory() {
        return Category.MODERATOR;
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
}
