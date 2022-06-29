package dev.isnow.foxac;

import com.github.retrooper.packetevents.PacketEvents;
import dev.isnow.foxac.data.PlayerDataManager;
import dev.isnow.foxac.packet.PacketProcessor;
import dev.isnow.foxac.pledge.PledgeLoader;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class FoxAC extends JavaPlugin {

    @Getter
    private static FoxAC instance;

    @Getter
    private final PlayerDataManager manager = new PlayerDataManager();

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));

        PacketEvents.getAPI().getSettings()
                .checkForUpdates(false)
                .bStats(false)
                .debug(false);
        PacketEvents.getAPI().load();
    }
    @Override
    public void onEnable() {
        instance = this;

        PacketEvents.getAPI().getEventManager().registerListener(new PacketProcessor());
        PacketEvents.getAPI().init();

        new PledgeLoader().load(this);

    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }
}
