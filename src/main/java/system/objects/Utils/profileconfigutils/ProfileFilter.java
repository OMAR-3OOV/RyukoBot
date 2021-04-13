package system.objects.Utils.profileconfigutils;

import java.util.Arrays;
import java.util.HashMap;

public class ProfileFilter {

    private String key;

    public ProfileFilter(String key) {
        this.key = key;
        ProfileInfoFilter.createFilters();
    }

    public HashMap<String, String> toFilterDisplayName() {
        return ProfileInfoFilter.getDisplayNames();
    }
    public HashMap<String, HashMap<String, String>> toFilter() {
        return ProfileInfoFilter.getFilterDisplayName();
    }

    public String toFilterEmoji() {
        return ProfileInfoFilter.getFilterEmoji().get(this.key);
    }

    public boolean isNULL() {
        return Arrays.stream(ProfileInfoFilter.values()).anyMatch(any -> any.getKey().contains(this.key));
    }
}
