package dev.isnow.foxac.util;

import com.github.retrooper.packetevents.protocol.world.Location;
import dev.isnow.foxac.data.PlayerData;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

@UtilityClass
public class PlayerUtilities {

    /**
     * @author Tecnio
     */
    public double getBlockFriction(Location loc, World world) {
        org.bukkit.Location bLoc = new org.bukkit.Location(world,loc.getX(), loc.getY(),loc.getZ(), 0, 0);
        Block block = bLoc.getBlock();
        return block.getType() == Material.PACKED_ICE ||
                block.getType() == Material.ICE ? 0.9800000190734863 :
                block.getType().toString().contains("SLIME") ? 0.800000011920929 : 0.6000000238418579;
    }

    /**
     * @author 5170
     */
    public org.bukkit.Location getBehind(Player player, double multi) {
        return player.getLocation().clone().add(player.getEyeLocation().getDirection().multiply(multi));
    }

    /**
     * @author Nik
     */
    public List<Entity> getEntitiesWithinRadius(final org.bukkit.Location location, final double radius) {
        final double expander = 16.0D;

        final double x = location.getX();
        final double z = location.getZ();

        final int minX = (int) Math.floor((x - radius) / expander);
        final int maxX = (int) Math.floor((x + radius) / expander);

        final int minZ = (int) Math.floor((z - radius) / expander);
        final int maxZ = (int) Math.floor((z + radius) / expander);

        final World world = location.getWorld();

        final List<Entity> entities = new LinkedList<>();

        try {
            for (int xVal = minX; xVal <= maxX; xVal++) {

                for (int zVal = minZ; zVal <= maxZ; zVal++) {

                    if (!world.isChunkLoaded(xVal, zVal)) continue;
                    if(world.getChunkAt(xVal, zVal) == null) {
                        continue;
                    }
                    for (final Entity entity : world.getChunkAt(xVal, zVal).getEntities()) {

                        if (entity == null) continue;


                        if (entity.getLocation().distanceSquared(location) > radius * radius) continue;

                        entities.add(entity);
                    }
                }
            }
        } catch(NoSuchElementException ignored) {

        }

        return entities;
    }

    /**
     * @author Tecnio/GladUrBad
     */
    public boolean isOnBoat(PlayerData user) {
        double offset = user.getPositionProcessor().getY() % 0.015625;
        if ((user.getPositionProcessor().isClientOnGround() && offset > 0 && offset < 0.009)) {
            return getEntitiesWithinRadius(user.getPlayer().getLocation(), 2).stream()
                    .anyMatch(entity -> entity.getType() == EntityType.BOAT);
        }

        return false;
    }

    /**
     * @author Tecnio/GladUrBad
     */
    public int getPotionLevel(final Player player, final PotionEffectType effect) {
        final int effectId = effect.getId();

        if (!player.hasPotionEffect(effect)) return 0;

        return player.getActivePotionEffects().stream().filter(potionEffect -> potionEffect.getType().getId() == effectId).map(PotionEffect::getAmplifier).findAny().orElse(0) + 1;
    }




}
