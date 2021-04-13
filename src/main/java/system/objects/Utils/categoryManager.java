package system.objects.Utils;

import system.objects.Category;

public class categoryManager {

    /* Related Category */
    private String name;
    private String description;
    private String email;

    public categoryManager(Category category) {
        this.name = category.getName();
        this.description = category.getDescription();
        this.email = category.getEmoji();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }
}
