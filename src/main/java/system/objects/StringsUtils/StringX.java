package system.objects.StringsUtils;

public enum StringX {

    DISCORD("https://i.imgur.com/UM8FDd7.png");

    private String string;

    StringX(String s) {
        this.string = s;

    }

    @Override
    public String toString() {
        return this.string;
    }
}
