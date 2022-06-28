package dev.isnow.foxac.data.processor;

import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import dev.isnow.foxac.data.PlayerData;
import dev.isnow.foxac.util.MathUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lindgey
 * made on dev.isnow.foxac.data.processor
 */

@Getter
@RequiredArgsConstructor
public class RotationProcessor {

    private final PlayerData data;

    private float prevMouseX, prevMouseY, mouseX, mouseY;
    private double prevMouseMotionX, prevMouseMotionY, mouseMotionX, mouseMotionY,
            mouseGcdX, mouseGcdY, tickXSensitivity, tickYSensitivity, xSensitivity, ySensitivity;
    ;

    private final List<Double> xSensitivitiesSamples = new ArrayList<>();
    private final List<Double> ySensitivitiesSamples = new ArrayList<>();


    public void processPacket(final WrapperPlayClientPlayerFlying packet) {

        /*
         * we have to do this even if the player didn't rotate, to keep the right last values.
         */

        prevMouseX = mouseX;
        prevMouseY = mouseY;

        prevMouseMotionX = mouseMotionX;
        prevMouseMotionY = mouseMotionY;


        /*
         * update rotation values only when i really need to
         */

        if (packet.hasRotationChanged()) {

            Location rotation = packet.getLocation();

            mouseX = MathUtil.wrapAngleTo180_float(rotation.getYaw());
            mouseY = rotation.getPitch();

            mouseMotionX = (mouseX - prevMouseX) % 360F;
            mouseMotionY = mouseY = prevMouseY;

            mouseGcdX = MathUtil.getGcd(mouseMotionX, prevMouseMotionX);
            mouseGcdY = MathUtil.getGcd(mouseMotionY, prevMouseMotionY);

            processSensitivity();


        } else {

            /*
             * if rotation haven't changed, we'll just assume player haven't rotated in the last tick.
             * this also prevents rotation values to be inaccurate.
             */

            mouseMotionX = mouseMotionY = 0;
            mouseGcdX = mouseGcdY = 0;


        }
    }

    private void processSensitivity() {

        /*
         * note that pitch sensitivity seems to be way more accurate
         * i still calculate both, since it could be useful for some checks.
         */

        if (Math.abs(mouseMotionX) > 0) {

            tickXSensitivity = (Math.cbrt(mouseGcdX / 0.8 / 0.15) - 0.2) / 0.6;
            xSensitivitiesSamples.add(tickXSensitivity);

            if (xSensitivitiesSamples.size() == 50) {
                xSensitivity = (double) MathUtil.getMode(xSensitivitiesSamples);

                /*
                 * only clearing half of the samples to make the calculation faster.
                 */

                xSensitivitiesSamples.subList(0, 24);
            }
        }

        if (Math.abs(mouseMotionY) > 0) {
            tickYSensitivity = (Math.cbrt(mouseGcdY / 0.8 / 0.15) - 0.2) / 0.6;
            ySensitivitiesSamples.add(tickYSensitivity);

            if (xSensitivitiesSamples.size() == 50) {
                ySensitivity = (double) MathUtil.getMode(ySensitivitiesSamples);

                /*
                 * only clearing half of the samples to make the calculation faster.
                 */

                ySensitivitiesSamples.subList(0, 24);
            }
        }


    }
}
