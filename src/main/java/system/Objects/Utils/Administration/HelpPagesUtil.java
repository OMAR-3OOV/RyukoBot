package system.Objects.Utils.Administration;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;

public class HelpPagesUtil {

    private final User user;

    private final HashMap<Integer, EmbedBuilder> pages = new HashMap<>();
    private final HashMap<User, Integer> pageNumber = new HashMap<>();

    public HelpPagesUtil(User user, Message message, EmbedBuilder embed) {
        this.user = user;

        this.pageNumber.put(user, 1);
        this.pages.put(pageNumber.get(user), embed);
    }

    @Contract(pure = true)
    public HashMap<Integer, EmbedBuilder> getPage() {
        return pages;
    }

    @Contract(pure = true)
    public void nextPage(EmbedBuilder embed) {
        pageNumber.entrySet().stream().filter(f -> f.getKey() == user).forEach((v) -> {
            v.setValue(pageNumber.get(user)+1);
        });
        this.pages.put(pageNumber.get(user), embed);
    }

    @Contract(pure = true)
    public void backPage() {
        this.pages.remove(pageNumber.get(user));

        pageNumber.entrySet().stream().filter(f -> f.getKey() == user).forEach((v) -> {
            v.setValue(pageNumber.get(user)-1);
        });
    }

    public HashMap<User, Integer> getPageNumber() {
        return pageNumber;
    }
}
