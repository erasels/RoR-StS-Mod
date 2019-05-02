package riskOfSpire.relics.Uncommon;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;
import riskOfSpire.relics.Interfaces.ModifyCooldownRelic;

public class FuelCell extends StackableRelic implements ModifyCooldownRelic {
    public static final String ID = RiskOfSpire.makeID("FuelCell");
    private static final int CD_PER = 1;

    public FuelCell() {
        super(ID, "FuelCell.png", RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public float modifyCooldown(float cd) {
        return cd - (CD_PER * relicStack);
    }

    @Override
    public String getUpdatedDescription() {
        //The cooldown of usable relics is reduced by _ turns.
        return DESCRIPTIONS[0] + (CD_PER * relicStack) + DESCRIPTIONS[1];
    }

    public AbstractRelic makeCopy() {
        return new FuelCell();
    }
}