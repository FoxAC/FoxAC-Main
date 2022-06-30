package dev.isnow.foxac.packet;

import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.UserConnectEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.*;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerKeepAlive;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import dev.isnow.foxac.FoxAC;
import dev.isnow.foxac.check.Check;
import dev.isnow.foxac.data.PlayerData;
import dev.isnow.foxac.packet.event.FPacketEvent;

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

        FoxAC.getInstance().getDataManager().initUser(user);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        PlayerData data = FoxAC.getInstance().getDataManager().getData(event.getUser());

        if (data == null) return;

        if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {
            data.getPositionProcessor().handlePosition(new WrapperPlayClientPlayerFlying(event));
            data.getRotationProcessor().processPacket(new WrapperPlayClientPlayerFlying(event));
            data.getReachProcessor().handleFlying();
            data.getActionProcessor().handleFlying();
        }

        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            data.getReachProcessor().handleInteractEntity(new WrapperPlayClientInteractEntity(event));
        }

        if (event.getPacketType() == PacketType.Play.Client.ENTITY_ACTION) {
            data.getActionProcessor().handleEntityAction(new WrapperPlayClientEntityAction(event));
        }

        if (event.getPacketType() == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
            data.getActionProcessor().handleBlockPlace(new WrapperPlayClientPlayerBlockPlacement(event));
        }

        if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING) {
            data.getActionProcessor().handleDigging(new WrapperPlayClientPlayerDigging(event));
        }

        if (event.getPacketType() == PacketType.Play.Client.USE_ITEM) {
            data.getActionProcessor().handleUseItem(new WrapperPlayClientUseItem(event));
        }

        if (event.getPacketType() == PacketType.Play.Client.WINDOW_CONFIRMATION) {
            data.getConnectionProcessor().handleWindowConfirmationRecieveing(new WrapperPlayClientWindowConfirmation(event));
        }

        if (event.getPacketType() == PacketType.Play.Client.KEEP_ALIVE) {
            data.getConnectionProcessor().handleKeepAliveRecieveing();
        }

        for (Check check : data.getCheckManager().getLoadedChecks()) {
            check.handleCheck(new FPacketEvent(event));


        }
    }

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        PlayerData data = FoxAC.getInstance().getDataManager().getData(event.getUser());

        if (data == null) return;

        if (event.getPacketType() == PacketType.Play.Server.WINDOW_CONFIRMATION) {
            data.getConnectionProcessor().handleWindowConfirmationSending(new WrapperPlayServerWindowConfirmation(event));
        }

        if (event.getPacketType() == PacketType.Play.Server.KEEP_ALIVE) {
            data.getConnectionProcessor().handleKeepAliveSending(new WrapperPlayServerKeepAlive(event));
        }

        for (Check check : data.getCheckManager().getLoadedChecks()) {
            check.handleCheck(new FPacketEvent(event));
        }

        data.getReachProcessor().process(event);

    }
}
