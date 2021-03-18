package system.Objects.TextUtils.EmojiUtils;

import net.dv8tion.jda.api.entities.Activity;

public enum XClock {

    // Night
    CLOCK_12PM("12:01","pm", 0, "clock12"),
    CLOCK_1P("1:00", "pm", 1, "clock1"),
    CLOCK_2P("2:00", "pm", 2, "clock2"),
    CLOCK_3P("3:00", "pm", 3, "clock3"),
    CLOCK_4P("4:00", "pm", 4, "clock4"),
    CLOCK_5P("5:00", "pm", 5, "clock5"),
    CLOCK_6P("6:00", "pm", 6, "clock6"),
    CLOCK_7P("7:00", "pm", 7, "clock7"),
    CLOCK_8P("8:00", "pm", 8, "clock8"),
    CLOCK_9P("9:00", "pm", 9, "clock9"),
    CLOCK_10P("10:00","pm", 10, "clock10"),
    CLOCK_11P("11:00","pm", 11, "clock11"),
    CLOCK_12P("12:00","pm", 12, "clock12"),

    // Morning
    CLOCK_12AM("12:01","am",12_1, "clock1230"),
    CLOCK_1A("1:00","am", 1_1, "clock130"),
    CLOCK_2A("2:00","am", 2_1, "clock230"),
    CLOCK_3A("3:00","am", 3_1, "clock330"),
    CLOCK_4A("4:00","am", 4_1, "clock430"),
    CLOCK_5A("5:00","am", 5_1, "clock530"),
    CLOCK_6A("6:00","am", 6_1, "clock630"),
    CLOCK_7A("7:00","am", 7_1, "clock730"),
    CLOCK_8A("8:00","am", 8_1, "clock830"),
    CLOCK_90A("9:00","am", 9_1, "clock930"),
    CLOCK_10A("10:00","am", 10_1, "clock1030"),
    CLOCK_11A("11:00","am", 11_1, "clock1130"),
    CLOCK_12AF("12:00","am", 12_2, "clock1230");

    private String name;
    private String interval;
    private int id;
    private String emoji;

    XClock(String clockString, String interval, int id, String emoji) {

    }
}
