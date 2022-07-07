package dev.isnow.foxac.packet.event;

import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Lindgey
 * made on dev.isnow.foxac.packet.event
 */

@AllArgsConstructor
@Getter
public class FPacketEvent {

    private final ProtocolPacketEvent<?> event;

    public PacketTypeCommon getPacketType() {
        return getEvent().getPacketType();
    }





}
