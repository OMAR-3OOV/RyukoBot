package system.commands.informationCategory;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.Objects.Utils.achievementsutils.AchievementsManager;
import system.Objects.Utils.canvasutils.DrawProfileCanvas;
import system.Objects.Category;
import system.Objects.Command;
import system.Objects.Utils.profileconfigutils.ProfileBuilder;
import system.Objects.Utils.levelUtils.LevelsCalculations;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ServerProfileIDCanvas implements Command, LevelsCalculations {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {
        try {
            List<String> handlers = new ArrayList<>(args);
            Message message = event.getChannel().sendMessage("Wait please that might take a little bit time!, " + event.getAuthor().getAsMention()).complete();

            if (handlers.isEmpty()) {
                ProfileBuilder profile = new ProfileBuilder(event.getAuthor());
                AchievementsManager achievementsManager = new AchievementsManager(event.getAuthor());

                DrawProfileCanvas profileCanvas = new DrawProfileCanvas(profile.getUser(), event.getGuild(), event.getMember().getRoles().stream().filter(f->f.getColor()!=null).findFirst().get(), profile.getRuko(), profile.getLevel(), achievementsManager.getAchievements().size());

                File profileFiles = new File("System/Profiles/" + profile.getUser().getId() + ".png");
                ImageIO.write(profileCanvas.getBufferedImage(), "png", profileFiles);

                message.delete().queue();
                event.getChannel().sendMessage("User has been find!, " + event.getAuthor().getAsMention()).addFile(profileFiles).queue();
            } else {

                String user = handlers.get(0);

                final Pattern regex = Pattern.compile(Message.MentionType.USER.getPattern().pattern());
                final Matcher matcher = regex.matcher(user);

                if (matcher.find()) {
                    user = user.replace("<", "").replace("!", "").replace("@", "").replace("#", "").replace("&", "").replace(">", "");
                } else if (matcher.find()) {
                    user = Objects.requireNonNull(event.getGuild().getMembersByName(user, true)).stream().map(m -> m.getUser().getId()).collect(Collectors.joining());
                }

                User target = Objects.requireNonNull(event.getGuild().getMemberById(user)).getUser();
                Member tm = event.getGuild().getMemberById(user);

                ProfileBuilder profile = new ProfileBuilder(target);
                AchievementsManager achievementsManager = new AchievementsManager(target);

                DrawProfileCanvas profileCanvas = new DrawProfileCanvas(profile.getUser(), event.getGuild(), tm.getRoles().stream().findFirst().get(), profile.getRuko(), profile.getLevel(), achievementsManager.getAchievements().size());

                File profileFiles = new File("System/Profiles/" + profile.getUser().getId() + ".png");
                ImageIO.write(profileCanvas.getBufferedImage(), "png", profileFiles);

                message.delete().queue();
                event.getChannel().sendMessage("User has been find!, " + event.getAuthor().getAsMention()).addFile(profileFiles).queue();
            }

        } catch (Exception e) {

        }
    }

    @Override
    public String getHelp() {
        return "r!id";
    }

    @Override
    public String getInVoke() {
        return "id";
    }

    @Override
    public String getDescription() {
        return "You can see here your ryuko id, `(Beta)`";
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
}
