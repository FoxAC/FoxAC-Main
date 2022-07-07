package dev.isnow.foxac.check.impl.movement.speed;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.isnow.foxac.check.Check;
import dev.isnow.foxac.packet.event.FPacketEvent;
import dev.isnow.foxac.util.PlayerUtilities;
import org.bukkit.potion.PotionEffectType;

/**
 * @author 5170
 * made on dev.isnow.foxac.check.impl.movement.speed
 */

// Taken from old fox
public class SpeedA extends Check {

    public SpeedA() {
        super("Speed", "A", true);
    }

    @Override
    public void handleCheck(FPacketEvent packetEvent) {
        if (packetEvent.getPacketType() == PacketType.Play.Client.PLAYER_FLYING) {
            if(data.getPositionProcessor().isClientOnGround() && (data.getPlayer().getWalkSpeed() < 0.2 || data.getPlayer().getWalkSpeed() > 0.30)) {
                return;
            }

            if(data.getPlayer().isFlying() || data.getCollisionProcessor().isPiston() || data.getActionProcessor().isInVehicle()) {
                return;
            }

            String modifiers = "";

            final boolean sprinting = data.getActionProcessor().isSprinting();

            final double lastDeltaX = data.getPositionProcessor().getLastMotionX();
            final double lastDeltaZ = data.getPositionProcessor().getLastMotionZ();

            final double deltaXZ = data.getPositionProcessor().getMotionXZ();
            final double deltaY = data.getPositionProcessor().getMotionY();

            final int groundTicks = data.getPositionProcessor().getClientGroundTicks();
            final int airTicks = data.getPositionProcessor().getClientAirTicks();

            final float modifierJump = PlayerUtilities.getPotionLevel(data.getPlayer(), PotionEffectType.JUMP) * 0.1F;
            final float jumpMotion = 0.42F + modifierJump;

            double limit = (PlayerUtilities.getPotionLevel(data.getPlayer(), PotionEffectType.SPEED) * 0.062f) + ((data.getPlayer().getWalkSpeed() - 0.2f) * 1.6f);

            double groundLimit = 0.289 + limit;

            double airLimit = 0.3615 + limit;


            if (Math.abs(deltaY - jumpMotion) < 1.0E-4 && airTicks == 1 && sprinting) {
                final float f = data.getRotationProcessor().getMouseY() * 0.017453292F;

                final double x = lastDeltaX - (Math.sin(f) * 0.2F);
                final double z = lastDeltaZ + (Math.cos(f) * 0.2F);

                airLimit += Math.hypot(x, z);
                modifiers = modifiers + ", jump";
            }

            // TODO: Add timers
//            if (data.getCollisionProcessor().getSinceSlimeTicks() || data.getCollisionProcessor().getSinceIceTicks() < 15) {
//                airLimit += 0.34F;
//                groundLimit += 0.34F;
//                modifiers = modifiers + ", ice/slime";
//            }

            if (data.getCollisionProcessor().isUnderBlock()) {
                airLimit += 0.91F;
                groundLimit += 0.91F;
                modifiers = modifiers + ", underblock";
            }

            if (groundTicks < 7) {
                groundLimit += (0.25F / groundTicks);
                modifiers = modifiers + ", freshground";
            }

            if (data.getCollisionProcessor().isHalfBlock()) {
                airLimit += 0.91F;
                groundLimit += 0.91F;
                modifiers = modifiers + ", halfblock";
            }

            if (!data.getTeleportProcessor().isDoneTeleporting()) {
                airLimit += 0.1;
                groundLimit += 0.1;
                modifiers = modifiers + ", teleport";
            }

            if (!data.getVelocityProcessor().getVelocities().isEmpty()) {
                groundLimit += data.getVelocityProcessor().getVelocities().getFirst().getXz() + 0.05;
                airLimit += data.getVelocityProcessor().getVelocities().getFirst().getXz() + 0.05;
                modifiers = modifiers + ", velocity";
            }

            if (airTicks > 0) {
                modifiers = modifiers + ", Air";
                if (deltaXZ > airLimit) {
                    if (increaseBuffer(1) > 3) {
                        fail("MotionXZ: " + deltaXZ + " Modifiers:" + modifiers);
                    }
                } else {
                    decreaseBuffer(0.15);
                }
            } else {
                modifiers = modifiers + ", Ground";
                if (deltaXZ > groundLimit) {
                    if (increaseBuffer(1) > 7) {
                        fail("MotionXZ: " + deltaXZ + " Modifiers:" + modifiers);
                    }
                } else {
                    decreaseBuffer(0.2);
                }
            }
        }
    }
}
