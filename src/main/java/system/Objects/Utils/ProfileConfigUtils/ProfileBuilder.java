package system.Objects.Utils.ProfileConfigUtils;

import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.Contract;

import java.io.*;
import java.util.*;

public class ProfileBuilder {

    private User user;

    // Files
    private final File settingsFolder = new File("System/Profiles/Settings.properties");
    private File profilefolder;

    // Properties
    private final Properties profileProperties = new SortProperties();
    private final Properties settingsProperties = new Properties();

    /**
     * get user information
     *
     * @param user
     */
    public ProfileBuilder(User user) {

        this.user = user;

        this.profilefolder = new File("System/Profiles/Users/" + user.getId() + ".properties");

        buildProfile(this.profilefolder, this.profileProperties);
        buildSettings(this.settingsFolder, this.settingsProperties);
    }

    /**
     * get settings information
     */
    public ProfileBuilder() {
        buildSettings(this.settingsFolder, this.settingsProperties);
    }

    /**
     * profile info
     *
     * @return profile
     */

    @Contract(pure = true)
    public User getUser() {
        return user;
    }

    @Contract(pure = true)
    public File getProfilefolder() {
        return profilefolder;
    }

    @Contract(pure = true)
    public File getSettingsFolder() {
        return settingsFolder;
    }

    @Contract(pure = true)
    public Properties getProfileProperties() {
        return profileProperties;
    }

    @Contract(pure = true)
    public Properties getSettingsProperties() {
        return settingsProperties;
    }

    @Contract(pure = true)
    public void buildProfile(File file, Properties p) {
        if (user.isBot() || user.isFake()) return;

        FileReader reader = null;

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {

        }

        if (file.exists()) {
            try {
                p.load(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!file.exists()) {
            try {
                file.createNewFile();

                p.put("profile-time-create", String.valueOf(0));
                p.put("username", user.getName());
                p.put("user-id", user.getId());
                p.put("language", "english");
                p.put("ruko", String.valueOf(0));
                p.put("level", String.valueOf(0));
                p.put("lasttime-use-command", String.valueOf(0));
                p.put("minecraft-account", "null");
                p.put("rank", "null");
                p.put("games-count", String.valueOf(0));
                p.put("last-game", String.valueOf(0));
                p.put("verify", Boolean.toString(false));
                p.put("banned", Boolean.toString(false));
                p.put("auto-banned", String.valueOf(0));

                save(file, p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Contract(pure = true)
    public void buildSettings(File file, Properties p) {
        FileReader reader = null;

        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {

        }

        if (file.exists()) {
            try {
                p.load(reader);
            } catch (IOException e) {

            }
        }

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();

                // Can changed
                p.setProperty("rukoSystem", Boolean.toString(true));
                p.setProperty("levelSystem", Boolean.toString(true));
                p.setProperty("profileSystem", Boolean.toString(true));
                p.setProperty("LanguageSystem", Boolean.toString(true));

                save(file, p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * get profile info
     *
     * @return get profile
     */

    @Contract(pure = true)
    public Date getTimeCraete() {
        return new Date(Long.parseLong(getProfileProperties().getProperty("profile-time-create")));
    }

    @Contract(pure = true)
    public String getUsername() {
        return getProfileProperties().getProperty("username");
    }

    @Contract(pure = true)
    public String getUserId() {
        return getProfileProperties().getProperty("user-id");
    }

    public String getLanguage() {
        return getProfileProperties().getProperty("language");
    }

    public int getRuko() {
        return Integer.parseInt(getProfileProperties().getProperty("ruko"));
    }

    public int getLevel() {
        return Integer.parseInt(getProfileProperties().getProperty("level"));
    }

    public Date getLastTimeCommandUse() {
        return new Date(Long.parseLong(getProfileProperties().getProperty("lasttime-use-command")));
    }

    public String getMinecraftAccount() {
        return getProfileProperties().getProperty("minecraft-account");
    }

    public String getRank() {
        return getProfileProperties().getProperty("rank");
    }

    public int getGamesCount() {
        return Integer.parseInt(getProfileProperties().getProperty("games-count"));
    }

    public String getLastGamePlay() {
        return getProfileProperties().getProperty("last-game");
    }

    public boolean getVerify() {
        return Boolean.parseBoolean(getProfileProperties().getProperty("verify"));
    }

    public boolean getBanned() {
        return Boolean.parseBoolean(getProfileProperties().getProperty("banned"));
    }

    public int getAutoBanned() {
        return Integer.parseInt(getProfileProperties().getProperty("auto-banned"));
    }
    /**
     * profile info
     *
     * @return set profile
     */

    public void setUsername(String name) {
        getProfileProperties().put("username", name);
        save(profilefolder, profileProperties);
    }

    public void setTimeCraete(Date date) {
        getProfileProperties().put("profile-time-create", String.valueOf(date.getTime()));
        save(profilefolder, profileProperties);
    }

    public void setLanguage(String language) {
        getProfileProperties().put("language", language);
        save(profilefolder, profileProperties);
    }

    public void setRuko(int ruko) {
        getProfileProperties().put("ruko", String.valueOf(ruko));
        save(profilefolder, profileProperties);
    }

    public void setLevel(int level) {
        getProfileProperties().put("level", Integer.toString(getLevel() + level));
        save(profilefolder, profileProperties);
    }

    public void setLastTimeCommandUse(Date date) {
        getProfileProperties().put("lasttime-use-command", String.valueOf(date.getTime()));
        save(profilefolder, profileProperties);
    }

    public void setMinecraftAccount(String minecraftAccount) {
        getProfileProperties().put("minecraft-account", minecraftAccount);
        save(profilefolder, profileProperties);
    }

    public void setRank(String rank) {
        getProfileProperties().put("rank", rank);
        save(profilefolder, profileProperties);
    }

    public void setGamesCount(int count) {
        getProfileProperties().put("games-count", String.valueOf(count));
        save(profilefolder, profileProperties);
    }

    public void setLastGamePlay(String lastGamePlay) {
        getProfileProperties().put("last-game", lastGamePlay);
        save(profilefolder, profileProperties);
    }

    public void setVerify(boolean boo) {
        getProfileProperties().put("verify", String.valueOf(boo));
        save(profilefolder, profileProperties);
    }

    public void setBanned(boolean boo) {
        getProfileProperties().put("banned", String.valueOf(boo));
        save(profilefolder, profileProperties);
    }

    public void addAutoBanned(int amount) {
        getProfileProperties().put("auto-banned", String.valueOf(getAutoBanned()+amount));
        save(profilefolder, profileProperties);
    }

    /**
     * get settings check
     * @return settings folder
     */
    public boolean getRukoSystem() {
        return Boolean.parseBoolean(getSettingsProperties().getProperty("rukoSystem"));
    }

    /**
     * check methods
     *
     * @return profile info
     */

    public boolean isProfile() {
        if (getProfilefolder().getName().contains(user.getId())) return true;
        else return false;
    }

    /**
     * save file
     *
     * @param properties
     */

    public void save(File file, Properties properties) {
        try {
            properties.save(new FileOutputStream(file), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static class SortProperties extends Properties {
        public Enumeration keys() {
            Enumeration keysEnum = super.keys();
            Vector<String> keyList = new Vector<>();

            while (keysEnum.hasMoreElements()) {
                keyList.add((String) keysEnum.nextElement());
            }

            Collections.sort(keyList, Collections.reverseOrder());
            return keyList.elements();
        }
    }
}
