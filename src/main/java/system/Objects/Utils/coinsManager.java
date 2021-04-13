package system.Objects.Utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.io.*;
import java.util.Properties;

/*
    The coins system name is : Ruko
    created for give special access commands
 */
public final class coinsManager {

    // File System
    private File file;
    private Properties properties;

    // File System User
    private File userFiles;
    private User user;

    // File System Guild
    private File guildFiles;
    private Guild guild;

    // Coins Options
    private int coins;
    private int gains;

    public coinsManager(User user) {
        this.user = user;
        this.file = new File("system/Profiles");
        this.userFiles = new File("system/Profiles/Users/" +user.getId()+".properties");
        this.properties = new Properties();
        createFile();
        this.coins = Integer.parseInt(getProperties().getProperty("ruko"));
        this.gains = Integer.parseInt(getProperties().getProperty("gains"));
    }

    public coinsManager(Guild guild) {
        this.guild = guild;
        this.file = new File("system/Profiles");
        this.guildFiles = new File("system/Profiles/Guilds/" +guild.getId()+".properties");
        this.properties = new Properties();
        createGuildFile();
        this.coins = Integer.parseInt(this.properties.getProperty("Coins"));
    }

    /*********************/
    // get user
    public User getUser() {
        return user;
    }

    // user file
    public File getUserFiles() {
        return userFiles;
    }

    // getting file by user
    public File getUserFiles(User user) {
        return new File("system/Profiles/Users/" +user.getId()+".properties");
    }
    /*********************/

    // get guild
    public Guild getGuild() {
        return guild;
    }

    public File getGuildFiles() {
        return guildFiles;
    }

    public File getGuildFiles(Guild guild) {
        return new File("system/Profiles/Guilds/" +guild.getId()+".properties");
    }
    /*********************/

    public File getFile() {
        return file;
    }

    // config system
    public Properties getProperties() {
        return properties;
    }
    /*********************/

