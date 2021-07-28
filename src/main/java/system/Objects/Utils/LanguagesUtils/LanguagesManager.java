package system.objects.Utils.LanguagesUtils;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import system.objects.TextUtils.MessageUtils;
import system.objects.Utils.achievementsutils.Achievements;
import system.objects.Utils.achievementsutils.Achievementypes;
import system.objects.Utils.profileconfigutils.ProfileBuilder;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class LanguagesManager {

    private ProfileBuilder profile;

    private User user;
    private final File file;
    private final Properties properties;
    private final Languages language;

    private final File defaultFileLanguage = new File("System/Languages/en.properties");

    public LanguagesManager (User user) {
        this.user = user;
        this.profile = new ProfileBuilder(user);

        /* Call the language from user data */
        this.language = profile.getLanguage();

        this.file = new File("System/Languages/" + language.getCode() + ".properties");
        this.properties = new Properties();

        try {
            properties.load(new FileInputStream(this.file));
        } catch (IOException e) {
            try {
                properties.load(new FileInputStream(defaultFileLanguage.getPath()));
                this.user.openPrivateChannel().queue(msg -> {
                    msg.sendMessage(this.language + " is under maintenance!, for now the main language gonna display only").queue();
                }, (ignore) -> {});
                return;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            e.printStackTrace();
        }

    }

    public LanguagesManager () {
        this.language = Languages.ENGLISH;
        this.file = new File("System/Languages/" + language.getCode() + ".properties");
        this.properties = new Properties();
    }

    public User getUser() {
        return user;
    }

    public File getFile() {
        return file;
    }

    public Properties getProperties() {
        return properties;
    }

    public Languages getLanguage() {
        return language;
    }

    public String getMessage(MessagesKeys messageKey, User user, TextChannel channel) {
        AtomicReference<String> message = new AtomicReference<>(messageKey.getMessageKey());

        final Charset fromCharset = StandardCharsets.ISO_8859_1;
        final Charset toCharset = StandardCharsets.UTF_8;

        message.set(new MessageUtils(new MessageHolder(new String(properties.getProperty(messageKey.getMessageKey()).getBytes(fromCharset), toCharset), user, channel).toHolder()).EmojisHolder());

        if (message.get() == null) {
            throw new IllegalArgumentException("message couldn't be null, key: " + messageKey);
        }

        return message.get();
    }

    public String getMessage(MessagesKeys messageKeys) {
        AtomicReference<String> message = new AtomicReference<>(messageKeys.getMessageKey());

        final Charset fromCharset = StandardCharsets.ISO_8859_1;
        final Charset toCharset = StandardCharsets.UTF_8;

        message.set(new MessageUtils(new MessageHolder(new String(properties.getProperty(messageKeys.getMessageKey()).getBytes(fromCharset), toCharset), this.user, null).toHolder()).EmojisHolder());

        if (message.get() == null) {
            throw new IllegalArgumentException("message couldn't be null, key: " + messageKeys);
        }

        return message.get();
    }


    public String getAchievementName(Achievements achievements) {
        AtomicReference<String> message = new AtomicReference<>();

        final Charset fromCharset = StandardCharsets.ISO_8859_1;
        final Charset toCharset = StandardCharsets.UTF_8;

        if (properties.getProperty(achievements.getAchievementName()) == null) {
            return "None text";
        }

        message.set(new MessageUtils(new MessageHolder(new String(properties.getProperty(achievements.getAchievementName()).getBytes(fromCharset), toCharset), this.user, null).toHolder()).EmojisHolder());

        if (message.get() == null) {
            throw new IllegalArgumentException("message couldn't be null, key: " + achievements.getAchievementName());
        }

        return message.get();
    }

    public String getAchievementDescription(Achievements achievements) {
        AtomicReference<String> message = new AtomicReference<>();

        final Charset fromCharset = StandardCharsets.ISO_8859_1;
        final Charset toCharset = StandardCharsets.UTF_8;

        if (properties.getProperty(achievements.getAchievementDescription()) == null) {
            return "None text";
        }

        message.set(new MessageUtils(new MessageHolder(new String(properties.getProperty(achievements.getAchievementDescription()).getBytes(fromCharset), toCharset), this.user, null).toHolder()).EmojisHolder());

        if (message.get() == null) {
            throw new IllegalArgumentException("message couldn't be null, key: " + achievements.getAchievementDescription());
        }

        return message.get();
    }

    public String getAchievementTypeName(Achievementypes type) {
        AtomicReference<String> message = new AtomicReference<>();

        final Charset fromCharset = StandardCharsets.ISO_8859_1;
        final Charset toCharset = StandardCharsets.UTF_8;

        if (properties.getProperty(type.getName()) == null || properties.getProperty(type.getName()).isEmpty()) {
            return "None text";
        }

        message.set(new MessageUtils(new MessageHolder(new String(properties.getProperty(type.getName()).getBytes(fromCharset), toCharset), this.user, null).toHolder()).EmojisHolder());

        if (message.get() == null) {
            throw new IllegalArgumentException("message couldn't be null, key: " + type.getKey());
        }

        return message.get();
    }

    public String getAchievementTypeDescription(Achievementypes type) {
        AtomicReference<String> message = new AtomicReference<>();

        final Charset fromCharset = StandardCharsets.ISO_8859_1;
        final Charset toCharset = StandardCharsets.UTF_8;

        if (properties.getProperty(type.getDescription()) == null || properties.getProperty(type.getDescription()).isEmpty()) {
            return "None text";
        }

        message.set(new MessageUtils(new MessageHolder(new String(properties.getProperty(type.getDescription()).getBytes(fromCharset), toCharset), this.user, null).toHolder()).EmojisHolder());

        if (message.get() == null) {
            throw new IllegalArgumentException("message couldn't be null, key: " + type.getKey());
        }

        return message.get();
    }

    public void BuildFiles() {
        if (!this.file.getParentFile().exists()) {
            this.file.getParentFile().mkdirs();
        }

        if (this.file.getParentFile().exists() && this.file.exists()) {
            try {
                properties.load(new FileInputStream(this.file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!this.file.exists()) {
            try {
                if (this.file.createNewFile()) {
                    Arrays.stream(MessagesKeys.values()).forEach((key) -> {
                        properties.setProperty(key.getMessageKey(), key.getEn_message());
                    });

                    save();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save() {
        try {
            properties.save(new FileOutputStream(this.file), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
