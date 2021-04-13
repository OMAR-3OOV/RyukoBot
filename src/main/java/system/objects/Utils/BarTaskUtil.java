package system.objects.Utils;

import kotlin.text.StringsKt;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BarTaskUtil {

    private final List<String> bars;
    private final float barIndex;
    private final Message message;

    private final HashMap<MessageReaction, String> bar = new HashMap<>();
    private final HashMap<MessageReaction, Float> percentage = new HashMap<>();

    public BarTaskUtil(Message message, float barIndex) {
        this.bars = new ArrayList<>();
        this.barIndex = barIndex;
        this.message = message;

        createTask();
    }

    public Message getMessage() {
        return message;
    }

    public float getBarIndex() {
        return barIndex;
    }

    public List<String> getBars() {
        return bars;
    }

    public HashMap<MessageReaction, String> getBar() {
        return bar;
    }

    public HashMap<MessageReaction, Float> getPercentage() {
        return percentage;
    }

    public HashMap<MessageReaction, String> createTask() {
        AtomicInteger allReact = new AtomicInteger(0);

        this.message.getReactions().forEach(r -> { // reactions
            allReact.addAndGet(r.getCount());
        });

        this.message.getReactions().forEach(react -> { // reactions per emoji
            float allAsFloat = allReact.get();
            final float total = (react.getCount() / allAsFloat * this.barIndex);

            final String msg = StringsKt.repeat("█", (Math.round(total))) + StringsKt.repeat("░", (int) (this.barIndex - Math.round(total)));
            this.bars.add(msg);

            this.percentage.put(react, (total*this.barIndex));
            this.bar.put(react, msg);
        });

        return this.bar;
    }

}
