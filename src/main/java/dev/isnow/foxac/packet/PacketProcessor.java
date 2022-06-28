package dev.isnow.foxac.packet;

import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.UserConnectEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import dev.isnow.foxac.FoxAC;
import dev.isnow.foxac.data.PlayerData;

/**
 * @author 5170
 * made on dev.isnow.foxac.packet
 */

public class PacketProcessor extends SimplePacketListenerAbstract {

    public PacketProcessor() {
        super(PacketListenerPriority.MONITOR);
    }

    @Override
    public void onUserConnect(UserConnectEvent event) {
        User user = event.getUser();

        FoxAC.getInstance().getManager().createData(user);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        PlayerData data = FoxAC.getInstance().getManager().getData(event.getUser());

        if (data == null) return;

        if (event.getPacketType() == PacketType.Play.Client.PLAYER_FLYING) {
            data.getPositionProcessor().handlePosition(new WrapperPlayClientPlayerFlying(event));
            data.getRotationProcessor().processPacket(new WrapperPlayClientPlayerFlying(event));
        }
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {

    }
}
