package dev.isnow.foxac.data;

import com.github.retrooper.packetevents.protocol.player.User;
import dev.isnow.foxac.check.CheckManager;
import dev.isnow.foxac.data.processor.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author 5170
 * made on dev.isnow.foxac.data
 */

@RequiredArgsConstructor
@Getter
public class PlayerData {

    private final User player;

    private final PositionProcessor positionProcessor = new PositionProcessor(this);
    private final RotationProcessor rotationProcessor = new RotationProcessor(this);
    private final ConnectionProcessor connectionProcessor = new ConnectionProcessor(this);
    private final ActionProcessor actionProcessor = new ActionProcessor(this);
    private final ReachProcessor reachProcessor = new ReachProcessor(this);
    private final StatusProcessor statusProcessor = new StatusProcessor(this);
    private final VelocityProcessor velocityProcessor = new VelocityProcessor(this);
    private final CollisionProcessor collisionProcessor = new CollisionProcessor(this);
    private final TeleportProcessor teleportProcessor = new TeleportProcessor(this);

    private final CheckManager checkManager = new CheckManager(this);

    public Player getPlayer() {
        return Bukkit.getPlayer(player.getUUID());
    }

}
