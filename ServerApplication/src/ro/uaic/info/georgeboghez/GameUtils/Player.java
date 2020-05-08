package ro.uaic.info.georgeboghez.GameUtils;

public class Player {
    private String name;
    private boolean isFirst;

    public Player(String name, boolean isFirst) {
        this.name = name;
        this.isFirst = isFirst;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }
}
