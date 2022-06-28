package dev.isnow.foxac.check;

import dev.isnow.foxac.data.PlayerData;

/**
 * @author 5170
 * made on dev.isnow.foxac.check
 */

public abstract class Check {

    private final String name;
    private final String type;
    private final Category category;
    private final boolean experimental;

    private final String description;
    private final int maxvl;

    private int vl;

    protected Check(String name, String type, Category category, boolean experimental) {
        this.name = name;
        this.type = type;
        this.category = category;
        this.experimental = experimental;


    }

    public abstract void process(PlayerData data);
}
