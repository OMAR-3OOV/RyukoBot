package system.Commands.Administration.moderatorCategory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import system.Objects.Category;
import system.Objects.Command;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class rolesCommand implements Command {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) {

        if (event.getMember().hasPermission(getPermission()) == false) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red.brighter());

            if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                embed.setTitle("Ooooh Administration ＼(〇_ｏ)／");
                embed.setDescription("Sorry sir, but you must have " + getPermission().getName() + " permission");
            }else {
                embed.setTitle("Noooooo! (╬ Ò﹏Ó)");
                embed.setDescription("You don't any access to do that, i'll call police!");
            }

            event.getChannel().sendMessage(embed.build()).queue();
            return;
        }

        try {
            String role = args.get(0);

            final Pattern regex = Pattern.compile(Message.MentionType.ROLE.getPattern().pattern());
            final Matcher matcher = regex.matcher(role);

            final Pattern regexRoleName = Pattern.compile("-?[A-Za-z]+");
            final Matcher matcherRoleName = regexRoleName.matcher(role);

            if (matcher.find()) {
                role = role.replace("<", "").replace("@", "").replace("&", "").replace(">", "");
            }else if (matcherRoleName.find()) {
                role = event.getGuild().getRolesByName(role,true).stream().collect(Collectors.toList()).stream().findFirst().get().getId();
            }

            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Objects.requireNonNull(Objects.requireNonNull(event.getGuild().getRoleById(role)).getColor()).getRGB());
            embed.setTitle(Objects.requireNonNull(event.getGuild().getRoleById(role)).getName() + " Role ( ´ ω ` )ノ");
            // embed.setDescription("**Members: ** \n> " + Objects.requireNonNull(event.getGuild().getRoleById(role)).getManager().getGuild().getMembersWithRoles(event.getGuild().getRoleById(role)).stream().map(f -> f.getUser().getAsTag()).collect(Collectors.joining("\n> ")));

            Stream<Member> stream = Objects.requireNonNull(event.getGuild().getRoleById(role)).getManager().getGuild().getMembersWithRoles(event.getGuild().getRoleById(role)).stream();
            StringBuilder disc = embed.getDescriptionBuilder();

            disc.append("**Members: **").append("\n> ");
            disc.append(stream.map(map -> map.getUser().getAsTag() + " ( `" + map.getEffectiveName() + "` ) ").collect(Collectors.joining("\n> ")));

            event.getChannel().sendMessage(embed.build()).queue();

        } catch (NullPointerException | NoSuchElementException | IndexOutOfBoundsException | NumberFormatException e) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red.brighter());
            embed.setTitle("I can't find it!  (ง •̀_•́)ง");
            embed.setDescription("Usage : " + getHelp());

            event.getChannel().sendMessage(embed.build()).queue();
            e.printStackTrace();
        } catch (InsufficientPermissionException e) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red.brighter());
            embed.setTitle("Missing Permission! (＃￣ω￣)");
            embed.setDescription(e.getMessage());

            event.getChannel().sendMessage(embed.build()).queue();
            e.printStackTrace();
        } catch (HierarchyException e) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.red.brighter());
            embed.setTitle("Missing Permission! (°ロ°)!");
            embed.setDescription("This role is higher than bot role");

            event.getChannel().sendMessage(embed.build()).queue();
            e.printStackTrace();
        }
    }

    @Override
    public String getHelp() {
        return "r!role <role>";
    }

    @Override
    public String getInVoke() {
        return "role";
    }

    @Override
    public String getDescription() {
        return "show the member who have the role";
    }

    @Override
    public Permission getPermission() {
        return Permission.MANAGE_ROLES;
    }

    @Override
    public Category getCategory() {
        return Category.MODERATOR;
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
