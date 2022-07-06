package dev.isnow.foxac.data.processor;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerPositionAndLook;
import dev.isnow.foxac.data.PlayerData;
import dev.isnow.foxac.util.mc.PlayerTeleport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author 5170
 * made on dev.isnow.foxac.data.processor
 */

@Getter
@RequiredArgsConstructor
public class TeleportProcessor {

    private final PlayerData data;

    private final Queue<PlayerTeleport> queuedTeleports = new LinkedList<>();

    private boolean teleported, doneTeleporting;

    private int sinceTeleportTicks;

    public void handleServerPosition(WrapperPlayServerPlayerPositionAndLook wrapper) {
        queuedTeleports.add(new PlayerTeleport(wrapper.getX(), wrapper.getY(), wrapper.getZ()));
    }

    public void handleFlying() {
        sinceTeleportTicks++;

        if(sinceTeleportTicks > data.getConnectionProcessor().getPingTicks())  {
            doneTeleporting = true;
        }
    }

    void checkTeleports(double x, double y, double z) {
        if (!queuedTeleports.isEmpty()) {

            PlayerTeleport teleport = queuedTeleports.peek();

            if (teleport.matches(x, y, z)) {

                queuedTeleports.poll();

                sinceTeleportTicks = 0;

                teleported = true;
            }
        }
    }
}
