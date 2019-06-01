package riskOfSpire.relics.Rare;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;
import riskOfSpire.relics.Interfaces.BonusRorRelicChanceRelic;

public class FiftySevenLeafClover extends StackableRelic implements BonusRorRelicChanceRelic {
    public static final String ID = RiskOfSpire.makeID("FiftySevenLeafClover");
    private static final float CHANCE_MOD = 0.1f;

    public FiftySevenLeafClover() {
        super(ID, "FiftySevenLeafClover.png", RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + MathUtils.round(getVal()*100) + DESCRIPTIONS[1];
    }

    @Override
    public float lunarCacheChanceModifier(float chance) {
        flash();
        return chance + getVal();
    }

    @Override
    public float flatBonusRelicChanceModifier(float chance) {
        flash();
        return chance + getVal();
    }

    public AbstractRelic makeCopy() {
        return new FiftySevenLeafClover();
    }

    public float getVal() {
        return CHANCE_MOD*relicStack>1.0f?1.0f:CHANCE_MOD*relicStack;
    }
}
