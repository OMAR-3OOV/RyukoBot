package system.Objects.Utils;

import net.dv8tion.jda.api.entities.User;
import java.util.HashMap;

public class RandomStringAPI {

    HashMap<User, String> map;
    User user;
    String key;

    public RandomStringAPI(User user, int number) {
        this.user = user;
        this.map = new HashMap<>();
        this.key = generateKey(number);

        map.put(user, this.key);
    }

    public String getKey() {
        return key;
    }

    public User getUser() {
        return user;
    }

    public HashMap<User, String> getMap() {
        return map;
    }

    private String generateKey(int number) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(number);

        for (int i = 0; i < number; i++) {
            int index = (int) (characters.length()*Math.random());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
