package system.Commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.kodehawa.lib.imageboards.DefaultImageBoards;
import net.kodehawa.lib.imageboards.entities.BoardImage;
import org.w3c.dom.Document;
import system.Objects.Category;
import system.Objects.Command;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class testcommand implements Command {

    @Override
    public void handle(List<String> args, GuildMessageReceivedEvent event) throws FileNotFoundException {
        /**

         String message = args.get(0);

         Message tc = event.getChannel().retrieveMessageById(message).complete(); // message

         EmbedBuilder embed = new EmbedBuilder();
         StringBuilder builder = embed.getDescriptionBuilder();

         AtomicInteger all = new AtomicInteger(0);

         for (int i = 0; i < tc.getReactions().size(); i++) {
         all.addAndGet(tc.getReactions().get(i).getCount());
         }

         tc.getReactions().forEach(react -> { // reactions per emoji

         final float barSize = 10;
         float x = all.get();
         final float total = (react.getCount() / x*barSize);

         final String msg = StringsKt.repeat("█", (Math.round(total))) + StringsKt.repeat("░", (int) (barSize - Math.round(total)));

         builder.append(react.getReactionEmote().getName()).append(": ").append(msg).append((total * barSize)).append("%").append("\n");
         });
         **/
        AtomicInteger i = new AtomicInteger(0);

//        DefaultImageBoards.SAFEBOORU.get().async(images -> {
//            System.out.println("Size: " + images.size());
//            for (BoardImage image: images) {
//                if (i.get() >= 10) {
//                    event.getChannel().sendMessage("**========================**").queue();
//                    return;
//                }
//                event.getChannel().sendMessage("`"+image.getScore()+"` |- " + image.getURL()).queue();
//                i.addAndGet(1);
//            }
//        });

        BoardImage image = DefaultImageBoards.YANDERE.search("neko").blocking().get((int) (Math.random()*DefaultImageBoards.YANDERE.search("pussy").blocking().size()));
        event.getChannel().sendMessage("`"+image.getRating()+"` |- " + image.getURL()).queue();

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

    private Document load(String url) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            return factory.newDocumentBuilder().parse(new URL(url).openStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
