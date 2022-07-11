package dev.isnow.foxac.data.processor;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import dev.isnow.foxac.FoxAC;
import dev.isnow.foxac.data.PlayerData;
import dev.isnow.foxac.tick.TimedObservable;
import dev.isnow.foxac.util.PlayerUtilities;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author 5170
 * made on dev.isnow.foxac.data.processor
 */

@Getter
@RequiredArgsConstructor
public class GhostBlockProcessor {

    private final PlayerData data;

    private int sinceServerPosTicks, ghostBlockFlags;

    private boolean ghostBlock;

    public void handleServerPosition() {
        sinceServerPosTicks = 0;
    }

    // TODO: Fix towering falses
    public void handleClientFlying(WrapperPlayClientPlayerFlying wrapper) {
        sinceServerPosTicks++;
        ghostBlock = false;
        if(wrapper.hasPositionChanged()) {
            if(FoxAC.getInstance().getConfiguration().getGhostBlockProcessor()) {
                if(PlayerUtilities.isOnBoat(data) || sinceServerPosTicks < 3 || data.getCollisionProcessor().isWeb() || data.getCollisionProcessor().isClimbable() || data.getCollisionProcessor().isSlime() || data.getActionProcessor().isInVehicle() || data.getCollisionProcessor().isLiquid()) {
                    ghostBlockFlags = 0;
                    return;
                }

                final boolean ground = data.getPositionProcessor().isClientOnGround() || data.getPositionProcessor().isLastClientOnGround();

                final boolean serverYGround = data.getPositionProcessor().isMathematicallyOnGround() || data.getPositionProcessor().isLastMathematicallyOnGround();

                final boolean serverGround = !data.getCollisionProcessor().isServerInAir() || !data.getCollisionProcessor().isLastServerInAir();


                if (ground && serverYGround && !serverGround) {
                    ghostBlock = true;
                    if(++ghostBlockFlags > 1) {
                        handleGhostblock();
                        ghostBlockFlags = 0;
                    }
                }

//                final boolean nearAnvil = data.getCollisionProcessor().isCollidingMinimumNumber(2, Material.ANVIL);
//
//                if(nearAnvil) {
//                    handleAnvilGhostBlock();
//                }

            }
        }
    }

    public void handleGhostblock() {
        data.getPlayer().teleport(PlayerUtilities.getBehind(data.getPlayer(), 2));
    }

    public void handleAnvilGhostBlock() {
        data.getPlayer().teleport(PlayerUtilities.getBehind(data.getPlayer(), 2));
    }


}
