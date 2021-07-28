package system.commands.Administration.moderatorCategory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.objects.Category;
import system.objects.Command;
import system.objects.TextUtils.MessageUtils;
import system.objects.Utils.suggestmanagement.SuggestFunction;

import java.io.FileNotFoundException;
import java.util.List;

public class SuggestModerator implements Command {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {
        try {
            SuggestFunction suggest = new SuggestFunction();
            EmbedBuilder embed = new EmbedBuilder();

            embed.setTitle("\uD83D\uDCC2 Suggest Moderator");

            embed.addField("```                                                    ```", "", false);
            embed.addField("\uD83D\uDD39 All Suggestions", "> there is **" + suggest.getSuggestManager().getSuggestions() + "** suggestions created", true);
            embed.addField(new MessageUtils("\uD83D\uDD39 Waiting Suggestions :loading:").EmojisHolder(), "> there is **" + suggest.getSuggestManager().getSuggestWaitingFiles() + "** Suggestions \uD83D\uDCD1", true);
            embed.addBlankField(true);
            embed.addField("\uD83D\uDD39 Accepted Suggestions", "> **" + suggest.getSuggestManager().getAccepted() + "** accepted", true);
            embed.addField("\uD83D\uDD39 Deny Suggestions", "> **" + suggest.getSuggestManager().getDenied() + "** denied", true);
            embed.addBlankField(true);
            embed.addField("```                                                    ```", "", false);

            suggest.getSuggestManager().addSuggestion();
            event.getChannel().sendMessage(embed.build()).queue();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "r!suggestcontrol";
    }

    @Override
    public String getInVoke() {
        return "suggestcontrol";
    }

    @Override
    public String getDescription() {
        return "View suggest moderator panel";
    }

    @Override
    public Permission getPermission() {
        return null;
    }

    @Override
    public Category getCategory() {
        return Category.MODERATOR;
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
    public Boolean displayCommand() {
        return false;
    }
}
