package system.Listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import system.commands.informationCategory.AchievementsCommand;
import system.Objects.TextUtils.MessageUtils;
import system.Objects.Utils.achievementsutils.AchievementsManager;
import system.Objects.Utils.achievementsutils.AchievementsPages;
import system.Objects.Utils.achievementsutils.Achievementypes;
import system.Objects.Utils.profileconfigutils.ProfileBuilder;

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
            ProfileBuilder profile = new ProfileBuilder(event.getUser());

            embed.setTitle(new MessageUtils("Your achievements :achievement:").EmojisHolder());
            description.append("> You can know how to collect the achievement by look below the achievement!").append("\n \n");
            description.append("```diff\n- NOTE : The secret message can display in Direct message if you collected only!```").append("\n \n");
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

            achievementsPages.getAchievementsPages().subMap(achievementsPages.input_1(), achievementsPages.input_2()).forEach((number ,achievement) -> {
                double collectedPercentage = (achievementsManager.getCollectedPercentage(achievement.getAchievementKey()) / profile.getUsers()) * 100;
                DecimalFormat decimalFormat = new DecimalFormat("0.00");

                if (achievement.getType() == Achievementypes.SECRETY) {
                    field.append("**" + new MessageUtils("Secret :achievement: " + (achievementsManager.isCollected(achievement) ? ":successful:" : "")).EmojisHolder() + "**").append("\n??? `" + decimalFormat.format(collectedPercentage) + "% collected`").append("\n \n");
                } else {
                    field.append("**" + new MessageUtils(achievement.getAchievementName() + " :achievement: " + (achievementsManager.isCollected(achievement) ? ":successful:" : "")).EmojisHolder() + "**").append("\n" + achievement.getAchievementDescription() + " `" + decimalFormat.format(collectedPercentage) + "% collected`").append("\n \n");
                }
            });

            embed.addField(" ", field.toString(), false);
            embed.setFooter("( " + (achievementsPages.input_1()+1) + " / " + (achievementsPages.input_2()+1) + " ) | Scroll ⬅️ BackWard & Forward ➡️");

            if (AchievementsCommand.pagesMessage.containsKey(event.getUser())) {
                event.getReaction().removeReaction(event.getUser()).queue();
                AchievementsCommand.pagesMessage.get(event.getUser()).editMessage(embed.build()).queue();
            }
        }
    }
}
