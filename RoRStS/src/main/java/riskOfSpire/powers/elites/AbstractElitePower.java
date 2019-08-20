package riskOfSpire.powers.elites;

import riskOfSpire.powers.abstracts.RoRStSPower;
import riskOfSpire.util.helpers.RiskOfRainRelicHelper;

public class AbstractElitePower extends RoRStSPower {
    @Override
    public void onDeath() {
        RiskOfRainRelicHelper.dropUsable = true;
    }
}
