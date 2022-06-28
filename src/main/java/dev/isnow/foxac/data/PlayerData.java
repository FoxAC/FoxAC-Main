package dev.isnow.foxac.data;

import com.github.retrooper.packetevents.protocol.player.User;
import dev.isnow.foxac.data.processor.PositionProcessor;
import dev.isnow.foxac.data.processor.RotationProcessor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PlayerData {

    private final User player;

    private final PositionProcessor positionProcessor = new PositionProcessor(this);
    private final RotationProcessor rotationProcessor = new RotationProcessor(this);

}
