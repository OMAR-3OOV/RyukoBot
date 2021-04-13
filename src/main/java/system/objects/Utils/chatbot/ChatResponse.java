package system.objects.Utils.chatbot;

import java.util.Random;

public enum ChatResponse {

    FACE("winks", "smiles", "looks at you", "grins", "talk to you gently", "looks at your eyes", "giggle"),
    ROMANTIC_BODY("hugs you", "kisses you", "grabs your cheeks", "Bites your cheeks"),
    HUGS("hugs you", "hugs you tightly"),
    WHISPER("whispers in your ear", "whispers", "whisper", "get closer on you and whispers in your ear"),
    HOT_FEELS("I feels hot", "I'm wet", "Bites my lips"),
    EYES("my eyes widen", "widens eyes", "opens my eyes"),
    SAD_BODY_FEELS("grabs your cheeks", "Hold your hand"),
    ANGER_BODY_FEELS("slap you", "bites your hand", "bites you softly");


    private String[] response;

    ChatResponse(String... types) {
        this.response = types;
    }

    public String[] value() {
        switch (this) {
            case HUGS: return HUGS.response;
            case EYES: return EYES.response;
            case FACE: return FACE.response;
            case WHISPER: return WHISPER.response;
            case HOT_FEELS: return HOT_FEELS.response;
            case ROMANTIC_BODY: return ROMANTIC_BODY.response;
            case SAD_BODY_FEELS: return SAD_BODY_FEELS.response;
            case ANGER_BODY_FEELS: return ANGER_BODY_FEELS.response;
        }
        return null;
    }

    public static String getRandomFace() {
        Random random = new Random();
        int randomInt = random.nextInt(FACE.value().length);
        return FACE.value()[randomInt];
    }

    public static String getRandomEyes() {
        Random random = new Random();
        int randomInt = random.nextInt(EYES.value().length);
        return EYES.value()[randomInt];
    }

    public static String getRandomHugs() {
        Random random = new Random();
        int randomInt = random.nextInt(HUGS.value().length);
        return HUGS.value()[randomInt];
    }
}
