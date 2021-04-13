package system.Objects.Utils.administration.BannedUtils;

/**
 * Created in 2021 - 16 - 3
 * for banned util
 * created by : OMAR
 */
public enum BannedElapsedTimes {

    SECONDS(1000L),
    MINUTE(1000L*60),
    HOURS(1000L*60*60),
    DAYS(1000L*60*60*24),
    WEEKS(1000L*60*60*24*7),
    MONTHS(1000L*60*60*24*30),
    YEARS(1000L*60*60*24*365);

    private final long time;
    BannedElapsedTimes(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
