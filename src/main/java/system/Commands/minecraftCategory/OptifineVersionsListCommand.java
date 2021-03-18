package system.Commands.minecraftCategory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import system.Objects.Category;
import system.Objects.Command;
import system.Objects.TextUtils.MessageUtils;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class OptifineVersionsListCommand implements Command {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {
        try {
            String version = args.get(0);

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://nitroxenon-minecraft-forge-v1.p.rapidapi.com/optifine/versionlist")
                    .get()
                    .addHeader("x-rapidapi-key", "8146da115dmsh0c8bed994d6e3bfp19752fjsn12372dfb45a5")
                    .addHeader("x-rapidapi-host", "nitroxenon-minecraft-forge-v1.p.rapidapi.com")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                JSONArray array = new JSONArray(response.body().string());

                EmbedBuilder embed = new EmbedBuilder();

                embed.setColor(new Color(0, 193, 255));
                embed.setTitle("Optifine version " + version);

                for (int i = 0; i < array.length(); i++) {
                    if (version.equalsIgnoreCase(array.getJSONObject(i).getString("mcversion"))) {
                        embed.addField("Version: `" + array.getJSONObject(i).getString("mcversion") + "` | Type: `" + array.getJSONObject(i).getString("type") + "` | Patch: `" + array.getJSONObject(i).getString("patch") + "`"
                                , "> https://optifine.net/adloadx?f=" + array.getJSONObject(i).getString("filename").replace("preview_", ""), false);
                    }
                }

                if (embed.getFields().isEmpty()) {
                    event.getChannel().sendMessage(new MessageUtils(":error: | " + event.getAuthor().getAsMention() + ", I can't find this version!").EmojisHolder()).queue();
                    return;
                }

                event.getChannel().sendMessage(embed.build()).queue();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red.brighter());
            embed.setTitle("I can't find Version!  (ง •̀_•́)ง");
            embed.setDescription("Usage :" +
                    "\n > r!optifine <version>");

            event.getChannel().sendMessage(embed.build()).queue();
            e.printStackTrace();        }
    }

    @Override
    public String getHelp() {
        return "r!optifine <version>";
    }

    @Override
    public String getInVoke() {
        return "optifine";
    }

    @Override
    public String getDescription() {
        return "Get optifine version list";
    }

    @Override
    public Permission getPermission() {
        return null;
    }

    @Override
    public Category getCategory() {
        return Category.MINECRAFT;
    }

    @Override
    public Boolean Lockdown() {
        return false;
    }

    @Override
    public Boolean isNsfw() {
        return false;
    }

    @Override
    public Boolean diplayCommand() {
        return true;
    }
}
