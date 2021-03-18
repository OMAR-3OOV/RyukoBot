package system;

import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import system.Listener.*;
import system.Objects.Utils.Administration.BannedUtils.BannedUtils;
import system.Objects.Utils.ProfileConfigUtils.ProfileBuilder;
import system.Objects.Utils.SuggestManagement.SuggestManager;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

/*
    Created 8 / 11 / 2020
    Creator : OMAR
    Bot name: Ryuko
 */

public class Core {

    public static void main(String[] args) throws LoginException {
        WebUtils.setUserAgent("RyukoBot/5.0 RyukoBot JDA Discord bot/OMAR");

        CommandManager commandManager = new CommandManager();
        SuggestManager suggestManager = new SuggestManager();
        ProfileBuilder settings = new ProfileBuilder();
        BannedUtils bannedUtils = new BannedUtils();

        Logger LOGGER = LoggerFactory.getLogger(Core.class);

        // Jda builder
        LOGGER.info("Ryuko bot is ready");
        JDABuilder jdaBuilder = JDABuilder.create(
                Secret.TOKEN,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_EMOJIS,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.GUILD_INVITES,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.GUILD_MESSAGE_TYPING,
                GatewayIntent.GUILD_VOICE_STATES);
        jdaBuilder.disableCache(EnumSet.of(
                CacheFlag.EMOTE,
                CacheFlag.ACTIVITY,
                CacheFlag.CLIENT_STATUS
        ));

        jdaBuilder.enableCache(CacheFlag.VOICE_STATE);
        jdaBuilder.addEventListeners(new Events(commandManager), new AIEvents(), new SuggestEvent(), new HelpEvent(), new BannedEvent());
        jdaBuilder.build();
    }
}
