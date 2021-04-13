package system.objects.Utils.levelUtils;

import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.Contract;
import system.objects.Utils.profileconfigutils.ProfileBuilder;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class LevelsManager extends ProfileBuilder implements LevelsCalculations {

    private final User user;
    private final ProfileBuilder profile;

    public LevelsManager(User user) {
        this.user = user;
        this.profile = new ProfileBuilder(this.user);
    }

    @Contract(pure = true)
    public LevelsManager build(Consumer<LevelsManager> onBuild) {
        onBuild.accept(this);
        return this;
    }

    @Contract(pure = true)
    public int getLevel() {
        return (int) LevelsCalculations.getLevelFromExp(profile.getExp());
    }

    @Contract(pure = true)
    public int getExp() {
        return profile.getExp();
    }

    @Contract(pure = true)
    public int getExpToLevel() {
        return (int) LevelsCalculations.getTotalExpToNextLevel(profile.getExp());
    }

    @Contract(pure = true)
    public void addExperience(int experience) {
        if (experience < 0) {
            throw new IllegalArgumentException("experience cannot be zero or empty!");
        }

        profile.addExperience(experience);
    }

    @Contract(pure = true)
    public void removeExperience(int experience) {
        if (experience < 0) {
            throw new IllegalArgumentException("experience cannot be minus!");
        }

        profile.removeExperience(experience);
    }

    @Contract(pure = true)
    public void setExperience(int experience) {
        if (experience < 0) {
            throw new IllegalArgumentException("experience cannot be minus!");
        }

        profile.setExperience(experience);
    }

    @Contract(pure = true)
    public int calculateMessage(String message) {
        List<String> words = new ArrayList<>();
        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(message);
        int lastIndex = breakIterator.first();

        while (BreakIterator.DONE != lastIndex) {
            int firstIndex = lastIndex;
            lastIndex = breakIterator.next();

            if (lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(message.charAt(firstIndex))) {
                words.add(message.substring(firstIndex, lastIndex));
            }
        }

        Random random = new Random();

        if (words.size() >= 10) return random.nextInt(10+1);
        if (words.size() < 10) return random.nextInt(4+1);
        if (words.size() > 20) return random.nextInt(20+1);
        if (words.size() < 5) return random.nextInt(2+1);

        return 1;
    }

}
