package riskOfSpire.relics.Lunar;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.patches.ForUsableRelics.UsableRelicSlot;
import riskOfSpire.relics.Abstracts.StackableRelic;
import riskOfSpire.relics.Interfaces.MultiplyCooldownRelic;

public class GestureOfTheDrowned extends StackableRelic implements MultiplyCooldownRelic {
    public static final String ID = RiskOfSpire.makeID("GestureOfTheDrowned");
    private static final float CD_PER = 0.5f;

    @Override
    public void onLoad(Integer integer) {
        super.onLoad(integer);

        if (UsableRelicSlot.usableRelic.get(AbstractDungeon.player) != null && UsableRelicSlot.usableRelic.get(AbstractDungeon.player).isUsable())
        {
            UsableRelicSlot.usableRelic.get(AbstractDungeon.player).updateDescriptionWhenNeeded();
        }
    }

    public GestureOfTheDrowned() {
        super(ID, "GestureOfTheDrowned.png", RelicTier.SPECIAL, LandingSound.MAGICAL);
        isLunar = true;
    }

    @Override
    public float modifyCooldown(float cd) {
        return (float)(cd * (Math.pow(CD_PER, relicStack)));
    }

    @Override
    public void atTurnStart() {
        if (UsableRelicSlot.usableRelic.get(AbstractDungeon.player) != null && UsableRelicSlot.usableRelic.get(AbstractDungeon.player).isUsable())
        {
            UsableRelicSlot.usableRelic.get(AbstractDungeon.player).onRightClick();
            UsableRelicSlot.usableRelic.get(AbstractDungeon.player).onRightClickInCombat();
        }
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
        //The cooldown of usable relics is reduced by x% but they are used automatically.
        return DESCRIPTIONS[0] + (int)((1.0f - Math.pow(CD_PER, relicStack)) * 100) + DESCRIPTIONS[1];
    }

    public AbstractRelic makeCopy() {
        return new GestureOfTheDrowned();
    }
}