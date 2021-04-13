package system.objects.Utils.administration.BannedUtils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import system.objects.Utils.profileconfigutils.ProfileBuilder;
import system.objects.Utils.RandomStringAPI;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class BannedUtils {

    private User user;
    private User by;
    private ProfileBuilder profile;

    private final File order = new File("system/Profiles/BanUser");
    private final File banFile;

    private final SortProperties properties;

    private RandomStringAPI rsa;

    public BannedUtils(User user, User by) {
        this.profile = new ProfileBuilder(user);
        this.user = user;
        this.rsa = new RandomStringAPI(user, 10);
        this.by = by;
        this.banFile = new File("system/Profiles/BanUser/" + rsa.getKey() + ".properties");
        this.properties = new SortProperties();

        if (this.banFile.exists()) {
            try {
                properties.load(new FileInputStream(this.banFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public BannedUtils(User user, String ticket) {
        this.profile = new ProfileBuilder(user);
        this.user = user;
        this.banFile = new File("system/Profiles/BanUser/" + ticket + ".properties");
        this.properties = new SortProperties();
    }

    public BannedUtils(User user) {
        this.profile = new ProfileBuilder(user);
        String ticket = profile.getProfileProperties().getProperty("banned-ticket");
        this.user = user;
        this.banFile = new File("system/Profiles/BanUser/" + ticket + ".properties");
        this.properties = new SortProperties();

        if (this.banFile.exists()) {
            try {
                properties.load(new FileInputStream(this.banFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public BannedUtils() {
        this.banFile = new File("system/Profiles/BanUser");
        this.properties = new SortProperties();

        if (banFile.listFiles().length > 0) {
            Arrays.stream(banFile.listFiles()).forEach(f -> {
                try {
                    properties.load(new FileInputStream(f));
                } catch (IOException e) {

                }
            });
        }
    }

    public User getUser() {
        return user;
    }

    public ProfileBuilder getProfile() {
        return profile;
    }

    public RandomStringAPI getRsa() {
        return rsa;
    }

    public File getBanFile() {
        return banFile;
    }

    public File getOrder() {
        return order;
    }

    public Properties getProperties() {
        return properties;
    }

    public void build(Date date, Date time, String ticketId, String reason) {
        if (this.user.isBot() || this.user.isFake()) return;

        if (!this.banFile.getParentFile().exists()) {
            this.banFile.getParentFile().mkdirs();
        }

        if (this.banFile.exists()) {
            try {
                properties.load(new FileInputStream(this.banFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!this.banFile.exists()) {
            try {
                this.banFile.createNewFile();

                PrintWriter p = new PrintWriter(this.banFile);
                properties.put("user-id", this.user.getId());
                properties.put("time-start", String.valueOf(date.getTime()));
                properties.put("time-end", String.valueOf(time.getTime()));
                properties.put("banned-ticket", ticketId);
                properties.put("banned-by", this.by.getId());
                properties.put("status", String.valueOf(true));
                properties.put("reason", reason);

                getProfile().setBanned(true);
                save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save() {
        try {
            try (FileOutputStream fileOutputStream = new FileOutputStream(banFile)) {
                properties.save(fileOutputStream, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * banned user
     *
     * @param target       to user wanted
     * @param amount       to get the time amount
     * @param elapsedTimes to calculate times
     */
    public void setBannedWithTime(User target, int amount, BannedElapsedTimes elapsedTimes, String reason) {
        target.openPrivateChannel().queue(t -> {
            EmbedBuilder embed = new EmbedBuilder();
            StringBuilder builder = embed.getDescriptionBuilder();

            Date date = new Date();
            SimpleDateFormat f = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss a");
            long elapsed = (date.getTime() + elapsedTimes.getTime() * amount);

            builder.append("**Expiration date : **").append(printTimeLeft(date, new Date(elapsed))).append("\n");
            builder.append("**Your Banned Ticket: **").append("`").append(this.getRsa().getKey()).append("`").append("\n");
            builder.append("**Banned by: **").append("Console system").append("\n \n");
            builder.append("**You'll get unbanned till: **").append(f.format(new Date(elapsed)));
            t.sendMessage(embed.build()).queue();
            this.build(date, new Date(elapsed), this.getRsa().getKey(),  reason);
        });
    }

    /**
     * Created on 2021 - 14 - 3
     * Created by : OMAR
     *
     * @param dateStart the first time for function you use
     * @param dateEnd   the future time for your function
     * @return to time left String
     */
    public String printTimeLeft(Date dateStart, Date dateEnd) {

        StringBuilder builder = new StringBuilder();
        long different = dateEnd.getTime() - dateStart.getTime();

        long seconds = 1000;
        long minutes = seconds * 60;
        long hours = minutes * 60;
        long days = hours * 24;
        long weeks = days * 7;
        long months = (long) (days * 30);
        long years = days * 365;

        long elapsedY = different / years;

        different = different % years;
        long elapsedM = different / months;

        different = different % months;
        long elapsedW = different / weeks;

        different = different % weeks;
        long elapsedD = different / days;

        different = different % days;
        long elapsedH = different / hours;

        different = different % hours;
        long elapsedMIN = different / minutes;

        different = different % minutes;
        long elapsedS = different / seconds;

        // System.out.println(elapsedY + " years," + elapsedM + " month, " + elapsedM + " weeks, " + elapsedD + "  days, " + elapsedH + " hours");

        builder.append(elapsedY > 0 ? elapsedY + " year" + (elapsedY > 1 ? "s " : " ") : " ");
        builder.append(elapsedM > 0 ? elapsedM + " month" + (elapsedM > 1 ? "s " : " ") : " ");
        builder.append(elapsedW > 0 ? elapsedW + " week" + (elapsedW > 1 ? "s " : " ") : " ");
        builder.append(elapsedD > 0 ? elapsedD + " day" + (elapsedD > 1 ? "s " : " ") : " ");
        builder.append(elapsedH > 0 ? elapsedH + " hour" + (elapsedH > 1 ? "s " : " ") : " ");
        builder.append(elapsedMIN > 0 ? elapsedMIN + " minute" + (elapsedMIN > 1 ? "s " : " ") : " ");
        builder.append(elapsedS > 0 ? elapsedS + " second" + (elapsedS > 1 ? "s " : " ") : " ");

        return builder.toString();
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