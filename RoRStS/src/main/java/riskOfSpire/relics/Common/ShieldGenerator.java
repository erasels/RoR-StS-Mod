package riskOfSpire.relics.Common;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.apache.commons.lang3.math.NumberUtils;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class ShieldGenerator extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("ShieldGenerator");
    private static final int STARTING_SHIELD = 10;
    private static final int ADDITIONAL_SHIELD = 5;

    private boolean took_dmg = true;

    public ShieldGenerator() {
        super(ID, "ShieldGenerator.png", RelicTier.COMMON, LandingSound.CLINK);
        isTempHP = true;
    }

    @Override
    public String getUpdatedDescription() {
        //At the the start of each turn, if you didn't take damage last turn and have less than 10 (+5) Temp HP, gain 1(+1) Temp HP
        return DESCRIPTIONS[0] + getVal() + DESCRIPTIONS[1] + relicStack + DESCRIPTIONS[2];
    }

    @Override
    public void atTurnStart() {
        if(!took_dmg) {
            int tmp = TempHPField.tempHp.get(AbstractDungeon.player) - getVal();
            if(tmp < 0) {
                this.flash();
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, relicStack - NumberUtils.max(relicStack + tmp, 0)));
            }
        }
        took_dmg = false;
        beginLongPulse();
    }

    @Override
    public void onLoseHp(int dmg) {
        if(dmg > 0) {
            took_dmg = true;
            stopPulse();
        }
    }

    @Override
    public void onVictory() {
        stopPulse();
        took_dmg = true;
    }

    private int getVal() {
        return STARTING_SHIELD + ADDITIONAL_SHIELD * (relicStack - 1);
    }

    public AbstractRelic makeCopy() {
        return new ShieldGenerator();
    }
}
