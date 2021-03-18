package system.Objects.Utils.PrivateChatUtils;

import net.dv8tion.jda.api.entities.*;
import system.Commands.Administration.PrivateChattingBot.PrivateChatFilterManager;

import java.io.File;
import java.util.*;

public class PrivateChat {

    private boolean started = false;

    private final User sender;
    private final User getter;

    private Message senderMessage;
    private Message getterMessage;

    private TextChannel channel;
    private Guild guild;

    private List<Message> lastBotMessages;

    private List<Message> senderMessages;
    private List<Message> getterMessages;

    private File file;
    private Properties properties;

    private int code;

    // .. Modes
    public HashMap<User, PrivateChatMode> mode = new HashMap<>();

    // Effects
    public HashMap<User, Message> editMessage = new HashMap<>();
    public HashMap<User, Message> replyMessage = new HashMap<>();
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

        this.mode.put(sender, PrivateChatMode.CHATTING);
        this.file = new File("system/PrivateChat/Users/" + this.code + ".properties");
        this.properties = new Properties();
    }

    // Getter
    public PrivateChat(User sender, User getter,TextChannel channel, Message message) {
        this.sender = sender;
        this.getter = getter;
        this.getterMessage = message;
        this.channel = channel;
        this.lastBotMessages = new ArrayList<>();
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
        this.senderMessage = senderMessage;
    }

    public Message getGetterMessage() {
        return getterMessage;
    }

    public void setGetterMessage(Message getterMessage) {
        this.getterMessage = getterMessage;
    }

    // Messages list methods
    public List<Message> getSenderMessages() {
        return senderMessages;
    }

    public List<Message> getGetterMessages() {
        return getterMessages;
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

    public HashMap<User, Message> getReplyMessage() {
        return replyMessage;
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

        PrivateChannel privateChannel = getGetter().openPrivateChannel().complete();
        Message message = privateChannel.sendMessage(new PrivateChatFilterManager(msg).toFilter(getGetter())).complete();
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

    private int generateFileCode() {
        Random random = new Random();
        int randomint = random.nextInt(99999999);

//        if (fileNameCheck(randomint) == false) {
//            randomint = random.nextInt(99999999);
//            return randomint;
//        }

        return randomint;
    }

    private boolean fileNameCheck(int file) {
        if (this.file.getName().contains(String.valueOf(file))) return false;
        else return true;
    }
}
