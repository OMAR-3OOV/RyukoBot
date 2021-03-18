package system.Objects.Utils.SuggestManagement;

public enum SuggestQuestions {

    TITLE("Put the suggest title ❓", 0),
    EXPLAINING("explain your suggest idea ❗", 1),
    ASK("Why do you think its important idea ❓", 2);

    private String text;
    private int id;

    SuggestQuestions(String text, int id) {
        this.text = text;
        this.id = id;
    }

    public String getQuestion() {
        return text;
    }

    public int getId() {
        return id;
    }

    public static SuggestQuestions get(int number) {
        return SuggestQuestions.values()[number];
    }

}
