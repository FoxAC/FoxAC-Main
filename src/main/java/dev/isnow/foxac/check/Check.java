package dev.isnow.foxac.check;

import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkDataBulk;
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


    protected final PlayerData data;

    private final String name, type, description;
    private final Category category;
    private final Boolean experimental;
    private final int maxvl;

    private int vl;

    public Check(String name, String type, boolean experimental, PlayerData data) {

        this.name = name;
        this.type = type;
        this.data = data;

        CheckInConfig checkInConfig = FoxAC.getInstance().getConfiguration().getChecks().get(name + type);
        this.category = checkInConfig.getCategory();
        this.experimental = experimental;
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
