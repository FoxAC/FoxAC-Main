package dev.isnow.foxac.tick;

import dev.isnow.foxac.FoxAC;
import dev.isnow.foxac.data.PlayerData;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Salers
 * made on dev.isnow.foxac.tick
 */
public class TickRunnable extends BukkitRunnable {

    @Override
    public void run() {
        for(PlayerData datas : FoxAC.getInstance().getDataManager().getWholeData()) {
            datas.getCollisionProcessor().getSlimeTimer().handleTick();
        }

    }
}
