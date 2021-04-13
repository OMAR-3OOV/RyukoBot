package system.Listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import system.commands.Administration.helpCommand;
import system.commands.informationCategory.AchievementsCommand;
import system.Objects.Category;
import system.Objects.Utils.administration.HelpPagesUtil;
import system.Objects.Utils.administration.HelpUtil;

import java.awt.*;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class HelpEvent extends ListenerAdapter {

    public static HashMap<User, HelpUtil> helper = helpCommand.helper;
    public static HashMap<User, Message> helperMessage = helpCommand.helperMessage;
    public static HashMap<User, HelpPagesUtil> page = helpCommand.helpPage;
    public static HashMap<User, Integer> helpCooldown = helpCommand.helpCooldown;

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                event.getJDA().getUsers().stream().filter(f -> !f.isBot() || !f.isFake()).forEach(user -> {
                    if (helpCooldown.containsKey(user)) {
                        try {
                            helpCooldown.forEach((key, value) -> {
                                if (value >= 120) {
                                    helper.remove(key);
                                    helperMessage.remove(key);
                                    page.remove(key);
                                    helpCooldown.remove(key);
                                }
                            });
                        } catch (Exception ignored) {

                        }
                    } else if (AchievementsCommand.achievementPages.containsKey(user)) {
                        try {
                            AchievementsCommand.pageCooldown.forEach((key, value) -> {
                                if (value >= 60) {
                                    AchievementsCommand.achievementPages.remove(user);
                                    AchievementsCommand.pagesMessage.remove(user);
                                    AchievementsCommand.pageCooldown.remove(user);
                                }
                            });
                        } catch (Exception ignored) {

                        }
                    }
                });
            }
        }, 0, (1000));
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {

        if (helper.containsKey(event.getUser())) {
            if (helperMessage.get(event.getUser()).getId().contains(event.getMessageId()) && helper.get(event.getUser()).getGuild().getId().contains(event.getGuild().getId())) {

                if (helpCooldown.containsKey(event.getUser())) {

                    EmbedBuilder helperEmbed = new EmbedBuilder();

                    EmbedBuilder embed = helperEmbed;
                    embed.setDescription(null);
                    embed.clearFields();

                    StringBuilder builder = embed.getDescriptionBuilder();

                    switch (event.getReactionEmote().getName()) {
                        case "⬅":
                            resetCooldown(event.getUser());
                            page.get(event.getUser()).backPage();

                            if (page.get(event.getUser()).getPageNumber().get(event.getUser()) == 0) {

                                helperMessage.get(event.getUser()).delete().queue();

                                helper.remove(event.getUser());
                                helperMessage.remove(event.getUser());
                                page.remove(event.getUser());
                                helpCooldown.remove(event.getUser());
                            }
                            break;
                        case "\uD83D\uDEE1": // Management
                            resetCooldown(event.getUser());
                            helperEmbed.setTitle("Management Category");
                            embed.setColor(new Color(255, 87, 87));
                            helper.get(event.getUser()).getMap().get(Category.MANAGEMENT).forEach(cmd -> {
                                embed.addField("** | ⬩ " + cmd.getInVoke() + "** " + (cmd.Lockdown() ? "\uD83D\uDD12 " : "") + (cmd.isNsfw() ? "\uD83D\uDD1E ":"") + (cmd.diplayCommand() ? " ":"⛔ "), "> " + cmd.getDescription(), true);
                            });

                            helperEmbed.setFooter("Usage: r!help <command>");
                            page.get(event.getUser()).nextPage(embed);

                            helper.get(event.getUser()).getNow().put(event.getUser(), Category.MANAGEMENT);
                            break;
                        case "\uD83D\uDD30": // Moderator
                            resetCooldown(event.getUser());
                            helperEmbed.setTitle("Moderator Category");
                            embed.setColor(new Color(255, 171, 87));
                            helper.get(event.getUser()).getMap().get(Category.MODERATOR).forEach(cmd -> {
                                embed.addField("** | ⬩ " + cmd.getInVoke() + "** " + (cmd.Lockdown() ? "\uD83D\uDD12 " : "") + (cmd.isNsfw() ? "\uD83D\uDD1E ":"") + (cmd.diplayCommand() ? " ":"⛔ "), "> " + cmd.getDescription(), true);
                            });

                            helperEmbed.setFooter("Usage: r!help <command>");
                            page.get(event.getUser()).nextPage(embed);
                            helper.get(event.getUser()).getNow().put(event.getUser(), Category.MODERATOR);
                            break;
                        case "\uD83D\uDCCA": // Information
                            resetCooldown(event.getUser());
                            helperEmbed.setTitle("Information Category");
                            embed.setColor(new Color(87, 221, 255));
                            helper.get(event.getUser()).getMap().get(Category.INFORMATION).forEach(cmd -> {
                                embed.addField("** | ⬩ " + cmd.getInVoke() + "** " + (cmd.Lockdown() ? "\uD83D\uDD12 " : "") + (cmd.isNsfw() ? "\uD83D\uDD1E ":"") + (cmd.diplayCommand() ? " ":"⛔ "), "> " + cmd.getDescription(), true);
                            });

                            helperEmbed.setFooter("Usage: r!help <command>");
                            page.get(event.getUser()).nextPage(embed);
                            helper.get(event.getUser()).getNow().put(event.getUser(), Category.INFORMATION);
                            break;
                        case "\uD83C\uDF8A": // Fun
                            resetCooldown(event.getUser());
                            helperEmbed.setTitle("Fun Category");
                            embed.setColor(new Color(104, 87, 255));
                            helper.get(event.getUser()).getMap().get(Category.FUN).forEach(cmd -> {
                                embed.addField("** | ⬩ " + cmd.getInVoke() + "** " + (cmd.Lockdown() ? "\uD83D\uDD12 " : "") + (cmd.isNsfw() ? "\uD83D\uDD1E ":"") + (cmd.diplayCommand() ? " ":"⛔ "), "> " + cmd.getDescription(), true);
                            });

                            helperEmbed.setFooter("Usage: r!help <command>");
                            page.get(event.getUser()).nextPage(embed);
                            helper.get(event.getUser()).getNow().put(event.getUser(), Category.FUN);
                            break;
                        case "\uD83C\uDF32": // Minecraft
                            resetCooldown(event.getUser());
                            helperEmbed.setTitle("Minecraft Category");
                            embed.setColor(new Color(132, 255, 130));
                            helper.get(event.getUser()).getMap().get(Category.MINECRAFT).forEach(cmd -> {
                                embed.addField("** | ⬩ " + cmd.getInVoke() + "** " + (cmd.Lockdown() ? "\uD83D\uDD12 " : "") + (cmd.isNsfw() ? "\uD83D\uDD1E ":"") + (cmd.diplayCommand() ? " ":"⛔ "), "> " + cmd.getDescription(), true);
                            });

                            helperEmbed.setFooter("Usage: r!help <command>");
                            page.get(event.getUser()).nextPage(embed);
                            helper.get(event.getUser()).getNow().put(event.getUser(), Category.MINECRAFT);
                            break;
                        case "\uD83D\uDD1E": // Nsfw
                            resetCooldown(event.getUser());
                            helperEmbed.setTitle("Nsfw Category");
                            embed.setColor(new Color(64, 0, 0));
                            helper.get(event.getUser()).getMap().get(Category.NSFW).forEach(cmd -> {
                                embed.addField("> ** | ⬩ " + cmd.getInVoke() + "** " + (cmd.Lockdown() ? "\uD83D\uDD12 " : "") + (cmd.isNsfw() ? "\uD83D\uDD1E ":"") + (cmd.diplayCommand() ? " ":"⛔ "), "> " + cmd.getDescription(), true);
                            });

                            builder.append("\n").append("**").append("Note:** This category is +18 so please don't use it if you underage!").append("\n");

                            helperEmbed.setFooter("Usage: r!help <command>");
                            page.get(event.getUser()).nextPage(embed);
                            helper.get(event.getUser()).getNow().put(event.getUser(), Category.NSFW);
                            break;
                        default:
                            event.getReaction().removeReaction(event.getUser()).queue();
                            break;
                    }
                    if (page.containsKey(event.getUser())) {

                        builder.append("```").append("\n \n").append("```");
                        helper.get(event.getUser()).getMap().forEach((key, value) -> {
                            builder.append("**").append(key.getEmoji()).append(" | ⬩ ").append(key.getName()).append("**").append((helper.get(event.getUser()).getNow().get(event.getUser())==key?" ⬅️ Selected":"")).append("\n");
                        });
                        builder.append("```").append("\n \n").append("```");

                        helperMessage.get(event.getUser()).editMessage(page.get(event.getUser()).getPage().get(page.get(event.getUser()).getPageNumber().get(event.getUser())).build()).queue();
                        event.getReaction().removeReaction(event.getUser()).queue();
                    }
                }
            }
        }
    }

    @Override
    public void onGuildMessageReactionRemove(@NotNull GuildMessageReactionRemoveEvent event) {
        if (helpCommand.helper.containsKey(event.getUser())) {
            return;
        }
    }

    private void resetCooldown(User user) {
        helpCooldown.entrySet().forEach((k) -> {
            if (k.getKey().getId().contains(user.getId())) {
                k.setValue(0);
            }
        });
    }

}
