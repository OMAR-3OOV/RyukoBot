package system.Objects.Utils;

import net.dv8tion.jda.api.entities.User;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.Properties;

public final class LastWinnersEvent {

    private final File file;
    private final Properties properties;

    public LastWinnersEvent() {
        this.file =  new File("System/Game/last_winner.properties");
        this.properties = new Properties();

        // create
        create();
    }

    private void create() {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists()) {
            FileReader fileReader = null;

            try {
                fileReader = new FileReader(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                getProperties().load(fileReader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!file.exists()) {
            try {
                getFile().createNewFile();

                saveFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addWinner(User user) {
        getProperties().setProperty(user.getId(), String.valueOf(ZonedDateTime.now()));
        saveFile();
    }

    public void saveFile() {
        try {
            properties.save(new FileOutputStream(getFile()), null);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public File getFile() {
        return file;
    }

    public Properties getProperties() {
        return properties;
    }
}
