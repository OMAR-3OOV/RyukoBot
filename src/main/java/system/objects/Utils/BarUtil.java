package system.objects.Utils;

import kotlin.text.StringsKt;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@NotNull
public class BarUtil {

    public static final BarUtil instance = new BarUtil();

    private float task;
    private float total;
    private float barIndex;

    public BarUtil() {

    }

    /**
     *
     * Builder consumer for class
     * @param onBuild to put class details on consumer
     * @return to class
     */
    public BarUtil build(@NotNull Consumer<BarUtil> onBuild) {
        onBuild.accept(this);
        return instance;
    }

    @Contract(pure = true)
    public float getBarIndex() {
        return barIndex;
    }

    @Contract(pure = true)
    public float getTask() {
        return task;
    }

    @Contract(pure = true)
    public float getTotal() {
        return total;
    }

    @Contract(pure = true)
    public void setTask(float task) {
        this.task = task;
    }

    /**
     *
     * @param total to total bar index
     */
    @Contract(pure = true)
    public void setTotal(float total) {
        if (total < 0) {
            throw new IllegalArgumentException("Total float must be more than 0");
        }

        this.total = total;
    }

    /**
     *
     * @param barIndex to bar index
     */
    @Contract(pure = true)
    public void setBarIndex(float barIndex) {
        if (barIndex < 0) {
            throw new IllegalArgumentException("Bar index can't be 0, the string bar will be empty");
        }

        this.barIndex = barIndex;
    }

    @Contract(pure = true)
    public String displayTaskBar(float total, float barIndex) {

        String display = StringsKt.repeat("█", (int) Math.round(total)) + StringsKt.repeat("░", (int) (barIndex - Math.round(total)));

        if (display.isEmpty()) {
            throw new IllegalArgumentException("Display bar cannot be empty!");
        }

        return display;
    }
}
