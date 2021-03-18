package system.Objects.Utils.chatbot;

import java.util.Random;

public enum Emotions {

    SADNESS("(ノ _ <。)", "(´-ω-`)", "o (T ヘ To)", "(｡╯︵╰｡)", "(> q <)", "(╥_╥)", "(つ ω`｡)", "(っ ˘̩╭╮˘̩) っ", "｡ ﾟ ･ (> ﹏ <) ･ ﾟ｡", "o (T ヘ To)"),
    HAPPINESS("(* ^ ω ^)", "٩ (◕‿◕｡) ۶", "(＠ ＾ ◡ ＾)", "o (≧ ▽ ≦) o", "(´｡ • ᵕ • ｡`)", "(„• - •„)", "٩ (｡ • ́‿ • ̀｡) ۶", "ヽ (o ＾ ▽ ＾ o) ノ", "(≧ ◡ ≦)", "(.❛ ᴗ ❛.)"),
    FEAR("(/ _＼)", "〜 (＞ ＜) 〜", "＼ (〇_ｏ) ／", ".. ・ ヾ (。 ＞ ＜) シ","(ノ ω ヽ)", "＼ (º □ º l | l) /"),
    ANGER("(＃ `D´)", "(`皿 ´ ＃)", "(`ω ´)", "ヽ (`d´ *) ノ", "(・ `Ω´ ・)", "(`ー ´)", "ヽ (`⌒´ メ) ノ", "凸 (`△ ´ ＃)", "(`ε`)", "ψ (`∇ ´) ψ", "ヾ (`ヘ ´) ﾉ ﾞ", "ヽ (‵ ﹏´) ノ", "(ﾒ `ﾛ ´)", "(҂ `z ')", "(҂` ﾛ ´) 凸", "＼＼٩ (๑` ^ ´๑) ۶ ／／"),
    SURPRISE("w (° ｏ °) w", "ヽ (° 〇 °) ﾉ", "(o_O)!", "(□ _ □)", "(: ౦ ‸ ౦:)", "Σ (° ロ °)"),
    DISGUST("(￣ ﾊ ￣ *)", "ﾍ (･ _ |", "__φ (．．)", "∪￣-￣∪", "ヾ (￣ ◇ ￣) ノ 〃"),
    MAGIC("(ﾉ ≧ ∀ ≦) ﾉ ‥… ━━━ ★","(ﾉ> ω <) ﾉ:｡ ･: *:","(⊃｡ • ́‿ • ̀｡) ⊃━✿✿✿✿✿✿"),
    RUN("☆ ﾐ (o * ･ ω ･) ﾉ", "C = C = ┌ (; ・ ω ・) ┘", "─ = ≡Σ (((つ ＞ ＜) つ", "。。。 ミ ヽ (。 ＞ ＜) ノ", "ε === (っ ≧ ω ≦) っ"),
    HUGS("(づ ￣ ³￣) づ", "(つ ≧ ▽ ≦) つ", "(⊃｡ • ́‿ • ̀｡) ⊃", "(づ ◕‿◕) づ","⊂ (´ ▽ `) ⊃"),
    DOUBT("(￢_￢)", "(¬ ¬)", "(← _ ←)", "(¬_¬)"),
    CONFUSION("(・ _ ・;)", "(・ _ ・ ヾ", "(￣ ～ ￣;)", "(¯. ¯٥)", "(• ิ _ • ิ)?"),
    INDIFFERENCE("ヽ (ー _ ー) ノ", "ヽ (´ ー `) ┌", "┐ (︶ ▽ ︶) ┌", "╮ (￣ ～ ￣) ╭", "┐ ('～ `) ┌", "┐ (￣ ヮ ￣) ┌"),
    PAIN("~ (> _ <~)", "☆ ⌒ (>。 <)", "(× _ ×) ⌒ ☆"),
    DISCONTENT("(＃ ＞ ＜)", "(」° ロ °)」", "(￣︿￣)", "(＞ ﹏ ＜)", "(￣ ヘ ￣)", "(＃ ￣0￣)"),
    SYMPATHY("(ノ _ <。) ヾ (´ ▽ `)", "ρ (- ω - 、) ヾ (￣ω￣;)", "ヽ (￣ω￣ (。。) ゝ", "(っ ´ω`) ﾉ (╥ω╥)", "(ｏ ・ _ ・) ノ ”(ノ _ <、)"),
    EMBARRASSMENT("(o ^ ^ o)", "(◡‿◡ *)", "(⁄ ⁄ • ⁄ω⁄ • ⁄ ⁄)", "(// ω //)", "(* / ▽ ＼ *)", "(* ﾉ ∀` *)", "(⁄ ⁄> ⁄ ▽ ⁄ <⁄ ⁄)", "(* ﾉ ω ﾉ)"),
    LOVE("(´ • ω • `) ♡", "♡ (￣З￣)", "(≧ ◡ ≦) ♡", "(´｡ • ᵕ • ｡`) ♡", "(っ ˘з (˘⌣˘) ♡", "Σ> - (〃 ° ω ° 〃) ♡ →", "╰ (* ´︶` *) ╯ ♡", "♡ (◡‿◡)");

