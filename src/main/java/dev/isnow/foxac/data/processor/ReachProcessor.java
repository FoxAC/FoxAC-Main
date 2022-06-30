package dev.isnow.foxac.data.processor;

import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRelativeMove;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRelativeMoveAndRotation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityTeleport;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnPlayer;
import dev.isnow.foxac.data.PlayerData;
import dev.isnow.foxac.util.MathUtil;
import dev.isnow.foxac.util.mc.AxisAlignedBB;
import dev.isnow.foxac.util.mc.MovingObjectPosition;
import dev.isnow.foxac.util.mc.Vec3;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lindgey
 * made on dev.isnow.foxac.data.processor
 */

@RequiredArgsConstructor
@Getter
public class ReachProcessor {

    private final PlayerData data;
    private double reach;
    private boolean attack;
    private int lastHitId;

    private final List<EData> tracked = new ArrayList<>();

    public void process(PacketPlayReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity packet = new WrapperPlayClientInteractEntity(event);
            if (packet.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                attack = true;
                lastHitId = packet.getEntityId();
            }
        } else if (WrapperPlayClientPlayerFlying.isFlying(event.getPacketType())) {


            if (attack) {
                attack = false;

                reach = getReach(lastHitId) - 0.15;
                if (reach < 7.5)
                    Bukkit.broadcastMessage("reach=" + reach);

            }

            tracked.forEach(EData::interpolate);


        }

    }

    private double getReach(int id) {

        /*
         *  Doing this because these things can be different due to pvp clients, +1.9 clients etc..
         */

        Vec3[] origins = {
                new Vec3(data.getPositionProcessor().getCurrentLocation().getX(),
                        data.getPositionProcessor().getCurrentLocation().getY() + 1.54,
                        data.getPositionProcessor().getCurrentLocation().getZ()),

                new Vec3(data.getPositionProcessor().getCurrentLocation().getX(),
                        data.getPositionProcessor().getCurrentLocation().getY() + 1.62,
                        data.getPositionProcessor().getCurrentLocation().getZ())
        };


        Vec3[] rotations = {
                MathUtil.getVectorForRotation(
                        data.getRotationProcessor().getMouseY(),
                        data.getRotationProcessor().getMouseX()),

                MathUtil.getVectorForRotation(
                        data.getRotationProcessor().getMouseY(),
                        data.getRotationProcessor().getMouseX()
                )
        };

        final AxisAlignedBB aabb = new AxisAlignedBB(getById(id).getFirst());
        //1.8 hitboxes
        aabb.expand(0.1, 0.1, 0.1);

        double reach = 10;

        /*
        trying to get the lowest reach as possible since it will more likely be the correct one
         */

        for (Vec3 origin : origins) {
            for (Vec3 rotation : rotations) {
                Vec3 ray = origin.addVector(rotation.xCoord * 6, rotation.yCoord * 6, rotation.zCoord * 6);

                MovingObjectPosition lastCollision = aabb.calculateIntercept(origin, ray);

                if (lastCollision == null) continue;

                if (origin.distanceTo(lastCollision.hitVec) < reach) {
                    reach = origin.distanceTo(lastCollision.hitVec);
                }
            }
        }

        return reach;

    }

    public void process(PacketPlaySendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.SPAWN_PLAYER) {
            final WrapperPlayServerSpawnPlayer packet = new WrapperPlayServerSpawnPlayer(event);

            tracked.add(new EData(packet.getEntityId(), packet.getPosition()));

        } else if (event.getPacketType() == PacketType.Play.Server.SPAWN_LIVING_ENTITY) {
            final WrapperPlayServerSpawnPlayer packet = new WrapperPlayServerSpawnPlayer(event);

            tracked.add(new EData(packet.getEntityId(), packet.getPosition()));

        } else if (event.getPacketType() == PacketType.Play.Server.ENTITY_RELATIVE_MOVE) {
            final WrapperPlayServerEntityRelativeMove packet = new WrapperPlayServerEntityRelativeMove(event);
            getById(packet.getEntityId()).processRelMove(new Vector3d(packet.getDeltaX(), packet.getDeltaY(), packet.getDeltaZ()));

        } else if (event.getPacketType() == PacketType.Play.Server.ENTITY_RELATIVE_MOVE_AND_ROTATION) {
            final WrapperPlayServerEntityRelativeMoveAndRotation packet = new WrapperPlayServerEntityRelativeMoveAndRotation(event);
            getById(packet.getEntityId()).processRelMove(new Vector3d(packet.getDeltaX(), packet.getDeltaY(), packet.getDeltaZ()));

        } else if (event.getPacketType() == PacketType.Play.Server.ENTITY_TELEPORT) {
            final WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport(event);
            getById(packet.getEntityId()).processTeleport(packet.getPosition());
        }
    }

    private EData getById(int id) {
        return tracked.stream().filter(eData -> eData.getId() == id).findAny().get();
    }


    @Getter
    @Setter
    class EData {

        private Vector3d first, second;
        private int id, steps;

        public EData(int id, Vector3d pos) {
            this.id = id;
            this.first = pos;
        }

        public void processRelMove(Vector3d move) {
            /*
            Confirm the next position using a post transaction
             */

            //TODO PUT THIS ON PRE TRANSACTION
            steps = 3;

            //TODO PUT THIS ON POST TRANSACTION
            second = first.add(move);


        }

        public void processTeleport(Vector3d pos) {
            /*
            Confirm the next position using a post transaction
             */

            //TODO PUT THIS ON PRE TRANSACTION
            data.getConnectionProcessor().addPreTask(() -> steps = 3);

            //TODO PUT THIS ON POST TRANSACTION

            double deltaX = first.x - pos.x;
            double deltaY = first.y - pos.y;
            double deltaZ = first.z - pos.z;

            if (Math.abs(deltaX) < 0.03125D && Math.abs(deltaY) < 0.015625D && Math.abs(deltaZ) < 0.03125D)
                second = first;
            else
                second = pos;


        }

        /*
        Moves the entity in 3 steps as the client does
         */
        public void interpolate() {
            if (steps > 0) {
                double d0 = this.first.getX() + (this.second.getX() - this.first.getX()) / (double) this.steps;
                double d1 = this.first.getY() + (this.second.getY() - this.first.getY()) / (double) this.steps;
                double d2 = this.first.getZ() + (this.second.getZ() - this.first.getZ()) / (double) this.steps;

                first = new Vector3d(d0, d1, d2);

                steps--;
            }


        }


    }
}
