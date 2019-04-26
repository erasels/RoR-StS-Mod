package riskOfSpire.relics.Common;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class BustlingFungus extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("BustlingFungus");
    private static final int TEMP_HP_PER_STACK = 3;

    private boolean active = false;

    public BustlingFungus() {
        super(ID, "BustlingFungus.png", RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + TEMP_HP_PER_STACK * relicStack + DESCRIPTIONS[1];
    }

    @Override
    public void atTurnStart() {
        this.active = true;
        this.beginLongPulse();
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if (targetCard.type == AbstractCard.CardType.ATTACK)
        {
            this.active = false;
            this.stopPulse();
        }
    }

    @Override
    public void onPlayerEndTurn() {
        if (active) {
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, relicStack * TEMP_HP_PER_STACK));
            this.stopPulse();
            this.flash();
        }
    }

    @Override
    public void onVictory() {
        this.stopPulse();
        this.active = false;
    }


    public AbstractRelic makeCopy() {
        return new BustlingFungus();
    }
}