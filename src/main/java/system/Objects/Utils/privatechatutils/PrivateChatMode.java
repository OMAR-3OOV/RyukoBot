package system.Objects.Utils.privatechatutils;

public enum PrivateChatMode {

    CHATTING(0, "chat"),
    VIEW(1, "view");

    private String name;
    private int id;

    PrivateChatMode(int id, String name) {
        this.name = name;
        this.id = id;
    }
    public String getName() {

        return name;
    }

    public int getId() {
        return id;
    }

}
