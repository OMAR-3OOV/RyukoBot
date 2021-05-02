package system.objects.Utils.LanguagesUtils;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import system.objects.TextUtils.MessageUtils;
import system.objects.Utils.achievementsutils.Achievements;
import system.objects.Utils.achievementsutils.Achievementypes;
import system.objects.Utils.profileconfigutils.ProfileBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

public class LanguagesManager {

    private ProfileBuilder profile;

    private final User user;
    private final File file;
    private final Properties properties;
    private final Languages language;

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
            e.printStackTrace();
        }

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

        message.set(new MessageUtils(new MessageHolder(new String(properties.getProperty(type.getName()).getBytes(fromCharset), toCharset), this.user, null).toHolder()).EmojisHolder());

        if (message.get() == null) {
            throw new IllegalArgumentException("message couldn't be null, key: " + type.getName());
        }

        return message.get();
    }

    public String getAchievementTypeDescription(Achievementypes type) {
        AtomicReference<String> message = new AtomicReference<>();

        final Charset fromCharset = StandardCharsets.ISO_8859_1;
        final Charset toCharset = StandardCharsets.UTF_8;

        message.set(new MessageUtils(new MessageHolder(new String(properties.getProperty(type.getDescription()).getBytes(fromCharset), toCharset), this.user, null).toHolder()).EmojisHolder());

        if (message.get() == null) {
            throw new IllegalArgumentException("message couldn't be null, key: " + type.getDescription());
        }

        return message.get();
    }
}
