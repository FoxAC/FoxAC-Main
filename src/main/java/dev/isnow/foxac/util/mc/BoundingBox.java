package dev.isnow.foxac.util.mc;

import com.github.retrooper.packetevents.protocol.world.Location;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 5170
 * made on dev.isnow.foxac.util.mc
 */

@Getter
public class BoundingBox {
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;

    public final long timestamp = System.currentTimeMillis();

    public BoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public BoundingBox(Location location, double expand, double height) {
        this.minX = location.getX();
        this.minY = location.getY();
        this.minZ = location.getZ();
        this.maxX = minX + expand;
        this.maxY = minY + height;
        this.maxZ = minZ + expand;
    }

    public BoundingBox(double x, double y, double z, float width, float height) {
        this.minX = x;
        this.minY = y;
        this.minZ = z;
        this.maxX = minX + width;
        this.maxY = minY + height;
        this.maxZ = minZ + width;
    }

    public boolean collides(BoundingBox other) {
        return other.maxX >= this.minX
                && other.minX <= this.maxX
                && other.maxY >= this.minY
                && other.minY <= this.maxY
                && other.maxZ >= this.minZ
                && other.minZ <= this.maxZ;
    }


    public BoundingBox clone() {
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public BoundingBox grow(double x, double y, double z) {
        this.minX -= x;
        this.minY -= y;
        this.minZ -= z;

        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;

        return this;
    }

    public BoundingBox move(double x, double y, double z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;

        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;

        return this;
    }

    public BoundingBox set(double x, double y, double z, double width, double height) {
        this.minX = x - width / 2.0;
        this.minY = y;
        this.minZ = z - width / 2.0;

        this.maxX = x + width / 2.0;
        this.maxY = y + height;
        this.maxZ = z + width / 2.0;

        return this;
    }

    public double posX() {
        return (maxX + minX) / 2.0;
    }

    public double posY() {
        return minY;
    }

    public double posZ() {
        return (maxZ + minZ) / 2.0;
    }

    public String toString() {
        return "x=" + posX() + " y=" + posY() + " posZ=" + posZ();
    }

    public List<Block> getColliding(World world) {
        List<Block> materials = new ArrayList<>();

        int x = MathHelper.floor(this.minX);
        int y = MathHelper.floor(this.minY);
        int z = MathHelper.floor(this.minZ);
        int x1 = MathHelper.floor(this.maxX + 1.0D);
        int y1 = MathHelper.floor(this.maxY + 1.0D);
        int z1 = MathHelper.floor(this.maxZ + 1.0D);

        for (int lx = x; lx < x1; lx++) {
            for (int lz = z; lz < z1; lz++) {
                if (world.isChunkLoaded(lx >> 4, lz >> 4)) {
                    for (int ly = y - 1; ly < y1; ly++) {
                        Block block = world.getBlockAt(lx, ly, lz);

                        if (block != null) {
                            materials.add(block);
                        }
                    }
                }
            }
        }

        return materials;
    }

}