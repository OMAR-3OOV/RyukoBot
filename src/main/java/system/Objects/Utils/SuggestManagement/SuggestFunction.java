package system.Objects.Utils.SuggestManagement;

import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.Contract;

import java.util.*;

public final class SuggestFunction {

    private User user;

    private SuggestQuestions questions;
    private int QuestionCount;
    private List<String> SuggestList;
    private boolean started = false;
    private SortProperties soted = new SortProperties();

    private final SuggestManager suggestManager;

    public SuggestFunction() {
        this.suggestManager = new SuggestManager();
    }

    public SuggestFunction(User user) {
        this.suggestManager = new SuggestManager(user);
        this.user = user;
    }

    @Contract(pure = true)
    public User getUser() {
        return user;
    }

    @Contract(pure = true)
    public SuggestManager getSuggestManager() {
        return suggestManager;
    }

    @Contract(pure = true)
    public SuggestQuestions getQuestions() {
        return questions;
    }

    @Contract(pure = true)
    public int getQuestionCount() {
        return QuestionCount;
    }

    @Contract(pure = true)
    public List<String> getSuggestList() {
        return SuggestList;
    }

    @Contract(pure = true)
    public SortProperties getSoted() {
        return soted;
    }

    @Contract(mutates = "this")
    public void build() {
        QuestionCount = 0;
        SuggestList = new ArrayList<>();
    }

    public void addQusetion(String string) {
        getSuggestList().add(string);
    }

    public void setQusetion(int index, String string) {
        getSuggestList().set(index, string);
    }

    public void removeQusetion(int index) {
        getSuggestList().set(index, null);
    }

    @Contract(pure = true)
    public boolean isQuestion(int questionIndex) {
        if (getQuestionCount() == questionIndex) {
            return true;
        } else return false;
    }

    @Contract(mutates = "this")
    public void next() {
        if (QuestionCount == 2) {
            QuestionCount = 0;
            return;
        }
        QuestionCount++;
    }

    @Contract(mutates = "this")
    public void back() {
        if (QuestionCount == 0) {
            QuestionCount = (questions.values().length - 1);
            return;
        }
        QuestionCount--;
    }

    @Contract(mutates = "this")
    public void setStarted(boolean bool) {
        started = bool;
    }

    @Contract(pure = true)
    public boolean isStarted() {
        return started;
    }


    public void done() {

        if (getSuggestList().isEmpty()) {
            System.out.println("sorry this form is empty :(");
            return;
        }

        SortProperties soted = getSoted();

        soted.put("username", getUser().getName());
        soted.put("id", getUser().getId());
        soted.put("date", String.valueOf(new Date().getTime()));
        soted.put("status", "waiting");
        soted.put("ticket-id", String.valueOf(getSuggestManager().getId()));

        for (int i = 0; i < getSuggestList().size(); i++) {
            soted.put(String.valueOf(i), getSuggestList().get(i));
        }

        getSuggestManager().addSuggestion();
        getSuggestManager().save(soted, suggestManager.getSuggestFileUser());
    }

    public class SortProperties extends Properties {
        public Enumeration keys() {
            Enumeration keysEnum = super.keys();
            Vector<String> keyList = new Vector<>();

            while (keysEnum.hasMoreElements()) {
                keyList.add((String) keysEnum.nextElement());
            }

            Collections.sort(keyList, Collections.reverseOrder());
            return keyList.elements();
        }
    }

}
