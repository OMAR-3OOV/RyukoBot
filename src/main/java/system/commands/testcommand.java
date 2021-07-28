package system.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.objects.Category;
import system.objects.Command;
import system.objects.Utils.LanguagesUtils.LanguagesManager;
import system.objects.Utils.LanguagesUtils.MessagesKeys;
import system.objects.Utils.levelUtils.LevelsCalculations;

import java.io.FileNotFoundException;
import java.util.List;

public class testcommand implements Command, LevelsCalculations {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {

        LanguagesManager language = new LanguagesManager(event.getAuthor());

//        Queue<User> users = new ConcurrentLinkedDeque<>();
//
//        StringBuilder queue = new StringBuilder();
//
//        event.getGuild().getMembers().stream().filter(filter -> !filter.getUser().isBot() && !filter.getUser().isFake()).forEach(member -> {
//            users.add(member.getUser());
//
//            if (users.size() >= 2) {
//                queue.append("**").append(users.remove().getName()).append("**").append(" and ").append("**").append(users.remove().getName()).append("**").append(" are couples right now uwu!").append("\n");
//            }
//        });
//
//        if (users.size() <= 1) {
//            queue.append("**").append(users.remove().getName()).append("**").append(" are lonely u_u").append("\n");
//        }
//
//        System.out.println("Queue size : " + users.size() + " | Members size: " + event.getGuild().getMembers().stream().filter(f -> !f.getUser().isBot()).count());
//
//        event.getChannel().sendMessage(queue.toString()).queue();

        //        String exp = args.get(0);
//        float level = (float) LevelsCalculations.getLevelFromExp(Double.parseDouble(exp));
//        event.getChannel().sendMessage("The currently level for this exp : " + level + " : " + LevelsCalculations.getTotalExpToNextLevel(Double.parseDouble(exp))).queue();

//        StringBuilder command = new StringBuilder();
//
//        for (String arg : args) {
//            command.append(arg).append(" ");
//        }
//
//        String cmds = command.toString();
//
//        event.getChannel().sendMessage(String.join(" " , cmds.split("\\|"))).queue();
////        cmds = cmds.replace("\n", "");
////        if (command.toString().startsWith(" | ")) cmds = cmds.substring(1);
////        String[] commandsParts = cmds.split("\\|");
////
////        if (commandsParts.length >= 3 && commandsParts.length<= 13) {
////            String topic = commandsParts[0].trim();
////
////            String[] voting = new String[commandsParts.length - 1];
////            System.arraycopy(commandsParts, 1, voting, 0, voting.length);
////
////            event.getChannel().sendMessage(String.join(" ", commandsParts)).queue();
////        }
    }

    @Override
    public String getHelp() {
        return "r!test";
    }

    @Override
    public String getInVoke() {
        return "test";
    }

    @Override
    public String getDescription() {
        return "test";
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
    public Boolean displayCommand() {
        return false;
    }

//    public Message.Attachment createAttachment() {
//        final int width = Helpers.optInt(jsonObject, "width", -1);
//        final int height = Helpers.optInt(jsonObject, "height", -1);
//        final int size = jsonObject.getInt("size");
//        final String url = jsonObject.optString("url", null);
//        final String proxyUrl = jsonObject.optString("proxy_url", null);
//        final String filename = jsonObject.getString("filename");
//        final long id = jsonObject.getLong("id");
//        return new Message.Attachment(id, url, proxyUrl, filename, size, height, width, getJDA());
//    }
}
