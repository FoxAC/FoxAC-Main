package dev.isnow.foxac.data;

import com.github.retrooper.packetevents.protocol.player.User;
import dev.isnow.foxac.data.processor.PositionProcessor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayerData {

    private final User player;

    @Getter
    public final PositionProcessor positionProcessor = new PositionProcessor(this);

}
