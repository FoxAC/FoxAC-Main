package dev.isnow.foxac.data.processor;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerKeepAlive;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.isnow.foxac.data.PlayerData;
import dev.thomazz.pledge.api.event.TransactionEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author Lindgey
 * made on dev.isnow.foxac.data.processor
 */

@Getter
@RequiredArgsConstructor
public class ConnectionProcessor {

    private final PlayerData data;

    private final Deque<Transaction> transactions = new ArrayDeque<>();
    private final Deque<Long> keepAlives = new ArrayDeque<>();
    private final Multimap<Short, Runnable> transactionTasks = ArrayListMultimap.create();
    private short index;
    private int keepAlivePing, transactionPing;
    private long lastKeepAliveSent;


    public void onTransaction(TransactionEvent event, TransactionType type) {
        switch (type) {
            case SEND_START:
            case SEND_END:
                index = type == TransactionType.SEND_START ? (short) event.getTransactionPair().getId1()
                        : (short) event.getTransactionPair().getId2();
                break;
        }
    }

    public void handleWindowConfirmationSending(WrapperPlayServerWindowConfirmation wrapper) {
        if (wrapper.getWindowId() == 0 && !wrapper.isAccepted()) {
            transactions.add(new Transaction(wrapper.getActionId(), System.currentTimeMillis()));
        }
    }

    public void handleKeepAliveSending(WrapperPlayServerKeepAlive wrapper) {
        this.lastKeepAliveSent = System.currentTimeMillis();

        keepAlives.add(wrapper.getId());
    }


    public void handleWindowConfirmationRecieveing(WrapperPlayClientWindowConfirmation wrapper) {
        Transaction transaction = transactions.poll();

        if (wrapper.getWindowId() == 0) {
            if(transaction == null) {
                return; // Not our transaction?
            }
            if (wrapper.getActionId() == transaction.id) {

                if (transactionTasks.containsKey(transaction.id)) {
                    for (Runnable runnable : transactionTasks.removeAll(transaction.id)) {
                        runnable.run();
                    }
                }

            /*
            transaction ping would be more accurate as I know
             */
                transactionPing = (int) (System.currentTimeMillis() - transaction.timestamp);

            }
        }

    }

    public void handleKeepAliveRecieveing() {
        keepAlivePing = (int) (System.currentTimeMillis() - lastKeepAliveSent);
    }

    private short nextIndex() {
        short nextIndex = (short) (index + 1);

        if (nextIndex < 0) {
            nextIndex = 0;
        }

        return nextIndex;
    }

    public void addPreTask(Runnable runnable) {
        transactionTasks.put(index, runnable);
    }

    public void addPostTask(Runnable runnable) {
        transactionTasks.put(nextIndex(), runnable);
    }

    public enum TransactionType {
        RECEIVE_START,
        RECEIVE_END,
        SEND_START,
        SEND_END
    }

    @RequiredArgsConstructor
    public static class Transaction {
        public final short id;
        public final long timestamp;
    }
}
