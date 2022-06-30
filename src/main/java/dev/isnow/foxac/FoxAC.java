package dev.isnow.foxac;

import com.github.retrooper.packetevents.PacketEvents;
import dev.isnow.foxac.check.CheckManager;
import dev.isnow.foxac.config.Config;
import dev.isnow.foxac.data.PlayerDataManager;
import dev.isnow.foxac.packet.PacketProcessor;
import dev.isnow.foxac.pledge.PledgeLoader;
import dev.isnow.foxac.util.MessageUtils;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class FoxAC extends JavaPlugin {

    @Getter
    private static FoxAC instance;
    @Getter
    private final PlayerDataManager dataManager = new PlayerDataManager();

    @Getter
    private final CheckManager checkManager = new CheckManager();
    @Getter
    private Config configuration;

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

        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));

        PacketEvents.getAPI().getSettings()
                .checkForUpdates(false)
                .bStats(false)
                .debug(false);
        PacketEvents.getAPI().load();

        long start = System.currentTimeMillis();

        MessageUtils.sendConsoleMessage(" /$$$$$$$$ /$$$$$$  /$$   /$$        /$$$$$$  /$$   /$$ /$$$$$$$$ /$$$$$$  /$$$$$$  /$$   /$$ /$$$$$$$$  /$$$$$$  /$$$$$$$$");
        MessageUtils.sendConsoleMessage("| $$_____//$$__  $$| $$  / $$       /$$__  $$| $$$ | $$|__  $$__/|_  $$_/ /$$__  $$| $$  | $$| $$_____/ /$$__  $$|__  $$__/");
        MessageUtils.sendConsoleMessage("| $$     | $$  \\ $$|  $$/ $$/      | $$  \\ $$| $$$$| $$   | $$     | $$  | $$  \\__/| $$  | $$| $$      | $$  \\ $$   | $$   ");
        MessageUtils.sendConsoleMessage("| $$$$$  | $$  | $$ \\  $$$$/       | $$$$$$$$| $$ $$ $$   | $$     | $$  | $$      | $$$$$$$$| $$$$$   | $$$$$$$$   | $$   ");
        MessageUtils.sendConsoleMessage("| $$__/  | $$  | $$  >$$  $$       | $$__  $$| $$  $$$$   | $$     | $$  | $$      | $$__  $$| $$__/   | $$__  $$   | $$   ");
        MessageUtils.sendConsoleMessage("| $$     | $$  | $$ /$$/\\  $$      | $$  | $$| $$\\  $$$   | $$     | $$  | $$    $$| $$  | $$| $$      | $$  | $$   | $$   ");
        MessageUtils.sendConsoleMessage("| $$     |  $$$$$$/| $$  \\ $$      | $$  | $$| $$ \\  $$   | $$    /$$$$$$|  $$$$$$/| $$  | $$| $$$$$$$$| $$  | $$   | $$   ");
        MessageUtils.sendConsoleMessage("|__/      \\______/ |__/  |__/      |__/  |__/|__/  \\__/   |__/   |______/ \\______/ |__/  |__/|________/|__/  |__/   |__/   ");
        MessageUtils.sendConsoleMessage("Loading config...");
        configuration = new Config();

        MessageUtils.sendConsoleMessage("Checking license...");
        // LicenseUtils.checkLicense();

        MessageUtils.sendConsoleMessage("Welcome, &cDEV-BUILD" );

        MessageUtils.sendConsoleMessage("Initializing PacketEvents...");
        PacketEvents.getAPI().getEventManager().registerListener(new PacketProcessor());
        PacketEvents.getAPI().init();

        new PledgeLoader().load(this);

        MessageUtils.sendConsoleMessage("Loaded in " + (System.currentTimeMillis() - start) / 1000 + " seconds!");
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }
}