    private String[] emotions;

    Emotions(String... types) {
        this.emotions = types;
    }

    public String[] value() {
        switch (this) {
            case RUN: return RUN.emotions;
            case FEAR: return FEAR.emotions;
            case HUGS: return HUGS.emotions;
            case LOVE: return LOVE.emotions;
            case PAIN: return PAIN.emotions;
            case ANGER: return ANGER.emotions;
            case DOUBT: return DOUBT.emotions;
            case MAGIC: return MAGIC.emotions;
            case DISGUST: return DISGUST.emotions;
            case SADNESS: return SADNESS.emotions;
            case SURPRISE: return SURPRISE.emotions;
            case SYMPATHY: return SYMPATHY.emotions;
            case CONFUSION: return CONFUSION.emotions;
            case HAPPINESS: return HAPPINESS.emotions;
            case DISCONTENT: return DISCONTENT.emotions;
            case INDIFFERENCE: return INDIFFERENCE.emotions;
            case EMBARRASSMENT: return EMBARRASSMENT.emotions;
        }
        return null;
    }

    public static String getRandomRun() {
        Random random = new Random();
        int randomInt = random.nextInt(RUN.value().length);
        return RUN.value()[randomInt];
    }

    public static String getRandomFear() {
        Random random = new Random();
        int randomInt = random.nextInt(FEAR.value().length);
        return FEAR.value()[randomInt];
    }

    public static String getRandomHug() {
        Random random = new Random();
        int randomInt = random.nextInt(HUGS.value().length);
        return HUGS.value()[randomInt];
    }

    public static String getRandomLove() {
        Random random = new Random();
        int randomInt = random.nextInt(LOVE.value().length);
        return LOVE.value()[randomInt];
    }

    public static String getRandomPain() {
        Random random = new Random();
        int randomInt = random.nextInt(PAIN.value().length);
        return PAIN.value()[randomInt];
    }

    public static String getRandomAnger() {
        Random random = new Random();
        int randomInt = random.nextInt(ANGER.value().length);
        return ANGER.value()[randomInt];
    }

    public static String getRandomDoubt() {
        Random random = new Random();
        int randomInt = random.nextInt(DOUBT.value().length);
        return DOUBT.value()[randomInt];
    }

    public static String getRandomMagic() {
        Random random = new Random();
        int randomInt = random.nextInt(MAGIC.value().length);
        return MAGIC.value()[randomInt];
    }

    public static String getRandomDisgust() {
        Random random = new Random();
        int randomInt = random.nextInt(DISGUST.value().length);
        return DISGUST.value()[randomInt];
    }

    public static String getRandomSadness() {
        Random random = new Random();
        int randomInt = random.nextInt(SADNESS.value().length);
        return SADNESS.value()[randomInt];
    }

    public static String getRandomSurprise() {
        Random random = new Random();
        int randomInt = random.nextInt(SURPRISE.value().length);
        return SURPRISE.value()[randomInt];
    }

    public static String getRandomSympathy() {
        Random random = new Random();
        int randomInt = random.nextInt(SYMPATHY.value().length);
        return SYMPATHY.value()[randomInt];
    }

    public static String getRandomConfusion() {
        Random random = new Random();
        int randomInt = random.nextInt(CONFUSION.value().length);
        return CONFUSION.value()[randomInt];
    }

    public static String getRandomHappiness() {
        Random random = new Random();
        int randomInt = random.nextInt(HAPPINESS.value().length);
        return HAPPINESS.value()[randomInt];
    }

    public static String getRandomDiscontent() {
        Random random = new Random();
        int randomInt = random.nextInt(DISCONTENT.value().length);
        return DISCONTENT.value()[randomInt];
    }

    public static String getRandomEmbarrassment() {
        Random random = new Random();
        int randomInt = random.nextInt(EMBARRASSMENT.value().length);
        return EMBARRASSMENT.value()[randomInt];
    }
}
