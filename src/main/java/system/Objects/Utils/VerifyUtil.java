package system.objects.Utils;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import system.objects.Utils.profileconfigutils.ProfileBuilder;

import java.util.HashMap;
import java.util.Timer;

public class VerifyUtil {

    /* APIS */
    private final RandomStringAPI rsu;
    private final ProfileBuilder profile;
    private final Timer timer;

    /* Functions */
    private final User user;
    private final TextChannel channel;
    private Message message;

    public HashMap<User, String> verify = new HashMap<>();

    public VerifyUtil (User user ,TextChannel channel , Message message) {
        this.user = user;
        this.channel = channel;
        this.profile = new ProfileBuilder(this.user);
        this.rsu = new RandomStringAPI(user, 20);
        this.message = message;
        this.timer = new Timer();

        verify.put(this.user, rsu.getKey());
    }

    public User getUser() {
        return user;
    }

    public ProfileBuilder getProfile() {
        return profile;
    }

    public TextChannel getChannel() {
        return channel;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public RandomStringAPI getRsu() {
        return rsu;
    }

    public HashMap<User, String> getVerify() {
        return verify;
    }

    public void setVerify(Boolean bool) {
        this.profile.setVerify(bool);
    }

    public Timer getTimer() {
        return timer;
    }

    public void close() {
        this.timer.cancel();
        verify.remove(this.user);
    }
}
