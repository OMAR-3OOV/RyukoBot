package system.commands.FunCategory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.objects.Category;
import system.objects.Command;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CountriesCommand implements Command {

    public TreeMap<String, String> pages = new TreeMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        try {
            String language = args.get(0);

            if (language.isEmpty()) {
                event.getMessage().addReaction("❌").queue();
            }

            Locale[] locales = Locale.getAvailableLocales();

            EmbedBuilder embed = new EmbedBuilder();
            StringBuilder builder = new StringBuilder();
            StringBuilder flags = new StringBuilder();

            embed.setColor(new Color(206, 206, 206));

            if (language.equalsIgnoreCase("languages")) {
                builder.append("```yml").append("\n");
                builder.append("Language:").append("\n");

                AtomicInteger listcheck = new AtomicInteger(0);

                for (Locale obj : locales) {

                    Locale object = new Locale(obj.getLanguage(), obj.getCountry());

                    if (builder.toString().contains(object.getDisplayLanguage())) continue;
                    if (object.getCountry().isEmpty()) continue;

                    int count = listcheck.addAndGet(1);
                    builder.append("-").append(" ").append(object.getDisplayLanguage()).append(spaceBetween(10 - object.getDisplayLanguage().length())).append(" | ");
                    if (count >= 3) {
                        listcheck.set(0);
                        builder.append("\n");
                    }
                }
                builder.append("```");
                embed.setDescription(builder.toString());
                event.getChannel().sendMessage(embed.build()).queue();
                return;
            }

            builder.append("```yml").append("\n");
            builder.append("Country:").append(spaceBetween(18)).append("|").append(spaceBetween(18)).append("Language:").append("\n");
            builder.append(HorizontalLine(24 + 30, "=")).append("\n");

            embed.setTitle("Countries of " + language + " language");
            StringBuilder countries = new StringBuilder();
            for (Locale obj : locales) {

                Locale object = new Locale(obj.getLanguage(), obj.getCountry());

                if (object.getDisplayCountry().isEmpty()) continue;
                if (object.getCountry().isEmpty()) continue;
                if (object.getDisplayLanguage().equalsIgnoreCase(language)) {
                    countries.append(object.getDisplayCountry()).append(" - ").append(object.getCountry())
                            .append(spaceBetween((8 + 18 - 3) - object.getDisplayCountry().length() - object.getCountry().length()))
                            .append("|")
                            .append(spaceBetween((9 + 18) - object.getDisplayLanguage().length()))
                            .append(object.getDisplayLanguage()).append("\n");
                    flags.append(":").append("flag_" + object.getCountry().toLowerCase()).append(":").append(" ");
                }
            }

            builder.append(countries.toString());
            builder.append("```");
            embed.setDescription(builder.toString());
            embed.addField("Flags: ", flags.toString(), false);

            event.getChannel().sendMessage(embed.build()).queue();
        } catch (Exception e) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red.brighter());
            embed.setTitle("I can't find this countries!  (ง •̀_•́)ง");
            embed.setDescription("> `r!countries <language>`");

            event.getChannel().sendMessage(embed.build()).queue();
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "r!countries";
    }

    @Override
    public String getInVoke() {
        return "countries";
    }

    @Override
    public String getDescription() {
        return "You can know the countries that use specific language";
    }

    @Override
    public Permission getPermission() {
        return null;
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
    public Boolean diplayCommand() {
        return true;
    }

    private StringBuilder spaceBetween(int amount) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < amount; i++) {
            builder.append(" ");
        }

        return builder;
    }

    private StringBuilder HorizontalLine(int amount, String line) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < amount; i++) {
            builder.append(line);
        }

        return builder;
    }
}
