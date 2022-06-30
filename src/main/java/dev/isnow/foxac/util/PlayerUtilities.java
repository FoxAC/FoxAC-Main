package dev.isnow.foxac.util;

import com.github.retrooper.packetevents.protocol.world.Location;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@UtilityClass
public class PlayerUtilities {

    public double getBlockFriction(Location loc, World world) {
        org.bukkit.Location bLoc = new org.bukkit.Location(world,loc.getX(), loc.getY(),loc.getZ(), 0, 0);
        Block block = bLoc.getBlock();
        return block.getType() == Material.PACKED_ICE ||
                block.getType() == Material.ICE ? 0.9800000190734863 :
                block.getType().toString().contains("SLIME") ? 0.800000011920929 : 0.6000000238418579;
    }

    public int getPotionLevel(final Player player, final PotionEffectType effect) {
        final int effectId = effect.getId();

        if (!player.hasPotionEffect(effect)) return 0;

        return player.getActivePotionEffects().stream().filter(potionEffect -> potionEffect.getType().getId() == effectId).map(PotionEffect::getAmplifier).findAny().orElse(0) + 1;
    }




}
