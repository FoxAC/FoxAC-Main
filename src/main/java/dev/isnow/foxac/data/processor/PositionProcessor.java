package dev.isnow.foxac.data.processor;

import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import dev.isnow.foxac.data.PlayerData;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PositionProcessor {

    private final PlayerData data;

    public boolean clientInAir, clientOnGround;
    public boolean lastClientInAir, lastClientOnGround;

    public int clientAirTicks, clientGroundTicks;

    public Location currentLocation, lastLocation;

    public double motionX, motionY, motionZ, motionXZ;
    public double lastMotionX, lastMotionY, lastMotionZ, lastMotionXZ;

    public void handlePosition(WrapperPlayClientPlayerFlying flying) {
        lastClientOnGround = clientOnGround;
        lastClientInAir = clientInAir;

        clientOnGround = flying.isOnGround();
        clientInAir = !clientOnGround;

        if(clientOnGround) {
            clientGroundTicks++;
        }
        else {
            clientAirTicks++;
        }

        if(flying.hasPositionChanged()) {
            if(currentLocation != null) { // Prevent NPE
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
