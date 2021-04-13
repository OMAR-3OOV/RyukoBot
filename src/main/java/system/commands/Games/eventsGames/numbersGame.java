package system.commands.Games.eventsGames;

import net.dv8tion.jda.api.entities.*;

import java.util.HashMap;

public class numbersGame {

    // Game options
    private final int gameKeyWinner;
    private final String gameName;
    private final int GameID;
    private final Guild guild;
    private final Message message;
    private final int maxNumberCount;
    private final Role reward;
    private final String date;
    private final TextChannel textChannel;

    public static final HashMap<Guild , numbersGame> numbersStart = new HashMap<>();
    private final HashMap<Integer, User> winner = new HashMap<>();

    public numbersGame(String name, Guild guild, TextChannel channel, int gameID, int gameKey, Message message, int numberCount, Role reward, String date) {
        this.gameName = name;
        this.GameID = gameID;
        this.guild = guild;
        this.textChannel = channel;
        this.gameKeyWinner = gameKey;
        this.message = message;
        this.maxNumberCount = numberCount;
        this.reward = reward;
        this.date = date;
    }

    public String getGameName() {
        return gameName;
    }

    public int getGameID() {
        return GameID;
    }

    public Guild getGuild() {
        return guild;
    }

    public int getGameKeyWinner() {
        return gameKeyWinner;
    }

    public Role getReward() {
        return reward;
    }

    public TextChannel getTextChannel() {
        return textChannel;
    }

    public Message getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public int getMaxNumberCount() {
        return maxNumberCount;
    }

    public HashMap<Integer, User> getWinner() {
        return winner;
    }

    public HashMap<Guild, numbersGame> getNumbersStart() {
        return numbersStart;
    }
}
