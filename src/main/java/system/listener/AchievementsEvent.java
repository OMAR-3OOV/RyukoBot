package system.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import system.commands.informationCategory.AchievementsCommand;
import system.objects.TextUtils.MessageUtils;
import system.objects.Utils.LanguagesUtils.LanguagesManager;
import system.objects.Utils.LanguagesUtils.MessagesKeys;
import system.objects.Utils.achievementsutils.Achievements;
import system.objects.Utils.achievementsutils.AchievementsManager;
import system.objects.Utils.achievementsutils.AchievementsPages;
import system.objects.Utils.achievementsutils.Achievementypes;
import system.objects.Utils.profileconfigutils.ProfileBuilder;

import java.awt.*;
import java.text.DecimalFormat;

public class AchievementsEvent extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (AchievementsCommand.achievementPages.containsKey(event.getUser()) && AchievementsCommand.pagesMessage.get(event.getUser()).getChannel() == event.getChannel()) {

            AchievementsPages achievementsPages = AchievementsCommand.achievementPages.get(event.getUser());

            EmbedBuilder embed = new EmbedBuilder();
            StringBuilder description = embed.getDescriptionBuilder();
            embed.setColor(new Color(175, 141, 252));

            AchievementsManager achievementsManager = new AchievementsManager(event.getUser());
            LanguagesManager languagesManager = new LanguagesManager(event.getUser());
            ProfileBuilder profile = new ProfileBuilder(event.getUser(), event.getGuild());

            embed.setTitle(languagesManager.getMessage(MessagesKeys.ACHIEVEMENT_MESSAGE_TITLE));
            description.append(languagesManager.getMessage(MessagesKeys.ACHIEVEMENT_MESSAGE_DESCRIPTION)).append("\n \n");
            description.append(languagesManager.getMessage(MessagesKeys.ACHIEVEMENT_MESSAGE_NOTE)).append("\n \n");

            StringBuilder field = new StringBuilder();

            switch (event.getReactionEmote().getName()) {
                case "⬅️":
                    achievementsPages.backward();
                    AchievementsCommand.pageCooldown.put(event.getUser(), 0);
                    break;
                case "➡️":
                    achievementsPages.forward();
                    AchievementsCommand.pageCooldown.put(event.getUser(), 0);
                    break;
                case "⏪":
                    achievementsPages.backward();
                    achievementsPages.backward();
                    AchievementsCommand.pageCooldown.put(event.getUser(), 0);
                    break;
                case "⏩":
                    achievementsPages.forward();
                    achievementsPages.forward();
                    AchievementsCommand.pageCooldown.put(event.getUser(), 0);
                    break;
                case "⏹️":
                    AchievementsCommand.pagesMessage.get(event.getUser()).clearReactions().queue();

                    AchievementsCommand.achievementPages.remove(event.getUser());
                    AchievementsCommand.pagesMessage.remove(event.getUser());
                    break;
                default:
                    event.getReaction().removeReaction(event.getUser()).queue();
                    break;
            }

            embed.addField("", "```\n ```", false);

            achievementsPages.getAchievementsPages().subMap(achievementsPages.input_1(), achievementsPages.input_2()).forEach((number ,achievement) -> {
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

            if (AchievementsCommand.pagesMessage.containsKey(event.getUser())) {
                event.getReaction().removeReaction(event.getUser()).queue();
                AchievementsCommand.pagesMessage.get(event.getUser()).editMessage(embed.build()).queue();
            }
        }
    }

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        AchievementsManager achievementsManager = new AchievementsManager(event.getAuthor());

        if (event.getMessage().getContentRaw().contains("❤️❤️❤️")) {
            if (!achievementsManager.isCollected(Achievements.THREE_HEART_AT_ONCE)) {
                achievementsManager.giveAchievements(Achievements.THREE_HEART_AT_ONCE);

                achievementsManager.sendCollectedMessage(event.getAuthor(), Achievements.THREE_HEART_AT_ONCE);
            }
        }
    }
}
