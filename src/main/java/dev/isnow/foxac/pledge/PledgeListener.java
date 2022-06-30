package dev.isnow.foxac.pledge;

import com.github.retrooper.packetevents.PacketEvents;
import dev.isnow.foxac.FoxAC;
import dev.isnow.foxac.data.PlayerData;
import dev.isnow.foxac.data.processor.ConnectionProcessor;
import dev.thomazz.pledge.api.event.TransactionEvent;
import dev.thomazz.pledge.api.event.TransactionListener;

/**
 * @author Lindgey
 * made on dev.isnow.foxac.pledge
 */

public class PledgeListener implements TransactionListener {
    @Override
    public void onSendStart(TransactionEvent event) {

        PlayerData data = FoxAC.getInstance().getDataManager().getData(PacketEvents.getAPI().getPlayerManager().getUser(event.getInfo().getPlayer()));

        if (data == null) return;

        data.getConnectionProcessor().onTransaction(event, ConnectionProcessor.TransactionType.SEND_START);
    }

    @Override
    public void onSendEnd(TransactionEvent event) {

        PlayerData data = FoxAC.getInstance().getDataManager().getData(PacketEvents.getAPI().getPlayerManager().getUser(event.getInfo().getPlayer()));

        if (data == null) return;

        data.getConnectionProcessor().onTransaction(event, ConnectionProcessor.TransactionType.SEND_END);
    }

    @Override
    public void onReceiveStart(TransactionEvent event) {

        PlayerData data = FoxAC.getInstance().getDataManager().getData(PacketEvents.getAPI().getPlayerManager().getUser(event.getInfo().getPlayer()));

        if (data == null) return;

        data.getConnectionProcessor().onTransaction(event, ConnectionProcessor.TransactionType.RECEIVE_START);
    }

    @Override
    public void onReceiveEnd(TransactionEvent event) {

        PlayerData data = FoxAC.getInstance().getDataManager().getData(PacketEvents.getAPI().getPlayerManager().getUser(event.getInfo().getPlayer()));

        if (data == null) return;

        data.getConnectionProcessor().onTransaction(event, ConnectionProcessor.TransactionType.RECEIVE_END);


    }

}
