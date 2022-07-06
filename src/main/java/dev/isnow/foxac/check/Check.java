package dev.isnow.foxac.check;

import dev.isnow.foxac.FoxAC;
import dev.isnow.foxac.config.impl.CheckInConfig;
import dev.isnow.foxac.data.PlayerData;
import dev.isnow.foxac.packet.event.FPacketEvent;
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
    protected PlayerData data;

    private final String name, type, description;
    private final Boolean experimental;
    private final Category category;
    private final int maxvl;

    private int vl;

    public Check(String name, String type, boolean experimental) {

        this.name = name;
        this.type = type;

        this.experimental = experimental;

        CheckInConfig checkInConfig = FoxAC.getInstance().getConfiguration().getChecks().get(name.toLowerCase(Locale.ROOT) + type.toLowerCase(Locale.ROOT));
        this.category = checkInConfig.getCategory();
        this.maxvl = checkInConfig.getMaxvl();
        this.description = checkInConfig.getDescription();

    }

    public abstract void handleCheck(FPacketEvent packetEvent);

    public void fail() {
        if (data == null) {
            return; // WTF?
        }
        vl++;
        Bukkit.broadcastMessage(data.getPlayer().getName() + " flagged: " + name + type.toUpperCase(Locale.ROOT) + " vl: " + vl);
    }


}
