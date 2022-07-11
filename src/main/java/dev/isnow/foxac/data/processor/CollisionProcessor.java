package dev.isnow.foxac.data.processor;

import dev.isnow.foxac.data.PlayerData;
import dev.isnow.foxac.tick.TimedObservable;
import dev.isnow.foxac.util.Pair;
import dev.isnow.foxac.util.mc.BoundingBox;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 5170
 * made on dev.isnow.foxac.data.processor
 */

@Getter
@RequiredArgsConstructor
public class CollisionProcessor {

    private final PlayerData data;


  
    private final TimedObservable slimeTimer = new TimedObservable(false);
    private final TimedObservable iceTimer = new TimedObservable(false);
    private final TimedObservable liquidTimer = new TimedObservable(false);
    private final TimedObservable halfBlockTimer = new TimedObservable(false);
    private final TimedObservable climbableTimer = new TimedObservable(false);
    private final TimedObservable pistonTimer = new TimedObservable(false);
    private final TimedObservable underBlockTimer = new TimedObservable(false);


    private boolean lastServerInAir, serverInAir, slime, ice, liquid, halfBlock, web, climbable, underBlock, piston;

    void updateCollisions() {
        BoundingBox box = new BoundingBox(data.getPositionProcessor().getCurrentLocation(), 0.6f, 1.8f);

        List<Pair<Material, Location>> blocks = box.clone()
                .grow(0.5, 0.5, 0.5)
                .move(0.0, 0.5, 0.0)
                .getColliding(data.getPlayer().getWorld())
                .stream()
                .map(block -> new Pair<>(block.getType(), new Location(block.getWorld(), block.getX(), block.getY(), block.getZ())))
                .collect(Collectors.toList());

        for (Pair<Material, Location> pair : blocks) {

            lastServerInAir = serverInAir;
            serverInAir = !pair.getKey().isSolid();
            slime = pair.getKey().toString().contains("SLIME");
            ice = pair.getKey().toString().contains("ICE");
            liquid = pair.getKey().toString().contains("WATER") || pair.getKey().toString().contains("LAVA");
            halfBlock = pair.getKey().toString().contains("STAIRS") || pair.getKey().toString().contains("SLAB") || pair.getKey().toString().contains("STEP") || pair.getKey().toString().contains("HEAD") || pair.getKey().toString().contains("SKULL");
            web = pair.getKey().toString().contains("WEB");
            climbable = pair.getKey().toString().contains("LADDER") || pair.getKey().toString().contains("VINE");
            piston = pair.getKey().toString().contains("PISTON");

            slimeTimer.setValue(slime);
            iceTimer.setValue(ice);
            liquidTimer.setValue(liquid);
            halfBlockTimer.setValue(halfBlock);
            climbableTimer.setValue(climbable);
            pistonTimer.setValue(piston);

            // FIXME: flags when touching a wall.
            if (pair.getValue().getY() >= box.minY && pair.getKey().isSolid()) underBlock = true;
            underBlockTimer.setValue(underBlock);
        }
    }
}
