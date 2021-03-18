package system.Objects;

import org.jetbrains.annotations.Contract;

public enum Category {

    MANAGEMENT(0,"Management", "This category make easy to management your server", "\uD83D\uDEE1"),
    MODERATOR(1,"Moderator", "This category make easy work for moderator", "\uD83D\uDD30"),
    INFORMATION(2,"Information", "This category to get informations", "\uD83D\uDCCA"),
    FUN(3, "Fun", "This command to get reaction pictures or fun commands", "\uD83C\uDF8A"),
    MINECRAFT(4,"Minecraft", "This category to get minecraft information and hypixel server info", "\uD83C\uDF32"),
    NSFW(5,"Nsfw", "This category to get nsfw pictures", "\uD83D\uDD1E");

    private final String name;
    private final String description;
    private final String emoji;
    private final int id;

    @Contract(pure = true)
    Category(int id, String name, String description, String emoji) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.emoji = emoji;
    }

    @Contract(pure = true)
    public int getId() {
        return id;
    }

    @Contract(pure = true)
    public String getName() {
        return name;
    }

    @Contract(pure = true)
    public String getDescription() {
        return description;
    }

    @Contract(pure = true)
    public String getEmoji() {
        return emoji;
    }

    @Contract(pure = true)
    public final boolean isNull() {
        return getName() == null;
    }

    public final Category getCategory(int categoryId) {
        return Category.values()[categoryId];
    }
}
