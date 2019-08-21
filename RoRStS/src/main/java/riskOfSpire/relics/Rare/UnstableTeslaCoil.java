package riskOfSpire.relics.Rare;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.unique.relicEffects.TeslaAction;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class UnstableTeslaCoil extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("UnstableTeslaCoil");

    private static final int BASE_AMT = 2;

    private boolean active = false;

    public UnstableTeslaCoil() {
        super(ID, "UnstableTeslacoil.png", RelicTier.RARE, LandingSound.HEAVY);
    }

    private int getVal()
    {
        return BASE_AMT + relicStack;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + getVal() + DESCRIPTIONS[1] + getVal() + DESCRIPTIONS[2];
    }

    @Override
    public void atTurnStart() {
        if (active)
            this.beginLongPulse();
    }

    @Override
    public void onPlayerEndTurn() {
        if (active)
        {
            this.stopPulse();
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new TeslaAction(getVal()));
        }
        active = !active;
    }

    public AbstractRelic makeCopy() {
        return new UnstableTeslaCoil();
    }
}