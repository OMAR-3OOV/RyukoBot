package system.Objects.Utils.PrivateChatUtils;

import net.dv8tion.jda.api.entities.*;
import system.Commands.Administration.PrivateChattingBot.PrivateChatFilterManager;

import java.io.*;
import java.util.*;

public class PrivateChat {

    private boolean started = false;

    private final User sender;
    private final User getter;

    private Message senderMessage;
    private Message getterMessage;

    private TextChannel channel;
    private Guild guild;

    private List<Message> messages;
    private List<Message> lastBotMessages;

    private File file;
    private Properties properties;

    private int code;

    // .. Modes
    public HashMap<User, PrivateChatMode> mode = new HashMap<>();

    // Effects
    public HashMap<User, Message> editMessage = new HashMap<>();
    public HashMap<User, Message> lastMessage = new HashMap<>();

    // Sender
    public PrivateChat(User sender, User getter, TextChannel channel, Guild guild, Message message) {
        this.sender = sender;
        this.getter = getter;
        this.channel = channel;
        this.guild = guild;
        this.senderMessage = message;
        this.lastBotMessages = new ArrayList<>();
        this.code = generateFileCode();
        this.messages = new ArrayList<>();

        this.mode.put(sender, PrivateChatMode.CHATTING);
        this.file = new File("system/PrivateChat/Users/" + this.code + ".properties");
        this.properties = new Properties();
    }

    public User getSender() {
        return sender;
    }

    public User getGetter() {
        return getter;
    }

    public Message getSenderMessage() {
        return senderMessage;
    }

    public void setSenderMessage(Message senderMessage) {
        addMessage(senderMessage);
        addFileMessage(this.sender, senderMessage);
        this.senderMessage = senderMessage;
    }

    public Message getGetterMessage() {
        return getterMessage;
    }

    public void setGetterMessage(Message getterMessage) {
        addMessage(getterMessage);
        addFileMessage(this.getter, senderMessage);
        this.getterMessage = getterMessage;
    }

    public void addFileMessage(User who, Message message) {
        properties.setProperty("message."+who.getId()+"."+getMessages().size(), String.valueOf(message));
        save();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        getMessages().add(message);
    }

    public int getCode() {
        return code;
    }

    public TextChannel getChannel() {
        return channel;
    }

    public Guild getGuild() {
        return guild;
    }

    public PrivateChatMode getMode() {
        return mode.get(sender);
    }

    public void setMode(PrivateChatMode chatMode) {
        mode.put(getSender(), chatMode);
    }

    public HashMap<User, Message> getEditMessage() {
        return editMessage;
    }

    public HashMap<User, Message> getLastMessage() {
        return lastMessage;
    }

    public List<Message> getLastBotMessages() {
        return lastBotMessages;
    }

    public File getFile() {
        return file;
    }

    public Properties getProperties() {
        return properties;
    }

    public void start(String msg) {
        setStarted(true);
        setMode(PrivateChatMode.CHATTING);
        buildFileMessage(this.sender, this.getter);

        PrivateChannel privateChannel = getGetter().openPrivateChannel().complete();
        Message message = privateChannel.sendMessage(new PrivateChatFilterManager(msg).toFilter(getGetter())).complete();

        setSenderMessage(message);
        lastBotMessages.add(message);
    }

    public void end() {
        setStarted(false);
    }

    public boolean getStarted() {
        return started;
    }

    private void setStarted(boolean bool) {
        started = bool;
    }

    private void buildFileMessage(User sender, User getter) {
        if (this.sender.isBot() || this.getter.isBot()) return;

        if (!this.file.getParentFile().exists()) {
            this.file.getParentFile().mkdirs();
        }

        if (this.file.exists()) {
            try {
                properties.load(new FileInputStream(this.file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!this.file.exists()) {
            try {
                this.file.createNewFile();

                properties.setProperty("UserSender", sender.getId());
                properties.setProperty("UserGetter", sender.getId());

                save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save() {
        try {
            properties.save(new FileOutputStream(this.file), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int generateFileCode() {
        Random random = new Random();

        return random.nextInt(99999999);
    }
}
