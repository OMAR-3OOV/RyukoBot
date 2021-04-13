package system.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import system.Objects.Category;
import system.Objects.Command;
import system.Objects.Utils.levelUtils.LevelsCalculations;

import java.io.FileNotFoundException;
import java.util.List;

public class testcommand implements LevelsCalculations, Command {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {

        event.getAuthor().openPrivateChannel().queue(msg -> {
            msg.sendMessage("wassap").queue(message -> {

            }, error -> {
                event.getChannel().sendMessage("I can't send direct message , `"+error.getMessage()+"`").queue();
            });
        }, error -> {
            event.getChannel().sendMessage("I can't send direct message , `"+error.getMessage()+"`").queue();
        });

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
    public Boolean diplayCommand() {
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
