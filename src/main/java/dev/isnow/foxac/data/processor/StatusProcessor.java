package dev.isnow.foxac.data.processor;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerAbilities;
import dev.isnow.foxac.data.PlayerData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author Lindgey
 * made on dev.isnow.foxac.data.processor
 */

@Getter
@RequiredArgsConstructor
public class StatusProcessor {

    private final PlayerData data;
    private final Deque<ConfirmedAbilities> statuses = new LinkedList<>();

    public void handleAbilities(WrapperPlayServerPlayerAbilities packet) {
        data.getConnectionProcessor().addPreTask(() -> statuses.add(
                new ConfirmedAbilities(
                        packet.isInGodMode(),
                        packet.isFlying(),
                        packet.isFlightAllowed(),
                        packet.isInCreativeMode(),
                        packet.getFlySpeed(),
                        packet.getFOVModifier())
        ));

        if (statuses.size() > 1)
            data.getConnectionProcessor().addPostTask(statuses::removeFirst);
    }

    @Data
    @AllArgsConstructor
    public class ConfirmedAbilities {

        private boolean godMode, flying, flightAllowed, creativeMode;
        private float flySpeed, fovModifier;
    }

    public ConfirmedAbilities getLastAbilities() {
        return statuses.peekLast() == null ? new ConfirmedAbilities(false, data.getPlayer().isFlying(), data.getPlayer().getAllowFlight(),
                data.getPlayer().getGameMode() == GameMode.CREATIVE, data.getPlayer().getFlySpeed(), data.getPlayer().getWalkSpeed() / 2) : statuses.peekLast();
    }


}
