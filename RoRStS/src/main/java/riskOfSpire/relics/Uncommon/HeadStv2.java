package riskOfSpire.relics.Uncommon;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;
import riskOfSpire.relics.Interfaces.OnBlockClearRelic;
import riskOfSpire.relics.Interfaces.OnPlayerBlockBrokenRelic;

public class HeadStv2 extends StackableRelic implements OnBlockClearRelic, OnPlayerBlockBrokenRelic {
    public static final String ID = RiskOfSpire.makeID("HeadStv2");

    public HeadStv2() {
        super(ID, "HeadStv2.png", RelicTier.UNCOMMON, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        if (relicStack == 1) {
            return DESCRIPTIONS[0] + DESCRIPTIONS[1] + DESCRIPTIONS[4];
        }
        return DESCRIPTIONS[0] + DESCRIPTIONS[2] + relicStack + DESCRIPTIONS[3] + DESCRIPTIONS[4];
    }

    @Override
    public void onPlayerBlockBroken() {
        if (counter > 0) {
            flash();
            setCounter(counter - 1);
            if (counter <= 0) {
                setCounter(-1);
                stopPulse();
            }
        }
    }

    @Override
    public int onBlockClear(int amount) {
        if (counter > 0 && amount > 0) {
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            return 0;
        }
        return amount;
    }

    @Override
    public void atBattleStart() {
        setCounter(relicStack);
        beginLongPulse();
    }

    @Override
    public void onVictory() {
        setCounter(-1);
        stopPulse();
    }

    public AbstractRelic makeCopy() {
        return new HeadStv2();
    }
}
