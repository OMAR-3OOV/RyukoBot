package system.commands.informationCategory;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.Objects.Category;
import system.Objects.Command;

import java.io.FileNotFoundException;
import java.util.List;

public class LevelCommand implements Command {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {

    }

    @Override
    public String getHelp() {
        return "r!level";
    }

    @Override
    public String getInVoke() {
        return "level";
    }

    @Override
    public String getDescription() {
        return "To know about your profile level and how you're activating";
    }

    @Override
    public Permission getPermission() {
        return null;
    }

    @Override
    public Category getCategory() {
        return Category.INFORMATION;
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
