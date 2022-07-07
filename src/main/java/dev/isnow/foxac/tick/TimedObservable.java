package dev.isnow.foxac.tick;

import lombok.Data;

/**
 * @author Lindgey
 * made on dev.isnow.foxac.util
 */

@Data
public class TimedObservable {

    private int ticksSinceTrue = 0;
    private boolean value;

    public TimedObservable( boolean value) {
        this.value = value;
    }

    public void handleTick() {

        ticksSinceTrue++;

        if (value)
            ticksSinceTrue = 0;

    }

    public boolean hasPassed(int ticks) {
        return ticks <= ticksSinceTrue;
    }
}
