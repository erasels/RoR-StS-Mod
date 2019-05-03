package riskOfSpire.relics.Uncommon;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.patches.ForUsableRelics.UsableRelicSlot;
import riskOfSpire.relics.Abstracts.StackableRelic;
import riskOfSpire.relics.Interfaces.ModifyCooldownRelic;

public class FuelCell extends StackableRelic implements ModifyCooldownRelic {
    public static final String ID = RiskOfSpire.makeID("FuelCell");
    //private static final int CD_PER = 1;

    @Override
    public void onLoad(Integer integer) {
        super.onLoad(integer);

        if (UsableRelicSlot.usableRelic.get(AbstractDungeon.player) != null && UsableRelicSlot.usableRelic.get(AbstractDungeon.player).isUsable())
        {
            UsableRelicSlot.usableRelic.get(AbstractDungeon.player).updateDescriptionWhenNeeded();
        }
    }

    public FuelCell() {
        super(ID, "Infusion.png", RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public float modifyCooldown(float cd) {
        return cd - relicStack;
    }

    @Override
    public void onEquip() {
        super.onEquip();
        if (UsableRelicSlot.usableRelic.get(AbstractDungeon.player) != null && UsableRelicSlot.usableRelic.get(AbstractDungeon.player).isUsable())
        {
            UsableRelicSlot.usableRelic.get(AbstractDungeon.player).updateDescriptionWhenNeeded();
        }
    }

    @Override
    public void onStack() {
        super.onStack();
        if (UsableRelicSlot.usableRelic.get(AbstractDungeon.player) != null && UsableRelicSlot.usableRelic.get(AbstractDungeon.player).isUsable())
        {
            UsableRelicSlot.usableRelic.get(AbstractDungeon.player).updateDescriptionWhenNeeded();
        }
    }

    @Override
    public String getUpdatedDescription() {
        //The cooldown of usable relics is reduced by _ turns.
        return DESCRIPTIONS[0] + relicStack + (relicStack == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2]);
    }

    public AbstractRelic makeCopy() {
        return new FuelCell();
    }
}