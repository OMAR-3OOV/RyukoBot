package system.objects.Utils.ranksutils;

import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.util.Properties;

public class RanksManager {

    // files
    private final File mainFile;
    private final File SettingsFile;
    private final File RanksFile;
    private final File ProfileFile;

    // Properties
    private final Properties SettingsProperties;
    private final Properties RanksProperties;
    private Properties profileProperties;

    // User
    private User user;

    // Ranks Calling
    private Ranks ranks;

    // create ranks file

    public RanksManager() {
        this.mainFile = new File("system/RyukoSystem");
        this.SettingsFile = new File("system/RyukoSystem/Settings.properties");
        this.RanksFile = new File("system/RyukoSystem/Ranks/" + ranks.getName() + ".properties");
        this.ProfileFile = new File("system/RyukoSystem/Profile");

        this.SettingsProperties = new Properties();
        this.RanksProperties = new Properties();
    }

    /**
     * Create user profile
     *
     * @param user user to be file
     */
    public RanksManager(User user) {
        this.user = user;

        this.mainFile = new File("system/RyukoSystem");
        this.SettingsFile = new File("system/RyukoSystem/Settings.properties");
        this.RanksFile = new File("system/RyukoSystem/Ranks/" + ranks.getName() + ".properties");
        this.ProfileFile = new File("system/RyukoSystem/Profile/" +user.getId()+".properties");

        this.SettingsProperties = new Properties();
        this.RanksProperties = new Properties();
        this.profileProperties = new Properties();
    }


    //
    // Files Methods
    //

    @Contract(pure = true)
    public File getMainFile() {
        return mainFile;
    }

    public File getSettingsFile() {
        return SettingsFile;
    }

    public File getProfileFile() {
        return ProfileFile;
    }

    //
    // Properties Methods
    //

    public Properties getSettingsProperties() {
        return SettingsProperties;
    }

    public Properties getProfileProperties() {
        return profileProperties;
    }

    //
    // Builds Methods
    //

    //
    // rank methods
    //

    public void getRank() {

    }

    public void setRank() {

    }

    public boolean hasRank() {
        return false;
    }

    public void createRank() {

    }

    //
    // About User
    //

    public String getAbout() {
        return "";
    }

}
