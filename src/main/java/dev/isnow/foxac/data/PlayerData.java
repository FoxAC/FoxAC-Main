package dev.isnow.foxac.data;

import com.github.retrooper.packetevents.protocol.player.User;
import dev.isnow.foxac.check.Check;
import dev.isnow.foxac.data.processor.ConnectionProcessor;
import dev.isnow.foxac.data.processor.PositionProcessor;
import dev.isnow.foxac.data.processor.ReachProcessor;
import dev.isnow.foxac.data.processor.RotationProcessor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

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
    private final ReachProcessor reachProcessor = new ReachProcessor(this);

    private final ArrayList<Check> loadedChecks = new ArrayList<>();
}
