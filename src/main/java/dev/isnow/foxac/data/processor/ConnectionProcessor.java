package dev.isnow.foxac.data.processor;


import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientKeepAlive;

import com.github.retrooper.packetevents.PacketEvents;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import dev.isnow.foxac.data.PlayerData;
import dev.isnow.foxac.util.MathUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.management.BufferPoolMXBean;
import java.util.ArrayDeque;
import java.util.Deque;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 5170
 * made on dev.isnow.foxac.data.processor
 */

@Getter
@RequiredArgsConstructor
public class ConnectionProcessor {

    private final PlayerData data;

    private final AtomicInteger transactionTick = new AtomicInteger();

    private final Map<Short, Long> transactionMap = new HashMap<>();
    private final Deque<Short> transactionDeque = new LinkedList<>();
    private final Map<Short, List<PacketConfirmedAction>> actionMap = new HashMap<>();

    private long lastFlying, lastDelta;

    @Getter
    private long ping;

    public short getNextTick() {
        transactionTick.incrementAndGet();
        return (short) (-(transactionTick.get() * 2) % Short.MAX_VALUE);
    }

    public void handleClientTransaction(WrapperPlayClientWindowConfirmation wrapper) {
        short id = wrapper.getActionId();

        if (transactionMap.containsKey(id)) {
            ping = System.currentTimeMillis() - transactionMap.get(id);

            actionMap.get(id).forEach(PacketConfirmedAction::run);
            actionMap.remove(id);

            int expected = transactionDeque.getFirst();

            if (expected != id) {
                return; // ????
            }

            transactionDeque.removeFirst();
        } else {
            // ???????
            return;
        }
        transactionMap.remove(id);
    }

    // TODO: complete this, not working atm
    public void tickAndConfirm(PacketConfirmedAction action) {
        short tick = getNextTick();

        PacketEvents.getAPI().getPlayerManager().getUser(data.getPlayer()).sendPacket(new WrapperPlayServerWindowConfirmation(0, tick, false));

        if (!actionMap.containsKey(tick)) {
            actionMap.put(tick, new ArrayList<>());
        }

        actionMap.get(tick).add(action);
    }

    public void handleServerTransaction(WrapperPlayServerWindowConfirmation wrapper) {
        short id = wrapper.getActionId();

        actionMap.putIfAbsent(id, new ArrayList<>());
        transactionMap.putIfAbsent(id, System.currentTimeMillis());
        transactionDeque.add(id);

        if ((transactionMap.size() / 40.0) > 15000) {
            data.getPlayer().kickPlayer("Timed out.");
        }
    }

    public void handleFlying() {
        long now = System.currentTimeMillis();

        lastDelta = now - lastFlying;
        lastFlying = now;
    }


    public int getPingTicks() {
        return Math.min(50, MathUtil.getPingTicks(ping, 10));
    }

    interface PacketConfirmedAction {
        void run();
    }
}
