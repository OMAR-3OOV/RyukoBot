package system.Objects.Utils.BoosterUtils;

import java.util.Collection;
import java.util.HashMap;

public class Booster {

    private final int sort;
    private final String name;
    private final int combo;
    private final int cost;
    private final String emoji;

    public static HashMap<Integer, BoosterManagement> booster = new HashMap<>();

    public Booster(BoosterManagement bm) {
        this.sort = bm.getId();
        this.name = bm.getName();
        this.combo = bm.getCombo();
        this.cost = bm.getCost();
        this.emoji = bm.getEmoji();
    }

    public static void loadBoosters() {
        // Add booster
        addBooster(BoosterManagement.BOOSTER_DOUBLE);
        addBooster(BoosterManagement.BOOSTER_TREBLE);
        addBooster(BoosterManagement.BOOSTER_QUADRUPLE);
    }

    public static HashMap<Integer, BoosterManagement> getBooster() {
        return booster;
    }

    private static void addBooster(BoosterManagement bm) {
        if (!booster.containsKey(bm)) {
            booster.put(bm.getId(), bm);
        }
    }

    private Collection<BoosterManagement> getBoosters() {
        return booster.values();
    }

    public int getSort() {
        return sort;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getName() {
        return name;
    }

    public int getCombo() {
        return combo;
    }

    public int getCost() {
        return cost;
    }

    public void addBoosters() {

    }
}
