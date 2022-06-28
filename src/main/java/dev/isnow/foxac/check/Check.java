package dev.isnow.foxac.check;

public abstract class Check {

    private final String name;
    private final String type;
    private final String description;
    private final Category category;
    private final int maxvl;
    private final boolean experimental;

    private int vl;

    protected Check(String name, String type) {
        this.name = name;
        this.type = type;


    }

}
enum Category {
    COMBAT,
    MOVEMENT,
    PLAYER,
    EXPLOIT
}
