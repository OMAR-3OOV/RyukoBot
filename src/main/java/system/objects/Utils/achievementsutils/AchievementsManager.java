package system.objects.Utils.achievementsutils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import system.objects.TextUtils.MessageUtils;
import system.objects.Utils.LanguagesUtils.LanguagesManager;
import system.objects.Utils.LanguagesUtils.MessagesKeys;

import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class AchievementsManager {

    private User user;

    private File file;
    private final File usersFile;
    private File settingsFile;

    private final Properties properties;
    private Properties settingsProperties;

    public AchievementsManager(String achievement) {
        this.file = new File("System/Achievements/keys/" + achievement + ".properties");
        this.usersFile = new File("System/Profiles/AchievementUsers");
        this.settingsFile = new File("System/Achievements/Guilds/settings.properties");

        this.properties = new Properties();
        this.settingsProperties = new Properties();
        buildSettings();
    }

    public AchievementsManager(User user) {
        this.user = user;
        this.usersFile = new File("System/Profiles/AchievementUsers/" + user.getId() + ".properties");
        this.settingsFile = new File("System/Achievements/Guilds/settings.properties");

        this.properties = new Properties();
        buildUsers();
    }

    public User getUser() {
        return user;
    }

    // achievements collected
    public List<Achievements> getAchievements() {
        List<Achievements> achievements = new ArrayList<>();

        for (int achievement = 0; achievement < Achievements.values().length; achievement++) {
            if (Boolean.parseBoolean(this.properties.getProperty(Achievements.values()[achievement].getAchievementKey()))) {
                achievements.add(Achievements.values()[achievement]);
            }
        }

        return achievements;
    }

    // check achievements collected
    public Map<Achievements, Boolean> AchievementsMap() {
        Map<Achievements, Boolean> achievements = new HashMap<>();

        for (Achievements achievement : Achievements.values()) {
            achievements.put(achievement, Boolean.parseBoolean(properties.getProperty(achievement.getAchievementKey())));
        }

        return achievements;
    }

    public void addAchievement(Achievements achievements) {
        this.properties.setProperty(achievements.getAchievementKey(), "true");
    }

    public double getCollectedPercentage(String achievementKey) {
        AtomicInteger percentage = new AtomicInteger(0);

        Arrays.stream(Objects.requireNonNull(this.usersFile.getParentFile().listFiles())).forEach(files -> {
            try {
                properties.load(new FileInputStream(files));

                if (Boolean.parseBoolean(properties.getProperty(achievementKey))) {
                    percentage.addAndGet(1);
                }

            } catch (IOException e) {
            }
        });

        return percentage.get();
    }

    public boolean isCollected(Achievements achievement) {
        AtomicBoolean bool = new AtomicBoolean(false);

//        Arrays.stream(getUsersFile().listFiles()).forEach(files -> {
//            try {
//                properties.load(new FileInputStream(files));
//
//                bool.set(Boolean.parseBoolean(properties.getProperty(achievement.getAchievementKey())));
//            } catch (IOException e) {
//            }
//        });

        if (getUsersFile().exists()) {
            try {
                properties.load(new FileInputStream(getUsersFile()));
                bool.set(Boolean.parseBoolean(properties.getProperty(achievement.getAchievementKey())));
            } catch (IOException e) {

            }
        } else {
            throw new IllegalArgumentException(this.user.getId() + " is doesn't longer in files");
        }

        return bool.get();
    }

    @Contract(pure = true)
    public void giveAchievements(Achievements... achievements) {
        try {
            properties.load(new FileInputStream(getUsersFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Arrays.stream(achievements).forEach(achievement -> {
            if (Boolean.parseBoolean(properties.getProperty(achievement.getAchievementKey()))) return;

            else {
                properties.setProperty(achievement.getAchievementKey(), Boolean.toString(true));
                save(getUsersFile());
            }
        });
    }

    @Contract(pure = true)
    public void giveAchievements(User user, Achievements... achievements) {

        Arrays.stream(Objects.requireNonNull(this.getUsersFile().getParentFile().listFiles())).forEach(file -> {
            if (file.getName().equals(user.getId())) {
                try {
                    properties.load(new FileInputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Arrays.stream(achievements).forEach(achievement -> {

            if (!Boolean.parseBoolean(properties.getProperty(achievement.getAchievementKey()))) {
                user.openPrivateChannel().queue();
                properties.setProperty(achievement.getAchievementKey(), Boolean.toString(true));
                save(getUsersFile());
            }
        });

    }

    public void sendCollectedMessage(User user, Achievements... achievements) {
        EmbedBuilder embed = new EmbedBuilder();
        StringBuilder builder = new StringBuilder();
        LanguagesManager languagesManager = new LanguagesManager(this.user);

        Arrays.stream(achievements).forEach(achievement -> {
            builder.append("\n- ").append(languagesManager.getAchievementName(achievement)).append(" | ").append(" Type : ").append(achievement.getAchievementDescription()).append("\n");
        });

        embed.setColor(new Color(175, 141, 252));
        embed.setTitle(languagesManager.getMessage(MessagesKeys.ACHIEVEMENT_COLLECTED_TITLE_MESSAGE));
        embed.setDescription(languagesManager.getMessage(MessagesKeys.ACHIEVEMENT_COLLECTED_DESCRIPTION_MESSAGE).replace("<achievements>", builder.toString()));
        embed.setFooter(languagesManager.getMessage(MessagesKeys.ACHIEVEMENT_COLLECTED_FOOTER_MESSAGE) + " " + new SimpleDateFormat("EEEE, dd MMM yyyy").format(new Date()));

        user.openPrivateChannel().queue(privatemsg -> {
            privatemsg.sendMessage(embed.build()).queue(message -> {

            }, (error) -> {

            });
        });
    }

    public File getFile() {
        return file;
    }

    public File getUsersFile() {
        return usersFile;
    }

    public File getSettingsFile() {
        return settingsFile;
    }

    public Properties getProperties() {
        return properties;
    }

    public Properties getSettingsProperties() {
        return settingsProperties;
    }

    public void setup(@NotNull Consumer<Properties> set) {
        set.accept(this.properties);
        save();
    }

    public void buildUsers() {
        if (!this.usersFile.getParentFile().exists()) {
            this.usersFile.getParentFile().mkdirs();
        }

        if (this.usersFile.exists()) {
            try {
                properties.load(new FileInputStream(this.usersFile));
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int achievements = 0; achievements < Achievements.values().length; achievements++) {
                Achievements achievement = Achievements.values()[achievements];
                if (this.properties.getProperty(achievement.getAchievementKey()) == null) {
                    this.properties.setProperty(achievement.getAchievementKey(), "false");
                    save(this.usersFile);
                }
            }
        }

        if (!this.usersFile.exists()) {
            try {
                this.usersFile.createNewFile();

                for (int achievements = 0; achievements < Achievements.values().length; achievements++) {
                    Achievements achievement = Achievements.values()[achievements];

                    properties.setProperty(achievement.getAchievementKey(), "false");
                }

                save(this.usersFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void buildSettings() {
        if (!this.settingsFile.getParentFile().exists()) {
            this.settingsFile.getParentFile().mkdirs();
        }

        if (this.settingsFile.exists()) {
            try {
                this.settingsProperties.load(new FileInputStream(this.settingsFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!this.settingsFile.exists()) {
            try {
                this.settingsFile.createNewFile();

                this.settingsProperties.setProperty("system", "true");
                this.settingsProperties.setProperty("collecting", "true");
                this.settingsProperties.setProperty("numbering-order", "true");
                this.settingsProperties.setProperty("display-percentage", "true");
                this.settingsProperties.setProperty("collect-icon", new MessageUtils(":successful:").EmojisHolder());
                this.settingsProperties.setProperty("completed-notification", "true");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createAchievements() {
        if (!this.file.getParentFile().exists()) {
            this.file.getParentFile().mkdirs();
        }

        if (this.file.exists()) {
            try {
                this.properties.load(new FileInputStream(this.file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
                save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void save() {
        for (int all = 0; all < Achievements.values().length; all++) {
            try {
                this.properties.save(new FileOutputStream(this.file), null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void save(File file) {
        for (int all = 0; all < Achievements.values().length; all++) {
            try {
                this.properties.save(new FileOutputStream(file), null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkAchievements(Achievements achievements) {
        return this.file.getName().contains(achievements.getAchievementKey());
    }
}
