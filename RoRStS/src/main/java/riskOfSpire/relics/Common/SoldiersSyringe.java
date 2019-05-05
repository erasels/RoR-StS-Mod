package riskOfSpire.relics.Common;

import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
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
import riskOfSpire.util.StringManipulationUtilities;

public class SoldiersSyringe extends StackableRelic implements OnAfterUseCardRelic {
    public static final String ID = RiskOfSpire.makeID("SoldiersSyringe");
    private static final int CARD_AMT = 20;

    private boolean fullSpeed  = false;

    public SoldiersSyringe() {
        super(ID, "SoldiersSyringe.png", RelicTier.COMMON, LandingSound.CLINK);
        setCounter(CARD_AMT);
    }

    @Override
    public String getUpdatedDescription() {
        int val = (CARD_AMT - (relicStack - 1)) > 0 ? (CARD_AMT - (relicStack - 1)) : 1;
        if (val > 1)
        {
            return (DESCRIPTIONS[0] + StringManipulationUtilities.ordinalNaming(val) + DESCRIPTIONS[1]);
        }
        return DESCRIPTIONS[2];
    }

    @Override
    public void onStack() {
        super.onStack();
        if (!(counter <= 1)) {
            manipCharge(-1);
        } else {
            if((CARD_AMT - (relicStack - 1)) < 2) {
                fullSpeed = true;
            }
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard c, UseCardAction uac) {
        if (c.type == CardType.ATTACK) {
            flash();
            manipCharge(-1);
            if (counter == 1) {
                beginLongPulse();
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DoubleAttackPower(AbstractDungeon.player, fullSpeed)));
            } else if (counter <= 0) {
                startingCharges();
            }
        }
    }

    @Override
    public void onVictory() {
        stopPulse();
    }

    public void atBattleStart() {
        if (this.counter == 1) {
            beginLongPulse();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DoubleAttackPower(AbstractDungeon.player, fullSpeed)));
        }
    }

    private void startingCharges() {
        if (CARD_AMT - (relicStack - 1) >= 1) {
            setCounter(CARD_AMT - (relicStack - 1));
            stopPulse();
        } else {
            setCounter(1);
            beginLongPulse();
        }
    }

    private void manipCharge(int amt) {
        setCounter(counter + amt);
    }

    public AbstractRelic makeCopy() {
        return new SoldiersSyringe();
    }
}