package system.objects;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.FileNotFoundException;
import java.util.List;

public interface Command {
    void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException;
    String getHelp();
    String getInVoke();
    String getDescription();
    Permission getPermission();
    Category getCategory();
    Boolean Lockdown();
    Boolean isNsfw();
    Boolean displayCommand();
}
