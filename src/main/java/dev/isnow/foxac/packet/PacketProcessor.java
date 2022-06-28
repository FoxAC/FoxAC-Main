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
        if(event.getPacketType() == PacketType.Play.Client.PLAYER_FLYING) {
            FoxAC.getInstance().getManager().getData(event.getUser()).getPositionProcessor().handlePosition(new WrapperPlayClientPlayerFlying(event));
        }
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {

    }
}
