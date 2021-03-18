package system.Listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import system.Commands.Administration.Suggest.SuggestCommand;
import system.Objects.TextUtils.MessageUtils;
import system.Objects.Utils.SuggestManagement.SuggestFunction;
import system.Objects.Utils.SuggestManagement.SuggestQuestions;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SuggestEvent extends ListenerAdapter {

    public static HashMap<User, SuggestFunction> Submit = new HashMap<>();

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (SuggestCommand.suggestion.containsKey(event.getAuthor())) {
            if (SuggestCommand.suggestionFrom.get(event.getAuthor()) == event.getGuild()) {

                SuggestFunction suggest = SuggestCommand.suggestion.get(event.getAuthor());
                String answer = event.getMessage().getContentRaw();

                EmbedBuilder embed = new EmbedBuilder();

                embed.setColor(new Color(255, 134, 0));

                if (suggest.isStarted()) {
                    if (suggest.getSuggestList().size() != 3) {
                        suggest.addQusetion(answer);
                        suggest.next();
                        event.getMessage().delete().queue();
                    } else if (Submit.containsKey(event.getAuthor())) {
                        suggest.setQusetion(suggest.getQuestionCount(), answer);
                        event.getMessage().delete().queue();
                    }
                }

                for (int i = 0; i < SuggestQuestions.values().length; i++) {
                    embed.addField(suggest.isQuestion(i) ? new MessageUtils(SuggestQuestions.get(i).getQuestion() + ":loading:").EmojisHolder() : SuggestQuestions.get(i).getQuestion(), (hasIndex(suggest.getSuggestList(), i) ? "> " + suggest.getSuggestList().get(i) : "| `-`"), false);
                }

                embed.setFooter("cancel ❌ ");
                SuggestCommand.suggestionMessage.get(event.getAuthor()).editMessage(embed.build()).queue();

                if (suggest.getSuggestList().size() >= 3) {

                    Submit.put(event.getAuthor(), suggest);
                    SuggestCommand.suggestionMessage.get(event.getAuthor()).clearReactions().completeAfter(1, TimeUnit.SECONDS);

                    embed.setDescription("> **You're able to change any answer you want by write it again and move by each question** ❗");
                    embed.setFooter("next ⬇️ | back ⬆️ | cancel ❌ | Submit ⬇️");

                    SuggestCommand.suggestionMessage.get(event.getAuthor()).addReaction("⬆").queue();
                    SuggestCommand.suggestionMessage.get(event.getAuthor()).addReaction("⬇").queue();
                    SuggestCommand.suggestionMessage.get(event.getAuthor()).addReaction("❌").queue();
                    SuggestCommand.suggestionMessage.get(event.getAuthor()).addReaction("⬇️").queue();

                    SuggestCommand.suggestionMessage.get(event.getAuthor()).editMessage(embed.build()).queue();
                    return;
                }

                if (suggest.isStarted() == false) {
                    suggest.setStarted(true);
                }
            }
        }
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        if (SuggestCommand.suggestion.containsKey(event.getUser())) {
            if (SuggestCommand.suggestionFrom.get(event.getUser()) == event.getGuild()) {

                SuggestFunction suggest = SuggestCommand.suggestion.get(event.getUser());
                EmbedBuilder embed = new EmbedBuilder();

                embed.setColor(new Color(255, 134, 0));
                for (int i = 0; i < SuggestQuestions.values().length; i++) {
                    embed.addField(suggest.isQuestion(i) ? new MessageUtils(SuggestQuestions.get(i).getQuestion() + ":loading:").EmojisHolder() : SuggestQuestions.get(i).getQuestion(), (hasIndex(suggest.getSuggestList(), i) ? "> " + suggest.getSuggestList().get(i) : "| `-`"), false);
                }

                switch (event.getReactionEmote().getName()) {
                    case "⬆":
                        if (Submit.containsKey(event.getUser())) {

                            suggest.back();
                            System.out.println(suggest.getQuestionCount());

                            embed.clearFields();
                            for (int i = 0; i < SuggestQuestions.values().length; i++) {
                                embed.addField(suggest.isQuestion(i) ? new MessageUtils(SuggestQuestions.get(i).getQuestion() + ":loading:").EmojisHolder() : SuggestQuestions.get(i).getQuestion(), (hasIndex(suggest.getSuggestList(), i) ? "> " + suggest.getSuggestList().get(i) : "| `-`"), false);
                            }
                            embed.setFooter("next ⬇️ | back ⬆️ | cancel ❌ | Submit ⬇️");

                            SuggestCommand.suggestionMessage.get(event.getUser()).editMessage(embed.build()).queue();
                        }
                        break;
                    case "⬇":
                        if (Submit.containsKey(event.getUser())) {

                            suggest.next();
                            System.out.println(suggest.getQuestionCount());

                            embed.clearFields();
                            for (int i = 0; i < SuggestQuestions.values().length; i++) {
                                embed.addField(suggest.isQuestion(i) ? new MessageUtils(SuggestQuestions.get(i).getQuestion() + ":loading:").EmojisHolder() : SuggestQuestions.get(i).getQuestion(), (hasIndex(suggest.getSuggestList(), i) ? "> " + suggest.getSuggestList().get(i) : "| `-`"), false);
                            }
                            embed.setFooter("next ⬇️ | back ⬆️ | cancel ❌ | Submit ⬇️");

                            SuggestCommand.suggestionMessage.get(event.getUser()).editMessage(embed.build()).queue();
                        }
                        break;
                    case "⬇️":
                        if (hasIndex(suggest.getSuggestList(), 0) && hasIndex(suggest.getSuggestList(), 1) && hasIndex(suggest.getSuggestList(), 2)) {
                            if (Submit.containsKey(event.getUser())) {
                                suggest.done();

                                embed.setTitle("\uD83D\uDCDC Your suggest has been submit");
                                embed.setColor(new Color(0, 255, 123));
                                embed.setDescription(
                                        "> **Your Ticket ID \uD83D\uDCE9 :** " + suggest.getSoted().getProperty("ticket-id") + "\n" +
                                                "> **Date \uD83D\uDCC6 :** " + new SimpleDateFormat("EEEE, dd MMMM YYYY").format(Long.valueOf(suggest.getSoted().getProperty("date")))
                                );

                                embed.clearFields();
                                for (int i = 0; i < SuggestQuestions.values().length; i++) {
                                    embed.addField(SuggestQuestions.get(i).getQuestion(), "> " + suggest.getSoted().getProperty(String.valueOf(i)), false);
                                }

                                embed.setFooter("Request by : " + suggest.getUser().getName()+"#"+suggest.getUser().getDiscriminator());

                                SuggestCommand.suggestionMessage.get(event.getUser()).clearReactions().queue();
                                SuggestCommand.suggestionMessage.get(event.getUser()).editMessage(embed.build()).queue();

                                SuggestCommand.suggestion.remove(event.getUser());
                                SuggestCommand.suggestionFrom.remove(event.getUser());
                                SuggestCommand.suggestionMessage.remove(event.getUser());
                            }
                        } else {
                            event.getChannel().sendMessage(new MessageUtils(":error: | You need to answer all questions!").EmojisHolder()).completeAfter(6, TimeUnit.SECONDS).delete().queue();
                        }

                        break;
                    case "❌":
                        event.getChannel().sendMessage("Suggest request has been closed").queue();
                        embed.setColor(new Color(255, 0, 0));
                        SuggestCommand.suggestionMessage.get(event.getUser()).delete().queue();

                        suggest.getSuggestManager().delete();
                        SuggestCommand.suggestion.remove(event.getUser());
                        SuggestCommand.suggestionFrom.remove(event.getUser());
                        SuggestCommand.suggestionMessage.remove(event.getUser());
                        break;
                    default:
                        event.getReaction().removeReaction(event.getUser()).queue();
                        break;
                }
                event.getReaction().removeReaction(event.getUser()).queue();
            }
        }
    }

    @Override
    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event) {
        return;
    }

    @Override
    public void onGuildMessageReactionRemoveAll(@NotNull GuildMessageReactionRemoveAllEvent event) {
        return;
    }

    private boolean hasIndex(List list, int index) {
        if (index < list.size()) {
            return true;
        } else return false;
    }
}
