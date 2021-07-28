package system.objects.Utils.profileconfigutils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import system.objects.Utils.guildconfigutils.GuildsBuilder;

public class animeFinderUtil extends GuildsBuilder {

    private final Guild guild;
    private final TextChannel channel;

    public animeFinderUtil(Guild guild, TextChannel channel) {
        super(guild);

        this.guild = guild;
        this.channel = channel;
    }

    public void setup() {
        if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null!");
        }

        try {
            getProperties().setProperty("animeFinder", channel.getId());
            save();
        } catch (Exception error) {
            System.out.println(error.getMessage());
        }
    }

    public void clear() {
        if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null!");
        }

        try {
            getProperties().remove("animeFinder");
            save();
        } catch (Exception error) {
        }
    }

    public boolean has() {
        if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null!");
        }

        try {

            if (getProperties().getProperty("animeFinder").equals(channel.getId())) {
                return true;
            } else return false;

        } catch (Exception error) {
            return false;
        }
    }
}
