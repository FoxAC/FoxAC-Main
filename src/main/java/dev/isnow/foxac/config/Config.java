package dev.isnow.foxac.config;

import dev.isnow.foxac.FoxAC;
import dev.isnow.foxac.check.Category;
import dev.isnow.foxac.check.Check;
import dev.isnow.foxac.config.impl.CheckInConfig;
import dev.isnow.foxac.config.impl.Theme;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author 5170
 * made on dev.isnow.foxac.config
 */


@Getter
public class Config {

    private final String key;

    private final Boolean antiVPN, debugMode, clientBlacklist, ghostBlockProcessor;

    private final HashMap<String, Theme> themes = new HashMap<>();
    private final String currentTheme;

    private final HashMap<String, CheckInConfig> checks = new HashMap<>();

    public Config() {
        FileConfiguration config = FoxAC.getInstance().getConfig();

        createAdditionalFiles();
        FileConfiguration themesConfig = YamlConfiguration.loadConfiguration(new File(FoxAC.getInstance().getDataFolder(), "themes.yml"));
        FileConfiguration checksConfig = YamlConfiguration.loadConfiguration(new File(FoxAC.getInstance().getDataFolder(), "checks.yml"));

        key = config.getString("license-key");

        antiVPN = config.getBoolean("settings.anti-vpn");
        debugMode = config.getBoolean("settings.debug-mode");
        clientBlacklist = config.getBoolean("settings.client-blacklist");
        ghostBlockProcessor = config.getBoolean("settings.ghostblock-processor");

        currentTheme = config.getString("current-theme").toLowerCase(Locale.ROOT);

        for(String themeName : themesConfig.getConfigurationSection("themes").getKeys(false)) {
            String staticString = "themes." + themeName + ".";
            themes.put(themeName, new Theme(
                    themesConfig.getString(staticString + "alerts"),
                    themesConfig.getString(staticString + "color_low"),
                    themesConfig.getString(staticString + "color_medium"),
                    themesConfig.getString(staticString + "color_high"),
                    themesConfig.getString(staticString + "experimental"),
                    themesConfig.getString(staticString + "prefix"),
                    themesConfig.getString(staticString + "alerts-on"),
                    themesConfig.getString(staticString + "alerts-off"),
                    themesConfig.getString(staticString + "vpn-kick-message"),
                    themesConfig.getString(staticString + "banning-message"),
                    themesConfig.getString(staticString + "join-message"),
                    themesConfig.getString(staticString + "client-kick"),
                    themesConfig.getString(staticString + "broadcast-message")));
            Bukkit.getConsoleSender().sendMessage("Theme " + themeName + " loaded!");
        }

        for(String checkCategory : checksConfig.getKeys(false)) {
            for(String checkType : checksConfig.getConfigurationSection(checkCategory).getKeys(false)) {
                for(String check : checksConfig.getConfigurationSection(checkCategory + "." + checkType).getKeys(false)) {
                    String staticString = checkCategory + "." + checkType + "." + checks + ".";
                    String checkFullName = checkType + check.toUpperCase(Locale.ROOT);
                    Boolean enabled = checksConfig.getBoolean(staticString + "enabled");
                    checks.put(checkFullName, new CheckInConfig(
                            checksConfig.getString(staticString + "description"),
                            checksConfig.getInt(staticString + "maxvl"),
                            enabled));
                    Bukkit.getConsoleSender().sendMessage("Check " + checkFullName + " loaded! Enabled: " + enabled);
                }
            }
        }
    }

    private void createAdditionalFiles() {
        File themeFile = new File(FoxAC.getInstance().getDataFolder(), "themes.yml");
        if (!themeFile.exists()) {
            themeFile.getParentFile().mkdirs();
            FoxAC.getInstance().saveResource("themes.yml", false);
        }

        File checksFile = new File(FoxAC.getInstance().getDataFolder(), "checks.yml");
        if (!checksFile.exists()) {
            checksFile.getParentFile().mkdirs();
            FoxAC.getInstance().saveResource("checks.yml", false);
        }


    }
}
