package system.Objects.Utils.chatbot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public final class AIManager {

    private final User user;
    private final File file = new File("system/chatbot/");
    private final File userFile;
    private final Message message;
    private JSONArray messagesList;
    private final JSONParser parser;

    public AIManager(User user, Message message) {
        this.user = user;
        this.message = message;
        this.userFile = new File( "system/chatbot/" + user.getId() + ".json");
        this.parser = new JSONParser();
        this.messagesList = new JSONArray();

        create();
    }

    public User getUser() {
        return user;
    }

    public Message getMessage() {
        return message;
    }

    public JSONArray getMessagelist() {
        return messagesList;
    }

    public File getUserFile() {
        return userFile;
    }

    public File getFile() {
        return file;
    }

    public JSONParser getParser() {
        return parser;
    }

    private void create() {
        if (getUser().isBot()) return;
        if (getUser().isFake()) return;

        if (!getFile().exists()) {
            getFile().mkdirs();
        }

        if (!getUserFile().exists()) {
            try {
                getUserFile().createNewFile();

                JSONObject messagesObj = new JSONObject();

                messagesObj.put((getMessagelist().size()+1), getMessage().getContentRaw());
                messagesList.add(messagesObj);

                try {
                    PrintWriter writer = new PrintWriter(getUserFile());
                    writer.println(getMessagelist().toJSONString());
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            addMessage(getMessage());
            try {
                PrintWriter writer = new PrintWriter(getUserFile());
                writer.println(messagesList.toJSONString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addMessage(Message message) {
        JSONArray object = getMessagelist();
        JSONObject jsonObject = new JSONObject();

        jsonObject.put((getMessagelist().stream().map(m->m.toString()).count()+1), message.getContentRaw());
        object.add(jsonObject);

        try {
            PrintWriter writer = new PrintWriter(getUserFile());
            writer.println(object.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLastMessage() {
        JSONParser parser = new JSONParser();
        try {
            JSONArray object = (JSONArray) parser.parse(new FileReader(getUserFile()));

            if (object.size() > 1) {
                return object.get((getMessagelist().size() - 1)).toString();
            } else {
                return object.get(getMessagelist().size()).toString();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
