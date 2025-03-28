package Generic;

public enum Consts {
    BUFNAME("buf");

    private final String name;
    Consts(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
