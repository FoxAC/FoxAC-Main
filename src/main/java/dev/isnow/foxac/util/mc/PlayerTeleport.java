package dev.isnow.foxac.util.mc;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
