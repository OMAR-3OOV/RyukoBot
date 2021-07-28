package system.commands.informationCategory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.objects.Category;
import system.objects.Command;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class readCommand implements Command {
    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        try {
            String word = args.get(0);
            StringBuilder wordsString = new StringBuilder();
            List<String> words = new ArrayList<>();

            for (int i = 0; i < args.size(); i++) {
                wordsString.append(args.get(i));
                words.add(args.get(i));
            }

            char[] analysisString = new char[wordsString.toString().toCharArray().length];

            List<String> characters = new ArrayList<>();
            for (int i = 0; i < analysisString.length; i++) {
                analysisString[i] = wordsString.charAt(i);
                characters.add(String.valueOf(wordsString.toString().charAt(i)));
            }

            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red.brighter());

            if (isURL(word) && word.contains("https://discord.gg/")) {
                embed.setTitle("Sorry but i can't read this link (๑´╹‸╹`๑)");
            } else if (analysisString.length <= 4) {
                embed.setTitle("Come on it's not that hard ( `ε´ )");
            } else if (analysisString.length <= 7) {
                embed.setTitle("I solve it  (*・ω・)ﾉ");
            } else if (analysisString.length >= 10) {
                embed.setTitle("Wanna kill me ヽ(°〇°)ﾉ");
            }

            if (isURL(words.stream().collect(Collectors.joining("")))) {

                if (word.contains("https://discord.gg/")) {
                    event.getMessage().addReaction("❌").delay(2000, TimeUnit.SECONDS).queue();
                    embed.setDescription("> Sorry but discord links is ignored from my system");
                } else {
                    StringBuilder string = embed.getDescriptionBuilder();
                    final AtomicInteger linksCounter = new AtomicInteger(0);

                    words.stream().forEach((links) -> {
                        try {
                            final URL link = new URL(links);

                            int urlCount = linksCounter.addAndGet(1);

                            string.append("**> Url " + urlCount + " : **").append("\n");
                            string.append("**Url: ** ").append(link.toString()).append("\n");
                            string.append("**Protocol: **").append(link.getProtocol()).append("\n");
                            string.append("**Host: **").append(link.getHost()).append("\n");
                            string.append("**File: **").append(link.getFile()).append("\n");
                            string.append("\n");
                        } catch (MalformedURLException e) {
                            EmbedBuilder embed_1 = new EmbedBuilder();
                            embed_1.setColor(Color.red.brighter());
                            embed_1.setTitle("How to user ( ´ ω ` )ノﾞ");
                            embed_1.setDescription("**Usage: ** " + getHelp());

                            event.getChannel().sendMessage(embed_1.build()).queue();
                            e.printStackTrace();
                        }
                    });

                    string.append("**Characters: **").append(characters.stream().collect(Collectors.joining(" "))).append("\n");
                    string.append("**Characters count:** ").append(analysisString.length).append("\n");
                }
            } else {
                StringBuilder string = embed.getDescriptionBuilder();

                final Pattern regexIsChar = Pattern.compile("-?[A-Za-z]+");
                final Pattern regex = Pattern.compile("^\\p{Punct}+$");
                final Matcher matcher = regex.matcher(word.replace(regexIsChar.toString(), ""));

                string.append("**Word: ** ").append(words.stream().collect(Collectors.joining(" "))).append("\n");

                if (matcher.find()) {
                    string.append("**Special Word: **");
                    string.append(matcher.group(matcher.groupCount()));
                    string.append("\n");
                }

                string.append("**Words count :** ").append(words.size()).append("\n");
                string.append("**Characters: **").append(characters.stream().collect(Collectors.joining(" "))).append("\n");
                string.append("**Characters count:** ").append(analysisString.length);
            }

            event.getChannel().sendMessage(embed.build()).queue();
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red.brighter());
            embed.setTitle("How to user ( ´ ω ` )ノﾞ");
            embed.setDescription("**Usage: ** " + getHelp());

            event.getChannel().sendMessage(embed.build()).queue();
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "r!read <text>";
    }

    @Override
    public String getInVoke() {
        return "read";
    }

    @Override
    public String getDescription() {
        return "Word segmentation and analysis";
    }

    @Override
    public Permission getPermission() {
        return Permission.UNKNOWN;
    }

    @Override
    public Category getCategory() {
        return Category.INFORMATION;
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
    public Boolean displayCommand() {
        return true;
    }

    public static boolean isURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
