package dev.isnow.foxac.check;

import dev.isnow.foxac.FoxAC;
import dev.isnow.foxac.config.impl.CheckInConfig;
import dev.isnow.foxac.data.PlayerData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.Locale;

/**
 * @author 5170
 * made on dev.isnow.foxac.check
 */

@Getter
public abstract class Check {

    @Setter
    private PlayerData data;

    private final String name, type, description;
    private final Category category;
    private final Boolean experimental;
    private final int maxvl;

    private int vl;

    public Check(String name, String type, boolean experimental) {

        this.name = name;
        this.type = type;

        CheckInConfig checkInConfig = FoxAC.getInstance().getConfiguration().getChecks().get(name + type);
        this.category = checkInConfig.getCategory();
        this.experimental = experimental;
        this.maxvl = checkInConfig.getMaxvl();
        this.description = checkInConfig.getDescription();

    }

    public abstract void process(PlayerData data);
    public void fail() {
        if (data == null) {
            return; // WTF?
        }
        vl++;
        Bukkit.broadcastMessage(data.getPlayer().getName() + " flagged: " + name + type.toUpperCase(Locale.ROOT) + " vl: " + vl);
    }


}
