package riskOfSpire.relics.Common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.powers.DoubleAttackPower;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class SoldiersSyringe extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("SoldiersSyringe");
    private static final int CARD_AMT = 20;

    public SoldiersSyringe() {
        super(ID, "SoldiersSyringe.png", RelicTier.COMMON, LandingSound.CLINK);
        setCounter(CARD_AMT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + ((CARD_AMT - (relicStack-1))>0?(CARD_AMT - (relicStack-1)):1) + DESCRIPTIONS[1];
    }

    @Override
    public void onStack() {
        super.onStack();
        if(!(counter<= 1)) {
            manipCharge(-1);
        }
    }

    @Override
    public void onUseCard(AbstractCard c, UseCardAction uac) {
        if (c.type == CardType.ATTACK) {
            flash();
            manipCharge(-1);
            if(counter == 1) {
                beginLongPulse();
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DoubleAttackPower(AbstractDungeon.player)));
            } else if(counter<=0) {
                startingCharges();
                stopPulse();
                flash();
            }
        }
    }

    @Override
    public void onVictory() {
        stopPulse();
    }

    public void atBattleStart()
    {
        if (this.counter == 1)
        {
            beginLongPulse();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DoubleAttackPower(AbstractDungeon.player)));        }
    }

    private void startingCharges() {
        if(CARD_AMT - (relicStack-1) >= 1) {
            setCounter(CARD_AMT - (relicStack-1));
        } else {
            setCounter(1);
            beginLongPulse();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DoubleAttackPower(AbstractDungeon.player)));        }
    }

    private void manipCharge(int amt) {
        setCounter(counter + amt);
    }

    public AbstractRelic makeCopy() {
        return new SoldiersSyringe();
    }

}