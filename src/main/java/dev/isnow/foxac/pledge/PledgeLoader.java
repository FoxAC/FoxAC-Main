package dev.isnow.foxac.pledge;

import dev.isnow.foxac.FoxAC;
import dev.thomazz.pledge.api.Pledge;
import lombok.Getter;

/**
 * @author Lindgey
 * made on dev.isnow.foxac.pledge
 */

@Getter
public class PledgeLoader {

    private final Pledge pledgeInst = Pledge.build().events(true);

    public void load(FoxAC plugin) {

        pledgeInst.start(plugin);
        pledgeInst.addListener(new PledgeListener());



    }
}
