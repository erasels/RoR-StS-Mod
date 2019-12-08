package riskOfSpire.relics.Uncommon;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class RoseBuckler extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("RoseBuckler");
    private static final int BLOCK_GAIN = 6;
    private static final int CPTHRESHOLD = 4;

    public RoseBuckler() {
        super(ID, "RoseBuckler.png", RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + CPTHRESHOLD + DESCRIPTIONS[1] + getVal() + DESCRIPTIONS[2];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction uac) {
        counter++;
        if(counter >= CPTHRESHOLD) {
            beginLongPulse();
        }
    }

    @Override
    public void onPlayerEndTurn() {
        if(counter >= CPTHRESHOLD) {
            stopPulse();
            flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, getVal()));
        }
        setCounter(0);
    }

    @Override
    public void onVictory() {
        setCounter(-1);
        stopPulse();
    }

    @Override
    public void atPreBattle() {
        setCounter(0);
    }

    public int getVal() {
        return ((BLOCK_GAIN*relicStack)/2)+(BLOCK_GAIN/2);
    }

    public AbstractRelic makeCopy() {
        return new RoseBuckler();
    }
}
