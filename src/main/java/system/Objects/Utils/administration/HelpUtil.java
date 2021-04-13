package system.Objects.Utils.administration;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import system.Objects.Category;
import system.Objects.Command;

import java.util.*;

public class HelpUtil {

    public HashMap<Category, Collection<Command>> map = new HashMap<>();
    public HashMap<User, Category> now = new HashMap<>();

    private Collection<Command> cmds;
    private final List<Category> categories = new ArrayList<>();

    private final Guild guild;

    public HelpUtil(Guild guild) {
        this.guild = guild;
    }

    public void addCategory(Category category, Collection<Command> commands) {
        if (!getMap().containsKey(category)) {
            getCategories().add(category);
            getMap().put(category, commands);
        }
    }

    public HashMap<Category, Collection<Command>> getMap() {
        return map;
    }

    public HashMap<User, Category> getNow() {
        return now;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public Collection<Command> getCmds() {
        return cmds;
    }

    public Guild getGuild() {
        return guild;
    }
}
