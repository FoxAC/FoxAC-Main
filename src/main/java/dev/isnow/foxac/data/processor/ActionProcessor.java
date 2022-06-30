package dev.isnow.foxac.data.processor;

import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientUseItem;
import dev.isnow.foxac.data.PlayerData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

/**
 * @author 5170
 * made on dev.isnow.foxac.data.processor
 */

@Getter
@RequiredArgsConstructor
public class ActionProcessor {

    private final PlayerData data;

    private boolean sprinting, digging, attacking, sneaking, eating, blocking, chargingBow;

    public void handleEntityAction(WrapperPlayClientEntityAction wrapper) {
        if(wrapper.getAction() == WrapperPlayClientEntityAction.Action.START_SNEAKING) {
            sneaking = true;
        }
        if(wrapper.getAction() == WrapperPlayClientEntityAction.Action.STOP_SNEAKING) {
            sneaking = false;
        }

        if(wrapper.getAction() == WrapperPlayClientEntityAction.Action.START_SPRINTING) {
            sprinting = true;
        }
        if(wrapper.getAction() == WrapperPlayClientEntityAction.Action.STOP_SPRINTING) {
            sprinting = false;
        }
    }

    public void handleBlockPlace(WrapperPlayClientPlayerBlockPlacement wrapper) {
        if(blocking) {
            blocking = false;
        }

        if(wrapper.getItemStack().isPresent() && wrapper.getFace() == BlockFace.OTHER && Bukkit.getPlayer(data.getPlayer().getUUID()).getItemInHand().toString().contains("SWORD")) {
            blocking = true;
        }
    }
    public void handleFlying() {
        attacking = false;
        if (!Bukkit.getPlayer(data.getPlayer().getUUID()).getItemInHand().toString().contains("SWORD")) blocking = false;
        if (!Bukkit.getPlayer(data.getPlayer().getUUID()).getItemInHand().getType().isEdible()) eating = false;
    }

    public void handleUseItem(WrapperPlayClientUseItem wrapper) {
        if (Bukkit.getPlayer(data.getPlayer().getUUID()).getInventory().getItemInHand().getType().isEdible()) {
            eating = true;
        }
    }

    public void handleDigging(WrapperPlayClientPlayerDigging wrapper) {
        if(wrapper.getAction() == DiggingAction.START_DIGGING) {
            digging = true;
        }

        if(wrapper.getAction() == DiggingAction.FINISHED_DIGGING || wrapper.getAction() == DiggingAction.CANCELLED_DIGGING) {
            digging = false;
        }

        if(wrapper.getAction() == DiggingAction.RELEASE_USE_ITEM) {
            eating = false;
            blocking = false;
            chargingBow = false;
        }
    }
}