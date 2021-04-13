package system;

import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import system.listener.*;
import system.objects.Utils.achievementsutils.Achievements;
import system.objects.Utils.achievementsutils.AchievementsManager;
import system.objects.Utils.administration.BannedUtils.BannedUtils;
import system.objects.Utils.profileconfigutils.ProfileBuilder;
import system.objects.Utils.suggestmanagement.SuggestManager;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

/*
    Created 8 / 11 / 2020
    Creator : OMAR
 */

public class Core {

    public static void main(String[] args) throws LoginException {
        WebUtils.setUserAgent("RyukoBot/5.0 RyukoBot JDA Discord bot/OMAR");

        CommandManager commandManager = new CommandManager();
        SuggestManager suggestManager = new SuggestManager();
        ProfileBuilder settings = new ProfileBuilder();
        BannedUtils bannedUtils = new BannedUtils();

        for (int achievements = 0; achievements < Achievements.values().length; achievements++) {
            Achievements achievement = Achievements.values()[achievements];
            AchievementsManager achievementsManager = new AchievementsManager(achievement.getAchievementKey());

            achievementsManager.setup(set -> {
                set.setProperty("key", achievement.getAchievementKey());
                set.setProperty("name", achievement.getAchievementName());
                set.setProperty("id", String.valueOf(achievement.getAchievementId()));
                set.setProperty("description", achievement.getAchievementDescription());
            });

            achievementsManager.createAchievements();
        }

        Logger LOGGER = LoggerFactory.getLogger(Core.class);

        // Jda builder
        LOGGER.info("Ryuko bot is ready");
        JDABuilder jdaBuilder = JDABuilder.create(Secret.TOKEN, GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.GUILD_PRESENCES, GatewayIntent.DIRECT_MESSAGE_REACTIONS, GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_INVITES, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.GUILD_MESSAGE_TYPING,
                GatewayIntent.GUILD_VOICE_STATES);
        jdaBuilder.disableCache(EnumSet.of(CacheFlag.EMOTE, CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS));

        jdaBuilder.enableCache(CacheFlag.VOICE_STATE, CacheFlag.ONLINE_STATUS);
        jdaBuilder.addEventListeners(
                new Events(commandManager), new AIEvents(), new SuggestEvent(), new HelpEvent(),
                new BannedEvent(), new AchievementsEvent());
        jdaBuilder.build();
    }
}
