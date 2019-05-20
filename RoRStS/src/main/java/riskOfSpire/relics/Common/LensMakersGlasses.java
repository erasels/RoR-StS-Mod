package riskOfSpire.relics.Common;

import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.powers.CriticalPower;
import riskOfSpire.relics.Abstracts.StackableRelic;
import riskOfSpire.util.StringManipulationUtilities;

public class LensMakersGlasses extends StackableRelic implements OnAfterUseCardRelic {
    public static final String ID = RiskOfSpire.makeID("LensMakersGlasses");
    private static final int CARD_AMT = 20;

    private boolean fullCrit  = false;

    public LensMakersGlasses() {
        super(ID, "LensMakersGlasses.png", RelicTier.COMMON, LandingSound.CLINK);
        setCounter(CARD_AMT);
        isCritical = true;
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
            if((CARD_AMT - (relicStack - 1)) <= 1) {
                fullCrit = true;
            }
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard c, UseCardAction uac) {
        if (c.type == AbstractCard.CardType.ATTACK) {
            flash();
            manipCharge(-1);
            if (counter == 1) {
                beginLongPulse();
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new CriticalPower(AbstractDungeon.player, fullCrit)));
            } else if (counter <= 0) {
                startingCharges();
                stopPulse();
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
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new CriticalPower(AbstractDungeon.player, fullCrit)));
        }
    }

    private void startingCharges() {
        if (CARD_AMT - (relicStack - 1) >= 2) {
            setCounter(CARD_AMT - (relicStack - 1));
        } else {
            setCounter(1);
            beginLongPulse();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new CriticalPower(AbstractDungeon.player, fullCrit)));
        }
    }

    public void decreaseCharge(int amt) {
        setCounter(counter - amt);
        if(counter <= 1) {
            counter = 1;
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new CriticalPower(AbstractDungeon.player, fullCrit)));
        }
    }

    private void manipCharge(int amt) {
        setCounter(counter + amt);
    }

    public AbstractRelic makeCopy() {
        return new LensMakersGlasses();
    }
}