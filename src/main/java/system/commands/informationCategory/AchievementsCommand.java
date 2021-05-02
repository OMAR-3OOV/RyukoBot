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
import system.objects.Utils.LanguagesUtils.LanguagesManager;
import system.objects.Utils.LanguagesUtils.MessagesKeys;
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

            LanguagesManager languagesManager = new LanguagesManager(event.getAuthor());
            EmbedBuilder embed = new EmbedBuilder();
            StringBuilder description = embed.getDescriptionBuilder();
            embed.setColor(new Color(175, 141, 252));

            if (handlers.isEmpty() && !achievementPages.containsKey(event.getAuthor())) {
                AchievementsManager achievementsManager = new AchievementsManager(event.getAuthor());
                AchievementsPages achievementsPages = new AchievementsPages(event.getAuthor());
                ProfileBuilder profile = new ProfileBuilder(event.getAuthor(), event.getGuild());

                embed.setTitle(languagesManager.getMessage(MessagesKeys.ACHIEVEMENT_MESSAGE_TITLE));
                description.append(languagesManager.getMessage(MessagesKeys.ACHIEVEMENT_MESSAGE_DESCRIPTION)).append("\n \n");
                description.append(languagesManager.getMessage(MessagesKeys.ACHIEVEMENT_MESSAGE_NOTE)).append("\n \n");

                StringBuilder field = new StringBuilder();

                embed.addField("", "```\n ```", false);

                achievementsPages.getAchievementsPages().subMap(0, 4).forEach((number, achievement) -> {
                    double collectedPercentage = (achievementsManager.getCollectedPercentage(achievement.getAchievementKey()) / profile.getUsers()) * 100;
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");

                    if (achievement.getType() == Achievementypes.SECRETY) {
                        field.append("**" + new MessageUtils(languagesManager.getAchievementTypeName(achievement.getType()) + " :achievement: " + (achievementsManager.isCollected(achievement) ? ":successful:" : "")).EmojisHolder() + "**").append("\n??? `" + decimalFormat.format(collectedPercentage) + "% collected`").append("\n \n");
                    } else {
                        field.append("**" + new MessageUtils(languagesManager.getAchievementName(achievement) + " :achievement: " + (achievementsManager.isCollected(achievement) ? ":successful:" : "")).EmojisHolder() + "**").append("\n" + languagesManager.getAchievementDescription(achievement) + " `" + decimalFormat.format(collectedPercentage) + "% collected`").append("\n \n");
                    }
                });

                embed.addField(" ", field.toString(), false);
                embed.addField("", "```\n ```", false);

                /* stats */

                StringBuilder stats = new StringBuilder();
                stats.append(languagesManager.getMessage(MessagesKeys.ACHIEVEMENT_TOTAL_ACHIEVEMENTS_STRING)).append(achievementsManager.getAchievements().size()).append("\n");
                stats.append(languagesManager.getMessage(MessagesKeys.ACHIEVEMENT_GLOBAL_ACHIEVEMENTS_STRING)).append(achievementsManager.getAchievements().stream().filter(m -> m.getType()==Achievementypes.GLOBAL).count()).append("\n");
                stats.append(languagesManager.getMessage(MessagesKeys.ACHIEVEMENT_SECRET_ACHIEVEMENTS_STRING)).append(achievementsManager.getAchievements().stream().filter(map -> map.getType()==Achievementypes.SECRETY).count()).append("\n");

                embed.addField(languagesManager.getMessage(MessagesKeys.ACHIEVEMENT_STATS) , stats.toString(), false);
                embed.setFooter("( " + (achievementsPages.input_1() + 1) + " / " + (achievementsPages.input_2() + 1) + " ) " + languagesManager.getMessage(MessagesKeys.ACHIEVEMENT_MESSAGE_FOOTER));

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
