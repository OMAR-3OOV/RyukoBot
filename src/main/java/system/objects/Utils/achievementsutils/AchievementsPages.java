package system.objects.Utils.achievementsutils;

import net.dv8tion.jda.api.entities.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AchievementsPages {

    private final User user;
    private final AchievementsManager achievementsManager;

    private final SortedMap<Integer, Achievements> achievementsPages = new TreeMap<>();

    private int input_1;
    private int input_2;

    private final AtomicInteger currentPage = new AtomicInteger(0);

    public AchievementsPages(User user) {
        achievementsManager = new AchievementsManager(user);

        this.user = user;
        this.input_1 = 0;
        this.input_2 = 4;

        getAchievementsManager().AchievementsMap().forEach((achievement, bool) -> {
            achievementsPages.put(currentPage.get(), achievement);
            currentPage.addAndGet(1);
        });
    }

    public User getUser() {
        return user;
    }

    public AchievementsManager getAchievementsManager() {
        return achievementsManager;
    }

    public SortedMap<Integer, Achievements> getAchievementsPages() {
        return achievementsPages;
    }

    public AtomicInteger getCurrentPage() {
        return currentPage;
    }

    public void forward() {
        if (input_2 >= getAchievementsPages().size()) {
            input_1 = 0;
            input_2 = 4;

            return;
        }

        input_1+=1;
        input_2+=1;
    }

    public void backward() {
        if (input_1 <= 0) {
            input_1 =  getAchievementsPages().size()-4;
            input_2 = getAchievementsPages().size();

            return;
        }

        input_1-=1;
        input_2-=1;
    }

    public int input_1() {
        return input_1;
    }

    public int input_2() {
        return input_2;
    }


}
