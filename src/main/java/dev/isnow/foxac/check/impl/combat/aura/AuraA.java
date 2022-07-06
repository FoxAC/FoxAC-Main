package dev.isnow.foxac.check.impl.combat.aura;

import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.isnow.foxac.check.Check;
import dev.isnow.foxac.packet.event.FPacketEvent;
import org.bukkit.Bukkit;

/**
 * @author 5170
 * made on dev.isnow.foxac.check.impl.combat.aura
 */

public class AuraA extends Check {

    public AuraA() {
        super("aura", "A", true);
    }

    @Override
    public void handleCheck(FPacketEvent packetEvent) {
        if(data.getActionProcessor().isSprinting() && data.getActionProcessor().isAttacking()) {
            fail(); // this shit is going to false so much, testing purposes only
        }
    }
}
