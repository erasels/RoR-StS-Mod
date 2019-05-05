package riskOfSpire.relics.Rare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.general.SetCostForTurnAction;
import riskOfSpire.relics.Abstracts.StackableRelic;
import riskOfSpire.util.StringManipulationUtilities;

public class AlienHead extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("AlienHead");

    private static final int CARD_AMT = 11;

    public AlienHead() {
        super(ID, "AlienHead.png", RelicTier.RARE, LandingSound.MAGICAL);
        setCounter(CARD_AMT);
    }

    private int getCardAmt()
    {
        return (CARD_AMT - (relicStack - 1)) > 0 ? (CARD_AMT - (relicStack - 1)) : 1;
    }

    @Override
    public String getUpdatedDescription() {
        if (getCardAmt() > 1)
        {
            return (DESCRIPTIONS[0] + StringManipulationUtilities.ordinalNaming(getCardAmt()) + DESCRIPTIONS[1]);
        }
        return DESCRIPTIONS[2];
    }

    @Override
    public void onStack() {
        super.onStack();
        if (counter > 1) {
            counter--;
        }
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if (counter > 1) {
            counter--;
            if (counter == 1) {
                beginLongPulse();
            }
        }
        else {
            this.flash();
            counter = getCardAmt();
            drawnCard.setCostForTurn(0);
            if (counter > 1)
                stopPulse();
        }
    }

    @Override
    public void onVictory() {
        stopPulse();
    }

    public void atBattleStart() {
        if (this.counter == 1) {
            beginLongPulse();
        }
    }

    public AbstractRelic makeCopy() {
        return new AlienHead();
    }
}