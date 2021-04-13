package system.Objects.Utils.levelUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface LevelsCalculations {

    List<Double> EXP = Collections.unmodifiableList(Arrays.asList(
            10000d,
            12500d,
            15000d,
            27500d,
            30000d,
            32500d,
            35000d,
            37500d,
            40000d,
            42500d
    ));

    double LEVEL_MAX = 50;
    double MAX = EXP.get(EXP.size()- 1);

    static double getLevelFromExp(double exp) {
        if (exp < 0) {
            throw new IllegalArgumentException("Experience shouldn't be unless under zero");
        }

        for (double level = 1d; ;level++) {
            double needed = getExpToNextLevel(level);
            exp -= needed;

            if (level >= 50d) {
                return LEVEL_MAX;
            }

            if (exp < 0) {
                return level;
            }
        }
    }

    static double getExpToNextLevel(double level) {
        if (level < 0) {
            throw new IllegalArgumentException("Experience shouldn't be unless under zero");
        }

        return level>= EXP.size()? MAX : EXP.get((int) level);
    }

    static double getTotalExpToNextLevel(double exp) {
        if (exp < 0) {
            throw new IllegalArgumentException("Experience shouldn't be unless under zero");
        }

        for (double level = 0d; ;level++) {
            double needed = getExpToNextLevel(level);
            exp -= needed;

            if (exp < 0) {
                return -exp;
            }
        }
    }
}
