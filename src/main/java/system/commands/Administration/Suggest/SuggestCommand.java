package system.commands.Administration.Suggest;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.objects.Category;
import system.objects.Command;
import system.objects.Utils.suggestmanagement.SuggestQuestions;
import system.objects.Utils.suggestmanagement.SuggestFunction;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

public class SuggestCommand implements Command {

    public static HashMap<User, SuggestFunction> suggestion = new HashMap<>();
    public static HashMap<User, Message> suggestionMessage = new HashMap<>();
    public static HashMap<User, Guild> suggestionFrom = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {
        if (!suggestion.containsKey(event.getAuthor())) {
            SuggestFunction suggest = new SuggestFunction(event.getAuthor());
            suggestion.put(event.getAuthor(), suggest);

            suggestion.get(event.getAuthor()).build();

            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(new Color(255, 134, 0));

            for (int i = 0; i < SuggestQuestions.values().length; i++) {
                embed.addField(SuggestQuestions.get(i).getQuestion(), "`-`", false);
            }

            embed.setFooter("cancel ❌ ");
            Message msg = event.getChannel().sendMessage(embed.build()).complete();

            suggestionMessage.put(event.getAuthor(), msg);
            suggestionFrom.put(event.getAuthor(), event.getGuild());

            msg.addReaction("❌").queue();
        }
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public String getInVoke() {
        return "suggest";
    }

    @Override
    public String getDescription() {
        return "suggest a new idea or a new command for our bot";
    }

    @Override
    public Permission getPermission() {
        return null;
    }

    @Override
    public Category getCategory() {
        return Category.MANAGEMENT;
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
