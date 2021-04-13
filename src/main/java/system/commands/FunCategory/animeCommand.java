package system.commands.FunCategory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import system.Objects.Category;
import system.Objects.Command;

import java.awt.*;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/*

   Powered by : Kitsu API
   Requested by using OkHttpClient

 */

public class animeCommand implements Command {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {
        try {
            String anime = args.stream().collect(Collectors.joining(" "));

            // ** Anime Options ** //

            String nextRelease = "NOT LONGER";

            // ## Language names
            String name_english = "NOT LONGER";
            String name_jp_kanji = "NOT LONGER";
            String name_jp_hiragana = "NOT LONGER";

            // ## informations
            String original = "NOT LONGER";
            String poster = "NOT LONGER";
            String create = "NOT LONGER";
            String synopsis = "NOT LONGER";
            String description = "NOT LONGER";
            String[] titiles = {"NOT LONGER"};
            float rating = 0;
            String startShow = "NOT LONGER";
            String endShow = "NOT LONGER";
            String age = "NOT LONGER";
            String globalAge = "NOT LONGER";
            String status = "NOT LONGER";
            String showType = "NOT LONGER";
            int episodes = 0;
            int episodesTotalTime = 0;

            // ** Anime Option getting characters ** //
            JSONObject relationships = null;
            JSONObject characters = null;
            JSONObject links = null;
            String related = "";

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://kitsu.io/api/edge/anime?filter[text]=" + anime.toLowerCase().replaceAll("\\s", "-"))
                    .get()
                    .addHeader("Content-Type", "application/vnd.api+json")
                    .addHeader("Accept", "application/vnd.api+json")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                assert response.body() != null;
                JSONObject result = new JSONObject(response.body().string());

                // Formatting
                //System.out.println(result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getJSONObject("titles").getString("en"));

                // # names
                if (!result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getJSONObject("titles").isNull("en_jp")) {
                    name_jp_hiragana = result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getJSONObject("titles").getString("en_jp");
                }
                if (!result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getJSONObject("titles").isNull("ja_jp")) {
                    name_jp_kanji = result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getJSONObject("titles").getString("ja_jp");
                }
                if (!result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getJSONObject("titles").isNull("en")) {
                    name_english = result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getJSONObject("titles").getString("en");

                }
                if (!result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getJSONObject("titles").isNull("en_us")) {
                    name_english = result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getJSONObject("titles").getString("en_us");
                }

                if (result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getJSONObject("titles").isNull("en_us") && result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getJSONObject("titles").isNull("en")) {
                    name_english = result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getJSONObject("titles").getString("en_jp");
                }

                // # informations
                poster = result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getJSONObject("posterImage").getString("small");
                create = result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getString("createdAt");
                synopsis = result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getString("synopsis");
                titiles = new String[]{name_english, name_jp_hiragana, name_jp_kanji};
                rating = result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getFloat("averageRating");
                startShow = result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getString("startDate");
                if (!result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").isNull("endDate")) {
                    endShow = result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getString("endDate");
                } else {
                    endShow = "Not yet";
                }
                age = result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getString("ageRating");
                globalAge = result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getString("ageRatingGuide");
                status = result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getString("status");
                if (!result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").isNull("showType")) {
                    showType = result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getString("showType");
                }
                if (!result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").isNull("episodeCount")) {
                    episodes = result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getInt("episodeCount");
                }
                if (!result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").isNull("episodeLength")) {
                    episodesTotalTime = result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getInt("episodeLength");
                }
                if (!result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").isNull("coverImage")) {
                    original = result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getJSONObject("coverImage").getString("tiny");
                }
                if (!result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").isNull("nextRelease")) {
                    nextRelease = result.getJSONArray("data").getJSONObject(0).getJSONObject("attributes").getString("nextRelease");
                } else {
                    nextRelease = endShow;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // ## Dates formatting ## //
            SimpleDateFormat current = new SimpleDateFormat("EEEE, dd MMMM yyyy");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            format.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date parse = format.parse(create);
            Date parse1 = format1.parse(startShow);
            Date parse2;
            Date date2 = null;

            String checkShow = "Checking";


            if (endShow.contains("Not yet")) {
                if (!nextRelease.equalsIgnoreCase("Not Yet")) {
                    checkShow = "Next Show";
                    parse2 = format1.parse(nextRelease);
                    date2 = new Date(parse2.getTime());
                }
            } else {
                checkShow = "Last Show";
                parse2 = format1.parse(endShow);
            }


            Date currentDate = new Date();
            Date date = new Date(parse.getTime());
            Date date1 = new Date(parse1.getTime());

            SimpleDateFormat formatAt = new SimpleDateFormat("E, dd MMMM yyyy");

            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.getHSBColor(99, 99, 99));
            embed.setTitle(name_english);
            embed.setThumbnail(poster);
            embed.setDescription(synopsis);
            embed.addField("Type", String.valueOf(showType), true);
            if (episodes == 0) {
                embed.addField("Episodes", "Not done yet" + " ( " + episodesTotalTime + "min )", true);
            } else {
                embed.addField("Episodes", episodes + " ( " + episodesTotalTime + "min )", true);
            }
            embed.addField("Rating", String.valueOf(rating), true);
            embed.addField("Age", String.valueOf(age), true);
            embed.addField("Global Age", String.valueOf(globalAge), true);
            embed.addField("Created", formatAt.format(date), true);
            embed.addField("First Show", formatAt.format(date1), true);
            if (date2 != null) {
                embed.addField(checkShow, formatAt.format(date2), true);
            } else {
                embed.addField("Last Show", "-", true);
            }
            embed.addField("status", String.valueOf(status), true);
            if (original != null) {
                embed.setImage(original);
            }
            embed.setFooter("\uD83D\uDCC6 | " + current.format(currentDate));

            event.getChannel().sendMessage(embed.build()).queue();


        } catch (NullPointerException | IndexOutOfBoundsException e) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(Color.red.brighter());
            error.setTitle("I can't find this type!  (ง •̀_•́)ง");
            error.setDescription("**r!anime <search>**");

            event.getChannel().sendMessage(error.build()).queue();
        } catch (Exception e) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(Color.red.brighter());
            error.setTitle("Something went wrong!  (ง •̀_•́)ง");
            error.setDescription("Report this error in Ryuko discord server\n \n**Error :** " + e.getMessage());

            event.getChannel().sendMessage(error.build()).queue();
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "r!anime <search>";
    }

    @Override
    public String getInVoke() {
        return "anime";
    }

    @Override
    public String getDescription() {
        return "to search any anime";
    }

    @Override
    public Permission getPermission() {
        return Permission.UNKNOWN;
    }

    @Override
    public Category getCategory() {
        return Category.FUN;
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
