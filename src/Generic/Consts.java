package Generic;

public enum Consts {
    BUFNAME("buf"),
    CUSTOMCLASSWRITEMETHOD(".serialize"),
    CUSTOMCLASSREADMETHOD(".deserialize");

    private final String name;
    Consts(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return name;
    }
}