    // create file for user
    public void createFile() {
        if (getUser().isBot()) return;
        if (getUser().isFake()) return;

        if (!getUserFiles().getParentFile().exists()) {
            getUserFiles().getParentFile().mkdirs();
        }

        if (getUserFiles().exists()) {

            try {
                getProperties().load(new FileInputStream(this.userFiles));
            }catch (IOException e) {

            }

            for (int keys = 0; keys < coinsConfig.values().length; keys++) {
                if (properties.getProperty(coinsConfig.values()[keys].getKey()) == null) {
                    properties.setProperty(coinsConfig.values()[keys].getKey(), "0");
                    saveFile();
                }
            }
        }

        if (!getUserFiles().exists()) {
            try {
                this.userFiles.createNewFile();

                properties.setProperty("Gains", "0");
                properties.setProperty("ruko", "0");
                properties.setProperty("Booster", "0");
            }catch (IOException e) {
                e.printStackTrace();
            }

            try {
                properties.save(new FileOutputStream(getUserFiles()), null);
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // create file by user
    public void createFile(User user) {
        if (getUser().isBot()) return;
        if (getUser().isFake()) return;

        if (!getUserFiles().getParentFile().exists()) {
            getUserFiles().getParentFile().mkdirs();
        }

        if (getUserFiles().exists()) {

            FileReader fileReader = null;
            try {
                fileReader = new FileReader(userFiles);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                getProperties().load(fileReader);
            }catch (IOException e) {

            }
        }

        if (!getUserFiles().exists()) {
            try {
                this.userFiles.createNewFile();

                properties.setProperty("username", user.getName());
                properties.setProperty("id", user.getId());
                properties.setProperty("Gains", "0");
                properties.setProperty("ruko", "0");
            }catch (IOException e) {
                e.printStackTrace();
            }

            try {
                properties.save(new FileOutputStream(getUserFiles()), null);
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void createGuildFile () {

        if (!getGuildFiles().getParentFile().exists()) {
            getGuildFiles().getParentFile().mkdirs();
        }

        if (getGuildFiles().exists()) {

            FileReader fileReader = null;
            try {
                fileReader = new FileReader(getGuildFiles());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                getProperties().load(fileReader);
            }catch (IOException e) {

            }
        }

        if (!getGuildFiles().exists()) {
            try {
                getGuildFiles().createNewFile();

                properties.setProperty("guildname", getGuild().getName());
                properties.setProperty("id", getGuild().getId());
                properties.setProperty("ruko", "0");
            }catch (IOException e) {
                e.printStackTrace();
            }

            try {
                properties.save(new FileOutputStream(getGuildFiles()), null);
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void addProperty(String key, String value) {
        getProperties().setProperty(key, value);
        saveFile();
    }

    public void saveFile() {
        try {
            properties.save(new FileOutputStream(getUserFiles()), null);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*********************/

    // Main get Ruko Gains
    public int getGains() {
        return gains;
    }

    // Main get user
    public int getRukoUser() {
        return coins;
    }

    // Main get guild
    public int getRukoGuild() {
        return coins;
    }

    /*********************/

    // get ruko by user
    public int getGains(User user) {
        if (getUserFiles(user).exists()) {

            FileReader fileReader = null;
            try {
                fileReader = new FileReader(getUserFiles(user));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                getProperties().load(fileReader);
            }catch (IOException e) {

            }

            return Integer.parseInt(getProperties().getProperty("Gains"));
        }
        return 0;
    }

    // get ruko by user
    public int getRukoUser(User user) {
        if (getUserFiles(user).exists()) {

            FileReader fileReader = null;
            try {
                fileReader = new FileReader(getUserFiles(user));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                getProperties().load(fileReader);
            }catch (IOException e) {

            }

            return Integer.parseInt(getProperties().getProperty("ruko"));
        }
        return 0;
    }

    // get ruko by guild
    public int getRukoGuild(Guild guild) {
        if (getGuildFiles(guild).exists()) {

            FileReader fileReader = null;
            try {
                fileReader = new FileReader(getGuildFiles(guild));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                getProperties().load(fileReader);
            }catch (IOException e) {

            }

            return Integer.parseInt(getProperties().getProperty("ruko"));
        }
        return 0;
    }

    /*********************/

    // Main add
    public void addRukoUser(int amount) {
        properties.setProperty("ruko", Integer.toString(getRukoUser()+amount));
        properties.setProperty("Gains", Integer.toString(getRukoUser()+amount));

        try {
            properties.save(new FileOutputStream(this.userFiles), null);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // add ruko by guild
    public void addRukoGuild(int amount) {
        properties.setProperty("ruko", Integer.toString(getRukoGuild()+amount));

        try {
            properties.save(new FileOutputStream(getGuildFiles()), null);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    // add ruko by user
    public void addRukoUser(User user, int amount) {
        properties.setProperty("ruko", Integer.toString(getRukoUser(user)+amount));
        properties.setProperty("Gains", Integer.toString(getRukoUser()+amount));

        try {
            properties.save(new FileOutputStream(getUserFiles(user)), null);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // add ruko by guild
    public void addRukoGuild(Guild guild, int amount) {
        properties.setProperty("ruko", Integer.toString(getRukoGuild(guild)+amount));

        try {
            properties.save(new FileOutputStream(getGuildFiles(guild)), null);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*********************/

    public void removeRukoUser(int amount) {
        properties.setProperty("ruko", Integer.toString(getRukoUser()-amount));

        try {
            properties.save(new FileOutputStream(this.userFiles), null);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void removeRukoUser(User user, int amount) {
        properties.setProperty("ruko", Integer.toString(getRukoUser(user)-amount));

        try {
            properties.save(new FileOutputStream(getUserFiles(user)), null);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void removeRukoGuild(Guild guild, int amount) {
        properties.setProperty("ruko", Integer.toString(getRukoGuild(guild)-amount));

        try {
            properties.save(new FileOutputStream(getUserFiles(user)), null);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*********************/

    // Main set user
    public void setRukoUser(int amount) {
        properties.setProperty("ruko", Integer.toString(amount));

        try {
            properties.save(new FileOutputStream(this.userFiles), null);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Main set Guild
    public void setRukoGuild(int amount) {
        properties.setProperty("ruko", Integer.toString(amount));

        try {
            properties.save(new FileOutputStream(getGuildFiles()), null);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*********************/

    // set ruko by user
    public void setRukoUser(User user, int amount) {
        properties.setProperty("ruko", Integer.toString(amount));

        try {
            properties.save(new FileOutputStream(getUserFiles(user)), null);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // set ruko by Guild
    public void setRukoGuild(Guild guild, int amount) {
        properties.setProperty("ruko", Integer.toString(amount));

        try {
            properties.save(new FileOutputStream(getGuildFiles(guild)), null);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
