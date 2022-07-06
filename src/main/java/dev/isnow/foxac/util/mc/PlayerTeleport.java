package dev.isnow.foxac.util.mc;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientTeleportConfirm;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

/**
 * @author 5170
 * made on dev.isnow.foxac.util.mc
 */

@AllArgsConstructor
@Getter
public class PlayerTeleport {
    private final double x;
    private final double y;
    private final double z;

    public boolean matches(double x, double y, double z) {
        return (this.x == x)
                && (this.y == y)
                && (this.z == z);
    }
}
