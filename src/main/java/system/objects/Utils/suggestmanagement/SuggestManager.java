package system.objects.Utils.suggestmanagement;

import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.Contract;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SuggestManager {

    private File suggestFile;
    private File suggestFileUser;
    private File suggestFileSettings = new File("system/Suggest/settings.properties");

    private Properties properties;
    private Properties psettings;
    private int id;
    private User user;

    private final List<String> waitingList = new ArrayList<>();
    private final List<String> voteList = new ArrayList<>();

    public SuggestManager() {
        this.suggestFile = new File("system/Suggest");
        this.psettings = new Properties();
        this.properties = new Properties();
        build();
    }

    public SuggestManager(User user) {
        this.user = user;
        this.id = generateSuggestId();
        this.suggestFileUser = new File("system/Suggest/Suggestions/" + getId() + ".properties");
        this.suggestFileSettings = new File("system/Suggest/settings.properties");
        this.properties = new Properties();
        this.psettings = new Properties();

        build(user);
    }

    @Contract(pure = true)
    public List<String> getWaitingList() {
        return waitingList;
    }

    @Contract(pure = true)
    public List<String> getVoteList() {
        return voteList;
    }

    public void addVoteList(int fileID) {
        if (getWaitingList().contains(String.valueOf(fileID))) {
            getWaitingList().remove(fileID);
            getVoteList().add(String.valueOf(fileID));
        }
    }

    public void addWaitingList(int fileID) {
        getWaitingList().add(String.valueOf(fileID));
    }

    public boolean checkWaitingList(int fileID) {
        if (getWaitingList().contains(String.valueOf(fileID))) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkVoteList(int fileID) {
        if (getVoteList().contains(String.valueOf(fileID))) {
            return true;
        } else {
            return false;
        }
    }

    @Contract(pure = true)
    public User getUser() {
        return user;
    }

    @Contract(pure = true)
    public File getSuggestFile() {
        return suggestFile;
    }

    @Contract(pure = true)
    public File getSuggestFileUser() {
        return suggestFileUser;
    }

    @Contract(pure = true)
    public Properties getProperties() {
        return properties;
    }

    public Properties getPsettings() {
        return psettings;
    }

    public int getSuggestWaitingFiles() {
        final AtomicInteger count = new AtomicInteger(0);
        final File filee = new File(suggestFile.getPath() + "/Suggestions");

        Arrays.stream(filee.listFiles()).forEach((file -> {
            FileReader reader = null;

            try {
                reader = new FileReader(file);
            } catch (FileNotFoundException e) {

            }

            if (file.exists()) {
                try {
                    properties.load(reader);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (properties.getProperty("status") != null) {
                if (properties.getProperty("status").contains("waiting")) {
                    count.addAndGet(1);
                }
            }
        }));
        return count.get();
    }

    public int getSuggestVotingFiles() {
        final AtomicInteger count = new AtomicInteger(0);
        final File filee = new File(suggestFile.getPath() + "/Suggestions");

        Arrays.stream(filee.listFiles()).forEach((file -> {
            FileReader reader = null;

            try {
                reader = new FileReader(file);
            } catch (FileNotFoundException e) {

            }

            if (file.exists()) {
                try {
                    properties.load(reader);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (properties.getProperty("status").contains("voting")) {
                count.addAndGet(1);
            }
        }));
        return count.get();
    }

    public void save() {
        try {
            getProperties().save(new FileOutputStream(getSuggestFileUser()), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveSettings() {
        try {
            psettings.save(new FileOutputStream(suggestFileSettings), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void save(Properties p, File file) {
        try {
            p.store(new FileOutputStream(file), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void build() {
        FileReader reader = null;

        try {
            reader = new FileReader(suggestFileSettings);
        } catch (FileNotFoundException e) {

        }

        if (!suggestFile.getParentFile().exists()) {
            suggestFile.getParentFile().mkdirs();
        }

        if (!suggestFile.exists()) {
            suggestFile.mkdirs();
        }

        if (!suggestFileSettings.getParentFile().exists()) {
            suggestFileSettings.getParentFile().mkdirs();

        }

        if (suggestFileSettings.exists()) {
            try {
                psettings.load(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!suggestFileSettings.exists()) {
            try {

                suggestFileSettings.createNewFile();

                psettings.setProperty("suggestions", String.valueOf(0));
                psettings.setProperty("accepted-suggestions", String.valueOf(0));
                psettings.setProperty("denied-suggestions", String.valueOf(0));

                saveSettings();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void build(User user) {

        try {
            FileReader reader = null;

            try {
                reader = new FileReader(suggestFileUser);
            } catch (FileNotFoundException e) {

            }

            if (getSuggestFileUser().exists()) {
                getProperties().load(reader);
            }

            if (!suggestFileUser.getParentFile().exists()) {
                suggestFileUser.getParentFile().mkdirs();
            }

            if (!suggestFileUser.exists()) {
                suggestFileUser.createNewFile();
            }

            save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileReader reader = null;

        try {
            reader = new FileReader(suggestFileSettings);
        } catch (FileNotFoundException e) {

        }

        if (suggestFileSettings.exists()) {
            try {
                psettings.load(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!suggestFileSettings.getParentFile().exists()) {
            suggestFileSettings.getParentFile().mkdirs();
        }

        if (!suggestFileSettings.exists()) {
            try {

                suggestFileSettings.createNewFile();

                psettings.setProperty("suggestions", String.valueOf(0));
                psettings.setProperty("accepted-suggestions", String.valueOf(0));
                psettings.setProperty("denied-suggestions", String.valueOf(0));

                saveSettings();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

    }

    @Contract(pure = true)
    public int getSuggestions() {
        return Integer.parseInt(psettings.getProperty("suggestions"));
    }

    @Contract(pure = true)
    public int getAccepted() {
        return Integer.parseInt(psettings.getProperty("accepted-suggestions"));
    }

    @Contract(pure = true)
    public int getDenied() {
        return Integer.parseInt(psettings.getProperty("denied-suggestions"));
    }

    public void addSuggestion() {
        final AtomicInteger s = new AtomicInteger(Integer.parseInt(psettings.getProperty("suggestions")));

        psettings.setProperty("suggestions", String.valueOf(s.addAndGet(1)));
        saveSettings();
    }

    public void addAcceptedSuggestion() {
        final AtomicInteger atomicInteger = new AtomicInteger(Integer.parseInt(psettings.getProperty("accepted-suggestions")));
        int add = atomicInteger.addAndGet(1);

        psettings.setProperty("accepted-suggestions", String.valueOf(add));
        saveSettings();
    }

    public void addDeniedSuggestion() {
        final AtomicInteger atomicInteger = new AtomicInteger(Integer.parseInt(psettings.getProperty("denied-suggestions")));
        int add = atomicInteger.addAndGet(1);

        psettings.setProperty("denied-suggestions", String.valueOf(add));
        saveSettings();
    }

    public void delete() {
        if (suggestFileUser.exists()) {
            getSuggestFileUser().delete();
        }
    }

    @Contract(pure = true)
    public int getId() {
        return id;
    }

    public int generateSuggestId() {
        Random random = new Random();
        int nextrandom = random.nextInt(999999999);

        if (waitingList.contains(nextrandom))
            return random.nextInt(999999999);
        else
            return nextrandom;
    }
}
