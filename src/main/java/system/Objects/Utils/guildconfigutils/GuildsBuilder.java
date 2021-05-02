package system.objects.Utils.guildconfigutils;

import net.dv8tion.jda.api.entities.Guild;
import system.objects.Utils.LanguagesUtils.Languages;

import java.io.*;
import java.util.*;

public class GuildsBuilder {

    private Guild guild;
    private final File guildFolder;
    private final Properties properties;

    public GuildsBuilder(Guild guild) {
        this.guild = guild;
        this.guildFolder = new File("System/Guilds/" + guild.getId() + ".properties");
        this.properties = new SortProperties();

        if (this.guildFolder.exists()) {
            try {
                properties.load(new FileInputStream(this.guildFolder));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public File getGuildFolder() {
        return guildFolder;
    }

    public Properties getProperties() {
        return properties;
    }

    public void buildGuild() {
        if (!this.guildFolder.getParentFile().exists()) {
            this.guildFolder.getParentFile().mkdirs();
        }

        if (!this.guildFolder.exists()) {
            try {
                if (this.guildFolder.createNewFile()) {

                    properties.setProperty("guild-name", this.guild.getName());
                    properties.setProperty("guild-id", this.guild.getId());
                    properties.setProperty("guild-description", this.guild.getDescription()==null?"none":this.guild.getDescription());
                    properties.setProperty("guild-ownership-id", Objects.requireNonNull(this.guild.getOwner()).getId());
                    properties.setProperty("bot-join-time", String.valueOf(new Date().getTime()));
                    properties.setProperty("language", "0");

                    save();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                properties.load(new FileInputStream(this.guildFolder));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteGuild() {
        if (this.guildFolder.exists()) {
            if (this.guildFolder.delete()) {

            }
        }
    }

    public void save() {
        try {
            this.properties.save(new FileOutputStream(this.guildFolder), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setName(String name) {
        properties.setProperty("guild-name", name);
        save();
    }

    public void setDescription(String description) {
        properties.setProperty("guild-description", description);
        save();
    }

    public Languages getLanguage() {
        return Languages.getLanguage(Integer.parseInt(properties.getProperty("language")));
    }

    public void setLanguage(Languages language) {
        properties.setProperty("language", String.valueOf(language.getId()));
        save();
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


