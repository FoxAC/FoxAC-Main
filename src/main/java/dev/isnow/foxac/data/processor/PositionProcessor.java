package dev.isnow.foxac.data.processor;

import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import dev.isnow.foxac.data.PlayerData;
import dev.isnow.foxac.util.MathUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author 5170
 * made on dev.isnow.foxac.data.processor
 */

@RequiredArgsConstructor
@Getter
public class PositionProcessor {

    private final PlayerData data;

    private boolean clientInAir, clientOnGround;
    public boolean lastClientInAir, lastClientOnGround;

    private int clientAirTicks, clientGroundTicks;

    private Location currentLocation, lastLocation;

    private double x,y,z, lastX, lastY, lastZ;
    private double motionX, motionY, motionZ, motionXZ;
    private double lastMotionX, lastMotionY, lastMotionZ, lastMotionXZ;

    public void handlePosition(WrapperPlayClientPlayerFlying flying) {

        lastClientOnGround = clientOnGround;
        lastClientInAir = clientInAir;

        clientOnGround = flying.isOnGround();
        clientInAir = !clientOnGround;

        if (clientOnGround) {
            clientGroundTicks++;
        } else {
            clientAirTicks++;
        }

        lastMotionX = motionX;
        lastMotionY = motionY;
        lastMotionZ = motionZ;
        lastMotionXZ = motionXZ;

        lastX = x;
        lastY = y;
        lastZ = z;

        if(flying.hasRotationChanged() && !clientOnGround) {
            data.getTeleportProcessor().checkTeleports(x, y, z);
        }

        if (flying.hasPositionChanged()) {

            Location flyingLoc = flying.getLocation();

            x = flyingLoc.getX();
            y = flyingLoc.getY();
            z = flyingLoc.getZ();

            motionX = x - lastX;
            motionY = y = lastY;
            motionZ = z - lastZ;

            motionXZ = MathUtil.hypot(motionX, motionZ);

            lastLocation = currentLocation != null ? currentLocation : new Location(new Vector3d(lastX, lastY, lastZ), 0, 0);
            currentLocation = flyingLoc;

        }

        data.getCollisionProcessor().updateCollisions();
    }
}
