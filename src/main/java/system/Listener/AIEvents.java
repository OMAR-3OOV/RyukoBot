package system.Listener;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import system.Objects.Utils.chatbot.AIManager;
import system.Objects.Utils.chatbot.ChatResponse;
import system.Objects.Utils.chatbot.Emotions;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AIEvents extends ListenerAdapter {

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (event.getAuthor().isFake()) return;

        String raw = event.getMessage().getContentRaw();

        AIManager ai = new AIManager(event.getAuthor(), event.getMessage());

        // chat bot
        {
            HashMap<User, String> message = new HashMap<>();

            final Pattern regex = Pattern.compile(Message.MentionType.ROLE.getPattern().pattern());
            final Pattern regex2 = Pattern.compile(Message.MentionType.USER.getPattern().pattern());

            final Matcher matcher = regex.matcher(raw);
            final Matcher matcher2 = regex2.matcher(raw);

            if (matcher.find() || matcher2.find()) {
                raw = raw.replace("<", "").replace("@", "").replace("!", "").replace("&", "").replace(">", "");
            }

            message.put(event.getAuthor(), raw);
            // System.out.println(ai.getLastMessage());

            if ((message.get(event.getAuthor()).contains("hug") || message.get(event.getAuthor()).contains("hugs"))) {
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + " " + Emotions.getRandomHug() + " * " + ChatResponse.getRandomHugs() + " *").queueAfter(1, TimeUnit.SECONDS);
            } else if ((message.get(event.getAuthor()).contains("thanks") || message.get(event.getAuthor()).contains("tnx") || message.get(event.getAuthor()).contains("thank you"))) {
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage(event.getAuthor().getAsMention() + " " + Emotions.getRandomHappiness() + " \" * " + ChatResponse.getRandomFace() + " * \" Oh your welcome, sweety").queueAfter(1, TimeUnit.SECONDS);
            }
        }
    }
}
