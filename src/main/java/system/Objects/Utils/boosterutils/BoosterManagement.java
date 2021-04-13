package system.Objects.Utils.boosterutils;

public enum BoosterManagement {
    BOOSTER_DOUBLE(0, "Double" , 2, 2000 , "\uD83D\uDFE7"),
    BOOSTER_TREBLE(1, "Treble", 3, 4000, "\uD83D\uDFEA"),
    BOOSTER_QUADRUPLE(2, "Quadruple" , 4, 6000, "\uD83D\uDFE5");

    private int id;
    private String name;
    private int combo;
    private int cost;
    private String emoji;

    BoosterManagement(int sort, String name, int combo, int cost, String emoji) {
        this.id = sort;
        this.name = name;
        this.combo = combo;
        this.cost = cost;
        this.emoji = emoji;
    }

    public int getCombo() {
        return combo;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public String getEmoji() {
        return emoji;
    }
}
