package system.Commands.Administration;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.Objects.Category;
import system.Objects.Command;
import system.Objects.TextUtils.MessageUtils;
import system.Objects.Utils.BoosterUtils.Booster;
import system.Objects.Utils.coinsManager;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

public class shopCommand implements Command {

    public static HashMap<User, Message> shopCheck = new HashMap<>();

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {
        try {
            coinsManager ruko = new coinsManager(event.getAuthor());
            EmbedBuilder embed = new EmbedBuilder();

            embed.setColor(Color.ORANGE);
            embed.setTitle("Ruko Market \uD83D\uDED2");

            Message message = event.getChannel().sendMessage(embed.build()).complete();
            StringBuilder disc = embed.getDescriptionBuilder();

            disc.append(new MessageUtils("**Boosters :booster: :**\n> You can buy booster buy clicking to Reactions\n \n").EmojisHolder());
            Booster.loadBoosters();
            Booster.getBooster().forEach((k, v) -> {
                Booster booster = new Booster(v);

                disc.append(new MessageUtils(booster.getEmoji() + " | `" + booster.getName() + "` x" + booster.getCombo() + " ➜ :ruko: " + booster.getCost() + "\n").EmojisHolder());
                message.addReaction(booster.getEmoji()).queue();
            });
            embed.setFooter( "⬩ Your Ruko's : " + ruko.getRukoUser());
            message.editMessage(embed.build()).queue();
            message.addReaction("❌").queue();

            shopCheck.put(event.getAuthor(), message);
        } catch (NullPointerException | NoSuchElementException | IndexOutOfBoundsException e) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red.brighter());
            embed.setTitle("I can't find it!  (ง •̀_•́)ง");
            embed.setDescription("Usage :" +
                    "\n > r!ruko pay <amount> <user>" +
                    "\n > r!ruko donation <amount>");

            event.getChannel().sendMessage(embed.build()).queue();
            e.printStackTrace();
        } catch (NumberFormatException e) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red.brighter());
            embed.setTitle("I can't find the amount!  (ง •̀_•́)ง");
            embed.setDescription("Make sure that you putting the amount `r!ruko pay <amount> <user> | r!ruko donation <amount>");

            event.getChannel().sendMessage(embed.build()).queue();
            e.printStackTrace();
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
        return "r!shop";
    }

    @Override
    public String getInVoke() {
        return "shop";
    }

    @Override
    public String getDescription() {
        return "To buy bot staff";
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
        return false;
    }

    @Override
    public Boolean isNsfw() {
        return false;
    }

    @Override
    public Boolean diplayCommand() {
        return false;
    }
}
