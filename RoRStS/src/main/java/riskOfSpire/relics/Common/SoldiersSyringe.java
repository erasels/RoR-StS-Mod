package riskOfSpire.relics.Common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DoubleTapPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class SoldiersSyringe extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("SoldiersSyringe");
    private static final int CARD_AMT = 20;

    public SoldiersSyringe() {
        super(ID, "SoldiersSyringe.png", RelicTier.COMMON, LandingSound.CLINK);
        startingCharges();
    }

    @Override
    public void onUseCard(AbstractCard c, UseCardAction uac) {
        if (c.type == CardType.ATTACK) {
            flash();
            manipCharge(-1);
            if(counter == 1) {
                beginLongPulse();
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DoubleTapPower(AbstractDungeon.player, 1), 1, true));
            } else if(counter<=0) {
                manipCharge(CARD_AMT-relicStack);
                stopPulse();
                flash();
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + (CARD_AMT - relicStack) + DESCRIPTIONS[1];
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
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DoubleTapPower(AbstractDungeon.player, 1), 1, true));
        }
    }

    private void startingCharges() {
        setCounter(CARD_AMT - relicStack);
    }

    private void manipCharge(int amt) {
        setCounter(counter + amt);
    }

    public AbstractRelic makeCopy() {
        return new SoldiersSyringe();
    }

}