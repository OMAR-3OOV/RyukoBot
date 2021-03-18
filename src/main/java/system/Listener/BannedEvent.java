package system.Listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import system.Objects.Utils.Administration.BannedUtils.BannedUtils;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

public class BannedEvent extends ListenerAdapter {

    public HashMap<String ,File> expiredFiles = new HashMap<>();

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Date date = new Date();
                BannedUtils bannedUtils = new BannedUtils();

                Arrays.stream(bannedUtils.getBanFile().listFiles()).filter(f -> bannedUtils.getProperties().getProperty("user-id") != null).forEach(f -> {
                    if (!bannedUtils.getProperties().getProperty("user-id").isEmpty()) {
                        User user = event.getJDA().getUserById(Long.parseLong(bannedUtils.getProperties().getProperty("user-id")));

                        BannedUtils userBan = new BannedUtils(user, bannedUtils.getProperties().getProperty("banned-ticket"));
                        User bannedFrom = event.getJDA().getUserById(bannedUtils.getProperties().getProperty("banned-by"));

                        if (userBan.getProfile().getBanned()) {
                            if (date.getTime() >= Long.parseLong(bannedUtils.getProperties().getProperty("time-end"))) {

                                user.openPrivateChannel().queue(u -> {
                                    EmbedBuilder embed = new EmbedBuilder();
                                    StringBuilder builder = embed.getDescriptionBuilder();

                                    embed.setColor(new Color(7, 255, 119));
                                    embed.setTitle("Banned expired!");
                                    builder.append("**Your banned has been expired**").append("\n \n");
                                    builder.append("**Banned ticket: **").append(bannedUtils.getProperties().getProperty("banned-ticket")).append("\n");
                                    builder.append("**You banned date was in: **").append(new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss a").format(date));

                                    embed.setFooter("Banned was by: " + event.getJDA().getUserById(bannedUtils.getProperties().getProperty("banned-by")).getName());
                                    userBan.getProfile().getProfileProperties().remove("banned-ticket");

                                    u.sendMessage(embed.build()).queue();
                                });

                                if (!bannedFrom.isBot()) {
                                    bannedFrom.openPrivateChannel().queue(u -> {
                                        EmbedBuilder embed = new EmbedBuilder();
                                        StringBuilder builder = embed.getDescriptionBuilder();

                                        embed.setColor(new Color(7, 255, 119));
                                        embed.setTitle(user.getName() + " has been removed from blacklist!");
                                        builder.append("**User has expired the banned that you punch by you**").append("\n \n");
                                        builder.append("**Banned ticket: **").append(bannedUtils.getProperties().getProperty("banned-ticket")).append("\n");
                                        builder.append("**You banned date was in: **").append(new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss a").format(date));

                                        embed.setFooter("Banned was by: " + event.getJDA().getUserById(bannedUtils.getProperties().getProperty("banned-by")).getName());

                                        u.sendMessage(embed.build()).queue();
                                    });
                                }

                                userBan.getProfile().setBanned(false);
                                userBan.getProperties().setProperty("status", String.valueOf(false));
                                userBan.save();

                                if (userBan.getBanFile().delete()) {
                                    System.out.println(1);
                                }
                            }
                        }
                    } else {
                        if (f.delete()) {
                            System.out.println(1);
                        }
                    }
                });
            }
        }, 0, (1000 * 3));
    }
}
