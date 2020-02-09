package riskOfSpire.relics.Uncommon;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class TougherTimes extends StackableRelic
{
    public static final String ID = RiskOfSpire.makeID("TougherTimes");
    private static final int DAMAGE_REDUCTION = 1;

    public TougherTimes()
    {
        super(ID, "TougherTimes.png", RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + DAMAGE_REDUCTION * relicStack + DESCRIPTIONS[1];
    }

    @Override
    public int onLoseHpLast(int damage) {
        if (damage > 0) {
            damage -= DAMAGE_REDUCTION * relicStack;
        }
        return Math.max(damage, 0);
    }

    public AbstractRelic makeCopy()
    {
        return new TougherTimes();
    }
}
