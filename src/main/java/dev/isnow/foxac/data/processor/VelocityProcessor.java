package dev.isnow.foxac.data.processor;

import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity;
import dev.isnow.foxac.data.PlayerData;
import dev.isnow.foxac.util.MathUtil;
import dev.isnow.foxac.util.PlayerUtilities;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.potion.PotionEffectType;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author Lindgey
 * made on dev.isnow.foxac.data.processor
 */

@Getter
@RequiredArgsConstructor
public class VelocityProcessor {

    private final PlayerData data;
    private Deque<ConfirmedVelocity> velocities = new LinkedList<>();
    private int velTicks;

    /*
    basic transaction sandwich, eliminating any non-desired packet values.
     */
    public void handleVelocity(WrapperPlayServerEntityVelocity packet) {
        data.getConnectionProcessor().addPreTask(() -> {
            velocities.add(new ConfirmedVelocity(packet.getVelocity()));
            velTicks = 0;
        });

        if (velocities.size() > 1)
            data.getConnectionProcessor().addPostTask(() -> velocities.removeFirst());
    }

    public void handleFlying() {
        velTicks++;
        velocities.forEach(confirmedVelocity -> confirmedVelocity.handleTick( velTicks));

    }

    @Getter
    public class ConfirmedVelocity {

        private double x, y, z, xz;

        public ConfirmedVelocity(final Vector3d vector3d) {
            this.x = vector3d.x;
            this.y = vector3d.y;
            this.z = vector3d.z;
            this.xz = MathUtil.hypot(x, z);
        }

        public void handleTick(int ticks) {

            /*
            using lasts, since its delayed from the client
             */
            float friction = data.getPositionProcessor().isLastClientOnGround() ?
                    (float) (PlayerUtilities.getBlockFriction(data.getPositionProcessor().getLastLocation(), data.getPlayer().getWorld()) * 0.91F)
                    : 0.91F;

            float f = 0.16277136F / (friction * friction * friction);

            //is this right?
            double moveFactor = data.getPositionProcessor().isLastClientOnGround() ? getAiMoveSpeed() *
                    f : (data.getActionProcessor().isSprinting() ? 0.026F : 0.02F);

            x += moveFactor;
            z += moveFactor;
            xz = MathUtil.hypot(x, z);

            if (data.getPlayer().hasPotionEffect(PotionEffectType.SPEED)) {
                int amplifier = PlayerUtilities.getPotionLevel(data.getPlayer(), PotionEffectType.SPEED);
                x = (x * Math.pow(0.9, amplifier)) - 0.01;
                z = (z * Math.pow(0.9, amplifier)) - 0.01;
                xz = MathUtil.hypot(x, z);
            }

            /*
            have to check this since this isn't done on the first vel tick
             */
            if (ticks > 0) {

                y -= 0.08;
                y *= 0.98F;

                x *= friction;
                z *= friction;
            }


        }

        private float getAiMoveSpeed() {

            float speed = PlayerUtilities.getPotionLevel(data.getPlayer(), PotionEffectType.SPEED);
            float slowness = PlayerUtilities.getPotionLevel(data.getPlayer(), PotionEffectType.SLOW);

            double movementFactor = data.getStatusProcessor().getLastAbilities().getFovModifier();

            movementFactor += movementFactor * 0.2F * speed;
            movementFactor += movementFactor * -0.15F * slowness;

            if (data.getActionProcessor().isSprinting())
                movementFactor += movementFactor * 0.3F;

            return (float) movementFactor;
        }
    }

}
