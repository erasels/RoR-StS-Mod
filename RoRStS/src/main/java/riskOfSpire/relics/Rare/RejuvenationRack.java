package riskOfSpire.relics.Rare;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;
import riskOfSpire.relics.Interfaces.OnGainTempHpRelic;

public class RejuvenationRack extends StackableRelic implements OnGainTempHpRelic {
    public static final String ID = RiskOfSpire.makeID("RejuvenationRack");
    private static final float HEAL_MOD = 0.25f;

    public RejuvenationRack() {
        super(ID, "RejuvenationRack.png", RelicTier.RARE, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + MathUtils.round((getVal()-1f)*100) + DESCRIPTIONS[1];
    }

    @Override
    public int onPlayerHeal(int healAmount) {
        flash();
        return MathUtils.floor(healAmount * getVal());
    }

    @Override
    public int onGainTempHp(AbstractCreature receiver, int amount) {
        if(receiver == AbstractDungeon.player) {
            flash();
            return MathUtils.floor(amount * getVal());
        }
        return amount;
    }

    public float getVal() {
        return 1f + (HEAL_MOD * (float) relicStack);
    }

    public AbstractRelic makeCopy() {
        return new RejuvenationRack();
    }
}