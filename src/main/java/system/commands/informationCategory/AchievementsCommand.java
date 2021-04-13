package system.commands.informationCategory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.Contract;
import system.objects.Category;
import system.objects.Command;
import system.objects.TextUtils.MessageUtils;
import system.objects.Utils.achievementsutils.AchievementsManager;
import system.objects.Utils.achievementsutils.AchievementsPages;
import system.objects.Utils.achievementsutils.Achievementypes;
import system.objects.Utils.profileconfigutils.ProfileBuilder;

import java.awt.*;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class AchievementsCommand implements Command {

    public static HashMap<User, AchievementsPages> achievementPages = new HashMap<>();
    public static HashMap<User, Message> pagesMessage = new HashMap<>();
    public static HashMap<User, Integer> pageCooldown = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {
        try {
            List<String> handlers = new ArrayList<>(args);

            EmbedBuilder embed = new EmbedBuilder();
            StringBuilder description = embed.getDescriptionBuilder();
            embed.setColor(new Color(175, 141, 252));

            if (handlers.isEmpty() && !achievementPages.containsKey(event.getAuthor())) {
                AchievementsManager achievementsManager = new AchievementsManager(event.getAuthor());
                AchievementsPages achievementsPages = new AchievementsPages(event.getAuthor());
                ProfileBuilder profile = new ProfileBuilder(event.getAuthor());

                embed.setTitle(new MessageUtils("Your achievements :achievement:").EmojisHolder());
                description.append("> You can know how to collect the achievement by look below the achievement!").append("\n \n");
                description.append("```diff\n- NOTE : The secret message can display in Direct message if you collected only!```").append("\n \n");

                StringBuilder field = new StringBuilder();

                achievementsPages.getAchievementsPages().subMap(0, 6).forEach((number, achievement) -> {
                    double collectedPercentage = (achievementsManager.getCollectedPercentage(achievement.getAchievementKey()) / profile.getUsers()) * 100;
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");

                    if (achievement.getType() == Achievementypes.SECRETY) {
                        field.append("**" + new MessageUtils("Secret :achievement: " + (achievementsManager.isCollected(achievement) ? ":successful:" : "")).EmojisHolder() + "**").append("\n??? `" + decimalFormat.format(collectedPercentage) + "% collected`").append("\n \n");
                    } else {
                        field.append("**" + new MessageUtils(achievement.getAchievementName() + " :achievement: " + (achievementsManager.isCollected(achievement) ? ":successful:" : "")).EmojisHolder() + "**").append("\n" + achievement.getAchievementDescription() + " `" + decimalFormat.format(collectedPercentage) + "% collected`").append("\n \n");
                    }
                });

                embed.addField(" ", field.toString(), false);
                embed.setFooter("( " + (achievementsPages.input_1() + 1) + " / " + (achievementsPages.input_2() + 1) + " ) | Scroll ⬅️ BackWard & Forward ➡️");

                Message message = event.getChannel().sendMessage(embed.build()).complete();

                message.addReaction("⏪").queue();
                message.addReaction("⬅️").queue();
                message.addReaction("⏹️").queue();
                message.addReaction("➡️").queue();
                message.addReaction("⏩").queue();

                achievementPages.put(event.getAuthor(), achievementsPages);
                pagesMessage.put(event.getAuthor(), message);
                pageCooldown.put(event.getAuthor(), 0);
                event.getMessage().delete().queue();

                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        pageCooldown.entrySet().forEach((k) -> {
                            if (k.getKey().getId().contains(event.getAuthor().getId())) {
                                if (!achievementPages.containsKey(event.getAuthor())) this.cancel();
                                k.setValue(pageCooldown.get(k.getKey()) + 1);
                            }
                        });
                    }
                }, 0, 1000);

            } else {
                event.getMessage().delete().queue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "r!achievements";
    }

    @Override
    public String getInVoke() {
        return "achievements";
    }

    @Override
    public String getDescription() {
        return "Check the achievements that you collected, `Note: there is a secret achievements that doesn't display how to collected!`";
    }

    @Override
    public Permission getPermission() {
        return null;
    }

    @Override
    public Category getCategory() {
        return Category.INFORMATION;
    }

    @Override
    public Boolean Lockdown() {
        return true;
    }

    @Override
    public Boolean isNsfw() {
        return false;
    }

    @Override
    public Boolean diplayCommand() {
        return true;
    }

    @Contract(pure = true)
    public static void endPageTask(User user) {
        achievementPages.remove(user);
        pagesMessage.remove(user);
    }
}
