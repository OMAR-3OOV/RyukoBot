package system.Objects.Utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.*;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Random;

public final class ActivityManagar {

    // File System
    private File activitiesFile;
    private Properties properties;

    private User user;
    private Guild guild;

    // Activity options
    public int activity;
    public static int activityCount;

    public ActivityManagar(Guild guild, User user) {
        this.user = user;
        this.guild = guild;
        this.activitiesFile = new File("System/Activities/Users/" + getUser().getId() + ".properties");
        this.properties = new Properties();
        create();
        this.activity = Integer.parseInt(this.properties.getProperty(user.getId()));
    }

    public User getUser() {
        return user;
    }

    public Guild getGuild() {
        return guild;
    }

    public File getActivitiesFile() {
        return activitiesFile;
    }

    public Properties getProperties() {
        return properties;
    }

    // Currently activities for user
    public int getActivity() {
        return activity;
    }

    // All users Activities table
    public Hashtable<Object, Object> getActivities() {
        final Hashtable<Object, Object> user = new Hashtable<>();
        getProperties().keySet().stream().forEach((key) -> {
            user.put(key, getProperties().get(key).toString());
        });
        return user;
    }

    // File creation
    public void create() {
        if (getUser().isBot()) return;
        if (getUser().isFake()) return;

        if (!getActivitiesFile().exists()) {
            getActivitiesFile().getParentFile().mkdirs();
        }

        if (getActivitiesFile().exists()) {
            FileReader fileReader = null;

            try {
                fileReader = new FileReader(activitiesFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                getProperties().load(fileReader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!getActivitiesFile().exists()) {
            try {
                getActivitiesFile().createNewFile();

                getProperties().setProperty(getUser().getId(), "0");
            } catch (IOException e) {

            }

            try {
                getProperties().save(new FileOutputStream(getActivitiesFile()), null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveProperties() {
        try {
            getProperties().save(new FileOutputStream(getActivitiesFile()), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addActivity(GuildMessageReceivedEvent event, int... amount) {
        Random random = new Random();
        int nextAmount = random.nextInt(100) + 1;

        if (event.getMessage().getContentRaw().length() > 20) {
            switch (nextAmount) {
                case 75:
                    // Bonus 99
                    getProperties().setProperty(getUser().getId(), Integer.toString(amount[0]));
                    break;
                case 60:
                    // Bonus 75
                    getProperties().setProperty(getUser().getId(), Integer.toString(Integer.parseInt(getProperties().getProperty(getUser().getId())) + amount[1]));
                    break;
                case 47:
                    // Bonus 45
                    getProperties().setProperty(getUser().getId(), Integer.toString(Integer.parseInt(getProperties().getProperty(getUser().getId())) + amount[2]));
                    break;
                case 30:
                    // Bonus 35
                    getProperties().setProperty(getUser().getId(), Integer.toString(Integer.parseInt(getProperties().getProperty(getUser().getId())) + amount[3]));
                    break;
                default:
                    // Normal
                    getProperties().setProperty(getUser().getId(), Integer.toString(Integer.parseInt(getProperties().getProperty(getUser().getId())) + amount[4]));
                    break;
            }
        } else if (event.getMessage().getContentRaw().length() > 10) {
            switch (nextAmount) {
                case 87:
                    // Bonus 99
                    getProperties().setProperty(getUser().getId(), Integer.toString(amount[0]));
                    break;
                case 74:
                    // Bonus 75
                    getProperties().setProperty(getUser().getId(), Integer.toString(Integer.parseInt(getProperties().getProperty(getUser().getId())) + amount[1]));
                    break;
                case 56:
                    // Bonus 45
                    getProperties().setProperty(getUser().getId(), Integer.toString(Integer.parseInt(getProperties().getProperty(getUser().getId())) + amount[2]));
                    break;
                case 43:
                    // Bonus 35
                    getProperties().setProperty(getUser().getId(), Integer.toString(Integer.parseInt(getProperties().getProperty(getUser().getId())) + amount[3]));
                    break;
                default:
                    // Normal
                    getProperties().setProperty(getUser().getId(), Integer.toString(Integer.parseInt(getProperties().getProperty(getUser().getId())) + amount[4]));
                    break;
            }
        } else if (event.getMessage().getContentRaw().length() < 10) {
            switch (nextAmount) {
                case 95:
                    // Bonus 99
                    getProperties().setProperty(getUser().getId(), Integer.toString(amount[0]));
                    break;
                case 80:
                    // Bonus 75
                    getProperties().setProperty(getUser().getId(), Integer.toString(Integer.parseInt(getProperties().getProperty(getUser().getId())) + amount[1]));
                    break;
                case 68:
                    // Bonus 45
                    getProperties().setProperty(getUser().getId(), Integer.toString(Integer.parseInt(getProperties().getProperty(getUser().getId())) + amount[2]));
                    break;
                case 53:
                    // Bonus 35
                    getProperties().setProperty(getUser().getId(), Integer.toString(Integer.parseInt(getProperties().getProperty(getUser().getId())) + amount[3]));
                    break;
                default:
                    // Normal
                    getProperties().setProperty(getUser().getId(), Integer.toString(Integer.parseInt(getProperties().getProperty(getUser().getId())) + amount[4]));
                    break;
            }
        }
        saveProperties();
    }

    // set user activities zero
    public void resetActivity() {
        getProperties().setProperty(getUser().getId(), Integer.toString(0));
        saveProperties();
    }
}
