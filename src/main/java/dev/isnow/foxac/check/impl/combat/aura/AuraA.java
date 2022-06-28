package dev.isnow.foxac.check.impl.combat.aura;

import dev.isnow.foxac.check.Category;
import dev.isnow.foxac.check.Check;
import dev.isnow.foxac.data.PlayerData;

public class AuraA extends Check {

    protected AuraA() {
        super("Aura", "A", Category.COMBAT, true);
    }

    @Override
    public void process(PlayerData data) {

    }
}
