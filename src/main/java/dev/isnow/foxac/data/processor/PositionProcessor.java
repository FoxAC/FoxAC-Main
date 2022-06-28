package dev.isnow.foxac.data.processor;

import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import dev.isnow.foxac.data.PlayerData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PositionProcessor {

    private final PlayerData data;

    private boolean clientInAir, clientOnGround;
    public boolean lastClientInAir, lastClientOnGround;

    private int clientAirTicks, clientGroundTicks;

    private Location currentLocation, lastLocation;

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

        if (flying.hasPositionChanged()) {
            if (currentLocation != null) { // Prevent NPE
                lastLocation = currentLocation;
            }
            lastMotionX = motionX;
            lastMotionY = motionY;
            lastMotionZ = motionZ;
            lastMotionXZ = motionXZ;

            currentLocation = flying.getLocation();

            motionX = currentLocation.getX() - lastLocation.getX();
            motionY = currentLocation.getY() - lastLocation.getY();
            motionZ = currentLocation.getZ() - lastLocation.getZ();

            motionXZ = Math.hypot(motionX, motionZ);

        }
    }
}
